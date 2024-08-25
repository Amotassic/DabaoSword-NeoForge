package com.amotassic.dabaosword.effect;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class TooHappyEffect extends MobEffect {
    public TooHappyEffect(MobEffectCategory p_19451_, int p_19452_) {super(p_19451_, p_19452_);}

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {return true;}

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amp) {
        int restTime = Objects.requireNonNull(entity.getEffect(ModItems.TOO_HAPPY)).getDuration();
        if(restTime<=1) {entity.setPose(Pose.STANDING);}
        else {
            if (!(entity instanceof Player)) {entity.setPose(Pose.SLEEPING);}
            entity.setDeltaMovement(0, 0, 0);
        }
        return true;
    }
}
