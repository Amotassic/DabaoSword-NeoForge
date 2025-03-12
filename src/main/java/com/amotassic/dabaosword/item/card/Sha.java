package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.api.ISha;
import com.amotassic.dabaosword.effect.ShandianEffect;
import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.amotassic.dabaosword.api.event.CardEvents.hurtByCard;
import static com.amotassic.dabaosword.util.ModTools.getDamageSource;

public class Sha extends CardItem implements ISha {
    @Override public Type getType() {return Type.BASIC;}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltip, tooltipFlag);
        tooltip.add(Component.translatable("item.dabaosword.sha.tip").withStyle(ChatFormatting.BOLD));

        if (stack.is(ModItems.SHA)) tooltip.add(getTip());
        if (stack.is(ModItems.FIRE_SHA)) tooltip.add(getTip().withStyle(ChatFormatting.RED));
        if (stack.is(ModItems.THUNDER_SHA)) tooltip.add(getTip().withStyle(ChatFormatting.BLUE));
    }

    @Override
    public boolean sha(LivingEntity user, LivingEntity target, float amount) {
        return target.hurt(user.damageSources().mobAttack(user), amount + 5);
    }

    @Override
    public void shaEffect(LivingEntity user, LivingEntity target, ItemStack sha) {
        hurtByCard(target, sha);
    }

    public static class Fire extends Sha {
        @Override
        public boolean sha(LivingEntity user, LivingEntity target, float amount) {
            return target.hurt(getDamageSource(user, DamageTypes.IN_FIRE), amount);
        }

        @Override
        public void shaEffect(LivingEntity user, LivingEntity target, ItemStack sha) {
            target.setRemainingFireTicks(120);
            hurtByCard(target, sha);
        }
    }

    public static class Thunder extends Sha {
        @Override
        public boolean sha(LivingEntity user, LivingEntity target, float amount) {
            return target.hurt(getDamageSource(user, DamageTypes.LIGHTNING_BOLT), amount + 5);
        }

        @Override
        public void shaEffect(LivingEntity user, LivingEntity target, ItemStack sha) {
            ShandianEffect.summonLightning(target, true, false);
            hurtByCard(target, sha);
        }
    }
}