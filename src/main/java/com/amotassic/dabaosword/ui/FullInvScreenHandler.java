package com.amotassic.dabaosword.ui;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.AllRegs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FullInvScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final LivingEntity target;
    private final boolean editable;

    public FullInvScreenHandler(int syncId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(syncId, inv, new SimpleContainer(64), (LivingEntity) inv.player.level().getEntity(buf.readInt()));
    }

    public FullInvScreenHandler(int syncId, Inventory playerInv, Container inventory, LivingEntity target) {
        super(AllRegs.Other.FULL_INV_SCREEN_HANDLER.get(), syncId);
        this.inventory =inventory;
        this.target = target;
        this.editable = !inventory.getItem(61).isEmpty();
        int i;
        for (i = 0; i < 4; ++i) { //物品栏
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }
        for (i = 0; i < 5; ++i) addSlot(new Slot(inventory, i + 36, 170, 18 + i * 18)); //盔甲、副手
        for (i = 0; i < 2; ++i) { //饰品栏
            for (int j = 0; j < 10; ++j) {
                addSlot(new Slot(inventory, j + i * 10 + 41, 8 + j * 18, 108 + i * 18));
            }
        }
        if (playerInv.player != target) addPlayerInventorySlots(playerInv);
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (editable) {
            super.clicked(slotId, button, clickType, player);
            saveInv(inventory, target);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return newStack;
    }

    public static void saveInv(Container from, LivingEntity to) {
        for (int i = 0; i < 61; i++) {
            ItemStack stack = from.getItem(i);
            if (i == 0 && !(to instanceof Player || to instanceof Villager)) to.setItemInHand(InteractionHand.MAIN_HAND, stack);
            if (i < 8 && to instanceof Villager villager) villager.getInventory().setItem(i, stack);
            if (i < 36 && to instanceof Player player)  player.getInventory().setItem(i, stack);
            if (i == 36) to.setItemSlot(EquipmentSlot.HEAD, stack);
            if (i == 37) to.setItemSlot(EquipmentSlot.CHEST, stack);
            if (i == 38) to.setItemSlot(EquipmentSlot.LEGS, stack);
            if (i == 39) to.setItemSlot(EquipmentSlot.FEET, stack);
            if (i == 40) to.setItemInHand(InteractionHand.OFF_HAND, stack);
            if (i >= 41) {
                var pair = findSlot(to, i - 41);
                if (pair != null) pair.getA().setStackInSlot(pair.getB(), stack);
            }
        }
    }

    public static Tuple<IDynamicStackHandler, Integer> findSlot(LivingEntity entity, int index) {
        //将饰品栏的每一格添加到一个List中，若index与List中的饰品格的序列号相同，则输出该饰品格
        var component = CuriosApi.getCuriosInventory(entity);
        List<Tuple<IDynamicStackHandler, Integer>> pairs = new ArrayList<>();
        if (component.isPresent()) {
            var slots = component.get().getCurios().values();
            for (var group : slots) {
                for (int i = 0; i < group.getSlots(); i++) {
                    pairs.add(new Tuple<>(group.getStacks(), i));
                }
            }
        }
        for (var pair : pairs) {
            if (pairs.indexOf(pair) == index) return pair;
        }
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.hasEffect(ModItems.COOLDOWN2) || (player.hasEffect(ModItems.COOLDOWN2) && Objects.requireNonNull(player.getEffect(ModItems.COOLDOWN2)).getAmplifier() != 2);
    }

    private void addPlayerInventorySlots(Inventory playerInventory) {
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 17 + j * 18, 158 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 17 + i * 18, 216));
        }
    }
}
