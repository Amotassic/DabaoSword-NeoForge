package com.amotassic.dabaosword.effect;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.amotassic.dabaosword.item.tool.ArrowRainItem.arrowAround;
import static com.amotassic.dabaosword.item.tool.ArrowRainItem.tridentStorm;

public class Cooldown2Effect extends MobEffect {
    public Cooldown2Effect() {super(MobEffectCategory.NEUTRAL, 0xFFFFFF);}

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {return true;}

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel world) {
            int restTime = Objects.requireNonNull(entity.getEffect(ModItems.COOLDOWN2)).getDuration();

            if (amplifier == 3 && restTime % 2 == 0) { //雷击的效果
                EntityType.LIGHTNING_BOLT.spawn(world, new BlockPos((int) entity.getX(), (int) entity.getY(), (int) entity.getZ()), MobSpawnType.MOB_SUMMONED);
            }

            if (amplifier == 1 && restTime % 3 == 0) {
                arrowAround(entity, 3, 18, 10, 3);
                arrowAround(entity, 3, 18, 8, 4);
                arrowAround(entity, 3, 18, 6, 5);
                arrowAround(entity, 3, 18, 4, 6);
                arrowAround(entity, 3, 18, 2, 7);
            }

            if (amplifier == 5 && restTime % 4 == 0) {
                tridentStorm(entity, 3, 18, 10, 3);
                tridentStorm(entity, 3, 18, 8, 4);
                tridentStorm(entity, 3, 18, 6, 5);
                tridentStorm(entity, 3, 18, 4, 6);
                tridentStorm(entity, 3, 18, 2, 7);
            }
        }
        return true;
    }
}
