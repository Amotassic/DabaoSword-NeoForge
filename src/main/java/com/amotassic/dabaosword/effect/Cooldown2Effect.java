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
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.amotassic.dabaosword.item.equipment.ArrowRainItem.arrowRain;
import static com.amotassic.dabaosword.util.ModTools.hasTrinket;
import static com.amotassic.dabaosword.util.ModTools.voice;

public class Cooldown2Effect extends MobEffect {
    public Cooldown2Effect(MobEffectCategory category, int color) {super(category, color);}

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {return true;}

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity.level() instanceof ServerLevel world) {
            int restTime = Objects.requireNonNull(entity.getEffect(ModItems.COOLDOWN2)).getDuration();
            //一级效果被用于万箭齐发
            if (amplifier == 1) {
                arrowRain(entity, 3);
                if (restTime <= 1) entity.getTags().remove("wanjian");
            }

            if (amplifier == 3 && hasTrinket(SkillCards.LEIJI, entity) && restTime >= 15) {//雷击的效果
                EntityType.LIGHTNING_BOLT.spawn(world, new BlockPos((int) entity.getX(), (int) entity.getY(), (int) entity.getZ()),null);
            }
        }
        return true;
    }

    @Override
    public void onEffectAdded(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && !player.level().isClientSide && hasTrinket(SkillCards.LEIJI, player) && amplifier == 3) {
            //雷击语音播放
            voice(player, Sounds.LEIJI);
        }
        super.onEffectAdded(entity, amplifier);
    }
}
