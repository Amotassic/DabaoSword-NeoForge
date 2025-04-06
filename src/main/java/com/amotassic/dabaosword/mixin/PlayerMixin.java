package com.amotassic.dabaosword.mixin;

import com.amotassic.dabaosword.item.ModItems;
import com.amotassic.dabaosword.util.ModTools;
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
        var entry = ModTools.getEntry(ModItems.CRIT, (Player) (Object) this);
        boolean crit = EnchantmentHelper.getTagEnchantmentLevel(entry, getItemBySlot(EquipmentSlot.HEAD)) > 0;
        return bl || crit;
    }
}
