package com.amotassic.dabaosword.effect;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.amotassic.dabaosword.item.equipment.ArrowRainItem.arrowRain;
import static com.amotassic.dabaosword.util.ModTools.hasTrinket;
import static com.amotassic.dabaosword.util.ModTools.voice;

public class Cooldown2Effect extends MobEffect {
    public Cooldown2Effect() {super(MobEffectCategory.NEUTRAL, 0xFFFFFF);}

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {return true;}

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel world) {
            int restTime = Objects.requireNonNull(entity.getEffect(ModItems.COOLDOWN2)).getDuration();
            //一级效果被用于万箭齐发
            if (amplifier == 1 && restTime % 5 == 0) arrowRain(entity, 3, 25);

            if (amplifier == 3 && hasTrinket(SkillCards.LEIJI, entity) && restTime >= 15) {//雷击的效果
                EntityType.LIGHTNING_BOLT.spawn(world, new BlockPos((int) entity.getX(), (int) entity.getY(), (int) entity.getZ()), MobSpawnType.MOB_SUMMONED);
            }
        }
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide && hasTrinket(SkillCards.LEIJI, entity) && amplifier == 3) {
            voice(entity, Sounds.LEIJI); //雷击语音播放
        }
        super.onEffectAdded(entity, amplifier);
    }
}
