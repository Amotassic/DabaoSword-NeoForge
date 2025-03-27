package com.amotassic.dabaosword.item.tool;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.List;

public class SunshineSmile extends Item implements Equipable {
    public SunshineSmile() {super(new Properties().durability(999).rarity(Rarity.UNCOMMON).component(DataComponents.UNBREAKABLE, new Unbreakable(true)));}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.sunshine_smile.tooltip"));
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {return EquipmentSlot.HEAD;}

    @Override
    public int getEnchantmentValue() {return 25;}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return this.swapWithEquipmentSlot(this, level, player, usedHand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slotId, boolean isSelected) {
        if (world instanceof ServerLevel sw && sw.getGameTime() % 1200 == 0) {
            var entry = ModTools.getEntry(ModItems.CRIT);
            if (EnchantmentHelper.getTagEnchantmentLevel(entry, stack) == 0) {
                stack.enchant(entry, 1);
            }
        }
    }
}
