package com.amotassic.dabaosword.item.equipment;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

import java.util.List;

public class SunshineSmile extends Item implements Equipable {
    public SunshineSmile() {super(new Properties().durability(999).rarity(Rarity.UNCOMMON));}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.sunshine_smile.tooltip"));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {return EquipmentSlot.HEAD;}
}
