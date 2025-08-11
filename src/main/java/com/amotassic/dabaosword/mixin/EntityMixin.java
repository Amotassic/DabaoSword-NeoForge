package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.damage_type.ModDT;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.MODConfig;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.HashSet;
import java.util.UUID;

import static com.amotassic.dabaosword.util.ModTools.getClosestEntity;
import static com.amotassic.dabaosword.util.ModTools.isCard;

@Mixin(Entity.class)
abstract class EntityMixin {

    @Shadow public abstract boolean hurt(DamageSource source, float amount);

    @Inject(method = "thunderHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), cancellable = true)
    public void onStruckByLightning(ServerLevel world, LightningBolt lightning, CallbackInfo ci) {
        if (lightning.getTags().contains("a")) {
            this.hurt(ModDT.shandian(lightning), lightning.getDamage());
            ci.cancel();
        }
    }

    @ModifyReturnValue(method = "dampensVibrations", at = @At("RETURN"))
    public boolean occludeVibrationSignals(boolean original) {
        if ((Entity) (Object) this instanceof Player player && player.getTags().contains("wuyan")) {
            return true;
        }
        return original;
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

    @Shadow private UUID target;
    @Unique ItemEntity thisItem = (ItemEntity) (Object) this;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (isCard(this.getItem())) this.setNoPickUpDelay();
        if (level() instanceof ServerLevel world) {
            Entity follow = world.getEntity(target);
            if (world.getGameTime() % 20 == 0 && getTags().contains("follow_owner") && follow != null) {
                teleportTo((ServerLevel) follow.level(), follow.getX(), follow.getY(), follow.getZ(), new HashSet<>(), getYRot(), getXRot());
            }
        }

        ItemStack stack = this.getItem();
        if (stack.is(Items.ARROW) && stack.getCount() == 64) {
            var entity = getClosestEntity(thisItem, Entity.class, 0.2, e -> true);
            if (entity instanceof ItemEntity item && item.getItem().is(Items.BOW)) {
                item.setItem(new ItemStack(ModItems.ARROW_RAIN));
                this.discard();
            }
        }

        if (stack.is(Items.EMERALD) && stack.getCount() == 64) {
            var entity = getClosestEntity(thisItem, Entity.class, 0.2, e -> true);
            if (entity instanceof Villager villager && villager.getVillagerData().getProfession() == VillagerProfession.NITWIT) {
                this.setItem(new ItemStack(ModItems.GIFTBOX, 1));
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

    @ModifyArgs(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    protected void onEntityHit(Args args) {
        if (getTags().contains("a") && getOwner() != null) {
            getOwner().addTag("sha"); //防止触发杀
            args.set(0, ModDT.huogong(getOwner()));
            args.set(1, 6f);
        }
    }
}

@Mixin(Explosion.class)
abstract class ExplosionMixin {

    @Shadow @Final @Nullable private Entity source;

    @ModifyArg(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    public DamageSource collectBlocksAndDamageEntities(DamageSource damageSource) {
        if (source instanceof Fireball fireball && source.getTags().contains("a") && fireball.getOwner() != null) {
            fireball.getOwner().addTag("sha"); //防止触发杀
            return ModDT.huogong(fireball.getOwner());
        }
        return damageSource;
    }
}

@Mixin(ThrownTrident.class)
abstract class TridentMixin extends AbstractArrow {
    protected TridentMixin(EntityType<? extends AbstractArrow> entityType, Level level) {super(entityType, level);}

    @Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void onHit(EntityHitResult result, CallbackInfo ci) {
        if (getTags().contains("a")) this.discard();
    }
}
