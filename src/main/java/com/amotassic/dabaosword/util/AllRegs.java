package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.effect.*;
import com.amotassic.dabaosword.entity.ModEntity;
import com.amotassic.dabaosword.item.GiftBoxItem;
import com.amotassic.dabaosword.item.card.*;
import com.amotassic.dabaosword.item.card.equipment.Armor;
import com.amotassic.dabaosword.item.card.equipment.Mount;
import com.amotassic.dabaosword.item.card.equipment.Weapon;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.item.skillcard.skills.Qun;
import com.amotassic.dabaosword.item.skillcard.skills.Shu;
import com.amotassic.dabaosword.item.skillcard.skills.Wei;
import com.amotassic.dabaosword.item.skillcard.skills.Wu;
import com.amotassic.dabaosword.item.tool.*;
import com.amotassic.dabaosword.ui.FullInvScreenHandler;
import com.amotassic.dabaosword.ui.PileScreenHandler;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "deprecation"})
public class AllRegs {

    public static class Items {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");

        public static final Supplier<CardItem>
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
        WUXIE = ITEMS.register("wuxie", CardItem.Armoury::new),
        WUZHONG = ITEMS.register("wuzhong", WuzhongItem::new),

        CIXIONG = ITEMS.register("cixiong", Weapon.Cixiong::new),
        FANGTIAN = ITEMS.register("fangtian", Weapon.Fangtian::new),
        GUANSHI = ITEMS.register("guanshi", Weapon.Guanshi::new),
        GUDING_WEAPON = ITEMS.register("guding_dao", Weapon.Guding::new),
        HANBING = ITEMS.register("hanbing", Weapon.Hanbing::new),
        QILIN = ITEMS.register("qilin", Weapon.Qilin::new),
        QINGGANG = ITEMS.register("qinggang", Weapon.Qinggang::new),
        QINGLONG = ITEMS.register("qinglong", Weapon.Qinglong::new),
        ZHANGBA = ITEMS.register("zhangba", Weapon.Zhangba::new),
        LIANNU = ITEMS.register("liannu", Weapon.Liannu::new),
        ZHUQUE = ITEMS.register("zhuque", Weapon.Zhuque::new),
        BAGUA = ITEMS.register("bagua", Armor.Bagua::new),
        BAIYIN = ITEMS.register("baiyin", Armor.Baiyin::new),
        RENWANG = ITEMS.register("renwang", Armor.Renwang::new),
        RATTAN_ARMOR = ITEMS.register("rattan_armor", Armor.Rattan::new),
        CHITU = ITEMS.register("chitu", Mount.Attack::new),
        DILU = ITEMS.register("dilu", Mount.Defend::new);

        public static final Supplier<Item>
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
        public static final Supplier<CardItem> EMPTY_CARD = ITEMS.register("empty_card", CardItem.Empty::new);
        public static final Supplier<SkillItem> EMPTY_SKILL = ITEMS.register("empty_skill", SkillItem::new);

    }

    public static class Skills {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");
        //魏
        public static final Supplier<SkillItem>
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
        PAOXIAO = ITEMS.register("paoxiao", Shu.Paoxiao::new),
        RENDE = ITEMS.register("rende", Shu.Rende::new),
        TIEJI = ITEMS.register("tieji", Shu.Tieji::new),
        WUSHENG = ITEMS.register("wusheng", Shu.Wusheng::new),
        //吴
        BUQU = ITEMS.register("buqu", Wu.Buqu::new),
        FANJIAN = ITEMS.register("fanjian", Wu.Fanjian::new),
        FENYIN = ITEMS.register("fenyin", Wu.Fenyin::new),
        GONGXIN = ITEMS.register("gongxin", Wu.Gongxin::new),
        GUOSE = ITEMS.register("guose", Wu.Guose::new),
        LIANYING = ITEMS.register("lianying", Wu.Lianying::new),
        LIULI = ITEMS.register("liuli", Wu.Liuli::new),
        KUROU = ITEMS.register("kurou", Wu.Kurou::new),
        POJUN = ITEMS.register("pojun", Wu.Pojun::new),
        QIXI = ITEMS.register("qixi", Wu.Qixi::new),
        SHIXIN = ITEMS.register("shixin", Wu.Shixin::new),
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

        public static DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "dabaosword");
        public static final ResourceKey<CreativeModeTab> ZZRS = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.parse("dabaosword:zzrs"));
        static {
            TABS.register("zzrs", () -> CreativeModeTab.builder().icon(Items.SUNSHINE_SMILE.get()::getDefaultInstance)
                    .title(Component.translatable("itemGroup.dabaosword.zzrs"))
                    .displayItems(Other::addToGroup).build());
        }

        private static void addToGroup(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output entries) {
            List<Item> items = new ArrayList<>(AllRegs.Items.ITEMS.getEntries().stream().map(Holder::value).toList());
            for (int i = 0; i < 6; i++) {items.removeLast();} //移除末尾6个注册项
            AllRegs.Skills.ITEMS.getEntries().stream().map(Holder::value).toList().forEach(item -> items.add(items.size() - 5, item));
            items.remove(Items.SUNSHINE_SMILE.get());
            for (var item : items) entries.accept(item);
        }

        //物品组件注册
        public static final DeferredRegister.DataComponents DATA_COMPONENT = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "dabaosword");
        public static final Supplier<DataComponentType<Integer>> TAGS = DATA_COMPONENT.registerComponentType("tags", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
        public static final Supplier<DataComponentType<Integer>> CD = DATA_COMPONENT.registerComponentType("cd", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

        public static final DeferredRegister<MenuType<?>> MENU = DeferredRegister.create(BuiltInRegistries.MENU, "dabaosword");
        public static final Supplier<MenuType<PlayerInvScreenHandler>> PLAYER_INV_SCREEN_HANDLER = MENU.register("player_inv", () -> IMenuTypeExtension.create(PlayerInvScreenHandler::new));
        public static final Supplier<MenuType<FullInvScreenHandler>> FULL_INV_SCREEN_HANDLER = MENU.register("full_inv", () -> IMenuTypeExtension.create(FullInvScreenHandler::new));
        public static final Supplier<MenuType<PileScreenHandler>> PILE_SCREEN_HANDLER = MENU.register("card_pile", () -> IMenuTypeExtension.create(PileScreenHandler::new));
    }
}
