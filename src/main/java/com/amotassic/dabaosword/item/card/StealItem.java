package com.amotassic.dabaosword.item.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

import static com.amotassic.dabaosword.api.CardEvents.cardMove;
import static com.amotassic.dabaosword.util.ModTools.*;

public class StealItem extends CardItem.Armoury {
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && canSteal(entity)) {
            onUse(user, user.getMainHandItem(), entity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity entity) {
        if (user instanceof Player player) {
            if (entity instanceof Player target) {
                openInv(player, target, Component.translatable("dabaosword.steal.title"), card, false, true, true, 1);
            } else {
                List<ItemStack> stacks = getItems(entity, isCard, true, false, true, false);
                if (!stacks.isEmpty()) {
                    ItemStack chosen = stacks.get(new Random().nextInt(stacks.size()));
                    var exData = d().cards(chosen, 1, isEquipped(entity, s -> s.equals(chosen)));
                    cardMove(entity, exData, player);
                }
            }
        }
    }

    @Override public boolean askForWuxie() {return true;}

    private boolean canSteal(LivingEntity entity) {
        int count = countAllCards(entity);
        for (ItemStack stack : entity.getArmorSlots()) {count += stack.getCount();}
        return count > 0;
    }
}
