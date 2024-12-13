package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.effect.*;
import com.amotassic.dabaosword.entity.ModEntity;
import com.amotassic.dabaosword.item.BBjiItem;
import com.amotassic.dabaosword.item.GiftBoxItem;
import com.amotassic.dabaosword.item.LetMeCCItem;
import com.amotassic.dabaosword.item.card.Sha;
import com.amotassic.dabaosword.item.card.*;
import com.amotassic.dabaosword.item.equipment.*;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.skills.Qun;
import com.amotassic.dabaosword.item.skillcard.skills.Shu;
import com.amotassic.dabaosword.item.skillcard.skills.Wei;
import com.amotassic.dabaosword.item.skillcard.skills.Wu;
import com.amotassic.dabaosword.ui.FullInvScreenHandler;
import com.amotassic.dabaosword.ui.PileScreenHandler;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import com.amotassic.dabaosword.ui.SimpleMenuHandler;
import net.minecraft.core.Holder;
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
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class AllRegs {

    public static class Items {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");

        public static final Supplier<Item> SHA = ITEMS.register("sha", Sha::new);
        public static final Supplier<Item> FIRE_SHA = ITEMS.register("fire_sha", Sha.Fire::new);
        public static final Supplier<Item> THUNDER_SHA = ITEMS.register("thunder_sha", Sha.Thunder::new);
        public static final Supplier<Item> SHAN = ITEMS.register("shan", ShanItem::new);
        public static final Supplier<Item> PEACH = ITEMS.register("peach", PeachItem::new);
        public static final Supplier<Item> JIU = ITEMS.register("jiu", JiuItem::new);

        public static final Supplier<Item> BINGLIANG_ITEM = ITEMS.register("bingliang", BingliangItem::new);
        public static final Supplier<Item> TOO_HAPPY_ITEM = ITEMS.register("too_happy", TooHappyItem::new);
        public static final Supplier<Item> SHANDIAN_ITEM = ITEMS.register("shandian", ShandianItem::new);
        public static final Supplier<Item> DISCARD = ITEMS.register("discard", DiscardItem::new);
        public static final Supplier<Item> FIRE_ATTACK = ITEMS.register("huogong", FireAttackItem::new);
        public static final Supplier<Item> JUEDOU = ITEMS.register("juedou", JuedouItem::new);
        public static final Supplier<Item> JIEDAO = ITEMS.register("jiedao", JiedaoItem::new);
        public static final Supplier<Item> NANMAN = ITEMS.register("nanman", NanmanItem::new);
        public static final Supplier<Item> STEAL = ITEMS.register("steal", StealItem::new);
        public static final Supplier<Item> TAOYUAN = ITEMS.register("taoyuan", TaoyuanItem::new);
        public static final Supplier<Item> TIESUO = ITEMS.register("tiesuo", TiesuoItem::new);
        public static final Supplier<Item> WANJIAN = ITEMS.register("wanjian", WanjianItem::new);
        public static final Supplier<Item> WUGU = ITEMS.register("wugu", WuguItem::new);
        public static final Supplier<Item> WUXIE = ITEMS.register("wuxie", CardItem::new);
        public static final Supplier<Item> WUZHONG = ITEMS.register("wuzhong", CardItem.Wuzhong::new);

        public static final Supplier<Item> CIXIONG = ITEMS.register("cixiong", Equipment.CixiongWeapon::new);
        public static final Supplier<Item> FANGTIAN = ITEMS.register("fangtian", Equipment.FangtianWeapon::new);
        public static final Supplier<Item> GUANSHI = ITEMS.register("guanshi", Equipment.GuanshiWeapon::new);
        public static final Supplier<Item> GUDING_WEAPON = ITEMS.register("guding_dao", Equipment.GudingWeapon::new);
        public static final Supplier<Item> HANBING = ITEMS.register("hanbing", Equipment.HanbingWeapon::new);
        public static final Supplier<Item> QILIN = ITEMS.register("qilin", Equipment.QilinWeapon::new);
        public static final Supplier<Item> QINGGANG = ITEMS.register("qinggang", Equipment.QinggangWeapon::new);
        public static final Supplier<Item> QINGLONG = ITEMS.register("qinglong", Equipment.QinglongWeapon::new);
        public static final Supplier<Item> ZHANGBA = ITEMS.register("zhangba", Equipment.ZhangbaWeapon::new);
        public static final Supplier<Item> LIANNU = ITEMS.register("liannu", Equipment.LiannuWeapon::new);
        public static final Supplier<Item> ZHUQUE = ITEMS.register("zhuque", Equipment.ZhuqueWeapon::new);
        public static final Supplier<Item> BAGUA = ITEMS.register("bagua", Equipment.BaguaArmor::new);
        public static final Supplier<Item> BAIYIN = ITEMS.register("baiyin", Equipment.BaiyinArmor::new);
        public static final Supplier<Item> RENWANG = ITEMS.register("renwang", Equipment.RenwangArmor::new);
        public static final Supplier<Item> RATTAN_ARMOR = ITEMS.register("rattan_armor", Equipment.RattanArmor::new);
        public static final Supplier<Item> CHITU = ITEMS.register("chitu", Equipment.AttackHorse::new);
        public static final Supplier<Item> DILU = ITEMS.register("dilu", Equipment.DefendHorse::new);

        public static final Supplier<Item> GAIN_CARD = ITEMS.register("gain_card", GainCardItem::new);
        public static final Supplier<Item> CARD_PILE = ITEMS.register("card_pile", CardPile::new);
        public static final Supplier<Item> GIFT_BOX = ITEMS.register("gift_box", ()-> new GiftBoxItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
        public static final Supplier<Item> GUDINGDAO = ITEMS.register("gudingdao", GudingdaoItem::new);
        public static final Supplier<Item> ARROW_RAIN = ITEMS.register("arrow_rain", ArrowRainItem::new);
        public static final Supplier<Item> BBJI = ITEMS.register("bbji", BBjiItem::new);
        public static final Supplier<Item> LET_ME_CC = ITEMS.register("let_me_cc", LetMeCCItem::new);
        public static final Supplier<Item> SUNSHINE_SMILE = ITEMS.register("sunshine_smile", SunshineSmile::new);
        @SuppressWarnings("deprecation")
        public static final Supplier<Item> XUYOU_SPAWN_EGG = ITEMS.register("xuyou_spawn_egg", () -> new SpawnEggItem(ModEntity.XUYOU.get(), 0x52BDF7, 0x8D8B96, new Item.Properties()));
        public static final Supplier<Item> INCOMPLETE_GUDINGDAO = ITEMS.register("incomplete_gdd", ()-> new Item(new Item.Properties().stacksTo(1)));
        public static final Supplier<Item> GUDING = ITEMS.register("guding", ()-> new Item(new Item.Properties()));

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

                            o.accept(CIXIONG.get());
                            o.accept(FANGTIAN.get());
                            o.accept(GUANSHI.get());
                            o.accept(GUDING_WEAPON.get());
                            o.accept(HANBING.get());
                            o.accept(QILIN.get());
                            o.accept(QINGGANG.get());
                            o.accept(QINGLONG.get());
                            o.accept(ZHANGBA.get());
                            o.accept(LIANNU.get());
                            o.accept(ZHUQUE.get());
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
                            o.accept(SkillCards.FENYIN);
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
                            o.accept(SkillCards.JIJIU);
                            o.accept(SkillCards.JIUCHI);
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
                            o.accept(XUYOU_SPAWN_EGG.get());
                        }).build());
    }

    public static class Skills {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");
        //魏
        public static final Supplier<Item> DUANLIANG = ITEMS.register("duanliang", Wei.Duanliang::new);
        public static final Supplier<Item> FANGZHU = ITEMS.register("fangzhu", Wei.Fangzhu::new);
        public static final Supplier<Item> XINGSHANG = ITEMS.register("xingshang", Wei.Xingshang::new);
        public static final Supplier<Item> GANGLIE = ITEMS.register("ganglie", Wei.Ganglie::new);
        public static final Supplier<Item> GONGAO = ITEMS.register("gongao", Wei.Gongao::new);
        public static final Supplier<Item> JIANXIONG = ITEMS.register("jianxiong", Wei.Jianxiong::new);
        public static final Supplier<Item> JUEQING = ITEMS.register("jueqing", Wei.Jueqing::new);
        public static final Supplier<Item> LUOSHEN = ITEMS.register("luoshen", Wei.Luoshen::new);
        public static final Supplier<Item> QINGGUO = ITEMS.register("qingguo", Wei.Qingguo::new);
        public static final Supplier<Item> LUOYI = ITEMS.register("luoyi", Wei.Luoyi::new);
        public static final Supplier<Item> QICE = ITEMS.register("qice", Wei.Qice::new);
        public static final Supplier<Item> QUANJI = ITEMS.register("quanji", Wei.Quanji::new);
        public static final Supplier<Item> SHANZHUAN = ITEMS.register("shanzhuan", Wei.Shanzhuan::new);
        public static final Supplier<Item> SHENSU = ITEMS.register("shensu", Wei.Shensu::new);
        public static final Supplier<Item> YIJI = ITEMS.register("yiji", Wei.Yiji::new);
        //蜀
        public static final Supplier<Item> BENXI = ITEMS.register("benxi", Shu.Benxi::new);
        public static final Supplier<Item> HUOJI = ITEMS.register("huoji", Shu.Huoji::new);
        public static final Supplier<Item> KANPO = ITEMS.register("kanpo", Shu.Kanpo::new);
        public static final Supplier<Item> JIZHI = ITEMS.register("jizhi", Shu.Jizhi::new);
        public static final Supplier<Item> KUANGGU = ITEMS.register("kuanggu", Shu.Kuanggu::new);
        public static final Supplier<Item> LIEGONG = ITEMS.register("liegong", Shu.Liegong::new);
        public static final Supplier<Item> LONGDAN = ITEMS.register("longdan", Shu.Longdan::new);
        public static final Supplier<Item> RENDE = ITEMS.register("rende", Shu.Rende::new);
        public static final Supplier<Item> TIEJI = ITEMS.register("tieji", Shu.Tieji::new);
        public static final Supplier<Item> WUSHENG = ITEMS.register("wusheng", Shu.Wusheng::new);
        //吴
        public static final Supplier<Item> BUQU = ITEMS.register("buqu", Wu.Buqu::new);
        public static final Supplier<Item> FENYIN = ITEMS.register("fenyin", Wu.Fenyin::new);
        public static final Supplier<Item> GONGXIN = ITEMS.register("gongxin", Wu.Gongxin::new);
        public static final Supplier<Item> GUOSE = ITEMS.register("guose", Wu.Guose::new);
        public static final Supplier<Item> LIANYING = ITEMS.register("lianying", Wu.Lianying::new);
        public static final Supplier<Item> LIULI = ITEMS.register("liuli", Wu.Liuli::new);
        public static final Supplier<Item> KUROU = ITEMS.register("kurou", Wu.Kurou::new);
        public static final Supplier<Item> POJUN = ITEMS.register("pojun", Wu.Pojun::new);
        public static final Supplier<Item> QIXI = ITEMS.register("qixi", Wu.Qixi::new);
        public static final Supplier<Item> XIAOJI = ITEMS.register("xiaoji", Wu.Xiaoji::new);
        public static final Supplier<Item> YINGZI = ITEMS.register("yingzi", Wu.Yingzi::new);
        public static final Supplier<Item> ZHIHENG = ITEMS.register("zhiheng", Wu.Zhiheng::new);
        public static final Supplier<Item> ZHIJIAN = ITEMS.register("zhijian", Wu.Zhijian::new);
        //群
        public static final Supplier<Item> JIJIU = ITEMS.register("jijiu", Qun.Jijiu::new);
        public static final Supplier<Item> JIUCHI = ITEMS.register("jiuchi", Qun.Jiuchi::new);
        public static final Supplier<Item> JIZHAN = ITEMS.register("jizhan", Qun.Jizhan::new);
        public static final Supplier<Item> LEIJI = ITEMS.register("leiji", Qun.Leiji::new);
        public static final Supplier<Item> LUANJI = ITEMS.register("luanji", Qun.Luanji::new);
        public static final Supplier<Item> TAOLUAN = ITEMS.register("taoluan", Qun.Taoluan::new);
        public static final Supplier<Item> WEIMU = ITEMS.register("weimu", Qun.Weimu::new);
        public static final Supplier<Item> MASHU = ITEMS.register("mashu", Qun.Mashu::new);

        public static final Supplier<Item> FEIYING = ITEMS.register("feiying", Qun.Feiying::new);
    }

    public static class Effects {
        public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, "dabaosword");
        public static final Holder<MobEffect> BINGLIANG = EFFECTS.register("bingliang", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0x46F732).addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.parse("bing"),-4, AttributeModifier.Operation.ADD_VALUE));
        public static final Holder<MobEffect> TOO_HAPPY = EFFECTS.register("too_happy", () -> new TooHappyEffect().addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.parse("le"),-10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        //触及距离增加
        public static final Holder<MobEffect> REACH = EFFECTS.register("reach", () -> new CommonEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)
                .addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, ResourceLocation.parse("-1ma"),1.0, AttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("-1ma"),1.0, AttributeModifier.Operation.ADD_VALUE));
        //近战防御范围增加
        public static final Holder<MobEffect> DEFEND = EFFECTS.register(
                "defend", () -> new CommonEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF));
        public static final Holder<MobEffect> DEFENDED = EFFECTS.register(
                "defended", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0xFFFFFF).addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("defend-"),-1.0, AttributeModifier.Operation.ADD_VALUE));
        //冷却状态效果
        public static final Holder<MobEffect> COOLDOWN = EFFECTS.register("cooldown", CooldownEffect::new);
        public static final Holder<MobEffect> COOLDOWN2 = EFFECTS.register("cooldown2", Cooldown2Effect::new);
        //无敌效果
        public static final Holder<MobEffect> INVULNERABLE = EFFECTS.register(
                "invulnerable", () -> new CommonEffect(MobEffectCategory.BENEFICIAL,0x35F5DF));
        //翻面效果
        public static final Holder<MobEffect> TURNOVER = EFFECTS.register("turn_over", TurnOverEffect::new);
        //铁骑效果
        public static final Holder<MobEffect> TIEJI = EFFECTS.register(
                "tieji", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0x07050F));
        public static final Holder<MobEffect> SHANDIAN = EFFECTS.register("shandian", ShandianEffect::new);
    }

    public static class Other {
        //物品组件注册
        public static final DeferredRegister.DataComponents DATA_COMPONENT = DeferredRegister.createDataComponents("dabaosword");
        public static final Supplier<DataComponentType<Integer>> TAGS = DATA_COMPONENT.registerComponentType("tags", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
        public static final Supplier<DataComponentType<Integer>> CD = DATA_COMPONENT.registerComponentType("cd", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

        public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(BuiltInRegistries.MENU, "dabaosword");
        public static final Supplier<MenuType<SimpleMenuHandler>> SIMPLE_MENU_HANDLER = MENU.register("simple_menu", () -> IMenuTypeExtension.create(SimpleMenuHandler::new));
        public static final Supplier<MenuType<PlayerInvScreenHandler>> PLAYER_INV_SCREEN_HANDLER = MENU.register("player_inv", () -> IMenuTypeExtension.create(PlayerInvScreenHandler::new));
        public static final Supplier<MenuType<FullInvScreenHandler>> FULL_INV_SCREEN_HANDLER = MENU.register("full_inv", () -> IMenuTypeExtension.create(FullInvScreenHandler::new));
        public static final Supplier<MenuType<PileScreenHandler>> PILE_SCREEN_HANDLER = MENU.register("card_pile", () -> IMenuTypeExtension.create(PileScreenHandler::new));
    }

    public static class Sounds {
        public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "dabaosword");
        public static final Supplier<SoundEvent> FENYIN = register("fenyin");
        public static final Supplier<SoundEvent> JIJIU = register("jijiu");
        public static final Supplier<SoundEvent> JIUCHI = register("jiuchi");
        public static final Supplier<SoundEvent> JIANXIONG = register("jianxiong");
        public static final Supplier<SoundEvent> JIZHAN = register("jizhan");
        public static final Supplier<SoundEvent> YINGZI = register("yingzi");
        public static final Supplier<SoundEvent> WUSHENG = register("wusheng");
        public static final Supplier<SoundEvent> WEIMU = register("weimu");
        public static final Supplier<SoundEvent> SHENSU = register("shensu");
        public static final Supplier<SoundEvent> LIANYING = register("lianying");
        public static final Supplier<SoundEvent> XIAOJI = register("xiaoji");
        public static final Supplier<SoundEvent> LET_ME_CC = register("letmecc");
        public static final Supplier<SoundEvent> LONGDAN = register("longdan");
        public static final Supplier<SoundEvent> GONGXIN = register("gongxin");
        public static final Supplier<SoundEvent> ZHIJIAN = register("zhijian");
        public static final Supplier<SoundEvent> SHANZHUAN = register("shanzhuan");
        public static final Supplier<SoundEvent> RENDE = register("rende");
        public static final Supplier<SoundEvent> ZHIHENG = register("zhiheng");
        public static final Supplier<SoundEvent> BUQU = register("buqu");
        public static final Supplier<SoundEvent> TIEJI = register("tieji");
        public static final Supplier<SoundEvent> GANGLIE = register("ganglie");
        public static final Supplier<SoundEvent> FANGZHU = register("fangzhu");
        public static final Supplier<SoundEvent> XINGSHANG = register("xingshang");
        public static final Supplier<SoundEvent> BBJI = register("bbji");
        public static final Supplier<SoundEvent> XUYOU = register("xuyou");
        public static final Supplier<SoundEvent> DUANLIANG = register("duanliang");
        public static final Supplier<SoundEvent> LUOSHEN = register("luoshen");
        public static final Supplier<SoundEvent> QIXI = register("qixi");
        public static final Supplier<SoundEvent> QINGGUO = register("qingguo");
        public static final Supplier<SoundEvent> LIEGONG = register("liegong");
        public static final Supplier<SoundEvent> GONGAO = register("gongao");
        public static final Supplier<SoundEvent> WEIZHONG = register("weizhong");
        public static final Supplier<SoundEvent> BENXI = register("benxi");
        public static final Supplier<SoundEvent> LEIJI = register("leiji");
        public static final Supplier<SoundEvent> GIFTBOX = register("giftbox");
        public static final Supplier<SoundEvent> KANPO = register("kanpo");
        public static final Supplier<SoundEvent> GUOSE = register("guose");
        public static final Supplier<SoundEvent> LIULI = register("liuli");
        public static final Supplier<SoundEvent> JUEQING = register("jueqing");
        public static final Supplier<SoundEvent> LUANJI = register("luanji");
        public static final Supplier<SoundEvent> KUROU = register("kurou");
        public static final Supplier<SoundEvent> JIZHI = register("jizhi");
        public static final Supplier<SoundEvent> QICE = register("qice");
        public static final Supplier<SoundEvent> LUOYI = register("luoyi");
        public static final Supplier<SoundEvent> HUOJI = register("huoji");
        public static final Supplier<SoundEvent> QUANJI = register("quanji");
        public static final Supplier<SoundEvent> ZILI = register("zili");
        public static final Supplier<SoundEvent> PAIYI = register("paiyi");
        public static final Supplier<SoundEvent> YIJI = register("yiji");
        public static final Supplier<SoundEvent> TAOLUAN = register("taoluan");
        public static final Supplier<SoundEvent> POJUN = register("pojun");
        public static final Supplier<SoundEvent> KUANGGU = register("kuanggu");

        public static final Supplier<SoundEvent> BAGUA = register("bagua");
        public static final Supplier<SoundEvent> BAIYIN = register("baiyin");
        public static final Supplier<SoundEvent> CIXIONG = register("cixiong");
        public static final Supplier<SoundEvent> FANGTIAN = register("fangtian");
        public static final Supplier<SoundEvent> GUANSHI = register("guanshi");
        public static final Supplier<SoundEvent> GUDING = register("guding");
        public static final Supplier<SoundEvent> HANBING = register("hanbing");
        public static final Supplier<SoundEvent> LIANNU = register("liannu");
        public static final Supplier<SoundEvent> QILIN = register("qilin");
        public static final Supplier<SoundEvent> QINGGANG = register("qinggang");
        public static final Supplier<SoundEvent> QINGLONG = register("qinglong");
        public static final Supplier<SoundEvent> RENWANG = register("renwang");
        public static final Supplier<SoundEvent> TENGJIA1 = register("tengjia1");
        public static final Supplier<SoundEvent> TENGJIA2 = register("tengjia2");
        public static final Supplier<SoundEvent> ZHANGBA = register("zhangba");
        public static final Supplier<SoundEvent> ZHUQUE = register("zhuque");

        public static final Supplier<SoundEvent> BINGLIANG = register("bingliang");
        public static final Supplier<SoundEvent> GUOHE = register("discard");
        public static final Supplier<SoundEvent> HUOGONG = register("huogong");
        public static final Supplier<SoundEvent> JIEDAO = register("jiedao");
        public static final Supplier<SoundEvent> JIU = register("jiu");
        public static final Supplier<SoundEvent> JUEDOU = register("juedou");
        public static final Supplier<SoundEvent> LEBU = register("too_happy");
        public static final Supplier<SoundEvent> RECOVER = register("peach");
        public static final Supplier<SoundEvent> SHAN = register("shan");
        public static final Supplier<SoundEvent> SHANDIAN = register("shandian");
        public static final Supplier<SoundEvent> SHUNSHOU = register("steal");
        public static final Supplier<SoundEvent> TAOYUAN = register("taoyuan");
        public static final Supplier<SoundEvent> TIESUO = register("tiesuo");
        public static final Supplier<SoundEvent> WANJIAN = register("wanjian");
        public static final Supplier<SoundEvent> WUGU = register("wugu");
        public static final Supplier<SoundEvent> WUXIE = register("wuxie");
        public static final Supplier<SoundEvent> WUZHONG = register("wuzhong");
        public static final Supplier<SoundEvent> NANMAN = register("nanman");
        public static final Supplier<SoundEvent> SHA = register("sha");
        public static final Supplier<SoundEvent> SHA_FIRE = register("fire_sha");
        public static final Supplier<SoundEvent> SHA_THUNDER = register("thunder_sha");

        public static Supplier<SoundEvent> register(String name){
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath("dabaosword", name);
            return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(location));
        }
    }
}
