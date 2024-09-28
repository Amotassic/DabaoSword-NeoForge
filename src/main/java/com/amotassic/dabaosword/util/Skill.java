package com.amotassic.dabaosword.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface Skill {

    //在攻击目标后，造成伤害前触发
    default void preAttack(ItemStack stack, LivingEntity target, Player attacker) {}

    //在近战攻击造成伤害后触发
    default void postAttack(ItemStack stack, LivingEntity target, LivingEntity attacker, float amount) {}

    //只要攻击造成伤害即可触发，包括远程
    default void postDamage(ItemStack stack, LivingEntity target, LivingEntity attacker, float amount) {}

    //受到伤害后触发
    default void onHurt(ItemStack stack, LivingEntity entity, DamageSource source, float amount) {}

    /**
     *当发动技能键按下后，若玩家没有铁骑效果即可触发，需要继承{@link com.amotassic.dabaosword.item.skillcard.SkillItem.ActiveSkill}或者{@link com.amotassic.dabaosword.item.skillcard.SkillItem.ActiveSkillWithTarget}才会生效
     */
    default void activeSkill(Player user, ItemStack stack, Player target) {}
}
