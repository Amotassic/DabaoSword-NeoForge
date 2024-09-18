package com.amotassic.dabaosword.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Arrow.class)
public abstract class ArrowEntityMixin extends AbstractArrow {
    protected ArrowEntityMixin(EntityType<? extends AbstractArrow> entityType, Level level) {super(entityType, level);}

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tick(CallbackInfo ci) {
        if (Objects.equals(this.getCustomName(), Component.nullToEmpty("a")) && this.inGround) this.discard();
    }
}
