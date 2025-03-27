package com.amotassic.dabaosword.ui;

import com.amotassic.dabaosword.api.CardEvents;
import com.amotassic.dabaosword.api.skill.ExData;
import com.amotassic.dabaosword.api.skill.Skill;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.AllRegs;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
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
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.amotassic.dabaosword.api.CardEvents.cardDiscard;
import static com.amotassic.dabaosword.util.ModTools.*;

public class PlayerInvScreenHandler extends AbstractContainerMenu {
    private final Player target;
    private final ItemStack stack;
    private final int cards;
    private final boolean isPlayerInv;
    private final Skill skill;
    private final List<Integer> clicks = new ArrayList<>();
    @Nullable private Player invOwner;

    public PlayerInvScreenHandler(int syncId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(syncId, new SimpleContainer(60), (Player) inv.player.level().getEntity(buf.readInt()));
    }

    public PlayerInvScreenHandler(int syncId, Container inventory, Player target) {
        super(AllRegs.Other.PLAYER_INV_SCREEN_HANDLER.get(), syncId);
        this.target = target;
        this.cards = inventory.getItem(54).getCount();
        this.stack = inventory.getItem(55);
        this.isPlayerInv = !inventory.getItem(56).isEmpty();
        this.skill = s(stack);
        for (int i = 0; i < 6; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 16 + i * 18));
            }
        }
        inventory.setItem(57, paibei());
        for (int i = 0; i < 4; i++) addSlot(new Slot(inventory, 54 + i, 114514, 114514));
    }

    @Override
    public void clicked(int index, int button, @NotNull ClickType action, @NotNull Player player) {
        if (index >= 0 && index < 54 && target != null) {
            if (invOwner == null) invOwner = isPlayerInv ? player : target;
            //System.out.println(index + " button: " + button + " action: " + action);
            var selectedStack = getStack(index);
            if (button == 65 && maxSelect() >= 100) forEachNonEmptySlot(s -> addClick(s.getContainerSlot(), 99)); //全选
            if (button == 90) forEachNonEmptySlot(s -> setClick(s.getContainerSlot(), 0)); //清空
            if (!selectedStack.isEmpty()) skill.item.onSlotClick(this, player, skill, target, index, button, action);
            writeClicks();

            if (stack.is(ModItems.WANJIAN)) {
                ItemStack mainHand = player.getMainHandItem(); var mainCopy = mainHand.copy();
                var cards = getCardPack(player);
                ItemStack selected = ItemStack.EMPTY; //对选择的卡牌进行赋值
                if (index == 8) selected = player.getOffhandItem();
                if (8 < index && index < 45) selected = cards.getItem(index - 9);
                if (index >= 45) selected = selectedStack;
                var copy = selected.copy(); //复制一份已选物品方便代码操作

                if (!selected.isEmpty()) {
                    if (isCard(mainHand)) { //如果主手物品是卡牌，就把主手物品设置为选择的牌，然后主手物品进入牌堆背包
                        player.setItemInHand(InteractionHand.MAIN_HAND, copy);
                        selected.setCount(0);
                        cards.insertStack(mainCopy);
                    } else {
                        if (index == 8 || index >= 45) { //如果选的不是牌堆中的牌，交换两者位置
                            player.setItemInHand(InteractionHand.MAIN_HAND, copy);
                            selected.setCount(0);
                            if (index == 8) player.setItemInHand(InteractionHand.OFF_HAND, mainCopy); //处理副手
                            if (index >= 45) player.getInventory().setItem(index - 45, mainCopy);
                        } else {
                            int emptySlot = player.getInventory().getFreeSlot();
                            if (emptySlot == -1) player.displayClientMessage(Component.translatable("card_pile.player_inv.full").withStyle(ChatFormatting.RED), true);
                            else { //如果选择牌堆中的牌且背包未满，则将主手物品设为选择的牌，主手物品移动到其它空槽位（显然不包括副手）
                                player.setItemInHand(InteractionHand.MAIN_HAND, copy);
                                cards.removeItemNoUpdate(index - 9);
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
                if (stack.is(ModItems.STEAL)) {
                    Component message = Component.translatable("dabaosword.steal", player.getDisplayName(), target.getDisplayName(), selectedStack.getDisplayName());
                    player.displayClientMessage(message, false);
                    target.displayClientMessage(message, false);
                    if (isCard(selectedStack)) { //如果选择的物品是卡牌才触发事件
                        var exData = d().cards(selectedStack, 1, index < 4);
                        CardEvents.cardMove(target, exData, player);
                    } else {
                        give(player, selectedStack.copyWithCount(1)); /*顺手：复制一个物品*/
                        selectedStack.shrink(1);
                    }
                    closeGUI(player);
                }

                if (stack.is(ModItems.DISCARD)) {
                    Component message = Component.translatable("dabaosword.discard", player.getDisplayName(), target.getDisplayName(), selectedStack.getDisplayName());
                    player.displayClientMessage(message, false);
                    target.displayClientMessage(message, false);
                    var exData = d().cards(selectedStack, 1, index < 4);
                    cardDiscard(target, exData);
                    closeGUI(player);
                }
            }
        }
    }

    @Override
    public void removed(@NotNull Player player) {
        skill.item.onGuiClose(this, player, skill, target);
    }

    /**获取该容器内对应slot上的物品，即使卡牌以牌背形态显示，也返回目标对应的卡牌*/
    public ItemStack getStack(int slotIndex) {
        if (slotIndex < 0 || slotIndex > 53) return ItemStack.EMPTY;
        var item = getSlot(slotIndex).getItem();
        if (cards != 1) return item;
        if (invOwner != null && item.is(ModItems.GAIN_CARD)) {
            var pack = getCardPack(invOwner); var main = invOwner.getInventory().items;
            if (pack.isEmpty()) return main.get(slotIndex - 9);
            else {
                if (slotIndex < 45) return pack.getItem(slotIndex - 9);
                else return main.get(slotIndex - 45);
            }
        }
        return item;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {return ItemStack.EMPTY;}

    @Override
    public boolean stillValid(Player player) {
        var effect = player.getEffect(ModItems.COOLDOWN2);
        return effect == null || effect.getAmplifier() != 2;
    }

    public void forEachNonEmptySlot(Consumer<Slot> action) {
        for (int i = 0; i < 54; i++) {
            Slot slot = getSlot(i);
            if (slot.hasItem()) action.accept(slot);
        }
    }

    private void writeClicks() {
        ItemStack cinfo = getSlot(57).getItem();
        CompoundTag nbt = new CompoundTag();
        nbt.putString("Clicks", getClickMap().toString());
        setNbt(cinfo, nbt);
    }

    public int maxSelect() {return skill.getMaxSelect();}

    private int getSlotClicked(int slot) {
        if (!clicks.contains(slot)) return 0;
        return getClickMap().get(slot);
    }

    /**使点击的槽位物品选择数量增加，如果增加后已选择物品数量大于最大可选数量，会自动移除最早的选择项*/
    public void addClick(int slot, int... count) {
        int num = count.length > 0 ? count[0] : 1;
        int n = getSlot(slot).getItem().getCount();
        num = Math.min(n - getSlotClicked(slot), num);
        for (int i = 0; i < num; i++) {
            clicks.add(slot);
            if (getSelectedCount() > maxSelect()) dropFirst();
        }
    }

    /**使点击的槽位物品选择数量减少*/
    public void dropClick(int slot, int... count) {
        if (!clicks.contains(slot)) return;
        int num = count.length > 0 ? count[0] : 1;
        for (int i = 0; i < num; i++) {
            if (!clicks.remove(Integer.valueOf(slot))) break;
        }
    }

    /**设置该槽位的选择数*/
    public void setClick(int slot, int count) {
        if (clicks.contains(slot)) dropClick(slot, 114514);
        if (count > 0) addClick(slot, count);
    }

    public void dropFirst(int... count) {
        if (clicks.isEmpty()) return;
        dropClick(clicks.getFirst(), count);
    }

    /**@return GUI中已选择卡牌的数量*/
    public int getSelectedCount() {return clicks.size();}

    public List<ItemStack> getSelected() {
        List<ItemStack> selected = new ArrayList<>();
        clicks.forEach(slot -> {
            ItemStack stack = getSlot(slot).getItem();
            if (!stack.isEmpty()) selected.add(stack.copyWithCount(1));
        });
        return selected;
    }

    public Map<Integer, Integer> getClickMap() {
        Map<Integer, Integer> map = new HashMap<>();
        for (Integer num : clicks) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else map.put(num, 1);
        }
        return map;
    }

    /**将GUI中已选的卡牌直接输出为ExData，省去手动获取已选卡牌的功夫，一般用于{@link com.amotassic.dabaosword.api.skill.ISkill#onGuiClose(PlayerInvScreenHandler, Player, Skill, Player)}*/
    public ExData toExData() {
        var exData = d(); var clickMap = getClickMap();
        clickMap.forEach((slot, count) -> {
            ItemStack stack = getStack(slot);
            if (isCard(stack)) exData.cards(stack, count, slot < 4);
        });
        return exData;
    }

}
