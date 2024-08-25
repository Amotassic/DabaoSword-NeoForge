package com.amotassic.dabaosword.effect;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InvulnerableEffect extends MobEffect {
    public InvulnerableEffect(MobEffectCategory category, int color) {super(category, color);}

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {return true;}

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amp) {
        //南蛮入侵召唤物死亡程序
        if (entity.getType() == EntityType.WOLF) {
            int restTime = Objects.requireNonNull(entity.getEffect(ModItems.INVULNERABLE)).getDuration();
            if(restTime<=1) {
                entity.setHealth(0);
            }
        }
        return true;
    }
}
