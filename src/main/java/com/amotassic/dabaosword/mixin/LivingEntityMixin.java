package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModifyDamage;
import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.amotassic.dabaosword.util.ModTools.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected abstract void hurtArmor(DamageSource damageSource, float damageAmount);

    @Shadow public abstract int getArmorValue();

    @Shadow public abstract double getAttributeValue(Holder<Attribute> attribute);

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {super(p_19870_, p_19871_);}

    @Unique LivingEntity dabaoSword$living = (LivingEntity) (Object) this;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        //若方天画戟被触发了，只要左键就可以造成群伤
        Player closestPlayer = level().getNearestPlayer(this, 5);
        if (closestPlayer != null && hasTrinket(ModItems.FANGTIAN.get(), closestPlayer) && !level().isClientSide && isAlive()) {
            ItemStack stack = trinketItem(ModItems.FANGTIAN.get(), closestPlayer);
            int time = getCD(stack);
            if (time > 15 && closestPlayer.swingTime == 1) {
                //给玩家本人一个极短的无敌效果，以防止被误伤
                closestPlayer.addEffect(new MobEffectInstance(ModItems.INVULNERABLE,2,0,false,false,false));
                float i = (float) closestPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE);
                this.hurt(damageSources().playerAttack(closestPlayer), i);
            }
        }

        if (dabaoSword$living instanceof Mob mob && mob.hasEffect(ModItems.TURNOVER)) mob.setTarget(null);
    }

    @Inject(method = "getDamageAfterArmorAbsorb", at = @At(value = "HEAD"), cancellable = true)
    protected void modifyDamageBeforeArmor(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        float value = ModifyDamage.modify(dabaoSword$living, source, amount);

        if (!source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            this.hurtArmor(source, value);
            value = CombatRules.getDamageAfterAbsorb(dabaoSword$living, value, source, this.getArmorValue(), (float)this.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
        }
        cir.setReturnValue(value);
    }
}
