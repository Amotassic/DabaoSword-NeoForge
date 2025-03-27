package com.amotassic.dabaosword.item;

import com.amotassic.dabaosword.item.card.CardItem;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.util.AllRegs;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModItems {

    public static CardItem
    GUANSHI = AllRegs.Items.GUANSHI.get(),
    GUDING_WEAPON = AllRegs.Items.GUDING_WEAPON.get(),
    FANGTIAN = AllRegs.Items.FANGTIAN.get(),
    HANBING = AllRegs.Items.HANBING.get(),
    QINGGANG = AllRegs.Items.QINGGANG.get(),
    QINGLONG = AllRegs.Items.QINGLONG.get(),
    BAGUA = AllRegs.Items.BAGUA.get(),
    BAIYIN = AllRegs.Items.BAIYIN.get(),
    RENWANG = AllRegs.Items.RENWANG.get(),
    RATTAN_ARMOR = AllRegs.Items.RATTAN_ARMOR.get(),
    CHITU = AllRegs.Items.CHITU.get(),
    DILU = AllRegs.Items.DILU.get(),

    SHA = AllRegs.Items.SHA.get(),
    FIRE_SHA = AllRegs.Items.FIRE_SHA.get(),
    THUNDER_SHA = AllRegs.Items.THUNDER_SHA.get(),
    SHAN = AllRegs.Items.SHAN.get(),
    PEACH = AllRegs.Items.PEACH.get(),
    JIU = AllRegs.Items.JIU.get(),

    BINGLIANG_ITEM = AllRegs.Items.BINGLIANG_ITEM.get(),
    TOO_HAPPY_ITEM = AllRegs.Items.TOO_HAPPY_ITEM.get(),
    SHANDIAN_ITEM = AllRegs.Items.SHANDIAN_ITEM.get(),
    DISCARD = AllRegs.Items.DISCARD.get(),
    FIRE_ATTACK = AllRegs.Items.FIRE_ATTACK.get(),
    JUEDOU = AllRegs.Items.JUEDOU.get(),
    JIEDAO = AllRegs.Items.JIEDAO.get(),
    NANMAN = AllRegs.Items.NANMAN.get(),
    STEAL = AllRegs.Items.STEAL.get(),
    TAOYUAN = AllRegs.Items.TAOYUAN.get(),
    TIESUO = AllRegs.Items.TIESUO.get(),
    WUGU = AllRegs.Items.WUGU.get(),
    WANJIAN = AllRegs.Items.WANJIAN.get(),
    WUXIE = AllRegs.Items.WUXIE.get(),
    WUZHONG = AllRegs.Items.WUZHONG.get();

    public static Item
    GAIN_CARD = AllRegs.Items.GAIN_CARD.get(),
    CARD_PILE = AllRegs.Items.CARD_PILE.get(),
    GIFT_BOX = AllRegs.Items.GIFT_BOX.get(),
    BBJI = AllRegs.Items.BBJI.get(),
    GUDINGDAO = AllRegs.Items.GUDINGDAO.get(),
    LET_ME_CC = AllRegs.Items.LET_ME_CC.get(),
    SUNSHINE_SMILE = AllRegs.Items.SUNSHINE_SMILE.get(),
    ARROW_RAIN = AllRegs.Items.ARROW_RAIN.get();
    public static CardItem EMPTY_CARD = AllRegs.Items.EMPTY_CARD.get();
    public static SkillItem EMPTY_SKILL = AllRegs.Items.EMPTY_SKILL.get();

    //状态效果
    public static Holder<MobEffect> BINGLIANG = AllRegs.Effects.BINGLIANG;
    public static Holder<MobEffect> TOO_HAPPY = AllRegs.Effects.TOO_HAPPY;
    //触及距离增加
    public static Holder<MobEffect> REACH = AllRegs.Effects.REACH;
    //近战防御范围增加
    public static Holder<MobEffect> DEFEND = AllRegs.Effects.DEFEND;
    public static Holder<MobEffect> DEFENDED = AllRegs.Effects.DEFENDED;
    //冷却状态效果
    public static Holder<MobEffect> COOLDOWN = AllRegs.Effects.COOLDOWN;
    public static Holder<MobEffect> COOLDOWN2 = AllRegs.Effects.COOLDOWN2;
    //无敌效果
    public static Holder<MobEffect> INVULNERABLE = AllRegs.Effects.INVULNERABLE;
    //翻面效果
    public static Holder<MobEffect> TURNOVER = AllRegs.Effects.TURNOVER;
    //铁骑效果
    public static Holder<MobEffect> TIEJI = AllRegs.Effects.TIEJI;
    public static Holder<MobEffect> SHANDIAN = AllRegs.Effects.SHANDIAN;

    public static final ResourceKey<Enchantment> CRIT = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("dabaosword:crit"));
}
