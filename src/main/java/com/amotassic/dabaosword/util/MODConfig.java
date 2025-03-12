package com.amotassic.dabaosword.util;

import com.amotassic.dabaosword.DabaoSword;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MODConfig {
    static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static final ModConfigSpec.IntValue Search_Radius = BUILDER.comment("创建游戏时搜索玩家的半径（单位：方块）")
            .defineInRange("SearchRadius", 20, 5, Integer.MAX_VALUE);

    static final ModConfigSpec.IntValue Wait_Time = BUILDER.comment("游戏开始前的等待时间（单位：秒），至少为5秒")
            .defineInRange("WaitTime", 10, 5, Integer.MAX_VALUE);

    static final ModConfigSpec.IntValue Time_Out = BUILDER.comment("若游戏中距上次得分超过该时间，则强制结束并结算（单位：秒）")
            .defineInRange("TimeOut", 300, 30, Integer.MAX_VALUE);

    static final ModConfigSpec.BooleanValue Kill_Streak = BUILDER.comment("是否开启手杀版连续击杀播报语音")
            .define("KillStreak", true);

    static final ModConfigSpec.BooleanValue Fire_Attack_Breaks_Block = BUILDER.comment("是否允许火攻破坏方块")
            .define("FireAttackBreaksBlock", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static int SearchRadius;
    public static int WaitTime;
    public static int TimeOut;
    public static boolean KillStreak;
    public static boolean FireAttackBreaksBlock;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        SearchRadius = Search_Radius.get();
        WaitTime = Wait_Time.get();
        TimeOut = Time_Out.get();
        KillStreak = Kill_Streak.get();
        FireAttackBreaksBlock = Fire_Attack_Breaks_Block.get();
    }
}
