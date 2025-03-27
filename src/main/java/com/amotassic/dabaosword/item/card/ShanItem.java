package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ShanItem extends CardItem.Basic {
    //使用后，向前冲刺一段距离，无敌0.5秒，冷却时间1秒
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        //判断是否有独立冷却buff，若冷却中则无法生效
        if (!world.isClientSide && !user.hasEffect(ModItems.COOLDOWN2) && hand == InteractionHand.MAIN_HAND) {
            onUse(user, user.getMainHandItem(), user);
            return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity target) {
        Vec3 momentum = user.getForward().scale(3);
        user.hurtMarked = true; user.setDeltaMovement(momentum.x,0 ,momentum.z);
        user.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20,0,false,false,false));
        user.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 20,0,false,false,false));
    }
}
