package com.amotassic.dabaosword.ui;

import com.amotassic.dabaosword.api.Skill;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.AllRegs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SimpleMenuHandler extends AbstractContainerMenu {
    private final ItemStack stack;
    private final Container inventory;
    private final Player target;

    public SimpleMenuHandler(int syncId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(syncId, new SimpleContainer(20), (Player) inv.player.level().getEntity(buf.readInt()));
    }

    public SimpleMenuHandler(int syncId, Container inventory, Player target) {
        super(AllRegs.Other.SIMPLE_MENU_HANDLER.get(), syncId);
        this.inventory = inventory;
        this.stack = inventory.getItem(18);
        this.target = target;
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 16 + i * 18));
            }
        }
    }

    @Override
    public void clicked(int slotIndex, int button, @NotNull ClickType clickType, @NotNull Player player) {
        ItemStack itemStack = inventory.getItem(slotIndex);
        if (!itemStack.isEmpty()) {
            if (stack.getItem() instanceof Skill skill) skill.onClickGUISlot(player, stack, target, itemStack, slotIndex);
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {return ItemStack.EMPTY;}

    @Override
    public boolean stillValid(Player player) {
        return !player.hasEffect(ModItems.COOLDOWN2) || (player.hasEffect(ModItems.COOLDOWN2) && Objects.requireNonNull(player.getEffect(ModItems.COOLDOWN2)).getAmplifier() != 2);
    }
}
