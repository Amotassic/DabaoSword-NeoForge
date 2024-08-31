package com.amotassic.dabaosword.event.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class CardDiscardListener extends PlayerEvent {

    private final ItemStack stack;
    private final int count;
    private final boolean fromEquip;

    public CardDiscardListener(Player player, ItemStack stack, int count, boolean fromEquip) {
        super(player);
        this.stack = stack;
        this.count = count;
        this.fromEquip = fromEquip;
    }

    public ItemStack getStack() {return this.stack;}

    public int getCount() {return this.count;}

    public boolean isFromEquip() {return this.fromEquip;}
}
