package com.amotassic.dabaosword.client;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.network.SimplePayload;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

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
            if (speed > 0) SimplePayload.sendToServer(SimplePayload.SHENSU, Float.toString(speed));
        }

        if (DabaoSwordClient.SELECT_CARD.consumeClick()) {
            if (user.isShiftKeyDown() && ctrl.consumeClick()) SimplePayload.sendToServer(SimplePayload.CANCEL_DODGE);
            else if (ctrl.consumeClick()) SimplePayload.sendToServer(SimplePayload.CARD_PILE);
            else SimplePayload.sendToServer(SimplePayload.QUICK_SWAP);
        }
    }
}
