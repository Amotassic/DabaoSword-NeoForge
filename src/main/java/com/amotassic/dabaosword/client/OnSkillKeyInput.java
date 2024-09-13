package com.amotassic.dabaosword.client;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.item.skillcard.SkillItem;
import com.amotassic.dabaosword.network.ActiveSkillPayload;
import com.amotassic.dabaosword.network.ShensuPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;

import static com.amotassic.dabaosword.util.ModTools.hasTrinket;

@EventBusSubscriber(value = Dist.CLIENT, modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class OnSkillKeyInput {

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        var user = Minecraft.getInstance().player;
        var result = Minecraft.getInstance().hitResult;
        if (DabaoSwordClient.ACTIVE_SKILL.consumeClick() && user != null) {
            if (hasActiveSkillWithTarget(user)) {
                if (result != null && result.getType() == HitResult.Type.ENTITY) {
                    var hitResult = (EntityHitResult) result; var entity = hitResult.getEntity();
                    if (entity instanceof Player player) {
                        PacketDistributor.sendToServer(new ActiveSkillPayload(player.getId()));
                        return;
                    }
                }
            }
            if (hasActiveSkill(user)) PacketDistributor.sendToServer(new ActiveSkillPayload(user.getId()));
        }
    }

    @SubscribeEvent
    public static void endClientTick(ClientTickEvent.Post event) {
        var player = Minecraft.getInstance().player;
        if (player != null && hasTrinket(SkillCards.SHENSU.get(), player)) {
            Vec3 lastPos = new Vec3(player.xOld, player.yOld, player.zOld);
            float speed = (float) (player.position().distanceTo(lastPos) * 20);
            if (speed > 0) PacketDistributor.sendToServer(new ShensuPayload(speed));
        }
    }

    public static boolean hasActiveSkill(Player player) {
        var optional = CuriosApi.getCuriosInventory(player);
        if (optional.isPresent()) {
            var handler = optional.get().findFirstCurio(stack -> stack.getItem() instanceof SkillItem.ActiveSkill);
            if (handler.isPresent()) {
                return handler.get().stack() != null;
            }
        }
        return false;
    }

    public static boolean hasActiveSkillWithTarget(Player player) {
        var optional = CuriosApi.getCuriosInventory(player);
        if (optional.isPresent()) {
            var handler = optional.get().findFirstCurio(stack -> stack.getItem() instanceof SkillItem.ActiveSkillWithTarget);
            if (handler.isPresent()) {
                return handler.get().stack() != null;
            }
        }
        return false;
    }
}
