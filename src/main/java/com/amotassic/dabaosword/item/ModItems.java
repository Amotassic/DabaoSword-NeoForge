package com.amotassic.dabaosword.item;

import com.amotassic.dabaosword.effect.*;
import com.amotassic.dabaosword.entity.ModEntity;
import com.amotassic.dabaosword.item.card.*;
import com.amotassic.dabaosword.item.card.equipment.Armor;
import com.amotassic.dabaosword.item.card.equipment.Mount;
import com.amotassic.dabaosword.item.card.equipment.Weapon;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.item.tool.*;
import com.amotassic.dabaosword.ui.FullInvScreenHandler;
import com.amotassic.dabaosword.ui.PileScreenHandler;
import com.amotassic.dabaosword.ui.PlayerInvScreenHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

@SuppressWarnings({"unused", "deprecation"})
public class ModItems {
    public static final List<CardItem> CARDS = new ArrayList<>();
    //杀
    public static final CardItem
    SHA = registerCard("sha", new Sha()),
    FIRE_SHA = registerCard("fire_sha", new Sha.Fire()),
    THUNDER_SHA = registerCard("thunder_sha", new Sha.Thunder()),
    //闪
    SHAN = registerCard("shan", new ShanItem()),
    //桃
    PEACH = registerCard("peach", new PeachItem()),
    //酒
    JIU = registerCard("jiu", new JiuItem()),

    //兵粮寸断
    BINGLIANG_ITEM = registerCard("bingliang",new BingliangItem()),
    //乐不思蜀
    TOO_HAPPY_ITEM = registerCard("too_happy", new TooHappyItem()),
    //闪电
    SHANDIAN_ITEM = registerCard("shandian", new ShandianItem()),
    //过河拆桥
    DISCARD = registerCard("discard", new DiscardItem()),
    //火攻
    FIRE_ATTACK = registerCard("huogong", new FireAttackItem()),
    //借刀杀人
    JIEDAO = registerCard("jiedao", new JiedaoItem()),
    //决斗
    JUEDOU = registerCard("juedou",new JuedouItem()),
    //南蛮入侵
    NANMAN = registerCard("nanman", new NanmanItem()),
    //顺手牵羊
    STEAL = registerCard("steal", new StealItem()),
    //桃园结义
    TAOYUAN = registerCard("taoyuan", new TaoyuanItem()),
    //铁锁连环
    TIESUO = registerCard("tiesuo",new TiesuoItem()),
    //万箭齐发
    WANJIAN = registerCard("wanjian", new WanjianItem()),
    //五谷丰登
    WUGU = registerCard("wugu", new WuguItem()),
    //无懈可击
    WUXIE = registerCard("wuxie", new CardItem.Armoury()),
    //无中生有
    WUZHONG = registerCard("wuzhong", new WuzhongItem()),

    //雌雄双股剑
    CIXIONG = registerCard("cixiong", new Weapon.Cixiong()),
    //方天画戟
    FANGTIAN = registerCard("fangtian", new Weapon.Fangtian()),
    //贯石斧
    GUANSHI = registerCard("guanshi", new Weapon.Guanshi()),
    // 古锭刀
    GUDING_WEAPON = registerCard("guding_dao", new Weapon.Guding()),
    //寒冰剑
    HANBING = registerCard("hanbing", new Weapon.Hanbing()),
    //麒麟弓
    QILIN = registerCard("qilin", new Weapon.Qilin()),
    //青釭剑
    QINGGANG = registerCard("qinggang", new Weapon.Qinggang()),
    //青龙偃月刀
    QINGLONG = registerCard("qinglong", new Weapon.Qinglong()),
    //丈八蛇矛
    ZHANGBA = registerCard("zhangba", new Weapon.Zhangba()),
    //诸葛连弩
    LIANNU = registerCard("liannu", new Weapon.Liannu()),
    //朱雀羽扇
    ZHUQUE = registerCard("zhuque", new Weapon.Zhuque()),
    //八卦阵
    BAGUA = registerCard("bagua", new Armor.Bagua()),
    //白银狮子
    BAIYIN = registerCard("baiyin", new Armor.Baiyin()),
    //仁王盾
    RENWANG = registerCard("renwang", new Armor.Renwang()),
    //寿衣
    RATTAN_ARMOR = registerCard("rattan_armor", new Armor.Rattan()),
    //-1马
    CHITU = registerCard("chitu", new Mount.Attack()),
    //+1马
    DILU = registerCard("dilu", new Mount.Defend());

    public static final Item
    //摸牌
    GAIN_CARD = register("gain_card", new GainCardItem()),
    //牌堆
    CARD_PILE = register("card_pile", new CardPile()),
    //礼盒
    GIFTBOX = register("gift_box", new GiftBoxItem()),
    GUDINGDAO = register("gudingdao", new GudingdaoItem()),
    ARROW_RAIN = register("arrow_rain", new ArrowRainItem()),
    //BB机
    BBJI = register("bbji", new BBjiItem()),
    //让我康康
    LET_ME_CC = register("let_me_cc", new LetMeCCItem()),
    //阳光开朗的笑容
    SUNSHINE_SMILE = register("sunshine_smile", new SunshineSmile()),
    XUYOU_SPAWN_EGG = register("xuyou_spawn_egg", new SpawnEggItem(ModEntity.XUYOU, 0x52BDF7, 0x8D8B96, new Item.Properties())),
    GUDING_ITEM = register("guding", new Item(new Item.Properties())),
    INCOMPLETE_GUDINGDAO = register("incomplete_gdd", new Item(new Item.Properties().stacksTo(1)));
    public static final CardItem EMPTY_CARD = register("empty_card", new CardItem.Empty());
    public static final SkillItem EMPTY_SKILL = register("empty_skill", new SkillItem());

    private static <T extends Item> T register(String name, T item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath("dabaosword", name), item);
    }
    /**将注册的卡牌添加到卡牌列表中，便于自动将物品添加到物品组*/
    public static CardItem registerCard(String name, CardItem item) {
        CardItem card = register(name, item);
        CARDS.add(card);
        return card;
    }

    public static final ResourceKey<CreativeModeTab> ZZRS = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath("dabaosword", "zzrs"));

    private static void addToGroup(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
        var wrapper = context.holders().lookup(Registries.ENCHANTMENT).orElseThrow();
        var entry = wrapper.get(CRIT).orElse(null);
        ItemStack smile = new ItemStack(SUNSHINE_SMILE);
        if (entry != null) smile.enchant(entry, 1);
        //添加所有卡牌
        CARDS.forEach(entries::accept);
        entries.accept(GAIN_CARD);
        entries.accept(CARD_PILE);
        //添加所有技能
        SkillCards.SKILLS.forEach(entries::accept);

        entries.accept(GIFTBOX);
        entries.accept(BBJI);
        entries.accept(LET_ME_CC);
        entries.accept(smile);
        entries.accept(XUYOU_SPAWN_EGG);
    }

    //注册部分
    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ZZRS,
                CreativeModeTab.builder().icon(() -> new ItemStack(SUNSHINE_SMILE))
                        .title(Component.translatable("itemGroup.dabaosword.zzrs"))
                        .displayItems(ModItems::addToGroup).build());
    }

    private static Holder<MobEffect> register(String id, MobEffect statusEffect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath("dabaosword", id), statusEffect);
    }//状态效果注册
    //兵粮寸断效果
    public static final Holder<MobEffect> BINGLIANG = register("bingliang", new CommonEffect(MobEffectCategory.HARMFUL, 0x46F732).addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.parse("bing"),-4, AttributeModifier.Operation.ADD_VALUE)),
    TOO_HAPPY = register("too_happy", new TooHappyEffect().addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.parse("le"),-10, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)),
    //触及距离增加
    REACH = register("reach", new CommonEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)
            .addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, ResourceLocation.parse("-1ma"),1.0, AttributeModifier.Operation.ADD_VALUE)
            .addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("-1ma"),1.0, AttributeModifier.Operation.ADD_VALUE)),
    //近战防御范围增加
    DEFEND = register("defend", new CommonEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF)),
    DEFENDED = register("defended", new CommonEffect(MobEffectCategory.HARMFUL, 0xFFFFFF).addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, ResourceLocation.parse("defend-"),-1.0, AttributeModifier.Operation.ADD_VALUE)),
    //冷却状态效果
    COOLDOWN = register("cooldown", new  CooldownEffect()),
    COOLDOWN2 = register("cooldown2", new Cooldown2Effect()),
    //无敌效果
    INVULNERABLE = register("invulnerable", new CommonEffect(MobEffectCategory.BENEFICIAL,0x35F5DF)),
    //翻面效果
    TURNOVER = register("turn_over", new TurnOverEffect()),
    //铁骑效果
    TIEJI = register("tieji", new CommonEffect(MobEffectCategory.HARMFUL, 0x07050F)),
    SHANDIAN = register("shandian", new ShandianEffect());

    //物品组件注册
    public static final DataComponentType<Integer> TAGS = regComp("tags", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DataComponentType<Integer> CD = regComp("cd", builder -> builder.persistent(ExtraCodecs.NON_NEGATIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

    private static <T> DataComponentType<T> regComp(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.parse("dabaosword:" + id), (builderOperator.apply(DataComponentType.builder())).build());
    }

    public static final MenuType<PlayerInvScreenHandler> PLAYER_INV_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, "player_inv", IMenuTypeExtension.create(PlayerInvScreenHandler::new));

    public static final MenuType<FullInvScreenHandler> FULL_INV_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, "full_inv", IMenuTypeExtension.create(FullInvScreenHandler::new));

    public static final MenuType<PileScreenHandler> PILE_SCREEN_HANDLER = Registry.register(BuiltInRegistries.MENU, "card_pile", IMenuTypeExtension.create(PileScreenHandler::new));

    public static final ResourceKey<Enchantment> CRIT = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.parse("dabaosword:crit"));
}
