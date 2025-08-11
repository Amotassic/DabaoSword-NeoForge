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
        if (!world.isClientSide && player.getHealth() < player.getMaxHealth() && !player.isShiftKeyDown()) {
            onUse(player, player.getItemInHand(hand), hand, player);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return super.use(world, player, hand);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && entity.getHealth() < entity.getMaxHealth() && user.isShiftKeyDown()) {
            onUse(user, user.getItemInHand(hand), hand, entity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity target) {
        target.heal(5);
    }
}
