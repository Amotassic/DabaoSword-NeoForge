package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.amotassic.dabaosword.util.ModTools.*;

public class TaoyuanItem extends CardItem {
    public TaoyuanItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            ((ServerLevel) world).players().forEach(player -> player.heal(5.0F));
            ((ServerLevel) world).players().forEach(player -> voice(player, Sounds.TAOYUAN.get()));
            jizhi(user); benxi(user);
            if (!user.isCreative()) {user.getItemInHand(hand).shrink(1);}
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }
}
