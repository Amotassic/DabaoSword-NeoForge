package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.api.event.CardCBs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.amotassic.dabaosword.util.ModTools.*;

public class JiedaoItem extends CardItem {
    public JiedaoItem(Properties p_41383_) {super(p_41383_);}

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
            if (isCard(stack1)) cardMove(entity, player, stack1, stack1.getCount(), CardCBs.T.INV_TO_INV);
            else {
                give(player, stack1.copy());
                stack1.setCount(0);
            }
        } else {
            user.setItemInHand(InteractionHand.MAIN_HAND, stack1.copy());
            stack1.setCount(0);
        }
    }
}
