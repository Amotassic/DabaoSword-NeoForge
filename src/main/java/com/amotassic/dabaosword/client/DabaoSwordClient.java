package com.amotassic.dabaosword.client;

import com.amotassic.dabaosword.DabaoSword;
import com.amotassic.dabaosword.api.Card;
import com.amotassic.dabaosword.entity.ModEntity;
import com.amotassic.dabaosword.entity.client.ModModelLayers;
import com.amotassic.dabaosword.entity.client.XuyouModel;
import com.amotassic.dabaosword.entity.client.XuyouRenderer;
import com.amotassic.dabaosword.ui.FullInvHandledScreen;
import com.amotassic.dabaosword.ui.PileHandledScreen;
import com.amotassic.dabaosword.ui.PlayerInvHandledScreen;
import com.amotassic.dabaosword.ui.SimpleMenuScreen;
import com.amotassic.dabaosword.util.AllRegs;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.lwjgl.glfw.GLFW;

import static net.minecraft.client.renderer.item.ItemProperties.register;

@EventBusSubscriber(modid = DabaoSword.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class DabaoSwordClient {
    public static final KeyMapping ACTIVE_SKILL = new KeyMapping("key.dabaosword.active_skill", GLFW.GLFW_KEY_J, "category.dabaosword.keybindings");

    public static final KeyMapping SELECT_CARD = new KeyMapping("key.dabaosword.select_card", GLFW.GLFW_KEY_K, "category.dabaosword.keybindings");

    @SubscribeEvent
    public static void registerKeyBinds(RegisterKeyMappingsEvent event) {
        event.register(ACTIVE_SKILL);
        event.register(SELECT_CARD);
        registerPredicates();
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(AllRegs.Other.SIMPLE_MENU_HANDLER.get(), SimpleMenuScreen::new);
        event.register(AllRegs.Other.PLAYER_INV_SCREEN_HANDLER.get(), PlayerInvHandledScreen::new);
        event.register(AllRegs.Other.FULL_INV_SCREEN_HANDLER.get(), FullInvHandledScreen::new);
        event.register(AllRegs.Other.PILE_SCREEN_HANDLER.get(), PileHandledScreen::new);
    }

    @SubscribeEvent
    public static void registerModel(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.XUYOU, XuyouModel::getTexturedModelData);
    }

    @SubscribeEvent
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntity.XUYOU.get(), XuyouRenderer::new);
    }

    private static void registerPredicates() {
        //用于添加卡牌的花色和点数
        var itemList = BuiltInRegistries.ITEM.stream().filter(item -> item instanceof Card).toList();
        for (var item : itemList) {registerCustomModelPredicate(item);}
    }

    private static void registerCustomModelPredicate(Item item) {
        register(item, ResourceLocation.parse(item.toString() + "_sr"), (stack, clientWorld, livingEntity, seed) -> {
            var sr = ModTools.getSuitAndRank(stack);
            if (sr == null) return 0.0F;
            int suit = sr.getA().ordinal();
            int rank = sr.getB().ordinal() + 1;
            return (float) (0.13 * suit + 0.01 * rank);
        });
    }
}
