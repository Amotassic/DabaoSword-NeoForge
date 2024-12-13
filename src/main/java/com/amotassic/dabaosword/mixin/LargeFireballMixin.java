package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.util.Gamerule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LargeFireball.class)
public abstract class LargeFireballMixin extends Fireball {
    public LargeFireballMixin(EntityType<? extends Fireball> p_37006_, Level p_37007_) {super(p_37006_, p_37007_);}

    @ModifyArgs(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"))
    public void onCollision(Args args) {
        boolean bl = !this.level().getGameRules().getBoolean(Gamerule.FIRE_ATTACK_BREAKS_BLOCK);
        if (bl && getTags().contains("a")) {
            args.set(5, false);
            args.set(6, Level.ExplosionInteraction.NONE);
        }
    }
}
