package com.amotassic.dabaosword.client;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.ui.FullInvHandledScreen;
import com.amotassic.dabaosword.ui.PlayerInvHandledScreen;
import com.amotassic.dabaosword.ui.SimpleMenuScreen;
import com.amotassic.dabaosword.util.AllRegs;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.lwjgl.glfw.GLFW;


@EventBusSubscriber(modid = DabaoSword.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class DabaoSwordClient {
    public static final KeyMapping ACTIVE_SKILL = new KeyMapping("key.dabaosword.active_skill", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, "category.dabaosword.keybindings");

    @SubscribeEvent
    public static void registerKeyBinds(RegisterKeyMappingsEvent event) {
        event.register(ACTIVE_SKILL);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(AllRegs.Other.SIMPLE_MENU_HANDLER.get(), SimpleMenuScreen::new);
        event.register(AllRegs.Other.PLAYER_INV_SCREEN_HANDLER.get(), PlayerInvHandledScreen::new);
        event.register(AllRegs.Other.FULL_INV_SCREEN_HANDLER.get(), FullInvHandledScreen::new);
    }
}
