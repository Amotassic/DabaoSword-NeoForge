package com.amotassic.dabaosword.item.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

import static com.amotassic.dabaosword.api.event.CardEvents.*;
import static com.amotassic.dabaosword.util.ModTools.*;

public class DiscardItem extends CardItem {
    @Override
    public @NotNull InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && countAllCards(entity) > 0) {
            if (cardUsePre(user, user.getMainHandItem(), entity)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity entity) {
        if (user instanceof Player player) {
            if (entity instanceof Player target) {
                openInv(player, target, Component.translatable("dabaosword.discard.title", stack.getDisplayName()), stack, false, true, false, 1);
            } else {
                List<ItemStack> stacks = getItems(entity, isCard, true, false, true, false);
                if (!stacks.isEmpty()) {
                    ItemStack chosen = stacks.get(new Random().nextInt(stacks.size()));
                    cardDiscard(entity, chosen, 1, isEquipped(entity, s -> s.equals(chosen)));
                    cardUsePost(player, stack, entity);
                }
            }
        } else {
            if (entity instanceof Player player) { //如果是玩家则弃牌
                List<ItemStack> candidate = getItems(entity, isCard, true, false, true, true);
                if (!candidate.isEmpty()) {
                    ItemStack chosen = candidate.get(new Random().nextInt(candidate.size()));
                    player.displayClientMessage(Component.translatable("dabaosword.discard", user.getDisplayName(), player.getDisplayName(), chosen.getDisplayName()), false);
                    cardDiscard(player, chosen, 1, isEquipped(entity, s -> s.equals(chosen)));
                    cardUsePost(user, stack, entity);
                }
            } else { //如果不是玩家则随机弃置它的主副手物品和装备
                var candidate = getItems(entity, s -> !s.isEmpty(), true, true, true, false);
                if (!candidate.isEmpty()) {
                    ItemStack chosen = candidate.get(new Random().nextInt(candidate.size()));
                    if (isCard(chosen)) cardDiscard(entity, chosen, 1, isEquipped(entity, s -> s.equals(chosen)));
                    else chosen.shrink(1);
                    cardUsePost(user, stack, entity);
                }
            }
        }
    }

    @Override
    public boolean notImmediatelyEffective() {return true;}
}
