package com.amotassic.dabaosword.item.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.amotassic.dabaosword.api.CardEvents.hurtByCard;
import static com.amotassic.dabaosword.util.ModTools.*;

public class JuedouItem extends CardItem.Armoury {
    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && entity.isAlive()) {
            onUse(user, user.getMainHandItem(), entity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity entity) {
        user.addTag("juedou"); entity.addTag("juedou"); //防止决斗触发杀
        if (user instanceof Player player && entity instanceof Player target) {
            int playerSha = countCard(player, isSha);
            int targetSha = countCard(target, isSha);
            if (playerSha >= targetSha) {
                juedou(player, card, target);
                target.displayClientMessage(Component.translatable("dabaosword.juedou2", player.getDisplayName()), false);
            } else {
                juedou(target, card, player);
                player.displayClientMessage(Component.translatable("dabaosword.juedou1"), false);
                //如果目标的杀比使用者的杀多，反击使用者，则目标减少一张杀
                if (targetSha != 0) {
                    ItemStack sha = getCard(target, isSha);
                    onUse(target, sha, true, true);
                }
            }
        } else juedou(user, card, entity);
    }

    private void juedou(LivingEntity attacker, ItemStack card, LivingEntity target) {
        var source = getDamageSource(attacker, DamageTypes.GENERIC);
        target.invulnerableTime = 0;
        if (target.hurt(source, 5f)) hurtByCard(target, card);
    }

    @Override public boolean askForWuxie() {return true;}
}
