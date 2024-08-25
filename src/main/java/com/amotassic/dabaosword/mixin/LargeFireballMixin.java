package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.util.Gamerule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LargeFireball.class)
public abstract class LargeFireballMixin extends Fireball {
    @Shadow private int explosionPower;
    public LargeFireballMixin(EntityType<? extends Fireball> p_37006_, Level p_37007_) {super(p_37006_, p_37007_);}

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    public void onCollision(HitResult hitResult, CallbackInfo ci) {
        boolean bl = !this.level().getGameRules().getBoolean(Gamerule.FIRE_ATTACK_BREAKS_BLOCK);
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            if (bl && this.explosionPower == 3) {
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3, false, Level.ExplosionInteraction.NONE);
                this.discard();
                ci.cancel();
            }
        }
    }
}
