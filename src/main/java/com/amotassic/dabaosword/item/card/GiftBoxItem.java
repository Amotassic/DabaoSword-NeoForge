package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.util.LootEntry;
import com.amotassic.dabaosword.util.LootTableParser;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

import static com.amotassic.dabaosword.util.ModTools.*;

public class GiftBoxItem extends Item {
    public GiftBoxItem(Properties p_41383_) {super(p_41383_);}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.gift_box.tooltip").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide) {
            ItemStack stack = user.getOffhandItem();
            float chance;
            if (!stack.isEmpty() && stack.getItem() == Items.GOLD_INGOT) {
                chance = 0.01f + 0.01f * stack.getCount();
                stack.setCount(0);
                giftBox(user, chance);
            }
            if (!stack.isEmpty() && stack.getItem() == Items.GOLD_BLOCK) {
                chance = 0.01f + 0.09f * stack.getCount();
                stack.shrink(Math.min(stack.getCount(), 11));
                giftBox(user, chance);
            }
            if (!user.isCreative()) user.getItemInHand(hand).shrink(1);
        }
        return super.use(world, user, hand);
    }

    public void giftBox(Player player, float chance) {
        if (new Random().nextFloat() < chance) {
            List<LootEntry> lootEntries = LootTableParser.parseLootTable(ResourceLocation.fromNamespaceAndPath("dabaosword", "loot_tables/draw_skill.json"));
            LootEntry selectedEntry = GainCardItem.selectRandomEntry(lootEntries);

            ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(selectedEntry.item()));
            if (stack.getItem() != Items.AIR) voice(player, Sounds.GIFTBOX.get(),3);
            give(player, stack);
        }
    }
}
