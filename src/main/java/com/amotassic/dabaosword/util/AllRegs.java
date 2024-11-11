package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.effect.*;
import com.amotassic.dabaosword.item.BBjiItem;
import com.amotassic.dabaosword.item.GiftBoxItem;
import com.amotassic.dabaosword.item.LetMeCCItem;
import com.amotassic.dabaosword.item.card.*;
import com.amotassic.dabaosword.item.equipment.*;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.ui.FullInvScreenHandler;
import com.amotassic.dabaosword.ui.PileScreenHandler;
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

@SuppressWarnings("unused")
public class AllRegs {

    public static class Items {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");

        public static final DeferredHolder<Item, Item> SHA = ITEMS.register("sha", CardItem.Sha::new);
        public static final DeferredHolder<Item, Item> FIRE_SHA = ITEMS.register("fire_sha", CardItem.Sha::new);
        public static final DeferredHolder<Item, Item> THUNDER_SHA = ITEMS.register("thunder_sha", CardItem.Sha::new);
        public static final DeferredHolder<Item, Item> SHAN = ITEMS.register("shan", ShanItem::new);
        public static final DeferredHolder<Item, Item> PEACH = ITEMS.register("peach", PeachItem::new);
        public static final DeferredHolder<Item, Item> JIU = ITEMS.register("jiu", JiuItem::new);

        public static final DeferredHolder<Item, Item> BINGLIANG_ITEM = ITEMS.register("bingliang", BingliangItem::new);
        public static final DeferredHolder<Item, Item> TOO_HAPPY_ITEM = ITEMS.register("too_happy", TooHappyItem::new);
        public static final DeferredHolder<Item, Item> SHANDIAN_ITEM = ITEMS.register("shandian", ShandianItem::new);
        public static final DeferredHolder<Item, Item> DISCARD = ITEMS.register("discard", DiscardItem::new);
        public static final DeferredHolder<Item, Item> FIRE_ATTACK = ITEMS.register("huogong", FireAttackItem::new);
        public static final DeferredHolder<Item, Item> JUEDOU = ITEMS.register("juedou", JuedouItem::new);
        public static final DeferredHolder<Item, Item> JIEDAO = ITEMS.register("jiedao", JiedaoItem::new);
        public static final DeferredHolder<Item, Item> NANMAN = ITEMS.register("nanman", NanmanItem::new);
        public static final DeferredHolder<Item, Item> STEAL = ITEMS.register("steal", StealItem::new);
        public static final DeferredHolder<Item, Item> TAOYUAN = ITEMS.register("taoyuan", TaoyuanItem::new);
        public static final DeferredHolder<Item, Item> TIESUO = ITEMS.register("tiesuo", TiesuoItem::new);
        public static final DeferredHolder<Item, Item> WANJIAN = ITEMS.register("wanjian", WanjianItem::new);
        public static final DeferredHolder<Item, Item> WUGU = ITEMS.register("wugu", WuguItem::new);
        public static final DeferredHolder<Item, Item> WUXIE = ITEMS.register("wuxie", CardItem::new);
        public static final DeferredHolder<Item, Item> WUZHONG = ITEMS.register("wuzhong", CardItem.Wuzhong::new);

        public static final DeferredHolder<Item, Item> FANGTIAN = ITEMS.register("fangtian", Equipment.FangtianWeapon::new);
        public static final DeferredHolder<Item, Item> GUDING_WEAPON = ITEMS.register("guding_dao", Equipment.GudingWeapon::new);
        public static final DeferredHolder<Item, Item> HANBING = ITEMS.register("hanbing", Equipment.HanbingWeapon::new);
        public static final DeferredHolder<Item, Item> QINGGANG = ITEMS.register("qinggang", Equipment.QinggangWeapon::new);
        public static final DeferredHolder<Item, Item> QINGLONG = ITEMS.register("qinglong", Equipment.QinglongWeapon::new);
        public static final DeferredHolder<Item, Item> ZHANGBA = ITEMS.register("zhangba", Equipment.ZhangbaWeapon::new);
        public static final DeferredHolder<Item, Item> BAGUA = ITEMS.register("bagua", Equipment.BaguaArmor::new);
        public static final DeferredHolder<Item, Item> BAIYIN = ITEMS.register("baiyin", Equipment.BaiyinArmor::new);
        public static final DeferredHolder<Item, Item> RENWANG = ITEMS.register("renwang", Equipment.RenwangArmor::new);
        public static final DeferredHolder<Item, Item> RATTAN_ARMOR = ITEMS.register("rattan_armor", Equipment.RattanArmor::new);
        public static final DeferredHolder<Item, Item> CHITU = ITEMS.register("chitu", Equipment::new);
        public static final DeferredHolder<Item, Item> DILU = ITEMS.register("dilu", Equipment::new);

        public static final DeferredHolder<Item, Item> GAIN_CARD = ITEMS.register("gain_card", GainCardItem::new);
        public static final DeferredHolder<Item, Item> CARD_PILE = ITEMS.register("card_pile", CardPile::new);
        public static final DeferredHolder<Item, Item> GIFT_BOX = ITEMS.register("gift_box", ()-> new GiftBoxItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
        public static final DeferredHolder<Item, Item> GUDINGDAO = ITEMS.register("gudingdao", GudingdaoItem::new);
        public static final DeferredHolder<Item, Item> ARROW_RAIN = ITEMS.register("arrow_rain", ArrowRainItem::new);
        public static final DeferredHolder<Item, Item> BBJI = ITEMS.register("bbji", BBjiItem::new);
        public static final DeferredHolder<Item, Item> LET_ME_CC = ITEMS.register("let_me_cc", LetMeCCItem::new);
        public static final DeferredHolder<Item, Item> SUNSHINE_SMILE = ITEMS.register("sunshine_smile", SunshineSmile::new);
        public static final DeferredHolder<Item, Item> YES = ITEMS.register("yes", ()-> new Item(new Item.Properties()));
        public static final DeferredHolder<Item, Item> NO = ITEMS.register("no", ()-> new Item(new Item.Properties()));
        public static final DeferredHolder<Item, Item> INCOMPLETE_GUDINGDAO = ITEMS.register("incomplete_gdd", ()-> new Item(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GUDING = ITEMS.register("guding", ()-> new Item(new Item.Properties()));

        public static DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "dabaosword");
        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DABAOSWORD_TAB = TABS.register("dabaosword_tab",
                () -> CreativeModeTab.builder().icon(SUNSHINE_SMILE.get()::getDefaultInstance)
                        .title(Component.translatable("itemGroup.dabaosword_tab"))
                        .displayItems((p, o) -> {
                            o.accept(SHA.get());
                            o.accept(FIRE_SHA.get());
                            o.accept(THUNDER_SHA.get());
                            o.accept(SHAN.get());
                            o.accept(PEACH.get());
                            o.accept(JIU.get());
                            o.accept(BINGLIANG_ITEM.get());
                            o.accept(TOO_HAPPY_ITEM.get());
                            o.accept(SHANDIAN_ITEM.get());
                            o.accept(DISCARD.get());
                            o.accept(FIRE_ATTACK.get());
                            o.accept(JIEDAO.get());
                            o.accept(JUEDOU.get());
                            o.accept(NANMAN.get());
                            o.accept(STEAL.get());
                            o.accept(TAOYUAN.get());
                            o.accept(TIESUO.get());
                            o.accept(WANJIAN.get());
                            o.accept(WUGU.get());
                            o.accept(WUXIE.get());
                            o.accept(WUZHONG.get());

                            o.accept(FANGTIAN.get());
                            o.accept(GUDING_WEAPON.get());
                            o.accept(HANBING.get());
                            o.accept(QINGGANG.get());
                            o.accept(QINGLONG.get());
                            o.accept(ZHANGBA.get());
                            o.accept(BAGUA.get());
                            o.accept(BAIYIN.get());
                            o.accept(RENWANG.get());
                            o.accept(RATTAN_ARMOR.get());
                            o.accept(CHITU.get());
                            o.accept(DILU.get());
                            o.accept(GAIN_CARD.get());
                            o.accept(CARD_PILE.get());
                            //魏
                            o.accept(SkillCards.DUANLIANG);
                            o.accept(SkillCards.FANGZHU);
                            o.accept(SkillCards.XINGSHANG);
                            o.accept(SkillCards.GANGLIE);
                            o.accept(SkillCards.GONGAO);
                            o.accept(SkillCards.JIANXIONG);
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
                            o.accept(SkillCards.WUSHENG);
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
                            o.accept(SkillCards.YINGZI);
                            o.accept(SkillCards.ZHIHENG);
                            o.accept(SkillCards.ZHIJIAN);
                            //群
                            o.accept(SkillCards.JIZHAN);
                            o.accept(SkillCards.LEIJI);
                            o.accept(SkillCards.LUANJI);
                            o.accept(SkillCards.TAOLUAN);
                            o.accept(SkillCards.WEIMU);
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
        public static final DeferredHolder<Item, Item> DUANLIANG = ITEMS.register("duanliang", SkillItem.Duanliang::new);
        public static final DeferredHolder<Item, Item> FANGZHU = ITEMS.register("fangzhu", SkillItem.Fangzhu::new);
        public static final DeferredHolder<Item, Item> XINGSHANG = ITEMS.register("xingshang", SkillItem::new);
        public static final DeferredHolder<Item, Item> GANGLIE = ITEMS.register("ganglie", SkillItem.Ganglie::new);
        public static final DeferredHolder<Item, Item> GONGAO = ITEMS.register("gongao", SkillItem.Gongao::new);
        public static final DeferredHolder<Item, Item> JIANXIONG = ITEMS.register("jianxiong", SkillItem.Jianxiong::new);
        public static final DeferredHolder<Item, Item> JUEQING = ITEMS.register("jueqing", SkillItem.Jueqing::new);
        public static final DeferredHolder<Item, Item> LUOSHEN = ITEMS.register("luoshen", SkillItem.Luoshen::new);
        public static final DeferredHolder<Item, Item> QINGGUO = ITEMS.register("qingguo", SkillItem.Qingguo::new);
        public static final DeferredHolder<Item, Item> LUOYI = ITEMS.register("luoyi", SkillItem.Luoyi::new);
        public static final DeferredHolder<Item, Item> QICE = ITEMS.register("qice", SkillItem.Qice::new);
        public static final DeferredHolder<Item, Item> QUANJI = ITEMS.register("quanji", SkillItem.Quanji::new);
        public static final DeferredHolder<Item, Item> SHANZHUAN = ITEMS.register("shanzhuan", SkillItem.Shanzhuan::new);
        public static final DeferredHolder<Item, Item> SHENSU = ITEMS.register("shensu", SkillItem.Shensu::new);
        public static final DeferredHolder<Item, Item> YIJI = ITEMS.register("yiji", SkillItem.Yiji::new);
        //蜀
        public static final DeferredHolder<Item, Item> BENXI = ITEMS.register("benxi", SkillItem.Benxi::new);
        public static final DeferredHolder<Item, Item> HUOJI = ITEMS.register("huoji", SkillItem.Huoji::new);
        public static final DeferredHolder<Item, Item> KANPO = ITEMS.register("kanpo", SkillItem.Kanpo::new);
        public static final DeferredHolder<Item, Item> JIZHI = ITEMS.register("jizhi", SkillItem::new);
        public static final DeferredHolder<Item, Item> KUANGGU = ITEMS.register("kuanggu", SkillItem.Kuanggu::new);
        public static final DeferredHolder<Item, Item> LIEGONG = ITEMS.register("liegong", SkillItem.Liegong::new);
        public static final DeferredHolder<Item, Item> LONGDAN = ITEMS.register("longdan", SkillItem.Longdan::new);
        public static final DeferredHolder<Item, Item> RENDE = ITEMS.register("rende", SkillItem.Rende::new);
        public static final DeferredHolder<Item, Item> TIEJI = ITEMS.register("tieji", SkillItem.Tieji::new);
        public static final DeferredHolder<Item, Item> WUSHENG = ITEMS.register("wusheng", SkillItem.Wusheng::new);
        //吴
        public static final DeferredHolder<Item, Item> BUQU = ITEMS.register("buqu", SkillItem.Buqu::new);
        public static final DeferredHolder<Item, Item> GONGXIN = ITEMS.register("gongxin", SkillItem.Gongxin::new);
        public static final DeferredHolder<Item, Item> GUOSE = ITEMS.register("guose", SkillItem.Guose::new);
        public static final DeferredHolder<Item, Item> LIANYING = ITEMS.register("lianying", SkillItem.Lianying::new);
        public static final DeferredHolder<Item, Item> LIULI = ITEMS.register("liuli", SkillItem.Liuli::new);
        public static final DeferredHolder<Item, Item> KUROU = ITEMS.register("kurou", SkillItem.Kurou::new);
        public static final DeferredHolder<Item, Item> POJUN = ITEMS.register("pojun", SkillItem.Pojun::new);
        public static final DeferredHolder<Item, Item> QIXI = ITEMS.register("qixi", SkillItem.Qixi::new);
        public static final DeferredHolder<Item, Item> XIAOJI = ITEMS.register("xiaoji", SkillItem::new);
        public static final DeferredHolder<Item, Item> YINGZI = ITEMS.register("yingzi", SkillItem.Yingzi::new);
        public static final DeferredHolder<Item, Item> ZHIHENG = ITEMS.register("zhiheng", SkillItem.Zhiheng::new);
        public static final DeferredHolder<Item, Item> ZHIJIAN = ITEMS.register("zhijian", SkillItem.Zhijian::new);
        //群
        public static final DeferredHolder<Item, Item> JIZHAN = ITEMS.register("jizhan", SkillItem.Jizhan::new);
        public static final DeferredHolder<Item, Item> LEIJI = ITEMS.register("leiji", SkillItem::new);
        public static final DeferredHolder<Item, Item> LUANJI = ITEMS.register("luanji", SkillItem.Luanji::new);
        public static final DeferredHolder<Item, Item> TAOLUAN = ITEMS.register("taoluan", SkillItem.Taoluan::new);
        public static final DeferredHolder<Item, Item> WEIMU = ITEMS.register("weimu", SkillItem.Weimu::new);
        public static final DeferredHolder<Item, Item> MASHU = ITEMS.register("mashu", SkillItem::new);

        public static final DeferredHolder<Item, Item> FEIYING = ITEMS.register("feiying", SkillItem::new);
    }

    public static class Effects {
        public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, "dabaosword");
        public static final DeferredHolder<MobEffect, MobEffect> BINGLIANG = EFFECTS.register("bingliang", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0x46F732).addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.parse("bing"),-4, AttributeModifier.Operation.ADD_VALUE));
        public static final DeferredHolder<MobEffect, MobEffect> TOO_HAPPY = EFFECTS.register("too_happy", () -> new TooHappyEffect().addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.parse("le"),-10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
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
        public static final DeferredHolder<MobEffect, MobEffect> COOLDOWN = EFFECTS.register("cooldown", CooldownEffect::new);
        public static final DeferredHolder<MobEffect, MobEffect> COOLDOWN2 = EFFECTS.register("cooldown2", Cooldown2Effect::new);
        //无敌效果
        public static final DeferredHolder<MobEffect, MobEffect> INVULNERABLE = EFFECTS.register("invulnerable", InvulnerableEffect::new);
        //翻面效果
        public static final DeferredHolder<MobEffect, MobEffect> TURNOVER = EFFECTS.register("turn_over", TurnOverEffect::new);
        //铁骑效果
        public static final DeferredHolder<MobEffect, MobEffect> TIEJI = EFFECTS.register(
                "tieji", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0x07050F));
        public static final DeferredHolder<MobEffect, MobEffect> SHANDIAN = EFFECTS.register("shandian", ShandianEffect::new);
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
        public static final Supplier<MenuType<PileScreenHandler>> PILE_SCREEN_HANDLER = MENU.register("card_pile", () -> IMenuTypeExtension.create(PileScreenHandler::new));
    }

    public static class Sounds {
        public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "dabaosword");
        public static final DeferredHolder<SoundEvent, SoundEvent> JIANXIONG = register("jianxiong");
        public static final DeferredHolder<SoundEvent, SoundEvent> JIZHAN = register("jizhan");
        public static final DeferredHolder<SoundEvent, SoundEvent> YINGZI = register("yingzi");
        public static final DeferredHolder<SoundEvent, SoundEvent> WUSHENG = register("wusheng");
        public static final DeferredHolder<SoundEvent, SoundEvent> WEIMU = register("weimu");
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
        public static final DeferredHolder<SoundEvent, SoundEvent> RENWANG = register("renwang");
        public static final DeferredHolder<SoundEvent, SoundEvent> TENGJIA1 = register("tengjia1");
        public static final DeferredHolder<SoundEvent, SoundEvent> TENGJIA2 = register("tengjia2");
        public static final DeferredHolder<SoundEvent, SoundEvent> ZHANGBA = register("zhangba");

        public static final DeferredHolder<SoundEvent, SoundEvent> BINGLIANG = register("bingliang");
        public static final DeferredHolder<SoundEvent, SoundEvent> GUOHE = register("discard");
        public static final DeferredHolder<SoundEvent, SoundEvent> HUOGONG = register("huogong");
        public static final DeferredHolder<SoundEvent, SoundEvent> JIEDAO = register("jiedao");
        public static final DeferredHolder<SoundEvent, SoundEvent> JIU = register("jiu");
        public static final DeferredHolder<SoundEvent, SoundEvent> JUEDOU = register("juedou");
        public static final DeferredHolder<SoundEvent, SoundEvent> LEBU = register("too_happy");
        public static final DeferredHolder<SoundEvent, SoundEvent> RECOVER = register("peach");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHAN = register("shan");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHANDIAN = register("shandian");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHUNSHOU = register("steal");
        public static final DeferredHolder<SoundEvent, SoundEvent> TAOYUAN = register("taoyuan");
        public static final DeferredHolder<SoundEvent, SoundEvent> TIESUO = register("tiesuo");
        public static final DeferredHolder<SoundEvent, SoundEvent> WANJIAN = register("wanjian");
        public static final DeferredHolder<SoundEvent, SoundEvent> WUGU = register("wugu");
        public static final DeferredHolder<SoundEvent, SoundEvent> WUXIE = register("wuxie");
        public static final DeferredHolder<SoundEvent, SoundEvent> WUZHONG = register("wuzhong");
        public static final DeferredHolder<SoundEvent, SoundEvent> NANMAN = register("nanman");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHA = register("sha");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHA_FIRE = register("fire_sha");
        public static final DeferredHolder<SoundEvent, SoundEvent> SHA_THUNDER = register("thunder_sha");

        public static DeferredHolder<SoundEvent, SoundEvent> register(String name){
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath("dabaosword", name);
            return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(location));
        }
    }
}
