package com.amotassic.dabaosword.item;

import com.amotassic.dabaosword.item.skillcard.SkillItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;

public class GiftBoxItem extends Item {
    public GiftBoxItem() {super(new Item.Properties().rarity(Rarity.UNCOMMON));}

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
            SkillItem.changeSkill(player);
            if (!player.isCreative()) player.getMainHandItem().shrink(1);
            return InteractionResultHolder.success(player.getMainHandItem());
        }
        return InteractionResultHolder.pass(player.getMainHandItem());
    }
}
