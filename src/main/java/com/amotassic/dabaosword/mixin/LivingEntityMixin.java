package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModifyDamage;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {super(p_19870_, p_19871_);}

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Unique LivingEntity dabaoSword$living = (LivingEntity) (Object) this;

    @Unique DamageSource dabaoSword$source;

    @Inject(method = "hurt", at = @At(value = "HEAD"))
    private void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        dabaoSword$source = source;
    }

    @ModifyVariable(method = "getDamageAfterArmorAbsorb", at = @At(value = "HEAD"), argsOnly = true)
    protected float modifyDamageBeforeArmor(float amount) {
        return ModifyDamage.modify(dabaoSword$living, dabaoSword$source, amount);
    }

    //翻面的生物无法发起攻击
    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (this.hasEffect(ModItems.TURNOVER)) cir.setReturnValue(false);
    }
}
