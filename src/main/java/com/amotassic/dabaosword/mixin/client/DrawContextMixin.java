package com.amotassic.dabaosword.mixin.client;

import com.amotassic.dabaosword.util.ModTools;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class DrawContextMixin {
    @Shadow @Final private PoseStack pose;

    @Shadow public abstract int drawString(Font font, Component text, int x, int y, int color, boolean dropShadow);

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isBarVisible()Z"))
    public void drawItemInSlot(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        var sr = ModTools.getSuitAndRank(stack);
        if (sr != null) {
            Component component = Component.translatable("card.suit_and_rank", sr.getA().suit, sr.getB().rank);
            int color = ModTools.isRedCard.test(stack) ? 0xFF5555 : 0xFFFFFF;
            this.pose.translate(0.0f, 0.0f, 200.0f);
            drawString(font, component, x, y, color, false);
        }
    }
}
