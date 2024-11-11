package com.amotassic.dabaosword.item.equipment;

import com.amotassic.dabaosword.api.CardPileInventory;
import com.amotassic.dabaosword.ui.PileScreenHandler;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CardPile extends Equipment {
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tooltip"));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tip1").withStyle(ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tip2").withStyle(ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tip3").withStyle(ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tip4").withStyle(ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tip5").withStyle(ChatFormatting.BOLD));
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {return true;}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity.level() instanceof ServerLevel world && entity instanceof Player player) {
            if (player.containerMenu.getClass() != PileScreenHandler.class && world.getGameTime() % 20 == 0) {
                CardPileInventory cards = new CardPileInventory(player);
                for (int i = 9; i < 36; i++) {
                    ItemStack item = player.getInventory().items.get(i);
                    if (ModTools.isCard(item) && cards.isNotFull()) {
                        cards.insertStack(item.copy());
                        item.setCount(0);
                    }
                }
            }
        }
    }

    @Override
    public int onDrawPhase(Player player, ItemStack stack) {return 2;}
}
