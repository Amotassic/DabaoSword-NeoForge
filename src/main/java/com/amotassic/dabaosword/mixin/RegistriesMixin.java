package com.amotassic.dabaosword.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*@Mixin(Minecraft.class)
class Boot {
    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;currentThread()Ljava/lang/Thread;"), remap = false)
    private void earlyInit(GameConfig gameConfig, CallbackInfo ci) {
        EarlyInit.commonSetup();
    }
}

@Mixin(Main.class)
class ServerBoot {
    @Inject(method = "main", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/server/loading/ServerModLoader;load()V"))
    private static void earlyInit(CallbackInfo ci) {
        EarlyInit.commonSetup();
    }
}*/

@Mixin(value = BuiltInRegistries.class, priority = 1145)
public class RegistriesMixin {
    /*@Redirect(method = "freeze", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;freeze()Lnet/minecraft/core/Registry;"))
    private static Registry<?> init(Registry<?> instance) {
        return instance;
    }*/

    @Inject(method = "freeze", at = @At(value = "HEAD"), cancellable = true)
    private static void init(CallbackInfo ci) {
        //if (!ModList.get().isLoaded("connector"))
            ci.cancel();
    }
}
