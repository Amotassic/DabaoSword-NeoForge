package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Inject(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), cancellable = true)
    public void onStruckByLightning(ServerLevel world, LightningBolt lightning, CallbackInfo ci) {
        this.hurt(ModTools.getDamageSource(lightning, DamageTypes.LIGHTNING_BOLT), lightning.getDamage());
        ci.cancel();
    }
}
