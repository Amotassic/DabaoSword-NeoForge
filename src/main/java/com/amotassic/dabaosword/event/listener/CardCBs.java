package com.amotassic.dabaosword.event.listener;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;

public class CardCBs {
    public static class UsePost extends PlayerEvent {

        private final ItemStack stack;
        private final LivingEntity target;

        public UsePost(Player player, ItemStack stack, @Nullable LivingEntity target) {
            super(player);
            this.stack = stack;
            this.target = target;
        }

        public ItemStack getStack() {return this.stack;}

        public ItemStack getCopy() {return this.stack.copy();}

        @Nullable
        public LivingEntity getTarget() {return this.target;}
    }

    public static class Discard extends PlayerEvent {

        private final ItemStack stack;
        private final int count;
        private final boolean fromEquip;

        public Discard(Player player, ItemStack stack, int count, boolean fromEquip) {
            super(player);
            this.stack = stack;
            this.count = count;
            this.fromEquip = fromEquip;
        }

        public ItemStack getStack() {return this.stack;}

        public ItemStack getCopy() {return this.stack.copyWithCount(count);}

        public int getCount() {return this.count;}

        public boolean isFromEquip() {return this.fromEquip;}
    }

    public static class Move extends PlayerEvent {

        private final LivingEntity from;
        private final ItemStack stack;
        private final int count;
        private final T type;

        public Move(LivingEntity from, Player to, ItemStack stack, int count, T type) {
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

        public T getType() {return this.type;}
    }

    public enum T {
        INV_TO_INV,
        INV_TO_EQUIP,
        EQUIP_TO_INV,
        EQUIP_TO_EQUIP
    }
}
