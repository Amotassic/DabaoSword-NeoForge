package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.event.listener.CardUsePostListener;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

import static com.amotassic.dabaosword.util.ModTools.voice;

public class PeachItem extends CardItem {
    public PeachItem(Properties p_41383_) {super(p_41383_);}

    //非潜行时右键，给自己回血
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide && player.getHealth() < player.getMaxHealth() && !player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            player.heal(5);
            NeoForge.EVENT_BUS.post(new CardUsePostListener(player, player.getItemInHand(hand), player));
            voice(player, Sounds.RECOVER.get());
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand) {
        if (!user.level().isClientSide && user.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(5);
                entity.playSound(Sounds.RECOVER.get(),1.0F,1.0F);
                NeoForge.EVENT_BUS.post(new CardUsePostListener(user, stack, user));
                return InteractionResultHolder.success(!user.level().isClientSide).getResult();
            }
        }
        return InteractionResult.PASS;
    }
}
