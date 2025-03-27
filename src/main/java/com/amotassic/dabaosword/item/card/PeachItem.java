package com.amotassic.dabaosword.item.card;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PeachItem extends CardItem.Basic {
    //非潜行时右键，给自己回血
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide && player.getHealth() < player.getMaxHealth() && !player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            onUse(player, player.getMainHandItem(), player);
            return InteractionResultHolder.success(player.getMainHandItem());
        }
        return super.use(world, player, hand);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && entity.getHealth() < entity.getMaxHealth() && user.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            onUse(user, user.getMainHandItem(), entity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity target) {
        target.heal(5);
    }
}
