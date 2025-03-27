package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.MODConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.amotassic.dabaosword.util.ModTools.*;

@Mixin(Entity.class)
abstract class EntityMixin {

    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Inject(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), cancellable = true)
    public void onStruckByLightning(ServerLevel world, LightningBolt lightning, CallbackInfo ci) {
        this.hurt(getDamageSource(lightning, DamageTypes.LIGHTNING_BOLT), lightning.getDamage());
        ci.cancel();
    }
}

@Mixin(AbstractArrow.class)
abstract class ArrowEntityMixin extends Projectile {
    protected ArrowEntityMixin(EntityType<? extends Projectile> entityType, Level level) {super(entityType, level);}

    @Shadow protected boolean inGround;

    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void tick(CallbackInfo ci) {
        var tags = getTags();
        if (this.inGround) {
            if (tags.contains("a") || tags.contains("cosmetic")) this.discard();
        }
    }

    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setRemainingFireTicks(I)V"))
    private void onHit(EntityHitResult result, CallbackInfo ci) {
        if (getTags().contains("a")) this.discard();
    }

    @Inject(method = "onHitEntity", at = @At(value = "HEAD"), cancellable = true)
    private void cosmetic(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (getTags().contains("cosmetic")) {
            this.discard(); ci.cancel();
        }
    }
}

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> entityType, Level level) {super(entityType, level);}

    @Shadow public abstract ItemStack getItem();

    @Shadow public abstract void setNoPickUpDelay();

    @Shadow public abstract void setItem(ItemStack stack);

    @Unique ItemEntity thisItem = (ItemEntity) (Object) this;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (isCard(this.getItem())) this.setNoPickUpDelay();

        ItemStack stack = this.getItem();
        var entity = getClosestEntity(thisItem, Entity.class, 0.2, e -> true);
        if (stack.is(Items.ARROW) && stack.getCount() == 64) {
            if (entity instanceof ItemEntity item && item.getItem().is(Items.BOW)) {
                item.setItem(new ItemStack(ModItems.ARROW_RAIN));
                this.discard();
            }
        }

        if (stack.is(Items.EMERALD) && stack.getCount() == 64) {
            if (entity instanceof Villager villager && villager.getVillagerData().getProfession() == VillagerProfession.NITWIT) {
                this.setItem(new ItemStack(ModItems.GIFT_BOX, 1));
            }
        }
    }
}

@Mixin(LargeFireball.class)
abstract class LargeFireballMixin extends Fireball {
    public LargeFireballMixin(EntityType<? extends Fireball> p_37006_, Level p_37007_) {super(p_37006_, p_37007_);}

    @ModifyArgs(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;)Lnet/minecraft/world/level/Explosion;"))
    public void onCollision(Args args) {
        if (!MODConfig.FireAttackBreaksBlock && getTags().contains("a")) {
            args.set(5, false);
            args.set(6, Level.ExplosionInteraction.NONE);
        }
    }
}

@Mixin(ThrownTrident.class)
abstract class TridentMixin extends AbstractArrow {
    protected TridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {super(entityType, level);}

    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void onHit(EntityHitResult result, CallbackInfo ci) {
        if (getTags().contains("a")) this.discard();
    }
}
