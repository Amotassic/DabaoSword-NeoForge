package com.amotassic.dabaosword.event.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;

public class CardUsePostListener extends PlayerEvent {

    private final ItemStack stack;
    private final LivingEntity target;

    public CardUsePostListener(Player player, ItemStack stack, @Nullable LivingEntity target) {
        super(player);
        this.stack = stack;
        this.target = target;
    }

    public ItemStack getStack() {return this.stack;}

    public ItemStack getCopy() {return this.stack.copy();}

    @Nullable
    public LivingEntity getTarget() {return this.target;}
}
