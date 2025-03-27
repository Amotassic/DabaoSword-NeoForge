package com.amotassic.dabaosword.item.card;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class JiuItem extends CardItem.Basic {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!user.hasEffect(MobEffects.DAMAGE_BOOST) && !world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            onUse(user, user.getMainHandItem(), user);
            return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 10, 0));
    }
}
