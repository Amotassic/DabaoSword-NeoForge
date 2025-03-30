package com.amotassic.dabaosword.item.tool;

import com.amotassic.dabaosword.ui.PileScreenHandler;
import com.amotassic.dabaosword.util.Gamerule;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

import static com.amotassic.dabaosword.util.ModTools.getCardPack;
import static com.amotassic.dabaosword.util.ModTools.isCard;

@SuppressWarnings("all")
public class CardPile extends Item implements ICurioItem {
    public CardPile() {super(new Properties().stacksTo(1));}

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tooltip"));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tip1").withStyle(ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tip2", Component.keybind("key.dabaosword.select_card")).withStyle(ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tip3", Component.keybind("key.sprint"), Component.keybind("key.dabaosword.select_card")).withStyle(ChatFormatting.BOLD));
        tooltip.add(Component.translatable("item.dabaosword.card_pile.tip4", Component.keybind("key.sprint"), Component.keybind("key.sneak"), Component.keybind("key.dabaosword.select_card")).withStyle(ChatFormatting.BOLD));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity.level() instanceof ServerLevel world && entity instanceof Player player) {
            long time = world.getGameTime();
            int skill = world.getGameRules().getInt(Gamerule.CHANGE_SKILL_INTERVAL) * 20;

            if (world.getGameRules().getBoolean(Gamerule.CARD_PILE_HUNGERLESS)) player.getFoodData().setFoodLevel(20);

            if (skill >= 0) {
                if (skill == 0) player.addTag("change_skill");
                else if (time % skill == 0) { //每5分钟可以切换技能
                    player.addTag("change_skill");
                    if (skill >= 600) {
                        player.displayClientMessage(Component.translatable("dabaosword.change_skill").withStyle(ChatFormatting.BOLD), false);
                        player.displayClientMessage(Component.translatable("dabaosword.change_skill2"), false);
                    }
                }
            }

            if (player.containerMenu.getClass() != PileScreenHandler.class && time % 20 == 0) {
                var cards = getCardPack(player);
                for (int i = 9; i < 36; i++) {
                    ItemStack item = player.getInventory().items.get(i);
                    if (isCard(item) && cards.isNotFull()) {
                        cards.insertStack(item.copy());
                        item.setCount(0);
                    }
                }
            }
        }
    }
}
