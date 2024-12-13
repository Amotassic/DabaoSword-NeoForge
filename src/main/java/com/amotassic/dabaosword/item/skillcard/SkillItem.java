package com.amotassic.dabaosword.item.skillcard;

import com.amotassic.dabaosword.api.*;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.function.Predicate;

import static com.amotassic.dabaosword.util.ModTools.*;

@SuppressWarnings("all")
public class SkillItem extends Item implements ICurioItem, Skill {
    public SkillItem() {super(new Properties().stacksTo(1));}

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world && !equipped(stack)) {
            world.players().forEach(player -> player.displayClientMessage(
                    Component.translatable("dabaosword.entity.equip", slotContext.entity().getDisplayName(), stack.getDisplayName()), false
            ));
            setEquipped(stack, true);
        }
    }

    public static boolean equipped(ItemStack stack) {return getOrCreateNbt(stack).contains("equipped");}

    public static void setEquipped(ItemStack stack, boolean equipped) {
        CompoundTag nbt = getOrCreateNbt(stack);
        if (equipped) nbt.putBoolean("equipped", true);
        else nbt.remove("equipped");
        setNbt(stack, nbt);
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

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity().level() instanceof ServerLevel world) {
            int cd = getCD(stack); //世界时间除以20取余为0时，技能内置CD减一秒
            if (cd > 0 && world.getGameTime() % 20 == 0) setCD(stack, cd - 1);
        }
    }

    public static void changeSkill(Player player) {
        var selectedId = parseLootTable(ResourceLocation.fromNamespaceAndPath("dabaosword", "loot_tables/draw_skill.json"));
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(selectedId));
        if (stack.getItem() != Items.AIR) voice(player, Sounds.GIFTBOX,3);
        give(player, stack);
    }

    /**转化卡牌技能通用方法*/
    public static void viewAs(LivingEntity entity, ItemStack skill, int CD, Predicate<ItemStack> predicate, ItemStack result) {
        if (!entity.level().isClientSide && noTieji(entity) && getCD(skill) == 0) {
            ItemStack stack = entity.getOffhandItem();
            if (predicate.test(stack)) {
                setCD(skill, CD);
                stack.shrink(1);
                give(entity, result);
                voice(entity, skill);
            }
        }
    }

    public static class ActiveSkill extends SkillItem {}

    public static class ActiveSkillWithTarget extends SkillItem {}
}
