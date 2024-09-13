package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModifyDamage;
import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {super(p_19870_, p_19871_);}

    @Shadow protected abstract void hurtArmor(DamageSource damageSource, float damageAmount);

    @Shadow public abstract int getArmorValue();

    @Shadow public abstract double getAttributeValue(Holder<Attribute> attribute);

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Unique LivingEntity dabaoSword$living = (LivingEntity) (Object) this;

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At(value = "HEAD"), cancellable = true)
    protected void modifyDamageBeforeArmor(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        float value = ModifyDamage.modify(dabaoSword$living, source, amount);

        if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            this.hurtArmor(source, value);
            value = CombatRules.getDamageAfterAbsorb(dabaoSword$living, value, source, this.getArmorValue(), (float)this.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        }
        cir.setReturnValue(value);
    }

    //翻面的生物无法发起攻击
    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void canTarget(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (this.hasEffect(ModItems.TURNOVER)) cir.setReturnValue(false);
    }
}
