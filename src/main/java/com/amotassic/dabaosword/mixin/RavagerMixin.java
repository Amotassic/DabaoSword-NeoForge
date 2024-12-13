package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.function.Predicate;

import static com.amotassic.dabaosword.api.event.CardEvents.hurtBy;
import static com.amotassic.dabaosword.api.event.CardEvents.notHurtBy;

@Mixin(Ravager.class)
public abstract class RavagerMixin extends Raider {
    @Shadow private int stunnedTick;

    @Shadow private int roarTick;

    @Shadow protected abstract void strongKnockback(Entity entity);

    protected RavagerMixin(EntityType<? extends Raider> entityType, Level level) {super(entityType, level);}

    @Inject(method = "aiStep", at = @At("HEAD"))
    public void tickMovement(CallbackInfo ci) {
        if (getTags().contains("a")) {
            stunnedTick = 20;
            getTags().remove("a");
        }
        if (getTags().contains("b") && roarTick == 1) discard();
    }

    @Inject(method = "roar", at = @At("HEAD"), cancellable = true)
    private void roar(CallbackInfo ci) {
        if (isAlive() && hasCustomName() && getTags().contains("b")) {
            int id = Integer.parseInt(Objects.requireNonNull(getCustomName()).getString());
            LivingEntity user = (LivingEntity) level().getEntity(id);
            if (user == null) return;
            Predicate<LivingEntity> target = e -> e.isAlive() && id != e.getId();
            for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(7.0), target)) {
                user.addTag("nanman");
                DamageSource source = damageSources().mobAttack(user);
                if (notHurtBy(entity, ModItems.NANMAN)) continue;
                entity.addEffect(new MobEffectInstance(ModItems.COOLDOWN2, 2, 0, false, false));
                if (entity.hurt(source, 6)) hurtBy(entity, ModItems.NANMAN);
                strongKnockback(entity);
            }
            ci.cancel();
        }
    }
}
