package com.amotassic.dabaosword.mixin.client;

import com.amotassic.dabaosword.item.card.CardItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBakery.class)
public abstract class ModelLoaderMixin {
    @Shadow protected abstract void loadSpecialItemModelAndDependencies(ModelResourceLocation modelLocation);

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.parse("dabaosword:card/gain_card")));
        var itemList = BuiltInRegistries.ITEM.stream().filter(item -> item instanceof CardItem).toList();
        for (var item : itemList) {
            String[] split = item.toString().split(":");
            String id = split[0] + ":card/" + split[1];
            loadSpecialItemModelAndDependencies(ModelResourceLocation.inventory(ResourceLocation.parse(id)));
        }
    }
}
