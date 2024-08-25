package com.amotassic.dabaosword.item.equipment;

import com.amotassic.dabaosword.item.card.CardItem;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ArrowRainItem extends CardItem {
    public ArrowRainItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            arrowRain(player, 5);
            if (!player.isCreative()) stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    public static void arrowRain(Player player, float speed) {
        ItemStack stack = new ItemStack(Items.ARROW);
        Component a = Component.nullToEmpty("a");
        Level world = player.level();
        Arrow arrow1 = new Arrow(world, player, stack, null);arrow1.setCustomName(a);
        Arrow arrow2 = new Arrow(world, player, stack, null);arrow2.setCustomName(a);
        Arrow arrow3 = new Arrow(world, player, stack, null);arrow3.setCustomName(a);
        Arrow arrow4 = new Arrow(world, player, stack, null);arrow4.setCustomName(a);
        Arrow arrow5 = new Arrow(world, player, stack, null);arrow5.setCustomName(a);
        arrow1.shootFromRotation(player, player.getXRot(), player.getYRot()+10, 0.0F, speed, 1.0F);
        arrow2.shootFromRotation(player, player.getXRot(), player.getYRot()+5, 0.0F, speed, 1.0F);
        arrow3.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, speed, 1.0F);
        arrow4.shootFromRotation(player, player.getXRot(), player.getYRot()-5, 0.0F, speed, 1.0F);
        arrow5.shootFromRotation(player, player.getXRot(), player.getYRot()-10, 0.0F, speed, 1.0F);
        arrow1.setCritArrow(true);arrow2.setCritArrow(true);arrow3.setCritArrow(true);arrow4.setCritArrow(true);arrow5.setCritArrow(true);
        world.addFreshEntity(arrow1);world.addFreshEntity(arrow2);world.addFreshEntity(arrow3);world.addFreshEntity(arrow4);world.addFreshEntity(arrow5);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
    }
}
