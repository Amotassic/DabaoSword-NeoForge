package com.amotassic.dabaosword.api;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CardPileInventory implements Container {
    public NonNullList<ItemStack> cards;
    public Player player;
    public ItemStack pile;
    public NonNullList<ItemStack> nonEmpty = NonNullList.create();

    public CardPileInventory(Player player) {
        this.player = player;
        this.pile = ModTools.trinketItem(ModItems.CARD_PILE, player);
        this.cards = NonNullList.withSize(36, ItemStack.EMPTY);
        readNbt();
        for (var stack : cards) { //生成非空的卡牌列表
            if (stack.isEmpty()) continue;
            nonEmpty.add(stack);
        }
    }

    @Override
    public void stopOpen(@NotNull Player player) {writeNbtToStack();}

    public int getEmptySlot() {
        for (int i = 0; i < getContainerSize(); ++i) {
            if (!cards.get(i).isEmpty()) continue;
            return i;
        }
        return -1;
    }

    public void readNbt() {
        if (pile.get(DataComponents.CUSTOM_DATA) != null) {
            ListTag list = (ListTag) Objects.requireNonNull(pile.get(DataComponents.CUSTOM_DATA)).copyTag().get("Items");
            if (list != null) readNbt(list);
        }
    }

    public void readNbt(ListTag nbtList) {
        cards.clear();
        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag nbtCompound = nbtList.getCompound(i);
            int j = nbtCompound.getByte("Slot");
            ItemStack itemStack = ItemStack.parse(player.registryAccess(), nbtCompound).orElse(ItemStack.EMPTY);
            if (itemStack.isEmpty()) continue;
            if (j >= 0 && j < getContainerSize()) setItem(j, itemStack);
        }
    }

    public void writeNbtToStack() { //当涉及牌堆物品变化后，必须调用这个方法
        ListTag nbtList = new ListTag();
        CompoundTag nbtCompound;
        for (int i = 0; i < getContainerSize(); ++i) {
            if (cards.get(i).isEmpty()) continue;
            nbtCompound = new CompoundTag();
            nbtCompound.putByte("Slot", (byte) i);
            nbtList.add(cards.get(i).save(player.registryAccess(), nbtCompound));
        }
        nbtCompound = ModTools.getOrCreateNbt(pile);
        nbtCompound.put("Items", nbtList);
        pile.set(DataComponents.CUSTOM_DATA, CustomData.of(nbtCompound));
    }

    @Override
    public int getContainerSize() {return cards.size();}

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : cards) {
            if (itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    public boolean isNotFull() {
        for (ItemStack itemStack : cards) {
            if (itemStack.isEmpty()) return true;
        }
        return false;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {return cards.get(slot);}

    public int getSlotWith(ItemStack stack) { //倒序检索
        for (int i = getContainerSize() - 1; i >= 0; i--) {
            ItemStack itemStack = getItem(i);
            if (itemStack.isEmpty()) continue;
            if (itemStack.equals(stack)) return i;
        }
        return -1;
    }

    public void removeStack(ItemStack stack, int count) {
        int i = getSlotWith(stack);
        if (i == -1) return;
        removeItem(i, count);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack stack = ContainerHelper.removeItem(cards, slot, amount);
        writeNbtToStack();
        return stack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack itemStack = cards.get(slot);
        cards.set(slot, ItemStack.EMPTY);
        writeNbtToStack();
        return itemStack;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {cards.set(slot, stack);}

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(@NotNull Player player) {return true;}

    @Override
    public void clearContent() {
        cards.clear();
        writeNbtToStack();
    }

    public void insertStack(ItemStack stack) {
        if (insertStack(-1, stack)) writeNbtToStack();;
    }

    public boolean insertStack(int slot, ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!stack.isDamaged()) {
            int i;
            do {
                i = stack.getCount();
                if (slot == -1) {
                    stack.setCount(addStack(stack));
                    continue;
                }
                stack.setCount(addStack(slot, stack));
            } while (!stack.isEmpty() && stack.getCount() < i);
            return stack.getCount() < i;
        }
        if (slot == -1) slot = getEmptySlot();
        if (slot >= 0) {
            cards.set(slot, stack.copyAndClear());
            return true;
        }
        return false;
    }

    private int addStack(ItemStack stack) {
        int i = getOccupiedSlotWithRoomForStack(stack);
        if (i == -1) i = getEmptySlot();
        if (i == -1) return stack.getCount();
        return addStack(i, stack);
    }

    private int addStack(int slot, ItemStack stack) {
        int i = stack.getCount();
        ItemStack itemStack = getItem(slot);
        if (itemStack.isEmpty()) {
            itemStack = stack.copyWithCount(0);
            setItem(slot, itemStack);
        }
        int j = getMaxStackSize(itemStack) - itemStack.getCount();
        int k = Math.min(i, j);
        if (k != 0) {
            i -= k;
            itemStack.grow(k);
        }
        return i;
    }

    public int getOccupiedSlotWithRoomForStack(ItemStack stack) {
        for (int i = 0; i < cards.size(); ++i) {
            if (!canStackAddMore(cards.get(i), stack)) continue;
            return i;
        }
        return -1;
    }

    private boolean canStackAddMore(ItemStack existingStack, ItemStack stack) {
        return !existingStack.isEmpty() && ItemStack.isSameItemSameComponents(existingStack, stack) && existingStack.isStackable() && existingStack.getCount() < this.getMaxStackSize(existingStack);
    }
}
