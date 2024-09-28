package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.item.skillcard.SkillItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllRegs {

    public static class Skills {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("dabaosword");
        //魏
        public static final DeferredHolder<Item, Item> DUANLIANG = ITEMS.register("duanliang", ()-> new SkillItem.Duanliang(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> FANGZHU = ITEMS.register("fangzhu", ()-> new SkillItem.Fangzhu(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> XINGSHANG = ITEMS.register("xingshang", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GANGLIE = ITEMS.register("ganglie", ()-> new SkillItem.Ganglie(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GONGAO = ITEMS.register("gongao", ()-> new SkillItem.Gongao(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> JUEQING = ITEMS.register("jueqing", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
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
        public static final DeferredHolder<Item, Item> BUQU = ITEMS.register("buqu", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GONGXIN = ITEMS.register("gongxin", ()-> new SkillItem.Gongxin(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> GUOSE = ITEMS.register("guose", ()-> new SkillItem.Guose(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> LIANYING = ITEMS.register("lianying", ()-> new SkillItem.Lianying(new Item.Properties().stacksTo(1)));
        public static final DeferredHolder<Item, Item> LIULI = ITEMS.register("liuli", ()-> new SkillItem(new Item.Properties().stacksTo(1)));
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
