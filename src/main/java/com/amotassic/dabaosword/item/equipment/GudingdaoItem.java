package com.amotassic.dabaosword.item.equipment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;

public class GudingdaoItem extends SwordItem {
    public GudingdaoItem() {
        super(Tiers.NETHERITE, new Properties().attributes(SwordItem.createAttributes(Tiers.NETHERITE, 5, -2.4F)).durability(999).rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.translatable("item.dabaosword.gudingdao.tooltip1").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal(""));
    }
}
