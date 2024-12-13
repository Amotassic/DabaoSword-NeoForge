package com.amotassic.dabaosword.item.card;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.amotassic.dabaosword.api.event.CardEvents.cardMove;
import static com.amotassic.dabaosword.api.event.CardEvents.cardUsePre;
import static com.amotassic.dabaosword.util.ModTools.give;
import static com.amotassic.dabaosword.util.ModTools.isCard;

public class JiedaoItem extends CardItem {
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && !entity.getMainHandItem().isEmpty()) {
            if (cardUsePre(user, user.getMainHandItem(), entity)) return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity entity) {
        ItemStack stack1 = entity.getMainHandItem();
        if (user instanceof Player player) {
            if (isCard(stack1)) cardMove(entity, player, stack1, stack1.getCount(), false, false);
            else {
                give(player, stack1.copy());
                stack1.setCount(0);
            }
        } else {
            user.setItemInHand(InteractionHand.MAIN_HAND, stack1.copy());
            if (user instanceof Mob mob) mob.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            stack1.setCount(0);
        }
    }
}
