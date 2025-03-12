package com.amotassic.dabaosword.item.card;

import com.amotassic.dabaosword.api.Card;
import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static com.amotassic.dabaosword.api.event.CardEvents.cardUsePre;
import static com.amotassic.dabaosword.util.ModTools.*;

@SuppressWarnings("all")
public class CardItem extends Item implements Card {
    public CardItem() {super(new Properties());}

    @Override public Type getType() {return Type.ARMOURY;}

    public static class Wuzhong extends CardItem {
        @Override
        public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
            if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
                if (cardUsePre(user, user.getMainHandItem(), null)) return InteractionResultHolder.success(user.getMainHandItem());
            }
            return super.use(level, user, hand);
        }

        @Override
        public void cardUse(LivingEntity user, ItemStack stack, LivingEntity target) {draw(user,2);}
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        addSRTip(stack, tooltip);

        if (stack.is(ModItems.SHAN)) {
            tooltip.add(Component.translatable("item.dabaosword.shan.tip").withStyle(ChatFormatting.BOLD));
            tooltip.add(getTip());
        }

        if (stack.is(ModItems.PEACH)) {
            tooltip.add(getTip("1").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(getTip("2").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.translatable("item.dabaosword.recover.tip").withStyle(ChatFormatting.BOLD));
        }

        if (stack.is(ModItems.JIU)) {
            tooltip.add(getTip());
            tooltip.add(Component.translatable("item.dabaosword.recover.tip").withStyle(ChatFormatting.BOLD));
        }

        if (stack.is(ModItems.FIRE_ATTACK) || stack.is(ModItems.JIEDAO) || stack.is(ModItems.NANMAN) || stack.is(ModItems.TAOYUAN) || stack.is(ModItems.TIESUO) || stack.is(ModItems.JUEDOU)) tooltip.add(getTip());

        if (stack.is(ModItems.SHANDIAN_ITEM) || stack.is(ModItems.WUGU) || stack.is(ModItems.WUXIE) || stack.is(ModItems.STEAL) || stack.is(ModItems.WUZHONG) || stack.is(ModItems.DISCARD)) {
            tooltip.add(getTip("1"));
            tooltip.add(getTip("2"));
        }

        if (stack.is(ModItems.DISCARD) || stack.is(ModItems.JUEDOU)) {
            tooltip.add(Component.translatable("item.dabaosword.long_hand").withStyle(ChatFormatting.BOLD));
        }

        if (stack.is(ModItems.BINGLIANG_ITEM)) {
            if (Screen.hasShiftDown()) {
                tooltip.add(getTip("1"));
                tooltip.add(getTip("2"));
            } else {
                tooltip.add(getTip().withStyle(ChatFormatting.BLUE));
                tooltip.add(Component.translatable("dabaosword.shift_tip", Component.keybind("key.sneak")));
            }
        }

        if (stack.is(ModItems.TOO_HAPPY_ITEM)) {
            if (Screen.hasShiftDown()) {
                tooltip.add(getTip("1"));
                tooltip.add(getTip("2"));
            } else {
                tooltip.add(getTip().withStyle(ChatFormatting.RED));
                tooltip.add(Component.translatable("dabaosword.shift_tip", Component.keybind("key.sneak")));
            }
        }

        if (stack.is(ModItems.WANJIAN)) { //有大病的工具提示
            if (Screen.hasShiftDown()) {
                int i = (int) (System.currentTimeMillis() / 1000) % 7;
                switch (i) {
                    case 1 -> tooltip.add(getTip("1").withStyle(ChatFormatting.AQUA));
                    case 2 -> tooltip.add(getTip("2").withStyle(ChatFormatting.RED));
                    case 3 -> tooltip.add(getTip("3").withStyle(ChatFormatting.GOLD));
                    case 4 -> tooltip.add(getTip("4").withStyle(ChatFormatting.GREEN));
                    case 5 -> tooltip.add(getTip("5").withStyle(ChatFormatting.DARK_PURPLE));
                    case 6 -> tooltip.add(getTip("6").withStyle(ChatFormatting.YELLOW));
                    case 0 -> tooltip.add(getTip("7").withStyle(ChatFormatting.BLUE));
                }
            } else {
                tooltip.add(getTip());
                tooltip.add(Component.translatable("item.dabaosword.wanjian.shift", Component.keybind("key.sneak")).withStyle(ChatFormatting.ITALIC));
            }
        }
    }

    public MutableComponent getTip() {return getTip("");}
    public MutableComponent getTip(String suffix) {
        return Component.translatable(getDescriptionId() + ".tooltip" + suffix);
    }

    public static void addSRTip(ItemStack stack, List<Component> tooltip) {
        var sr = getSuitAndRank(stack);
        if (sr != null) {
            Card.Suits suit = sr.getA(); Card.Ranks rank = sr.getB();
            if (isRedCard.test(stack)) tooltip.add(Component.translatable("card.suit_and_rank", suit.suit, rank.rank).withStyle(ChatFormatting.RED));
            else tooltip.add(Component.translatable("card.suit_and_rank", suit.suit, rank.rank));
        }
    }
}
