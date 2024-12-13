package com.amotassic.dabaosword.item.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

import static com.amotassic.dabaosword.api.event.CardEvents.*;
import static com.amotassic.dabaosword.util.ModTools.*;

public class StealItem extends CardItem {
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && canSteal(entity)) {
            if (cardUsePre(user, user.getMainHandItem(), entity)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity entity) {
        if (user instanceof Player player) {
            if (entity instanceof Player target) {
                openInv(player, target, Component.translatable("dabaosword.steal.title"), stack, false, true, true, 1);
            } else {
                List<ItemStack> stacks = getItems(entity, isCard, true, false, true, false);
                if (!stacks.isEmpty()) {
                    ItemStack chosen = stacks.get(new Random().nextInt(stacks.size()));
                    cardMove(entity, player, chosen, 1, isEquipped(entity, s -> s.equals(chosen)), false);
                    cardUsePost(player, stack, entity);
                }
            }
        } else cardUsePost(user, stack, entity);
    }

    @Override
    public boolean notImmediatelyEffective() {return true;}

    private boolean canSteal(LivingEntity entity) {
        int count = countAllCards(entity);
        for (ItemStack stack : entity.getArmorSlots()) {count += stack.getCount();}
        return count > 0;
    }
}
