package com.amotassic.dabaosword.item.equipment;

import com.amotassic.dabaosword.item.card.CardItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
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

    public static void arrowRain(LivingEntity entity, float speed) {
        ServerLevel world = (ServerLevel) entity.level();
        int[] angles = {10, 5, 0, -5, -10};
        for (int angle : angles) {summonArrow(entity, angle, speed);}
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
    }

    private static void summonArrow(LivingEntity entity, int angle, float speed) {
        ItemStack stack = new ItemStack(Items.ARROW);
        ServerLevel world = (ServerLevel) entity.level();
        Arrow arrow = new Arrow(world, entity, stack, null);
        arrow.setCustomName(Component.nullToEmpty("a"));
        arrow.shootFromRotation(entity, entity.getXRot(), entity.getYRot() + angle, 0.0F, speed, 1.0F);
        arrow.setCritArrow(true);
        world.addFreshEntity(arrow);
    }
}
