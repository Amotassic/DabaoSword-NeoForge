package com.amotassic.dabaosword.client;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.network.ActiveSkillPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.amotassic.dabaosword.util.ModTools.hasTrinkets;

@EventBusSubscriber(value = Dist.CLIENT, modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ActiveSkillEvent {

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        Item[] active_skills_with_target = {SkillCards.RENDE.get(), SkillCards.YIJI.get(), SkillCards.GONGXIN.get(), SkillCards.ZHIJIAN.get()};
        Item[] active_skills = {SkillCards.ZHIHENG.get(), SkillCards.QICE.get(), SkillCards.TAOLUAN.get(), SkillCards.LUOSHEN.get(), SkillCards.KUROU.get()};
        if (DabaoSwordClient.ACTIVE_SKILL.consumeClick()) {
            var user = Minecraft.getInstance().player;
            var result = Minecraft.getInstance().hitResult;
            if (hasTrinkets(active_skills_with_target, user)) {
                if (result != null && result.getType() == HitResult.Type.ENTITY) {
                    var hitResult = (EntityHitResult) result; var entity = hitResult.getEntity();
                    if (entity instanceof Player player) {
                        PacketDistributor.sendToServer(new ActiveSkillPayload(player.getId()));
                        return;
                    }
                }
            }
            if (hasTrinkets(active_skills, user) && user != null) PacketDistributor.sendToServer(new ActiveSkillPayload(user.getId()));
        }
    }
}
