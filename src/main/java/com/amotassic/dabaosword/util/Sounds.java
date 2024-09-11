package com.amotassic.dabaosword.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class Sounds {
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
