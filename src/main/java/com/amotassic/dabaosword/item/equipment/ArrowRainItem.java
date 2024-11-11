package com.amotassic.dabaosword.item.equipment;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

@SuppressWarnings("all")
public class ArrowRainItem extends Item {
    public ArrowRainItem() {super(new Properties().durability(50).rarity(Rarity.UNCOMMON));}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.arrowrain.tooltip"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            arrowRain(player, 5, 5);
            if (!player.isCreative()) stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    public static void arrowRain(LivingEntity entity, float speed, int count) {
        ServerLevel world = (ServerLevel) entity.level();
        for (int i = 0; i < count; i++) {
            int j;
            if (i % 2 == 0) j = -5 * i / 2; else j = 5 * (i + 1) / 2;
            summonArrow(entity, j, speed);
        }
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
    }

    private static void summonArrow(LivingEntity entity, int angle, float speed) {
        ItemStack stack = new ItemStack(Items.ARROW);
        ServerLevel world = (ServerLevel) entity.level();
        Arrow arrow = new Arrow(world, entity, stack, null);
        arrow.setCustomName(Component.nullToEmpty("a"));
        arrow.shootFromRotation(entity, entity.getXRot(), entity.getYRot() + angle, 0.0F, speed, 1.0F);
        arrow.setCritArrow(true);
        world.addFreshEntity(arrow);
    }
}
