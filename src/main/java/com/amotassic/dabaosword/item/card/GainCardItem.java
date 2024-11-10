package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.amotassic.dabaosword.util.ModTools.cardUsePre;
import static com.amotassic.dabaosword.util.ModTools.draw;

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
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            int m;
            //摸牌
            if (user.getMainHandItem().is(ModItems.GAIN_CARD)) {
                if (user.isShiftKeyDown()) {m=user.getMainHandItem().getCount();} else {m=1;}
                draw(user,m);
                if (!user.isCreative()) {user.getMainHandItem().shrink(m);}
                return InteractionResultHolder.success(user.getMainHandItem());
            }
            //无中生有
            if (user.getMainHandItem().is(ModItems.WUZHONG)) {
                if (cardUsePre(user, user.getMainHandItem(), null)) return InteractionResultHolder.success(user.getMainHandItem());
            }
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }

    @Override
    public void cardUse(LivingEntity user, ItemStack stack, LivingEntity target) {
        if (user instanceof Player player) draw(player,2);
    }
}
