package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.amotassic.dabaosword.network.ServerNetworking.openInv;
import static com.amotassic.dabaosword.network.ServerNetworking.targetInv;
import static com.amotassic.dabaosword.util.ModTools.*;

public class StealItem extends CardItem {
    public StealItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Player target && !user.level().isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (hasItem(target, ModItems.WUXIE.get())) {
                voice(target, Sounds.WUXIE.get());
                voice(user, Sounds.SHUNSHOU.get());
                if (!user.isCreative()) {stack.shrink(1);}
                jizhi(user); benxi(user);
                removeItem(target, ModItems.WUXIE.get());
                jizhi(target); benxi(target);
            } else {
                openInv(user, target, Component.translatable("dabaosword.steal.title"), targetInv(target, true, true, 1, user.getMainHandItem()));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
