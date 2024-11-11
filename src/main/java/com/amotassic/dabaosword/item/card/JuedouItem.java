package com.amotassic.dabaosword.item.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.amotassic.dabaosword.util.ModTools.*;

public class JuedouItem extends CardItem {
    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && entity.isAlive()) {
            if (cardUsePre(user, user.getMainHandItem(), entity)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity entity) {
        user.addTag("juedou"); entity.addTag("juedou"); //防止决斗触发杀
        if (user instanceof Player player && entity instanceof Player target) {
            int playerSha = countCard(player, isSha);
            int targetSha = countCard(target, isSha);
            if (playerSha >= targetSha) {
                juedou(player, target);
                target.displayClientMessage(Component.translatable("dabaosword.juedou2", player.getDisplayName()), false);
            } else {
                juedou(target, player);
                player.displayClientMessage(Component.translatable("dabaosword.juedou1"), false);
                //如果目标的杀比使用者的杀多，反击使用者，则目标减少一张杀
                if (targetSha != 0) cardUsePost(target, getCard(target, isSha).getB(), player);
            }
        } else juedou(user, entity);
    }

    private void juedou(LivingEntity attacker, LivingEntity target) {
        ItemStack juedou = new ItemStack(this);
        DamageSource source = getDamageSource(attacker, DamageTypes.GENERIC_KILL);
        if (canHurtByCard(target, source, juedou)) {
            target.invulnerableTime = 0;
            if (target.hurt(source, 5f)) hurtByCard(target, source, juedou);
        }
    }
}
