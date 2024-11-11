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

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Ljava/util/Stack;push(Ljava/lang/Object;)Ljava/lang/Object;"), cancellable = true)
    public void hurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        int i = ModifyDamage.shouldCancel(dabaoSword$living, source, amount);
        if (i == 1) cir.setReturnValue(false);
        if (i == 2) cir.setReturnValue(true);
    }

    @ModifyVariable(method = "getDamageAfterArmorAbsorb", at = @At(value = "HEAD"), argsOnly = true)
    protected float modifyDamageBeforeArmor(float amount, DamageSource source) {
        return ModifyDamage.modify(dabaoSword$living, source, amount);
    }

    //翻面的生物无法发起攻击
    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (this.hasEffect(ModItems.TURNOVER)) cir.setReturnValue(false);
    }
}
