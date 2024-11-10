package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.api.Skill;
import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.amotassic.dabaosword.util.ModTools.*;

public class ModifyDamage {
    //注入"applyArmorToDamage"以修改生物受到的伤害值
    public static float modify(LivingEntity entity, DamageSource source, float value) {
        float multiply = 0; //倍率增伤乘区
        float add = 0; //固定数值加减伤害区
        List<Float> reducing = new ArrayList<>(); //最终减伤乘区，存储负值，每个值使最终伤害为原来的（1+x）倍

        //处理所有饰品带来的增/减伤结算
        List<Tuple<Tuple<Float, Float>, List<Float>>> pairList = new ArrayList<>();
        pairList.add(calculateDMG(entity, source, value, entity));
        if (source.getDirectEntity() instanceof LivingEntity SE) {
            if (!SE.getTags().contains("sha")) { //防止杀的效果再次触发近战加伤
                pairList.add(calculateDMG(entity, source, value, SE));

                //插入一个武器版古锭刀的结算
                int i = 0; //i == 4则说明受击者的盔甲栏没有任何物品
                for (var s : entity.getArmorSlots()) {if (s.isEmpty()) i++;}
                if (i == 4 && SE.getMainHandItem().is(ModItems.GUDINGDAO)) multiply += 1;
            } //这里的这个else很重要！防止两个条件同时满足时会触发双重结算
        } else if (source.getEntity() instanceof LivingEntity AT) pairList.add(calculateDMG(entity, source, value, AT));
        for (var p : pairList) {
            multiply += p.getA().getA();
            add += p.getA().getB();
            reducing.addAll(p.getB());
        }

        //伤害结算
        value = value * (1 + multiply) + add;
        for (var f : reducing) {value *= (1 + f);}

        return value;
    }

    private static Tuple<Tuple<Float, Float>, List<Float>> calculateDMG(LivingEntity entity, DamageSource source, float value, LivingEntity trinketOwner) {
        float m = 0; float a = 0;
        List<Float> r = new ArrayList<>();
        for (var stack : allTrinkets(trinketOwner)) {
            Tuple<Float, Float> fp = null;
            if (stack.getItem() instanceof Skill skill) fp = skill.modifyDamage(entity, source, value);

            if (fp == null) continue;
            if (fp.getA() < 0) r.add(fp.getA()); else m += fp.getA();
            a += fp.getB();
        }
        return new Tuple<>(new Tuple<>(m, a), r);
    }

    private static List<List<ItemStack>> eventStacks(LivingEntity entity, DamageSource source, float value, LivingEntity trinketOwner) {
        List<List<ItemStack>> stacks = new ArrayList<>(Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        for (var stack : allTrinkets(trinketOwner)) {
            Skill.Priority priority = null;
            if (stack.getItem() instanceof Skill skill) priority = skill.getPriority(entity, source, value);

            if (priority != null) stacks.get(priority.ordinal()).add(stack);
        }
        return stacks;
    }

    private static boolean execute(LivingEntity entity, DamageSource source, float amount, List<List<ItemStack>> list, int index) {
        for (var s : list.get(index)) {
            if (s.getItem() instanceof Skill skill && skill.cancelDamage(entity, source, amount)) return true;
        }
        return false;
    }

    //取消伤害结算：先获取所有输出了结算优先度的ItemStack，将它们按照优先度依次排列（见eventStacks方法），再分别结算（见execute方法）
    public static boolean shouldCancel(LivingEntity entity, DamageSource source, float amount) {
        //检查事件优先度，获取输出了优先度的stack
        List<List<ItemStack>> list = eventStacks(entity, source, amount, entity);
        List<List<ItemStack>> l1 = null; List<List<ItemStack>> l2 = null;
        if (source.getDirectEntity() instanceof LivingEntity SE) l1 = eventStacks(entity, source, amount, SE);
        else if (source.getEntity() instanceof LivingEntity AT) l2 = eventStacks(entity, source, amount, AT);
        //合并3个list中的所有优先度和stack
        for (int i = 0; i < 5; i++) {
            if (l1 != null) list.get(i).addAll(l1.get(i));
            if (l2 != null) list.get(i).addAll(l2.get(i));
        }
        //0.最高优先度执行，暂无用途
        if (execute(entity, source, amount, list, 0)) return true;
        //无敌效果
        if (entity.hasEffect(ModItems.INVULNERABLE) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return true;

        if (source.getDirectEntity() instanceof LivingEntity SE) {
            //被乐的生物无法造成伤害
            if (SE.hasEffect(ModItems.TOO_HAPPY)) return true;
            //沈佳宜防御效果
            if (!(SE instanceof Player) && entity.hasEffect(ModItems.DEFEND)) {
                if (Objects.requireNonNull(entity.getEffect(ModItems.DEFEND)).getAmplifier() >= 2) return true;
            }
            //决斗等物品虽然手长，但过远时普通伤害无效
            if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && entity.distanceTo(SE) > 5) {
                if (SE.getMainHandItem().is(ModItems.JUEDOU) || SE.getMainHandItem().is(ModItems.DISCARD)) return true;
            }
        } else if (source.getEntity() instanceof LivingEntity AT) {
            //被乐的生物无法造成伤害
            if (AT.hasEffect(ModItems.TOO_HAPPY)) return true;
        }
        //1.高优先度执行：装备
        if (execute(entity, source, amount, list, 1)) return true;
        //2.一般优先度执行：技能
        if (execute(entity, source, amount, list, 2)) return true;
        //3.低优先度执行：卡牌 闪以及响应南蛮的杀
        if (execute(entity, source, amount, list, 3)) return true;
        if (source.getDirectEntity() instanceof Wolf dog && dog.hasEffect(ModItems.INVULNERABLE)) {
            //被南蛮入侵的狗打中可以消耗杀以免疫伤害
            if (hasCard(entity, isSha)) {
                dog.setHealth(0);
                var stack = getCard(entity, isSha).getB();
                voice(entity, stack);
                cardUsePost(entity, stack, null);
                return true;
            }
        }
        if (source.getEntity() instanceof LivingEntity) {
            if (!entity.hasEffect(ModItems.COOLDOWN2) && !entity.getTags().contains("juedou")) {
                //此处条件是故意设置得与八卦阵条件不一样的，虽然感觉没啥用
                if (hasCard(entity, s -> s.is(ModItems.SHAN)) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                    shan(entity, false, source, amount);
                    return true;
                }
            }
        }
        //4.最低优先度执行：绝情
        return execute(entity, source, amount, list, 4);
        //吐槽：别看这里写了那么多，一旦有一个return true，就没事了
    }

    public static void shan(LivingEntity entity, boolean bl, DamageSource source, float amount) {
        ItemStack stack = new ItemStack(ModItems.SHAN);
        int cd = bl ? 60 : 40;
        entity.addEffect(new MobEffectInstance(ModItems.INVULNERABLE, 20,0,false,false,false));
        entity.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, cd,0,false,false,false));
        if (bl) voice(entity, Sounds.BAGUA);
        cardUsePost(entity, stack, null, !bl); //如果触发八卦阵，就不用移除闪了
        if (entity instanceof Player player) {
            writeDamage(source, amount, !bl, trinketItem(ModItems.CARD_PILE, player));
            if (bl) player.displayClientMessage(Component.translatable("dabaosword.bagua"),true);
        }
        //虽然没有因为杀而触发闪，但如果攻击者的杀处于自动触发状态，则仍会消耗
        if (source.getDirectEntity() instanceof LivingEntity SE && hasItem(SE, isSha)) {
            ItemStack sha = isSha.test(SE.getMainHandItem()) ? SE.getMainHandItem() : getItem(SE, isSha);
            voice(SE, sha);
            cardUsePost(SE, sha, entity);
        }
    }
}
