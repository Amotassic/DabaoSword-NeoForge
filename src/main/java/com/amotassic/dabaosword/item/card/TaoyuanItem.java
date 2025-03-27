package com.amotassic.dabaosword.item.card;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

import static com.amotassic.dabaosword.util.ModTools.voice;

public class TaoyuanItem extends CardItem.Armoury {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world instanceof ServerLevel sw && hand == InteractionHand.MAIN_HAND) {
            Set<LivingEntity> targets = new HashSet<>(sw.players());
            onUse(user, user.getMainHandItem(), targets.toArray(new LivingEntity[0]));
            return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity target) {
        target.heal(5.0F);
        if (target != user) voice(target, this);
    }
}
