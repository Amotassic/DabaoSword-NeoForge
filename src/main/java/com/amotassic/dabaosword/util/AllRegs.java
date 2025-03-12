package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.effect.*;
import com.amotassic.dabaosword.entity.ModEntity;
import com.amotassic.dabaosword.item.BBjiItem;
import com.amotassic.dabaosword.item.GiftBoxItem;
import com.amotassic.dabaosword.item.LetMeCCItem;
import com.amotassic.dabaosword.item.card.*;
import com.amotassic.dabaosword.item.equipment.*;
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
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings({"unused", "deprecation"})
public class AllRegs {

    public static class Items {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");

        public static final Supplier<Item>
        SHA = ITEMS.register("sha", Sha::new),
        FIRE_SHA = ITEMS.register("fire_sha", Sha.Fire::new),
        THUNDER_SHA = ITEMS.register("thunder_sha", Sha.Thunder::new),
        SHAN = ITEMS.register("shan", ShanItem::new),
        PEACH = ITEMS.register("peach", PeachItem::new),
        JIU = ITEMS.register("jiu", JiuItem::new),

        BINGLIANG_ITEM = ITEMS.register("bingliang", BingliangItem::new),
        TOO_HAPPY_ITEM = ITEMS.register("too_happy", TooHappyItem::new),
        SHANDIAN_ITEM = ITEMS.register("shandian", ShandianItem::new),
        DISCARD = ITEMS.register("discard", DiscardItem::new),
        FIRE_ATTACK = ITEMS.register("huogong", FireAttackItem::new),
        JUEDOU = ITEMS.register("juedou", JuedouItem::new),
        JIEDAO = ITEMS.register("jiedao", JiedaoItem::new),
        NANMAN = ITEMS.register("nanman", NanmanItem::new),
        STEAL = ITEMS.register("steal", StealItem::new),
        TAOYUAN = ITEMS.register("taoyuan", TaoyuanItem::new),
        TIESUO = ITEMS.register("tiesuo", TiesuoItem::new),
        WANJIAN = ITEMS.register("wanjian", WanjianItem::new),
        WUGU = ITEMS.register("wugu", WuguItem::new),
        WUXIE = ITEMS.register("wuxie", CardItem::new),
        WUZHONG = ITEMS.register("wuzhong", CardItem.Wuzhong::new),

        CIXIONG = ITEMS.register("cixiong", Equipment.CixiongWeapon::new),
        FANGTIAN = ITEMS.register("fangtian", Equipment.FangtianWeapon::new),
        GUANSHI = ITEMS.register("guanshi", Equipment.GuanshiWeapon::new),
        GUDING_WEAPON = ITEMS.register("guding_dao", Equipment.GudingWeapon::new),
        HANBING = ITEMS.register("hanbing", Equipment.HanbingWeapon::new),
        QILIN = ITEMS.register("qilin", Equipment.QilinWeapon::new),
        QINGGANG = ITEMS.register("qinggang", Equipment.QinggangWeapon::new),
        QINGLONG = ITEMS.register("qinglong", Equipment.QinglongWeapon::new),
        ZHANGBA = ITEMS.register("zhangba", Equipment.ZhangbaWeapon::new),
        LIANNU = ITEMS.register("liannu", Equipment.LiannuWeapon::new),
        ZHUQUE = ITEMS.register("zhuque", Equipment.ZhuqueWeapon::new),
        BAGUA = ITEMS.register("bagua", Equipment.BaguaArmor::new),
        BAIYIN = ITEMS.register("baiyin", Equipment.BaiyinArmor::new),
        RENWANG = ITEMS.register("renwang", Equipment.RenwangArmor::new),
        RATTAN_ARMOR = ITEMS.register("rattan_armor", Equipment.RattanArmor::new),
        CHITU = ITEMS.register("chitu", Equipment.AttackHorse::new),
        DILU = ITEMS.register("dilu", Equipment.DefendHorse::new),

        GAIN_CARD = ITEMS.register("gain_card", GainCardItem::new),
        CARD_PILE = ITEMS.register("card_pile", (ResourceLocation properties) -> new CardPile()),
        GIFT_BOX = ITEMS.register("gift_box", ()-> new GiftBoxItem(new Item.Properties().rarity(Rarity.UNCOMMON))),
        BBJI = ITEMS.register("bbji", BBjiItem::new),
        LET_ME_CC = ITEMS.register("let_me_cc", LetMeCCItem::new),
        SUNSHINE_SMILE = ITEMS.register("sunshine_smile", SunshineSmile::new),
        XUYOU_SPAWN_EGG = ITEMS.register("xuyou_spawn_egg", () -> new SpawnEggItem(ModEntity.XUYOU.get(), 0x52BDF7, 0x8D8B96, new Item.Properties())),
        GUDINGDAO = ITEMS.register("gudingdao", GudingdaoItem::new),
        ARROW_RAIN = ITEMS.register("arrow_rain", ArrowRainItem::new),
        INCOMPLETE_GUDINGDAO = ITEMS.register("incomplete_gdd", ()-> new Item(new Item.Properties().stacksTo(1))),
        GUDING = ITEMS.register("guding", ()-> new Item(new Item.Properties()));

        public static DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "dabaosword");
        static {
            TABS.register("dabaosword_tab", () -> CreativeModeTab.builder().icon(SUNSHINE_SMILE.get()::getDefaultInstance)
                    .title(Component.translatable("itemGroup.dabaosword_tab")).build());
        }
    }

    public static class Skills {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");
        //魏
        public static final Supplier<Item>
        DUANLIANG = ITEMS.register("duanliang", Wei.Duanliang::new),
        FANGZHU = ITEMS.register("fangzhu", Wei.Fangzhu::new),
        XINGSHANG = ITEMS.register("xingshang", Wei.Xingshang::new),
        GANGLIE = ITEMS.register("ganglie", Wei.Ganglie::new),
        GONGAO = ITEMS.register("gongao", Wei.Gongao::new),
        JIANXIONG = ITEMS.register("jianxiong", Wei.Jianxiong::new),
        JUEQING = ITEMS.register("jueqing", Wei.Jueqing::new),
        LUOSHEN = ITEMS.register("luoshen", Wei.Luoshen::new),
        QINGGUO = ITEMS.register("qingguo", Wei.Qingguo::new),
        LUOYI = ITEMS.register("luoyi", Wei.Luoyi::new),
        QICE = ITEMS.register("qice", Wei.Qice::new),
        QUANJI = ITEMS.register("quanji", Wei.Quanji::new),
        SHANZHUAN = ITEMS.register("shanzhuan", Wei.Shanzhuan::new),
        SHENSU = ITEMS.register("shensu", Wei.Shensu::new),
        YIJI = ITEMS.register("yiji", Wei.Yiji::new),
        //蜀
        BENXI = ITEMS.register("benxi", Shu.Benxi::new),
        HUOJI = ITEMS.register("huoji", Shu.Huoji::new),
        KANPO = ITEMS.register("kanpo", Shu.Kanpo::new),
        JIZHI = ITEMS.register("jizhi", Shu.Jizhi::new),
        KUANGGU = ITEMS.register("kuanggu", Shu.Kuanggu::new),
        LIEGONG = ITEMS.register("liegong", Shu.Liegong::new),
        LONGDAN = ITEMS.register("longdan", Shu.Longdan::new),
        RENDE = ITEMS.register("rende", Shu.Rende::new),
        TIEJI = ITEMS.register("tieji", Shu.Tieji::new),
        WUSHENG = ITEMS.register("wusheng", Shu.Wusheng::new),
        //吴
        BUQU = ITEMS.register("buqu", Wu.Buqu::new),
        FENYIN = ITEMS.register("fenyin", Wu.Fenyin::new),
        GONGXIN = ITEMS.register("gongxin", Wu.Gongxin::new),
        GUOSE = ITEMS.register("guose", Wu.Guose::new),
        LIANYING = ITEMS.register("lianying", Wu.Lianying::new),
        LIULI = ITEMS.register("liuli", Wu.Liuli::new),
        KUROU = ITEMS.register("kurou", Wu.Kurou::new),
        POJUN = ITEMS.register("pojun", Wu.Pojun::new),
        QIXI = ITEMS.register("qixi", Wu.Qixi::new),
        XIAOJI = ITEMS.register("xiaoji", Wu.Xiaoji::new),
        YINGZI = ITEMS.register("yingzi", Wu.Yingzi::new),
        ZHIHENG = ITEMS.register("zhiheng", Wu.Zhiheng::new),
        ZHIJIAN = ITEMS.register("zhijian", Wu.Zhijian::new),
        //群
        JIJIU = ITEMS.register("jijiu", Qun.Jijiu::new),
        JIUCHI = ITEMS.register("jiuchi", Qun.Jiuchi::new),
        JIZHAN = ITEMS.register("jizhan", Qun.Jizhan::new),
        LEIJI = ITEMS.register("leiji", Qun.Leiji::new),
        LUANJI = ITEMS.register("luanji", Qun.Luanji::new),
        TAOLUAN = ITEMS.register("taoluan", Qun.Taoluan::new),
        WEIMU = ITEMS.register("weimu", Qun.Weimu::new),
        MASHU = ITEMS.register("mashu", Qun.Mashu::new),

        FEIYING = ITEMS.register("feiying", Qun.Feiying::new);
    }

    public static class Effects {
        public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, "dabaosword");
        public static final Holder<MobEffect> BINGLIANG = EFFECTS.register("bingliang", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0x46F732).addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.parse("bing"),-4, AttributeModifier.Operation.ADD_VALUE)),
        TOO_HAPPY = EFFECTS.register("too_happy", () -> new TooHappyEffect().addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.parse("le"),-10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)),
        //触及距离增加
        REACH = EFFECTS.register("reach", () -> new CommonEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)
                .addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, ResourceLocation.parse("-1ma"),1.0, AttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("-1ma"),1.0, AttributeModifier.Operation.ADD_VALUE)),
        //近战防御范围增加
        DEFEND = EFFECTS.register("defend", () -> new CommonEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
        DEFENDED = EFFECTS.register("defended", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0xFFFFFF).addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("defend-"),-1.0, AttributeModifier.Operation.ADD_VALUE)),
        //冷却状态效果
        COOLDOWN = EFFECTS.register("cooldown", CooldownEffect::new),
        COOLDOWN2 = EFFECTS.register("cooldown2", Cooldown2Effect::new),
        //无敌效果
        INVULNERABLE = EFFECTS.register("invulnerable", () -> new CommonEffect(MobEffectCategory.BENEFICIAL,0x35F5DF)),
        //翻面效果
        TURNOVER = EFFECTS.register("turn_over", TurnOverEffect::new),
        //铁骑效果
        TIEJI = EFFECTS.register("tieji", () -> new CommonEffect(MobEffectCategory.HARMFUL, 0x07050F)),
        SHANDIAN = EFFECTS.register("shandian", ShandianEffect::new);
    }

    public static class Other {
        //物品组件注册
        public static final DeferredRegister.DataComponents DATA_COMPONENT = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "dabaosword");
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
        public static final Supplier<SoundEvent>
        FENYIN = register("fenyin"),
        JIJIU = register("jijiu"),
        JIUCHI = register("jiuchi"),
        JIANXIONG = register("jianxiong"),
        JIZHAN = register("jizhan"),
        YINGZI = register("yingzi"),
        WUSHENG = register("wusheng"),
        WEIMU = register("weimu"),
        SHENSU = register("shensu"),
        LIANYING = register("lianying"),
        XIAOJI = register("xiaoji"),
        LET_ME_CC = register("letmecc"),
        LONGDAN = register("longdan"),
        GONGXIN = register("gongxin"),
        ZHIJIAN = register("zhijian"),
        SHANZHUAN = register("shanzhuan"),
        RENDE = register("rende"),
        ZHIHENG = register("zhiheng"),
        BUQU = register("buqu"),
        TIEJI = register("tieji"),
        GANGLIE = register("ganglie"),
        FANGZHU = register("fangzhu"),
        XINGSHANG = register("xingshang"),
        BBJI = register("bbji"),
        XUYOU = register("xuyou"),
        DUANLIANG = register("duanliang"),
        LUOSHEN = register("luoshen"),
        QIXI = register("qixi"),
        QINGGUO = register("qingguo"),
        LIEGONG = register("liegong"),
        GONGAO = register("gongao"),
        WEIZHONG = register("weizhong"),
        BENXI = register("benxi"),
        LEIJI = register("leiji"),
        GIFTBOX = register("giftbox"),
        KANPO = register("kanpo"),
        GUOSE = register("guose"),
        LIULI = register("liuli"),
        JUEQING = register("jueqing"),
        LUANJI = register("luanji"),
        KUROU = register("kurou"),
        JIZHI = register("jizhi"),
        QICE = register("qice"),
        LUOYI = register("luoyi"),
        HUOJI = register("huoji"),
        QUANJI = register("quanji"),
        ZILI = register("zili"),
        PAIYI = register("paiyi"),
        YIJI = register("yiji"),
        TAOLUAN = register("taoluan"),
        POJUN = register("pojun"),
        KUANGGU = register("kuanggu"),

        BAGUA = register("bagua"),
        BAIYIN = register("baiyin"),
        CIXIONG = register("cixiong"),
        FANGTIAN = register("fangtian"),
        GUANSHI = register("guanshi"),
        GUDING = register("guding"),
        HANBING = register("hanbing"),
        LIANNU = register("liannu"),
        QILIN = register("qilin"),
        QINGGANG = register("qinggang"),
        QINGLONG = register("qinglong"),
        RENWANG = register("renwang"),
        TENGJIA1 = register("tengjia1"),
        TENGJIA2 = register("tengjia2"),
        ZHANGBA = register("zhangba"),
        ZHUQUE = register("zhuque"),

        BINGLIANG = register("bingliang"),
        GUOHE = register("discard"),
        HUOGONG = register("huogong"),
        JIEDAO = register("jiedao"),
        JIU = register("jiu"),
        JUEDOU = register("juedou"),
        LEBU = register("too_happy"),
        RECOVER = register("peach"),
        SHAN = register("shan"),
        SHANDIAN = register("shandian"),
        SHUNSHOU = register("steal"),
        TAOYUAN = register("taoyuan"),
        TIESUO = register("tiesuo"),
        WANJIAN = register("wanjian"),
        WUGU = register("wugu"),
        WUXIE = register("wuxie"),
        WUZHONG = register("wuzhong"),
        NANMAN = register("nanman"),
        SHA = register("sha"),
        SHA_FIRE = register("fire_sha"),
        SHA_THUNDER = register("thunder_sha");

        static {
            register("diankuang"); register("wushuang"); register("win"); register("kill1"); register("kill2"); register("kill3"); register("kill4"); register("kill5"); register("kill6"); register("kill7");
        }


        public static Supplier<SoundEvent> register(String name){
            ResourceLocation location = ResourceLocation.fromNamespaceAndPath("dabaosword", name);
            return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(location));
        }
    }
}
