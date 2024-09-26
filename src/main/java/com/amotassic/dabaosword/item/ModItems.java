package com.amotassic.dabaosword.item;

import com.amotassic.dabaosword.effect.*;
import com.amotassic.dabaosword.item.card.*;
import com.amotassic.dabaosword.item.equipment.*;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.ui.FullInvScreenHandler;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import com.amotassic.dabaosword.ui.SimpleMenuHandler;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");

    public static final DeferredHolder<Item, Item> GAIN_CARD = ITEMS.register("gain_card", ()-> new GainCardItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CARD_PILE = ITEMS.register("card_pile", ()-> new Equipment(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> GUDINGDAO = ITEMS.register("gudingdao", GudingdaoItem::new);
    public static final DeferredHolder<Item, Item> GUDING_WEAPON = ITEMS.register("guding_dao", ()-> new Equipment(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> INCOMPLETE_GUDINGDAO = ITEMS.register("incomplete_gdd", ()-> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> GUDING = ITEMS.register("guding", ()-> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FANGTIAN = ITEMS.register("fangtian", ()-> new Equipment.FangtianWeapon(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> HANBING = ITEMS.register("hanbing", ()-> new Equipment.HanbingWeapon(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> QINGGANG = ITEMS.register("qinggang", ()-> new Equipment.QinggangWeapon(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> QINGLONG = ITEMS.register("qinglong", ()-> new Equipment.QinglongWeapon(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> BAGUA = ITEMS.register("bagua", ()-> new Equipment(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> BAIYIN = ITEMS.register("baiyin", ()-> new Equipment(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> RATTAN_ARMOR = ITEMS.register("rattan_armor", ()-> new Equipment.RattanArmor(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> CHITU = ITEMS.register("chitu", ()-> new Equipment(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> DILU = ITEMS.register("dilu", ()-> new Equipment(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> SHA = ITEMS.register("sha", ()-> new CardItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FIRE_SHA = ITEMS.register("fire_sha", ()-> new CardItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> THUNDER_SHA = ITEMS.register("thunder_sha", ()-> new CardItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> SHAN = ITEMS.register("shan", ()-> new ShanItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> PEACH = ITEMS.register("peach", ()-> new PeachItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> JIU = ITEMS.register("jiu", ()-> new JiuItem(new Item.Properties()));

    public static final DeferredHolder<Item, Item> BINGLIANG_ITEM = ITEMS.register("bingliang", ()-> new BingliangItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> TOO_HAPPY_ITEM = ITEMS.register("too_happy", ()-> new TooHappyItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> DISCARD = ITEMS.register("discard", ()-> new DiscardItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FIRE_ATTACK = ITEMS.register("huogong", ()-> new FireAttackItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> JUEDOU = ITEMS.register("juedou", ()-> new JuedouItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> JIEDAO = ITEMS.register("jiedao", ()-> new JiedaoItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> NANMAN = ITEMS.register("nanman", ()-> new NanmanItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> STEAL = ITEMS.register("steal", ()-> new StealItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> TAOYUAN = ITEMS.register("taoyuan", ()-> new TaoyuanItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> TIESUO = ITEMS.register("tiesuo", ()-> new TiesuoItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ARROW_RAIN = ITEMS.register("arrow_rain", ()-> new ArrowRainItem(new Item.Properties().durability(50).rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> WANJIAN = ITEMS.register("wanjian", ()-> new WanjianItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WUXIE = ITEMS.register("wuxie", ()-> new CardItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> WUZHONG = ITEMS.register("wuzhong", ()-> new GainCardItem(new Item.Properties()));
    public static final DeferredHolder<Item, Item> GIFT_BOX = ITEMS.register("gift_box", ()-> new GiftBoxItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredHolder<Item, Item> BBJI = ITEMS.register("bbji", ()-> new BBjiItem(new Item.Properties().durability(250)));
    public static final DeferredHolder<Item, Item> LET_ME_CC = ITEMS.register("let_me_cc", ()-> new LetMeCCItem(new Item.Properties().stacksTo(1)));

    //状态效果注册
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, "dabaosword");
    public static final DeferredHolder<MobEffect, MobEffect> BINGLIANG = EFFECTS.register("bingliang", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0x46F732).addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.parse("bing"),-4, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> TOO_HAPPY = EFFECTS.register("too_happy", () -> new TooHappyEffect(MobEffectCategory.HARMFUL, 0xF73C0A).addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.parse("le"),-10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    //触及距离增加
    public static final DeferredHolder<MobEffect, MobEffect> REACH = EFFECTS.register("reach", () -> new CommonEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)
            .addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, ResourceLocation.parse("-1ma"),1.0, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("-1ma"),1.0, AttributeModifier.Operation.ADD_VALUE));
    //近战防御范围增加
    public static final DeferredHolder<MobEffect, MobEffect> DEFEND = EFFECTS.register(
            "defend", () -> new CommonEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
    public static final DeferredHolder<MobEffect, MobEffect> DEFENDED = EFFECTS.register(
            "defended", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0xFFFFFF).addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("defend-"),-1.0, AttributeModifier.Operation.ADD_VALUE));
    //冷却状态效果
    public static final DeferredHolder<MobEffect, MobEffect> COOLDOWN = EFFECTS.register(
            "cooldown", () -> new CooldownEffect(MobEffectCategory.NEUTRAL, 0xFFFFFF));
    public static final DeferredHolder<MobEffect, MobEffect> COOLDOWN2 = EFFECTS.register(
            "cooldown2", () -> new Cooldown2Effect(MobEffectCategory.NEUTRAL, 0xFFFFFF));
    //无敌效果
    public static final DeferredHolder<MobEffect, MobEffect> INVULNERABLE = EFFECTS.register(
            "invulnerable", () -> new InvulnerableEffect(MobEffectCategory.BENEFICIAL,0x35F5DF));
    //翻面效果
    public static final DeferredHolder<MobEffect, MobEffect> TURNOVER = EFFECTS.register(
            "turn_over", () -> new TurnOverEffect(MobEffectCategory.HARMFUL, 0x07050F));
    //铁骑效果
    public static final DeferredHolder<MobEffect, MobEffect> TIEJI = EFFECTS.register(
            "tieji", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0x07050F));

    //物品组件注册
    public static final DeferredRegister.DataComponents DATA_COMPONENT = DeferredRegister.createDataComponents("dabaosword");
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TAGS = DATA_COMPONENT.registerComponentType("tags", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CD = DATA_COMPONENT.registerComponentType("cd", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(BuiltInRegistries.MENU, "dabaosword");
    public static final Supplier<MenuType<SimpleMenuHandler>> SIMPLE_MENU_HANDLER = MENU.register("simple_menu", () -> IMenuTypeExtension.create(SimpleMenuHandler::new));
    public static final Supplier<MenuType<PlayerInvScreenHandler>> PLAYER_INV_SCREEN_HANDLER = MENU.register("player_inv", () -> IMenuTypeExtension.create(PlayerInvScreenHandler::new));
    public static final Supplier<MenuType<FullInvScreenHandler>> FULL_INV_SCREEN_HANDLER = MENU.register("full_inv", () -> IMenuTypeExtension.create(FullInvScreenHandler::new));

    //物品组注册
    public static DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "dabaosword");
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DABAOSWORD_TAB = TABS.register("dabaosword_tab", 
            () -> CreativeModeTab.builder().icon(GUDINGDAO.get()::getDefaultInstance)
                    .title(Component.translatable("itemGroup.dabaosword_tab"))
                    .displayItems((p, o) -> {
                        o.accept(GUDING_WEAPON.get());
                        o.accept(FANGTIAN.get());
                        o.accept(HANBING.get());
                        o.accept(QINGGANG.get());
                        o.accept(QINGLONG.get());
                        o.accept(BAGUA.get());
                        o.accept(BAIYIN.get());
                        o.accept(RATTAN_ARMOR.get());
                        o.accept(GAIN_CARD.get());
                        o.accept(CARD_PILE.get());
                        o.accept(SHA.get());
                        o.accept(FIRE_SHA.get());
                        o.accept(THUNDER_SHA.get());
                        o.accept(SHAN.get());
                        o.accept(PEACH.get());
                        o.accept(JIU.get());
                        o.accept(BINGLIANG_ITEM.get());
                        o.accept(TOO_HAPPY_ITEM.get());
                        o.accept(DISCARD.get());
                        o.accept(FIRE_ATTACK.get());
                        o.accept(JIEDAO.get());
                        o.accept(JUEDOU.get());
                        o.accept(NANMAN.get());
                        o.accept(STEAL.get());
                        o.accept(TAOYUAN.get());
                        o.accept(TIESUO.get());
                        o.accept(WANJIAN.get());
                        o.accept(WUXIE.get());
                        o.accept(WUZHONG.get());
                        o.accept(CHITU.get());
                        o.accept(DILU.get());
                        //魏
                        o.accept(SkillCards.DUANLIANG);
                        o.accept(SkillCards.FANGZHU);
                        o.accept(SkillCards.XINGSHANG);
                        o.accept(SkillCards.GANGLIE);
                        o.accept(SkillCards.GONGAO);
                        o.accept(SkillCards.JUEQING);
                        o.accept(SkillCards.LUOSHEN);
                        o.accept(SkillCards.QINGGUO);
                        o.accept(SkillCards.LUOYI);
                        o.accept(SkillCards.QICE);
                        o.accept(SkillCards.QUANJI);
                        o.accept(SkillCards.SHANZHUAN);
                        o.accept(SkillCards.SHENSU);
                        o.accept(SkillCards.YIJI);
                        //蜀
                        o.accept(SkillCards.BENXI);
                        o.accept(SkillCards.HUOJI);
                        o.accept(SkillCards.KANPO);
                        o.accept(SkillCards.JIZHI);
                        o.accept(SkillCards.KUANGGU);
                        o.accept(SkillCards.LIEGONG);
                        o.accept(SkillCards.LONGDAN);
                        o.accept(SkillCards.RENDE);
                        o.accept(SkillCards.TIEJI);
                        //吴
                        o.accept(SkillCards.BUQU);
                        o.accept(SkillCards.GONGXIN);
                        o.accept(SkillCards.GUOSE);
                        o.accept(SkillCards.LIANYING);
                        o.accept(SkillCards.LIULI);
                        o.accept(SkillCards.KUROU);
                        o.accept(SkillCards.POJUN);
                        o.accept(SkillCards.QIXI);
                        o.accept(SkillCards.XIAOJI);
                        o.accept(SkillCards.ZHIHENG);
                        o.accept(SkillCards.ZHIJIAN);
                        //群
                        o.accept(SkillCards.LEIJI);
                        o.accept(SkillCards.LUANJI);
                        o.accept(SkillCards.TAOLUAN);
                        o.accept(SkillCards.MASHU);
                        o.accept(SkillCards.FEIYING);

                        o.accept(GIFT_BOX.get());
                        o.accept(BBJI.get());
                        o.accept(LET_ME_CC.get());
                    }).build());
}
