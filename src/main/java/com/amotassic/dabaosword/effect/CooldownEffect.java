package com.amotassic.dabaosword.effect;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CooldownEffect extends MobEffect {
    public CooldownEffect(MobEffectCategory category, int color) {super(category, color);}

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {return true;}

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amp) {
        if (entity instanceof Player player) {
            int restTime = Objects.requireNonNull(entity.getEffect(ModItems.COOLDOWN)).getDuration();
            if(restTime<=1) {
                player.displayClientMessage(Component.translatable("dabaosword.cooldown_end").withStyle(ChatFormatting.GREEN),true);
            }
        }
        return true;
    }
}
