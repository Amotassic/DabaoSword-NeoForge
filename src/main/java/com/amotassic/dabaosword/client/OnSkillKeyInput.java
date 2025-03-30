package com.amotassic.dabaosword.client;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.command.DabaoSwordCommand;
import com.amotassic.dabaosword.item.skillcard.SkillCards;
import com.amotassic.dabaosword.network.ActiveSkillPayload;
import com.amotassic.dabaosword.network.QuickSwapPayload;
import com.amotassic.dabaosword.network.ShensuPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

import static com.amotassic.dabaosword.util.ModTools.*;

@EventBusSubscriber(value = Dist.CLIENT, modid = DabaoSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class OnSkillKeyInput {

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        var client = Minecraft.getInstance();
        var user = client.player;
        int key = event.getKey(); int action = event.getAction(); int mod = event.getModifiers();
        onKeyInput(user, key, action, mod);

        var ctrl = client.options.keySprint;
        if (user != null) {
            if (DabaoSwordClient.SELECT_CARD.consumeClick()) {
                int i = 0;
                if (user.isShiftKeyDown() && ctrl.consumeClick()) i = 3;
                else if (ctrl.consumeClick()) i = 2;
                PacketDistributor.sendToServer(new QuickSwapPayload(i));
                return;
            }

            var result = client.hitResult; LivingEntity target;
            if (result instanceof EntityHitResult eResult && eResult.getEntity() instanceof LivingEntity entity) {
                target = entity;
            } else target = user;

            if (DabaoSwordClient.ACTIVE_SKILL.consumeClick() && isEquipped(user, s -> s(s).isActiveSkill())) {
                PacketDistributor.sendToServer(new ActiveSkillPayload(target.getId()));
            }
        }
    }

    @SubscribeEvent
    public static void endClientTick(ClientTickEvent.Post event) {
        var player = Minecraft.getInstance().player;
        if (player != null && hasTrinket(SkillCards.SHENSU, player)) {
            Vec3 lastPos = new Vec3(player.xOld, player.yOld, player.zOld);
            float speed = (float) (player.position().distanceTo(lastPos) * 20);
            if (speed > 0) PacketDistributor.sendToServer(new ShensuPayload(speed));
        }
    }

    private static String keysPressed = "";
    private static void onKeyInput(LocalPlayer player, int key, int action, int modifiers) {
        //System.out.println("key: " + key + " scancode: " + scancode + " action: " + action + "mod: " + modifiers);
        if (action == 1 && modifiers == 2 && player != null) {
            if (key == GLFW.GLFW_KEY_M) player.displayClientMessage(DabaoSwordCommand.menu, false);
            if (key == GLFW.GLFW_KEY_I) PacketDistributor.sendToServer(new QuickSwapPayload(9));
            return;
        }
        if (action != GLFW.GLFW_PRESS) return;

        String keyPressed = getInputKey(key);
        if (keyPressed.isEmpty()) {
            keysPressed = "";
            return;
        }
        keysPressed += keyPressed;
        String MURASAME = "MURASAME";
        if (!MURASAME.startsWith(keysPressed)) keysPressed = "";
        if (MURASAME.equals(keysPressed)) {
            doSomething(player);
            keysPressed = "";
        }
    }

    private static void doSomething(LocalPlayer player) {
        if (player == null) return;
        player.displayClientMessage(Component.nullToEmpty("MURASAME"), false);
    }

    private static String getInputKey(int key) {
        if (key >= 48 && key <= 57) return String.valueOf(key - 48);
        if (key >= 65 && key <= 90) return String.valueOf((char) key);
        return "";
    }
}
