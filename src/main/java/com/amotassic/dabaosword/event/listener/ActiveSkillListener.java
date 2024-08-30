package com.amotassic.dabaosword.event.listener;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;

public class ActiveSkillListener extends PlayerEvent {

    private final ItemStack stack;
    private final Player target;

    public ActiveSkillListener(Player player, ItemStack stack, @Nullable Player target) {
        super(player);
        this.stack = stack;
        this.target = target;
    }

    public ItemStack getStack() {return this.stack;}

    @Nullable
    public Player getTarget() {return this.target;}
}
