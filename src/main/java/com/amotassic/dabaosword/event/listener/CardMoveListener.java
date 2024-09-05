package com.amotassic.dabaosword.event.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class CardMoveListener extends PlayerEvent {

    private final LivingEntity from;
    private final ItemStack stack;
    private final int count;
    private final Type type;

    public CardMoveListener(LivingEntity from, Player to, ItemStack stack, int count, Type type) {
        super(to);
        this.from = from;
        this.stack = stack;
        this.count = count;
        this.type = type;
    }

    public LivingEntity getFrom() {return this.from;}

    public ItemStack getStack() {return this.stack;}

    public ItemStack getCopy() {return this.stack.copyWithCount(count);}

    public int getCount() {return this.count;}

    public Type getType() {return this.type;}

    public enum Type {
        INV_TO_INV,
        INV_TO_EQUIP,
        EQUIP_TO_INV,
        EQUIP_TO_EQUIP
    }
}
