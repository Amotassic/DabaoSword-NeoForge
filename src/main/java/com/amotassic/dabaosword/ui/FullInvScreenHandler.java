package com.amotassic.dabaosword.ui;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModTools;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import java.util.HashSet;
import java.util.Set;

import static net.minecraft.world.inventory.InventoryMenu.*;
import static top.theillusivec4.curios.api.CuriosApi.getCuriosInventory;

public class FullInvScreenHandler extends AbstractContainerMenu {
    private final Container inventory;
    private final LivingEntity target;
    private final boolean editable;
    public final Set<Integer> slotsEnabled; //目标实体的可编辑槽位
    public final int rows; //总行数
    public final int armorRow; //盔甲栏以及副手物品所在的行
    public final boolean notSelf;

    public FullInvScreenHandler(int syncId, Inventory inv, FriendlyByteBuf buf) {
        super(ModItems.FULL_INV_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleContainer(86);
        this.target = (LivingEntity) inv.player.level().getEntity(buf.readInt());
        this.editable = buf.readBoolean();
        this.slotsEnabled = setup(inventory, target, editable);
        this.notSelf = inv.player != target;
        int row = 2, armor = 2; //行数和护甲栏所在行数
        int[] keySlots0 = new int[]{9, 18, 27};
        for (int i : keySlots0) {if (slotsEnabled.contains(i)) armor++;}
        this.armorRow = armor;
        int[] keySlots = new int[]{9, 18, 27, 41, 50, 59, 68, 77};
        for (int i : keySlots) {if (slotsEnabled.contains(i)) row++;}
        this.rows = row;
        int i, j;
        for (i = 0; i < 4; i++) { //物品栏
            for (j = 0; j < 9; j++) {
                int index = j + i * 9; boolean enabled = slotsEnabled.contains(index);
                int x = enabled ? 8 + j * 18 : 114514; int y = enabled ? 18 + i * 18 : 114514;
                addSlot(new Slot(inventory, index, x, y) {public boolean mayPlace(ItemStack stack) {return enabled;}});
            }
        }
        addSlot(new Slot(inventory, 36, 8 + 4 * 18, 18 * armor) {
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, EMPTY_ARMOR_SLOT_HELMET);
            }
        });
        addSlot(new Slot(inventory, 37, 8 + 5 * 18, 18 * armor) {
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, EMPTY_ARMOR_SLOT_CHESTPLATE);
            }
        });
        addSlot(new Slot(inventory, 38, 8 + 6 * 18, 18 * armor) {
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, EMPTY_ARMOR_SLOT_LEGGINGS);
            }
        });
        addSlot(new Slot(inventory, 39, 8 + 7 * 18, 18 * armor) {
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, EMPTY_ARMOR_SLOT_BOOTS);
            }
        });
        addSlot(new Slot(inventory, 40, 8 + 8 * 18, 18 * armor) {
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
        for (i = 0; i < 5; i++) { //饰品栏
            for (j = 0; j < 9; j++) {
                int index = 41 + j + i * 9; boolean enabled = slotsEnabled.contains(index);
                int x = enabled ? 8 + j * 18 : 114514; int y = enabled ? (armor + 1 + i) * 18 : 114514;
                addSlot(new Slot(inventory, index, x, y) {public boolean mayPlace(ItemStack stack) {return enabled;}});
            }
        }
        if (notSelf) addPlayerInventorySlots(inv, rows);
    }

    private static Set<Integer> setup(Container inventory, LivingEntity target, boolean editable) {
        Set<Integer> set = new HashSet<>();
        //物品栏
        if (editable) {
            if (target instanceof Player player) {
                var inv = player.getInventory().items;
                for (int i = 0; i < inv.size(); i++) {
                    if (i > 35) break;  // 只取前36个槽位
                    inventory.setItem(i, inv.get(i));
                    set.add(i);
                }
            } else if (target instanceof InventoryCarrier owner) {
                var stacks = owner.getInventory().getItems();
                for (int i = 0; i < stacks.size(); i++) {
                    inventory.setItem(i + 1, stacks.get(i));
                    set.add(i + 1);
                }
            }
        }

        int armorIndex = 0;
        for (ItemStack stack : target.getArmorSlots()) {
            inventory.setItem(39 - armorIndex, stack); set.add(39 - armorIndex); armorIndex++;
        } //盔甲栏

        if (!(target instanceof Player)) {inventory.setItem(0, target.getMainHandItem()); set.add(0);}
        inventory.setItem(40, target.getOffhandItem()); set.add(40);

        getCuriosInventory(target).ifPresent(inv -> {
            IItemHandlerModifiable curios = inv.getEquippedCurios();
            for (int i = 0; i < curios.getSlots(); i++) {
                if (i >= 45) break;
                inventory.setItem(41 + i, curios.getStackInSlot(i));
                set.add(41 + i);
            }
        });
        return set;
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (editable) {
            if (button == 114 && !player.level().isClientSide) {
                getSlot(slotId).set(ItemStack.EMPTY);
                saveInv(inventory, target);
                return;
            }
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
        for (int i = 0; i < 86; i++) {
            ItemStack stack = from.getItem(i);
            if (i == 0 && !(to instanceof Player)) to.setItemInHand(InteractionHand.MAIN_HAND, stack);
            if (to instanceof InventoryCarrier owner) {
                int size = owner.getInventory().getContainerSize();
                if (i > 0 && i - 1 < size) owner.getInventory().setItem(i  - 1, stack);
            }
            if (i < 36 && to instanceof Player player)  player.getInventory().setItem(i, stack);
            if (i == 36) to.setItemSlot(EquipmentSlot.HEAD, stack);
            if (i == 37) to.setItemSlot(EquipmentSlot.CHEST, stack);
            if (i == 38) to.setItemSlot(EquipmentSlot.LEGS, stack);
            if (i == 39) to.setItemSlot(EquipmentSlot.FEET, stack);
            if (i == 40) to.setItemInHand(InteractionHand.OFF_HAND, stack);
            if (i >= 41) {
                var pairs = ModTools.trinketsWithSlots(to);
                for (var pair : pairs) {
                    if (pairs.indexOf(pair) == i - 41) pair.getA().setStackInSlot(pair.getB(), stack);
                }
            }
        }
    }

    @Override public boolean stillValid(Player player) {return true;}

    private void addPlayerInventorySlots(Inventory inventory, int rows) {
        int i, j;
        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 32 + 18 * (rows + i)));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 36 + 18 * (rows + 3)));
        }
    }
}
