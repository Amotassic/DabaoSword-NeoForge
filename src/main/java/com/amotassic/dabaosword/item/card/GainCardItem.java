package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.amotassic.dabaosword.util.ModTools.*;

public class GainCardItem extends CardItem {
    public GainCardItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!entity.level().isClientSide && entity instanceof Player player) {
            if (!player.isCreative() && !player.isSpectator() && stack.getItem() == ModItems.GAIN_CARD) {
                draw(player, stack.getCount());
                stack.setCount(0);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide) {
            int m;
            //摸牌
            if (user.getItemInHand(hand).getItem() == ModItems.GAIN_CARD) {
                if (user.isShiftKeyDown()) {m=user.getItemInHand(hand).getCount();} else {m=1;}
                draw(user,m);
                if (!user.isCreative()) {user.getItemInHand(hand).shrink(m);}
            }
            //无中生有
            if (user.getItemInHand(hand).getItem() == ModItems.WUZHONG) {
                draw(user,2);
                cardUsePost(user, user.getItemInHand(hand), user);
                voice(user, Sounds.WUZHONG);
            }
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }
}
