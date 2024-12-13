package com.amotassic.dabaosword.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ReachDefend {

    default int getExtraReach(Player player, ItemStack stack) {return 0;}

    default int getDefend(Player player, ItemStack stack) {return 0;}
}
