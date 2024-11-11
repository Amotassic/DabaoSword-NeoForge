package com.amotassic.dabaosword.item.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static com.amotassic.dabaosword.util.ModTools.cardUsePre;

public class FireAttackItem extends CardItem {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (cardUsePre(user, user.getMainHandItem(), null)) return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity target) {
        Level world = user.level();
        Vec3 momentum = user.getForward().scale(3);
        LargeFireball fireballEntity = new LargeFireball(world, user, momentum, 3);
        fireballEntity.setCustomName(Component.nullToEmpty("a"));
        fireballEntity.setPos(user.getX(), user.getY(0.5) + 0.5, user.getZ());
        world.addFreshEntity(fireballEntity);
    }
}
