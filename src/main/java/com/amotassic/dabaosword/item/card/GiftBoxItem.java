package com.amotassic.dabaosword.item.card;

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
        ItemStack stack = user.getOffhandItem();
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND && !stack.isEmpty()) {
            float chance = 0.01f;
            if (stack.getItem() == Items.GOLD_INGOT) {
                chance += 0.01f * stack.getCount();
                stack.setCount(0);
                return giftBox(user, chance);
            }
            if (stack.getItem() == Items.GOLD_BLOCK) {
                chance += 0.09f * stack.getCount();
                stack.shrink(Math.min(stack.getCount(), 11));
                return giftBox(user, chance);
            }
        }
        return super.use(world, user, hand);
    }

    public InteractionResultHolder<ItemStack> giftBox(Player player, float chance) {
        if (new Random().nextFloat() < chance) {
            var selectedId = parseLootTable(ResourceLocation.fromNamespaceAndPath("dabaosword", "loot_tables/draw_skill.json"));
            ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(selectedId));
            if (stack.getItem() != Items.AIR) voice(player, Sounds.GIFTBOX,3);
            give(player, stack);
            if (!player.isCreative()) player.getMainHandItem().shrink(1);
            return InteractionResultHolder.success(player.getMainHandItem());
        }
        return InteractionResultHolder.pass(player.getMainHandItem());
    }
}
