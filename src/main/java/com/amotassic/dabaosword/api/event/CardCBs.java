package com.amotassic.dabaosword.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import javax.annotation.Nullable;

/**
 * 卡牌事件，用于监听卡牌的移动、丢弃、使用等事件
 * <p>
 * 注意：
 * 不要直接调用这个类里面的方法，而是通过使用{@link com.amotassic.dabaosword.util.ModTools}中已有的静态方法来调用监听器。
 * 因为事件内不会处理卡牌的减少，所有卡牌减少和相关的逻辑都在ModTools对应的方法中处理。
 */

public class CardCBs {
    public static class UsePre extends LivingEvent implements ICancellableEvent {

        private final ItemStack stack;
        private final LivingEntity target;

        public UsePre(LivingEntity entity, ItemStack stack, @Nullable LivingEntity target) {
            super(entity);
            this.stack = stack;
            this.target = target;
        }

        public ItemStack getStack() {return this.stack;}

        @Nullable
        public LivingEntity getTarget() {return this.target;}
    }

    public static class UsePost extends LivingEvent {

        private final ItemStack stack;
        private final LivingEntity target;

        public UsePost(LivingEntity entity, ItemStack stack, @Nullable LivingEntity target) {
            super(entity);
            this.stack = stack;
            this.target = target;
        }

        public ItemStack getStack() {return this.stack;}

        @Nullable
        public LivingEntity getTarget() {return this.target;}
    }

    public static class Discard extends LivingEvent {

        private final ItemStack stack;
        private final int count;
        private final boolean fromEquip;

        public Discard(LivingEntity entity, ItemStack stack, int count, boolean fromEquip) {
            super(entity);
            this.stack = stack;
            this.count = count;
            this.fromEquip = fromEquip;
        }

        public ItemStack getStack() {return this.stack;}

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
