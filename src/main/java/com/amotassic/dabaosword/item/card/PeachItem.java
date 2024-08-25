package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.amotassic.dabaosword.util.ModTools.*;

public class PeachItem extends CardItem {
    public PeachItem(Properties p_41383_) {super(p_41383_);}

    //非潜行时右键，给自己回血
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide && player.getHealth() < player.getMaxHealth() && !player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            player.heal(5);
            voice(player, Sounds.RECOVER.get()); benxi(player);
            if (!player.isCreative()) {stack.shrink(1);}
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        ItemStack stack1 = user.getItemInHand(hand);
        if (!user.level().isClientSide && user.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(5); benxi(user);
                entity.playSound(Sounds.RECOVER.get(),1.0F,1.0F);
                if (!user.isCreative()) {stack1.shrink(1);}
                return InteractionResultHolder.success(!user.level().isClientSide).getResult();
            }
        }
        return InteractionResult.PASS;
    }
}
