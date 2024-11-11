package com.amotassic.dabaosword.item.card;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.amotassic.dabaosword.util.ModTools.draw;

@SuppressWarnings("all")
public class GainCardItem extends Item {
    public GainCardItem() {super(new Properties());}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.gain_card.tooltip"));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!entity.level().isClientSide && entity instanceof Player player) {
            if (!player.isCreative() && !player.isSpectator()) {
                draw(player, stack.getCount());
                stack.setCount(0);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            int m;
            if (user.isShiftKeyDown()) m=user.getMainHandItem().getCount(); else m=1;
            draw(user,m);
            return InteractionResultHolder.success(user.getMainHandItem());
        }
        return InteractionResultHolder.pass(user.getItemInHand(hand));
    }
}
