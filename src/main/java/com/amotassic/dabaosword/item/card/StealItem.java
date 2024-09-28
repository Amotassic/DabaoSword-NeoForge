package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.event.listener.CardCBs;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

public class StealItem extends CardItem {
    public StealItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (entity instanceof Player target) {
                if (hasItem(target, ModItems.WUXIE.get())) {
                    cardUsePost(target, getItem(target, ModItems.WUXIE.get()), null);
                    voice(target, Sounds.WUXIE);
                    cardUsePost(user, stack, entity);
                    voice(user, Sounds.SHUNSHOU);
                } else {
                    openInv(user, target, Component.translatable("dabaosword.steal.title"), targetInv(target, true, true, 1, user.getMainHandItem()));
                }
            } else {
                List<ItemStack> stacks = new ArrayList<>();
                if (isCard(entity.getMainHandItem())) stacks.add(entity.getMainHandItem());
                if (isCard(entity.getOffhandItem())) stacks.add(entity.getOffhandItem());
                if (!stacks.isEmpty()) {
                    ItemStack chosen = stacks.get(new Random().nextInt(stacks.size()));
                    voice(user, Sounds.SHUNSHOU);
                    cardMove(entity, user, chosen, 1, CardCBs.T.INV_TO_INV);
                    cardUsePost(user, stack, entity);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
