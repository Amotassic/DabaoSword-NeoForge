package com.amotassic.dabaosword.client;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.network.QuickSwapPayload;
import com.amotassic.dabaosword.network.ShensuPayload;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(value = Dist.CLIENT, modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ClientTickEnd {

    @SubscribeEvent
    public static void endClientTick(ClientTickEvent.Post event) {
        var client = Minecraft.getInstance();
        var user = client.player;
        var ctrl = client.options.keySprint;
        // 当打开screen后，关闭选择技能渲染
        if (ChangeSkillRender.isRendering && client.screen != null) {
            ChangeSkillRender.close();
        }

        if (user == null) return;
        if (ModTools.hasTrinket(SkillCards.SHENSU, user)) {
            Vec3 lastPos = new Vec3(user.xOld, user.yOld, user.zOld);
            float speed = (float) (user.position().distanceTo(lastPos) * 20);
            if (speed > 0) PacketDistributor.sendToServer(new ShensuPayload(speed));
        }

        if (DabaoSwordClient.SELECT_CARD.consumeClick()) {
            int i = 0;
            if (user.isShiftKeyDown() && ctrl.consumeClick()) i = 3;
            else if (ctrl.consumeClick()) i = 2;
            PacketDistributor.sendToServer(new QuickSwapPayload(i));
        }
    }
}
