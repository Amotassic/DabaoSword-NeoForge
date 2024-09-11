package com.amotassic.dabaosword.ui;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.util.Sounds;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.*;

import static com.amotassic.dabaosword.util.ModTools.*;

public class SimpleMenuHandler extends AbstractContainerMenu {
    private final ItemStack eventStack;
    private final Container inventory;
    private final Player target;

    public SimpleMenuHandler(int syncId, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(syncId, new SimpleContainer(20), (Player) inv.player.level().getEntity(buf.readInt()));
    }

    public SimpleMenuHandler(int syncId, Container inventory, Player target) {
        super(ModItems.SIMPLE_MENU_HANDLER.get(), syncId);
        this.inventory = inventory;
        this.eventStack = inventory.getItem(18);
        this.target = target;
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9, 8 + j * 18, 16 + i * 18));
            }
        }
    }

    @Override
    public void clicked(int slotIndex, int button, ClickType clickType, Player player) {
        var stack = getStack(player, eventStack);
        ItemStack itemStack = inventory.getItem(slotIndex);
        if (!itemStack.isEmpty()) {

            if (stack.getItem() == SkillCards.QICE.get()) {
                give(player, itemStack);
                if (!player.isCreative()) player.getOffhandItem().shrink(2);
                setCD(stack, 20);
                voice(player, Sounds.QICE.get());
                closeGUI(player);
            }

            if (stack.getItem() == SkillCards.TAOLUAN.get()) {
                give(player, itemStack);
                if (!player.isCreative()) {
                    player.invulnerableTime = 0;
                    player.hurt(player.damageSources().genericKill(), 4.99f);
                }
                voice(player, Sounds.TAOLUAN.get());
                closeGUI(player);
            }
        }
    }

    private ItemStack getStack(Player player, ItemStack eventStack) {
        List<ItemStack> candidate = new ArrayList<>();
        var component = CuriosApi.getCuriosInventory(player);
        if(component.isPresent()) {
            var allEquipped = component.get().getEquippedCurios();
            for(int i = 0; i < allEquipped.getSlots(); i++) {
                candidate.add(allEquipped.getStackInSlot(i));
            }
        }
        candidate.addAll(player.getInventory().items);
        for (ItemStack stack : candidate) {
            if (stack.equals(eventStack)) return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {return ItemStack.EMPTY;}

    @Override
    public boolean stillValid(Player player) {
        return !player.hasEffect(ModItems.COOLDOWN2) || (player.hasEffect(ModItems.COOLDOWN2) && Objects.requireNonNull(player.getEffect(ModItems.COOLDOWN2)).getAmplifier() != 2);
    }

    private void closeGUI(Player player) {
        player.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 1,2,false,false,false));
    }
}
