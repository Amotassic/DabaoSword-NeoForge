package com.amotassic.dabaosword.item.skillcard;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Objects;

import static com.amotassic.dabaosword.util.ModTools.noTieji;

public class ShensuSkill extends SkillItem {
    public ShensuSkill(Properties p_41383_) {super(p_41383_);}

    public static double speed;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide && entity instanceof Player player && noTieji(player)) {
            double d = Math.min(getEmptySlots(player), 20d) / 40; //当空余20格时，获得最大加成0.5
            gainSpeed(player, Math.max(0, d));
        }

        if (entity.level().isClientSide && entity instanceof Player player) {
            Vec3 lastPos = new Vec3(player.xOld, player.yOld, player.zOld);
            speed = player.position().distanceTo(lastPos) * 20;
            //if (speed > 0) player.displayClientMessage(Component.literal("Speed: " + speed), true);
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!entity.level().isClientSide) gainSpeed(entity,0);
    }

    public static void gainSpeed(LivingEntity entity, double value) {
        AttributeModifier modifier = new AttributeModifier(ResourceLocation.parse("shensu"), value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        Objects.requireNonNull(entity.getAttributes().getInstance(Attributes.MOVEMENT_SPEED)).addOrUpdateTransientModifier(modifier);
    }

    private int getEmptySlots(Player player) {
        int i = 0;
        for (var slot : player.getInventory().items) {if (slot.isEmpty()) i++;}
        return i;
    }
}
