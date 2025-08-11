package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.api.skill.ISkill;
import com.amotassic.dabaosword.api.skill.Skill;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

import static com.amotassic.dabaosword.util.ModTools.*;

@SuppressWarnings("all")
public class SkillItem extends Item implements ISkill {
    public SkillItem() {super(new Properties().stacksTo(1));}

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        // curios的饰品会同时触发原版的inventoryTick（slotId为1）和curioTick
        if (slotId != -1 && !level.isClientSide && equipped(stack)) setEquipped(stack, false);
    }

    public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {addTip(s(s), t);}
    public void addTip(Skill skill, List<Component> tooltip) {}
    public MutableComponent getTip(ChatFormatting... format) {return getTip("", format);}
    public MutableComponent getTip(String suffix, ChatFormatting... format) {
        return Component.translatable(getDescriptionId() + ".tooltip" + suffix).withStyle(format);
    }

    public final void curioTick(SlotContext slotContext, ItemStack stack) {
        ISkill.super.curioTick(slotContext, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && user.getTags().contains("change_skill") && hand == InteractionHand.OFF_HAND && user.isShiftKeyDown()) {
            ItemStack stack = user.getItemInHand(hand);
            if (stack.getItem() instanceof SkillItem) {
                stack.setCount(0);
                changeSkill(user);
                user.getTags().remove("change_skill");
            }
        }
        return super.use(world, user, hand);
    }

    public static void changeSkill(Player player) {
        ItemStack stack = customLoot(player, "draw_skill");
        if (!stack.isEmpty()) voice(player, "giftbox",3);
        give(player, stack);
    }

    public Component activeSkillText(Player user, Skill skill) {
        return Component.translatable("active_skill.select_target").withStyle(ChatFormatting.AQUA).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dabaosword " + user.getName().getString() + " " + BuiltInRegistries.ITEM.getKey(skill.stack.getItem()) + " ")));
    }
}
