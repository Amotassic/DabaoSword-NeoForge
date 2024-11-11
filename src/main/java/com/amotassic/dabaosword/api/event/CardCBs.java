package com.amotassic.dabaosword.api.event;

import net.minecraft.world.damagesource.DamageSource;
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
        public final ItemStack stack;
        @Nullable public final LivingEntity target;

        public UsePre(LivingEntity entity, ItemStack stack, @Nullable LivingEntity target) {
            super(entity);
            this.stack = stack;
            this.target = target;
        }
    }

    public static class UsePost extends LivingEvent {
        public final ItemStack stack;
        @Nullable public final LivingEntity target;

        public UsePost(LivingEntity entity, ItemStack stack, @Nullable LivingEntity target) {
            super(entity);
            this.stack = stack;
            this.target = target;
        }
    }

    public static class Discard extends LivingEvent {
        public final ItemStack stack;
        public final int count;
        public final boolean fromEquip;

        public Discard(LivingEntity entity, ItemStack stack, int count, boolean fromEquip) {
            super(entity);
            this.stack = stack;
            this.count = count;
            this.fromEquip = fromEquip;
        }
    }

    public static class Move extends PlayerEvent {
        public final LivingEntity from;
        public final ItemStack stack;
        public final int count;
        public final T type;

        public Move(LivingEntity from, Player to, ItemStack stack, int count, T type) {
            super(to);
            this.from = from;
            this.stack = stack;
            this.count = count;
            this.type = type;
        }
    }

    public enum T {
        INV_TO_INV,
        INV_TO_EQUIP,
        EQUIP_TO_INV,
        EQUIP_TO_EQUIP
    }

    public static class CanHurtByCard extends LivingEvent implements ICancellableEvent {
        public final DamageSource source;
        public final ItemStack card;

        public CanHurtByCard(LivingEntity entity, DamageSource source, ItemStack card) {
            super(entity);
            this.source = source;
            this.card = card;
        }
    }

    public static class HurtByCard extends LivingEvent {
        public final DamageSource source;
        public final ItemStack card;

        public HurtByCard(LivingEntity entity, DamageSource source, ItemStack card) {
            super(entity);
            this.source = source;
            this.card = card;
        }
    }
}
