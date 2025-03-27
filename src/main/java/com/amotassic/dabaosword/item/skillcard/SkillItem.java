package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.api.skill.ISkill;
import com.amotassic.dabaosword.api.skill.Skill;
import com.amotassic.dabaosword.item.card.CardItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.function.Predicate;

import static com.amotassic.dabaosword.util.ModTools.*;

@SuppressWarnings("all")
public class SkillItem extends Item implements ISkill {
    public SkillItem() {super(new Properties().stacksTo(1));}

    public void appendHoverText(ItemStack s, TooltipContext c, List<Component> t, TooltipFlag f) {addTip(s(s), t);}
    public void addTip(Skill skill, List<Component> tooltip) {}
    public MutableComponent getTip(ChatFormatting... format) {return getTip("", format);}
    public MutableComponent getTip(String suffix, ChatFormatting... format) {
        return Component.translatable(getDescriptionId() + ".tooltip" + suffix).withStyle(format);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world && !equipped(stack)) {
            world.players().forEach(player -> player.displayClientMessage(
                    Component.translatable("dabaosword.entity.equip", slotContext.entity().getDisplayName(), stack.getDisplayName()), false
            ));
            setEquipped(stack, true);
        }
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

    /**转化卡牌技能通用方法*/
    public static void viewAs(LivingEntity entity, Skill skill, int CD, Predicate<ItemStack> p, CardItem result) {
        if (entity.level().isClientSide) return;
        if (skill.getCD() > 0) return;
        ItemStack off = entity.getOffhandItem(); var copy = off.copy();
        if (off.isEmpty()) return;
        if (p.test(off)) {
            skill.setCD(CD);
            off.shrink(1);
            give(entity, c(copy, result).toStack());
            voice(entity, skill.stack);
        }
    }

    public Component activeSkillText(Player user, Skill skill) {
        return Component.translatable("active_skill.select_target").withStyle(ChatFormatting.AQUA).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/dabaosword " + user.getName().getString() + " " + BuiltInRegistries.ITEM.getKey(skill.stack.getItem()) + " ")));
    }
}
