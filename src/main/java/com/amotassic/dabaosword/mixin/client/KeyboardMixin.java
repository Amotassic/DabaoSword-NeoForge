package com.amotassic.dabaosword.mixin.client;

import com.amotassic.dabaosword.client.KeyInputHandler;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {

    @Inject(method = "keyPress", at = @At("HEAD"))
    public void keyPress(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        KeyInputHandler.onKeyInput(key, scanCode, action, modifiers);
    }
}

@Mixin(MouseHandler.class)
class MouseMixin {

    @Shadow private double xpos;
    @Shadow private double ypos;

    @Inject(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MouseHandler;grabMouse()V"))
    private void onPress(long windowPointer, int button, int action, int modifiers, CallbackInfo ci) {
        com.amotassic.dabaosword.client.ChangeSkillRender.handleSkillSelect(xpos, ypos);
    }
}
