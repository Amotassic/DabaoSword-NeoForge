package com.amotassic.dabaosword.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CrafterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.amotassic.dabaosword.util.ModifyDamage.modifyStack;

@Mixin(CraftingMenu.class)
abstract class CraftingMixin {

    @Inject(method = "slotChangedCraftingGrid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private static void updateResult(AbstractContainerMenu menu, Level level, Player player, CraftingContainer craftSlots, ResultContainer resultSlots, RecipeHolder<CraftingRecipe> recipe, CallbackInfo ci, @Local(ordinal = 0) ItemStack stack) {
        modifyStack(stack);
    }
}

@Mixin(CrafterMenu.class)
abstract class CrafterScreen {

    @Inject(method = "refreshRecipeResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private void updateResult(CallbackInfo ci, @Local(ordinal = 0) ItemStack stack) {
        modifyStack(stack);
    }
}

@Mixin(CrafterBlock.class)
abstract class Crafter {

    @ModifyVariable(method = "dispenseItem", at = @At("HEAD"), argsOnly = true)
    private ItemStack transferOrSpawnStack(ItemStack stack) {
        return modifyStack(stack);
    }
}