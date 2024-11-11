package com.amotassic.dabaosword.item.card;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.amotassic.dabaosword.util.ModTools.cardUsePre;

public class JiuItem extends CardItem {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!user.hasEffect(MobEffects.DAMAGE_BOOST) && !world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (cardUsePre(user, user.getMainHandItem(), user)) return InteractionResultHolder.success(user.getMainHandItem());
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity target) {
        user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 10, 0));
    }
}
