package com.amotassic.dabaosword.ui;

import com.amotassic.dabaosword.api.CardPileInventory;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.AllRegs;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

import static com.amotassic.dabaosword.util.ModTools.*;

public class PileScreenHandler extends AbstractContainerMenu {
    private final Container inventory;

    public PileScreenHandler(int syncId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(syncId, inv, new CardPileInventory(inv.player));
    }

    public PileScreenHandler(int syncId, Inventory inv, Container inventory) {
        super(AllRegs.Other.PILE_SCREEN_HANDLER.get(), syncId);
        this.inventory = inventory;
        inventory.startOpen(inv.player);
        int j, k;
        for (j = 0; j < 4; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new PileSlot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }
        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.addSlot(new Slot(inv, k + j * 9 + 9, 8 + k * 18, 103 + j * 18));
            }
        }
        for (j = 0; j < 9; ++j) {
            this.addSlot(new Slot(inv, j, 8 + j * 18, 161));
        }
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.inventory.stopOpen(player);
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (button == 114) {
            if (!player.level().isClientSide && countCards(player) > 10) {
                ItemStack pile = trinketItem(ModItems.CARD_PILE, player);
                CompoundTag nbt = getOrCreateNbt(pile);
                int dropped = nbt.getInt("DroppedCards");
                ItemStack stack = getSlot(slotId).getItem();
                if (isCard(stack)) { //按下delete键后，如果卡片数量大于10，则丢弃卡片，当丢弃3张卡片后，摸一张牌
                    cardDiscard(player, stack, 1, false);
                    if (dropped == 2) {
                        nbt.remove("DroppedCards");
                        draw(player);
                    } else nbt.putInt("DroppedCards", dropped + 1);
                    pile.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                }
            }
            return;
        }
        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2.hasItem()) {
            ItemStack itemStack2 = slot2.getItem();
            itemStack = itemStack2.copy();
            if (slot < 36 ? !this.moveItemStackTo(itemStack2, 4 * 9, this.slots.size(), true) : !this.moveItemStackTo(itemStack2, 0, 4 * 9, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot2.setByPlayer(ItemStack.EMPTY);
            } else {
                slot2.setChanged();
            }
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {return true;}

    private static class PileSlot extends Slot {
        public PileSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {return isCard(stack);}
    }
}
