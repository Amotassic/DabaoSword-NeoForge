package com.amotassic.dabaosword.ui;

import com.amotassic.dabaosword.api.Skill;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.AllRegs;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static com.amotassic.dabaosword.api.event.CardEvents.*;
import static com.amotassic.dabaosword.util.ModTools.*;

public class PlayerInvScreenHandler extends AbstractContainerMenu {
    private final Player target;
    private final ItemStack stack;
    private final int cards;
    private final boolean isPlayerInv;

    public PlayerInvScreenHandler(int syncId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(syncId, new SimpleContainer(60), (Player) inv.player.level().getEntity(buf.readInt()));
    }

    public PlayerInvScreenHandler(int syncId, Container inventory, Player target) {
        super(AllRegs.Other.PLAYER_INV_SCREEN_HANDLER.get(), syncId);
        this.target = target;
        this.cards = inventory.getItem(54).getCount();
        this.stack = inventory.getItem(55);
        this.isPlayerInv = !inventory.getItem(56).isEmpty();
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 16 + i * 18));
            }
        }
    }

    @Override
    public void clicked(int slotIndex, int button, @NotNull ClickType clickType, @NotNull Player player) {
        if (slotIndex >= 0 && slotIndex < 54 && target != null) {
            var selectedStack = selected(isPlayerInv ? player : target, slotIndex);

            if (stack.is(ModItems.WANJIAN)) {
                ItemStack mainHand = player.getMainHandItem(); var mainCopy = mainHand.copy();
                var cards = getCardPack(player);
                ItemStack selected = ItemStack.EMPTY; //对选择的卡牌进行赋值
                if (slotIndex == 8) selected = player.getOffhandItem();
                if (8 < slotIndex && slotIndex < 45) selected = cards.getItem(slotIndex - 9);
                if (slotIndex >= 45) selected = selectedStack;
                var copy = selected.copy(); //复制一份已选物品方便代码操作

                if (!selected.isEmpty()) {
                    if (isCard(mainHand)) { //如果主手物品是卡牌，就把主手物品设置为选择的牌，然后主手物品进入牌堆背包
                        player.setItemInHand(InteractionHand.MAIN_HAND, copy);
                        selected.setCount(0);
                        cards.insertStack(mainCopy);
                    } else {
                        if (slotIndex == 8 || slotIndex >= 45) { //如果选的不是牌堆中的牌，交换两者位置
                            player.setItemInHand(InteractionHand.MAIN_HAND, copy);
                            selected.setCount(0);
                            if (slotIndex == 8) player.setItemInHand(InteractionHand.OFF_HAND, mainCopy); //处理副手
                            if (slotIndex >= 45) player.getInventory().setItem(slotIndex - 45, mainCopy);
                        } else {
                            int emptySlot = player.getInventory().getFreeSlot();
                            if (emptySlot == -1) player.displayClientMessage(Component.translatable("card_pile.player_inv.full").withStyle(ChatFormatting.RED), true);
                            else { //如果选择牌堆中的牌且背包未满，则将主手物品设为选择的牌，主手物品移动到其它空槽位（显然不包括副手）
                                player.setItemInHand(InteractionHand.MAIN_HAND, copy);
                                cards.removeItemNoUpdate(slotIndex - 9);
                                //如果主手原本是空的，就不需要交换这一步，这点很重要
                                if (!mainCopy.isEmpty()) player.getInventory().setItem(emptySlot, mainCopy);
                            }
                        }
                    }
                }
                closeGUI(player);
            }

            if (stack.is(ModItems.SUNSHINE_SMILE)) {
                ItemStack mainHand = player.getMainHandItem();
                if (selectedStack.isEmpty()) { //如果玩家点了一个空的格子————
                    int emptySlot = player.getInventory().getFreeSlot();
                    if (emptySlot != -1) { //如果主手不为空，就把主手的物品移动到其他空格子，主手设为空
                        player.getInventory().setItem(emptySlot, mainHand.copy());
                        mainHand.setCount(0);
                    }
                } else { //如果玩家选了一个非空的格子，就交换主手和该格子的物品
                    ItemStack mainCopy = mainHand.copy(); ItemStack swapCopy = selectedStack.copy();
                    if (player.getOffhandItem().equals(selectedStack)) {
                        player.setItemInHand(InteractionHand.MAIN_HAND, swapCopy);
                        player.setItemInHand(InteractionHand.OFF_HAND, mainCopy);
                    } else {
                        int swapSlot = player.getInventory().findSlotMatchingItem(selectedStack);
                        player.setItemInHand(InteractionHand.MAIN_HAND, swapCopy);
                        player.getInventory().setItem(swapSlot, mainCopy);
                    }
                }
                closeGUI(player);
            }

            if (selectedStack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof Skill skill) skill.onClickGUISlot(player, stack, target, selectedStack, slotIndex);

                if (stack.is(ModItems.STEAL)) {
                    Component message = Component.translatable("dabaosword.steal", player.getDisplayName(), target.getDisplayName(), selectedStack.getDisplayName());
                    player.displayClientMessage(message, false);
                    target.displayClientMessage(message, false);
                    if (isCard(selectedStack)) cardMove(target, player, selectedStack, 1, slotIndex < 4, false);
                        //如果选择的物品是卡牌才触发事件
                    else {give(player, selectedStack.copyWithCount(1)); /*顺手：复制一个物品*/
                        selectedStack.shrink(1);}
                    cardUsePost(player, stack, target);
                    closeGUI(player);
                }

                if (stack.is(ModItems.DISCARD)) {
                    Component message = Component.translatable("dabaosword.discard", player.getDisplayName(), target.getDisplayName(), selectedStack.getDisplayName());
                    player.displayClientMessage(message, false);
                    target.displayClientMessage(message, false);
                    cardDiscard(target, selectedStack, 1, slotIndex < 4);
                    cardUsePost(player, stack, target);
                    closeGUI(player);
                }
            }
        }
    }

    private ItemStack selected(Player player, int slotIndex) {
        var itemStack = getSlot(slotIndex).getItem();
        if (itemStack.isEmpty() && cards == 1 && slotIndex >= 8) {
            List<ItemStack> candidate = ModTools.getItems(player, isCard, true, false, false, true);
            if(!candidate.isEmpty()) return candidate.get(new Random().nextInt(candidate.size()));
        }
        return itemStack;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {return ItemStack.EMPTY;}

    @Override
    public boolean stillValid(Player player) {
        return !player.hasEffect(ModItems.COOLDOWN2) || (player.hasEffect(ModItems.COOLDOWN2) && Objects.requireNonNull(player.getEffect(ModItems.COOLDOWN2)).getAmplifier() != 2);
    }
}
