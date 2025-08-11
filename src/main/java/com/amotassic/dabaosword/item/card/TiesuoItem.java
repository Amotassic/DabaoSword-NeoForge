package com.amotassic.dabaosword.item.card;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Set;

public class TiesuoItem extends CardItem.Armoury {
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && !entity.isCurrentlyGlowing()) {
            AABB box = user.getBoundingBox().expandTowards(user.getViewVector(1.0F).scale(10));
            Set<LivingEntity> targets = new HashSet<>(user.level().getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive));
            onUse(user, user.getItemInHand(hand), hand, targets.toArray(new LivingEntity[0]));
            user.removeEffect(MobEffects.GLOWING);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity target) {
        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, -1, 0, false, true,false));
    }

    @Override public boolean askForWuxie() {return true;}
}
