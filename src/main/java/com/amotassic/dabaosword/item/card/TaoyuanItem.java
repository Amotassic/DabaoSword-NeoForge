package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.amotassic.dabaosword.api.event.CardEvents.cardUsePre;
import static com.amotassic.dabaosword.util.ModTools.voice;

public class TaoyuanItem extends CardItem {
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (cardUsePre(user, user.getMainHandItem(), null)) return InteractionResultHolder.success(user.getMainHandItem());
        }
        return super.use(world, user, hand);
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity target) {
        ((ServerLevel) user.level()).players().forEach(player -> {
            player.heal(5.0F);
            if (player!= user) voice(player, Sounds.TAOYUAN);
        });
    }
}
