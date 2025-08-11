package com.amotassic.dabaosword.item.card;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FireAttackItem extends CardItem.Armoury {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide) {
            onUse(user, user.getItemInHand(hand), hand, user);
            return InteractionResultHolder.success(user.getItemInHand(hand));
        }
        return super.use(world, user, hand);
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity target) {
        Level world = user.level();
        Vec3 momentum = user.getForward().scale(3);
        LargeFireball fireballEntity = new LargeFireball(world, user, momentum, 2);
        fireballEntity.addTag("a");
        fireballEntity.setPos(user.getX(), user.getY(0.5) + 0.5, user.getZ());
        world.addFreshEntity(fireballEntity);
    }
}
