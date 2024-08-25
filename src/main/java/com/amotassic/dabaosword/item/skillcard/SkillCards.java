package com.amotassic.dabaosword.item.skillcard;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SkillCards {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");
    //魏
    public static final DeferredHolder<Item, Item> DUANLIANG = ITEMS.register("duanliang", ()-> new DuanliangSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> FANGZHU = ITEMS.register("fangzhu", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> XINGSHANG = ITEMS.register("xingshang", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> GANGLIE = ITEMS.register("ganglie", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> GONGAO = ITEMS.register("gongao", ()-> new GongaoSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> JUEQING = ITEMS.register("jueqing", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> LUOSHEN = ITEMS.register("luoshen", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> QINGGUO = ITEMS.register("qingguo", ()-> new QingguoSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> LUOYI = ITEMS.register("luoyi", ()-> new LuoyiSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> QICE = ITEMS.register("qice", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> QUANJI = ITEMS.register("quanji", ()-> new QuanjiSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> SHANZHUAN = ITEMS.register("shanzhuan", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> YIJI = ITEMS.register("yiji", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    //蜀
    public static final DeferredHolder<Item, Item> BENXI = ITEMS.register("benxi", ()-> new BenxiSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> HUOJI = ITEMS.register("huoji", ()-> new HuojiSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> KANPO = ITEMS.register("kanpo", ()-> new KanpoSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> JIZHI = ITEMS.register("jizhi", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> KUANGGU = ITEMS.register("kuanggu", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> LIEGONG = ITEMS.register("liegong", ()-> new LiegongSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> LONGDAN = ITEMS.register("longdan", ()-> new LongdanSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> RENDE = ITEMS.register("rende", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> TIEJI = ITEMS.register("tieji", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    //吴
    public static final DeferredHolder<Item, Item> BUQU = ITEMS.register("buqu", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> GONGXIN = ITEMS.register("gongxin", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> GUOSE = ITEMS.register("guose", ()-> new GuoseSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> LIULI = ITEMS.register("liuli", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> KUROU = ITEMS.register("kurou", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> POJUN = ITEMS.register("pojun", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> QIXI = ITEMS.register("qixi", ()-> new QixiSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> ZHIHENG = ITEMS.register("zhiheng", ()-> new ZhihengSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> ZHIJIAN = ITEMS.register("zhijian", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    //群
    public static final DeferredHolder<Item, Item> LEIJI = ITEMS.register("leiji", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> LUANJI = ITEMS.register("luanji", ()-> new LuanjiSkill(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> TAOLUAN = ITEMS.register("taoluan", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> MASHU = ITEMS.register("mashu", ()-> new SkillItem(new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> FEIYING = ITEMS.register("feiying", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
}
