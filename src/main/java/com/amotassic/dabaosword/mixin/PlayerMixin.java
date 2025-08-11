package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.damage_type.ModDT;
import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModTools;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {super(p_20966_, p_20967_);}

    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot1);

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo ci) {
        if (this.getTags().contains("px")) this.attackStrengthTicker = 1145;
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE"), ordinal = 2)
    public boolean attack(boolean bl) {
        boolean crit = false;
        var entry = ModTools.getEntry(ModItems.CRIT, (Player) (Object) this);
        if (entry != null) crit = EnchantmentHelper.getTagEnchantmentLevel(entry, getItemBySlot(EquipmentSlot.HEAD)) > 0;
        return bl || crit;
    }

    @Inject(method = "getHurtSound", at = @At("RETURN"), cancellable = true)
    protected void getHurtSound(DamageSource source, CallbackInfoReturnable<SoundEvent> cir) {
        if (source.is(ModDT.LOSEHP)) cir.setReturnValue(ModTools.getSound("dabaosword", "losehp"));
    }
}
