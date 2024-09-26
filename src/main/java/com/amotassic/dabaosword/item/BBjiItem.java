package com.amotassic.dabaosword.item;

import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static com.amotassic.dabaosword.util.ModTools.voice;

public class BBjiItem extends Item {
    public BBjiItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.bbji.tooltip"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide) {
            AABB box = new AABB(user.getOnPos()).inflate(13);
            for (LivingEntity nearbyEntity : world.getEntitiesOfClass(LivingEntity.class, box, LivingEntity -> LivingEntity != user)) {
                nearbyEntity.invulnerableTime = 0;
                nearbyEntity.hurt(user.damageSources().sonicBoom(user),2);
            }
            voice(user, Sounds.BBJI);
            ItemStack stack = user.getItemInHand(hand);
            EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            stack.hurtAndBreak(1, user, slot);
        }
        return super.use(world, user, hand);
    }
}
