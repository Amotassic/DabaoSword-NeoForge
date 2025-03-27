package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.api.CardEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.amotassic.dabaosword.util.ModTools.*;

public class JiedaoItem extends CardItem.Armoury {
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND && !entity.getMainHandItem().isEmpty()) {
            onUse(user, user.getMainHandItem(), entity);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void effect(LivingEntity user, ItemStack card, LivingEntity entity) {
        ItemStack main = entity.getMainHandItem();
        if (user instanceof Player player) {
            if (isCard(main)) {
                var exData = d().cards(main, 1);
                CardEvents.cardMove(entity, exData, player);
            } else {
                give(player, main.copy());
                main.setCount(0);
            }
        } else {
            user.setItemInHand(InteractionHand.MAIN_HAND, main.copy());
            if (user instanceof Mob mob) mob.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            main.setCount(0);
        }
    }

    @Override public boolean askForWuxie() {return true;}
}
