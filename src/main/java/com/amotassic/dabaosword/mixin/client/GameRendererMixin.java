package com.amotassic.dabaosword.mixin.client;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final Minecraft minecraft;

    @Shadow public abstract void loadEffect(ResourceLocation resourceLocation);

    @Shadow @Nullable PostChain postEffect;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        Player player = minecraft.player;
        if (player != null) {
            //如果玩家有翻面效果，玩家的视野会上下翻转
            if (!player.isSpectator() && player.hasEffect(ModItems.TURNOVER)) this.loadEffect(ResourceLocation.withDefaultNamespace("shaders/post/flip.json"));
            else if (postEffect != null) {postEffect.close();}
        }
    }
}
