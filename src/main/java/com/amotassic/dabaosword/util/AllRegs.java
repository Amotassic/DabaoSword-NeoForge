package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.effect.*;
import com.amotassic.dabaosword.item.BBjiItem;
import com.amotassic.dabaosword.item.LetMeCCItem;
import com.amotassic.dabaosword.item.card.*;
import com.amotassic.dabaosword.item.equipment.ArrowRainItem;
import com.amotassic.dabaosword.item.equipment.Equipment;
import com.amotassic.dabaosword.item.equipment.GudingdaoItem;
import com.amotassic.dabaosword.item.equipment.SunshineSmile;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.ui.FullInvScreenHandler;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import com.amotassic.dabaosword.ui.SimpleMenuHandler;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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

public class AllRegs {

    public static class Items {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");

        public static final DeferredHolder<Item, Item> GAIN_CARD = ITEMS.register("gain_card", ()-> new GainCardItem(new Item.Properties()));
        public static final DeferredHolder<Item, Item> CARD_PILE = ITEMS.register("card_pile", ()-> new Equipment(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GUDINGDAO = ITEMS.register("gudingdao", GudingdaoItem::new);
        public static final DeferredHolder<Item, Item> GUDING_WEAPON = ITEMS.register("guding_dao", ()-> new Equipment.GudingWeapon(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> INCOMPLETE_GUDINGDAO = ITEMS.register("incomplete_gdd", ()-> new Item(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GUDING = ITEMS.register("guding", ()-> new Item(new Item.Properties()));
        public static final DeferredHolder<Item, Item> FANGTIAN = ITEMS.register("fangtian", ()-> new Equipment.FangtianWeapon(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> HANBING = ITEMS.register("hanbing", ()-> new Equipment.HanbingWeapon(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> QINGGANG = ITEMS.register("qinggang", ()-> new Equipment.QinggangWeapon(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> QINGLONG = ITEMS.register("qinglong", ()-> new Equipment.QinglongWeapon(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> BAGUA = ITEMS.register("bagua", ()-> new Equipment.BaguaArmor(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> BAIYIN = ITEMS.register("baiyin", ()-> new Equipment.BaiyinArmor(new Item.Properties().stacksTo(1)));
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
        public static final DeferredHolder<Item, Item> SUNSHINE_SMILE = ITEMS.register("sunshine_smile", ()-> new SunshineSmile(new Item.Properties().durability(999).rarity(Rarity.UNCOMMON)));

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
                            o.accept(SUNSHINE_SMILE.get());
                        }).build());
    }

    public static class Skills {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");
        //魏
        public static final DeferredHolder<Item, Item> DUANLIANG = ITEMS.register("duanliang", ()-> new SkillItem.Duanliang(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> FANGZHU = ITEMS.register("fangzhu", ()-> new SkillItem.Fangzhu(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> XINGSHANG = ITEMS.register("xingshang", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GANGLIE = ITEMS.register("ganglie", ()-> new SkillItem.Ganglie(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GONGAO = ITEMS.register("gongao", ()-> new SkillItem.Gongao(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> JUEQING = ITEMS.register("jueqing", ()-> new SkillItem.Jueqing(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> LUOSHEN = ITEMS.register("luoshen", ()-> new SkillItem.Luoshen(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> QINGGUO = ITEMS.register("qingguo", ()-> new SkillItem.Qingguo(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> LUOYI = ITEMS.register("luoyi", ()-> new SkillItem.Luoyi(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> QICE = ITEMS.register("qice", ()-> new SkillItem.Qice(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> QUANJI = ITEMS.register("quanji", ()-> new SkillItem.Quanji(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> SHANZHUAN = ITEMS.register("shanzhuan", ()-> new SkillItem.Shanzhuan(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> SHENSU = ITEMS.register("shensu", ()-> new SkillItem.Shensu(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> YIJI = ITEMS.register("yiji", ()-> new SkillItem.Yiji(new Item.Properties().stacksTo(1)));
        //蜀
        public static final DeferredHolder<Item, Item> BENXI = ITEMS.register("benxi", ()-> new SkillItem.Benxi(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> HUOJI = ITEMS.register("huoji", ()-> new SkillItem.Huoji(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> KANPO = ITEMS.register("kanpo", ()-> new SkillItem.Kanpo(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> JIZHI = ITEMS.register("jizhi", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> KUANGGU = ITEMS.register("kuanggu", ()-> new SkillItem.Kuanggu(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> LIEGONG = ITEMS.register("liegong", ()-> new SkillItem.Liegong(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> LONGDAN = ITEMS.register("longdan", ()-> new SkillItem.Longdan(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> RENDE = ITEMS.register("rende", ()-> new SkillItem.Rende(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> TIEJI = ITEMS.register("tieji", ()-> new SkillItem.Tieji(new Item.Properties().stacksTo(1)));
        //吴
        public static final DeferredHolder<Item, Item> BUQU = ITEMS.register("buqu", ()-> new SkillItem.Buqu(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GONGXIN = ITEMS.register("gongxin", ()-> new SkillItem.Gongxin(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GUOSE = ITEMS.register("guose", ()-> new SkillItem.Guose(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> LIANYING = ITEMS.register("lianying", ()-> new SkillItem.Lianying(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> LIULI = ITEMS.register("liuli", ()-> new SkillItem.Liuli(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> KUROU = ITEMS.register("kurou", ()-> new SkillItem.Kurou(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> POJUN = ITEMS.register("pojun", ()-> new SkillItem.Pojun(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> QIXI = ITEMS.register("qixi", ()-> new SkillItem.Qixi(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> XIAOJI = ITEMS.register("xiaoji", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> ZHIHENG = ITEMS.register("zhiheng", ()-> new SkillItem.Zhiheng(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> ZHIJIAN = ITEMS.register("zhijian", ()-> new SkillItem.Zhijian(new Item.Properties().stacksTo(1)));
        //群
        public static final DeferredHolder<Item, Item> LEIJI = ITEMS.register("leiji", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> LUANJI = ITEMS.register("luanji", ()-> new SkillItem.Luanji(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> TAOLUAN = ITEMS.register("taoluan", ()-> new SkillItem.Taoluan(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> MASHU = ITEMS.register("mashu", ()-> new SkillItem(new Item.Properties().stacksTo(1)));

        public static final DeferredHolder<Item, Item> FEIYING = ITEMS.register("feiying", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    }

    public static class Effects {
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
    }

    public static class Other {
        //物品组件注册
        public static final DeferredRegister.DataComponents DATA_COMPONENT = DeferredRegister.createDataComponents("dabaosword");
        public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TAGS = DATA_COMPONENT.registerComponentType("tags", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
        public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CD = DATA_COMPONENT.registerComponentType("cd", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

        public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(BuiltInRegistries.MENU, "dabaosword");
        public static final Supplier<MenuType<SimpleMenuHandler>> SIMPLE_MENU_HANDLER = MENU.register("simple_menu", () -> IMenuTypeExtension.create(SimpleMenuHandler::new));
        public static final Supplier<MenuType<PlayerInvScreenHandler>> PLAYER_INV_SCREEN_HANDLER = MENU.register("player_inv", () -> IMenuTypeExtension.create(PlayerInvScreenHandler::new));
        public static final Supplier<MenuType<FullInvScreenHandler>> FULL_INV_SCREEN_HANDLER = MENU.register("full_inv", () -> IMenuTypeExtension.create(FullInvScreenHandler::new));
    }

    public static class Sounds {
        public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "dabaosword");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHENSU = register("shensu");
        public static final DeferredHolder<SoundEvent, SoundEvent> LIANYING = register("lianying");
        public static final DeferredHolder<SoundEvent, SoundEvent> XIAOJI = register("xiaoji");
        public static final DeferredHolder<SoundEvent, SoundEvent> LET_ME_CC = register("letmecc");
        public static final DeferredHolder<SoundEvent, SoundEvent> LONGDAN = register("longdan");
        public static final DeferredHolder<SoundEvent, SoundEvent> GONGXIN = register("gongxin");
        public static final DeferredHolder<SoundEvent, SoundEvent> ZHIJIAN = register("zhijian");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHANZHUAN = register("shanzhuan");
        public static final DeferredHolder<SoundEvent, SoundEvent> RENDE = register("rende");
        public static final DeferredHolder<SoundEvent, SoundEvent> ZHIHENG = register("zhiheng");
        public static final DeferredHolder<SoundEvent, SoundEvent> BUQU = register("buqu");
        public static final DeferredHolder<SoundEvent, SoundEvent> TIEJI = register("tieji");
        public static final DeferredHolder<SoundEvent, SoundEvent> GANGLIE = register("ganglie");
        public static final DeferredHolder<SoundEvent, SoundEvent> FANGZHU = register("fangzhu");
        public static final DeferredHolder<SoundEvent, SoundEvent> XINGSHANG = register("xingshang");
        public static final DeferredHolder<SoundEvent, SoundEvent> BBJI = register("bbji");
        public static final DeferredHolder<SoundEvent, SoundEvent> XUYOU = register("xuyou");
        public static final DeferredHolder<SoundEvent, SoundEvent> DUANLIANG = register("duanliang");
        public static final DeferredHolder<SoundEvent, SoundEvent> LUOSHEN = register("luoshen");
        public static final DeferredHolder<SoundEvent, SoundEvent> QIXI = register("qixi");
        public static final DeferredHolder<SoundEvent, SoundEvent> QINGGUO = register("qingguo");
        public static final DeferredHolder<SoundEvent, SoundEvent> LIEGONG = register("liegong");
        public static final DeferredHolder<SoundEvent, SoundEvent> GONGAO = register("gongao");
        public static final DeferredHolder<SoundEvent, SoundEvent> WEIZHONG = register("weizhong");
        public static final DeferredHolder<SoundEvent, SoundEvent> BENXI = register("benxi");
        public static final DeferredHolder<SoundEvent, SoundEvent> LEIJI = register("leiji");
        public static final DeferredHolder<SoundEvent, SoundEvent> GIFTBOX = register("giftbox");
        public static final DeferredHolder<SoundEvent, SoundEvent> KANPO = register("kanpo");
        public static final DeferredHolder<SoundEvent, SoundEvent> GUOSE = register("guose");
        public static final DeferredHolder<SoundEvent, SoundEvent> LIULI = register("liuli");
        public static final DeferredHolder<SoundEvent, SoundEvent> JUEQING = register("jueqing");
        public static final DeferredHolder<SoundEvent, SoundEvent> LUANJI = register("luanji");
        public static final DeferredHolder<SoundEvent, SoundEvent> KUROU = register("kurou");
        public static final DeferredHolder<SoundEvent, SoundEvent> JIZHI = register("jizhi");
        public static final DeferredHolder<SoundEvent, SoundEvent> QICE = register("qice");
        public static final DeferredHolder<SoundEvent, SoundEvent> LUOYI = register("luoyi");
        public static final DeferredHolder<SoundEvent, SoundEvent> HUOJI = register("huoji");
        public static final DeferredHolder<SoundEvent, SoundEvent> QUANJI = register("quanji");
        public static final DeferredHolder<SoundEvent, SoundEvent> ZILI = register("zili");
        public static final DeferredHolder<SoundEvent, SoundEvent> PAIYI = register("paiyi");
        public static final DeferredHolder<SoundEvent, SoundEvent> YIJI = register("yiji");
        public static final DeferredHolder<SoundEvent, SoundEvent> TAOLUAN = register("taoluan");
        public static final DeferredHolder<SoundEvent, SoundEvent> POJUN = register("pojun");
        public static final DeferredHolder<SoundEvent, SoundEvent> KUANGGU = register("kuanggu");

        public static final DeferredHolder<SoundEvent, SoundEvent> BAGUA = register("bagua");
        public static final DeferredHolder<SoundEvent, SoundEvent> BAIYIN = register("baiyin");
        public static final DeferredHolder<SoundEvent, SoundEvent> FANGTIAN = register("fangtian");
        public static final DeferredHolder<SoundEvent, SoundEvent> GUDING = register("guding");
        public static final DeferredHolder<SoundEvent, SoundEvent> HANBING = register("hanbing");
        public static final DeferredHolder<SoundEvent, SoundEvent> QINGGANG = register("qinggang");
        public static final DeferredHolder<SoundEvent, SoundEvent> QINGLONG = register("qinglong");
        public static final DeferredHolder<SoundEvent, SoundEvent> TENGJIA1 = register("tengjia1");
        public static final DeferredHolder<SoundEvent, SoundEvent> TENGJIA2 = register("tengjia2");

        public static final DeferredHolder<SoundEvent, SoundEvent> BINGLIANG = register("bingliang");
        public static final DeferredHolder<SoundEvent, SoundEvent> GUOHE = register("guohe");
        public static final DeferredHolder<SoundEvent, SoundEvent> HUOGONG = register("huogong");
        public static final DeferredHolder<SoundEvent, SoundEvent> JIEDAO = register("jiedao");
        public static final DeferredHolder<SoundEvent, SoundEvent> JIU = register("jiu");
        public static final DeferredHolder<SoundEvent, SoundEvent> JUEDOU = register("juedou");
        public static final DeferredHolder<SoundEvent, SoundEvent> LEBU = register("lebu");
        public static final DeferredHolder<SoundEvent, SoundEvent> RECOVER = register("recover");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHAN = register("shan");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHUNSHOU = register("shunshou");
        public static final DeferredHolder<SoundEvent, SoundEvent> TAOYUAN = register("taoyuan");
        public static final DeferredHolder<SoundEvent, SoundEvent> TIESUO = register("tiesuo");
        public static final DeferredHolder<SoundEvent, SoundEvent> WANJIAN = register("wanjian");
        public static final DeferredHolder<SoundEvent, SoundEvent> WUXIE = register("wuxie");
        public static final DeferredHolder<SoundEvent, SoundEvent> WUZHONG = register("wuzhong");
        public static final DeferredHolder<SoundEvent, SoundEvent> NANMAN = register("nanman");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHA = register("sha");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHA_FIRE = register("sha_fire");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHA_THUNDER = register("sha_thunder");

        public static DeferredHolder<SoundEvent, SoundEvent> register(String name){
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath("dabaosword", name);
            return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(location));
        }
    }
}
