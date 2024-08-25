package com.amotassic.dabaosword.effect;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.amotassic.dabaosword.util.ModTools.*;

public class BingliangEffect extends MobEffect {
    public BingliangEffect(MobEffectCategory p_19451_, int p_19452_) {super(p_19451_, p_19452_);}

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {return true;}

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amp) {
        if (entity instanceof Player player) {
            //清除玩家的牌
            if(amp == 1) {
                if (hasItem(player, ModItems.GAIN_CARD.get())) {
                    removeItem(player, ModItems.GAIN_CARD.get());
                    //将2级效果换成1级
                    player.removeEffect(ModItems.BINGLIANG);
                    player.addEffect(new MobEffectInstance(ModItems.BINGLIANG, MobEffectInstance.INFINITE_DURATION));
                }
            }
            if (amp == 0) {
                if (hasItem(player, ModItems.GAIN_CARD.get())) {
                    removeItem(player, ModItems.GAIN_CARD.get());
                    player.removeEffect(ModItems.BINGLIANG);
                }
            }
        }
        return true;
    }
}
