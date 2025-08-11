package com.amotassic.dabaosword.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class ModEntity {
    public static void register() {}

    public static final EntityType<XuyouEntity> XUYOU = Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.parse("dabaosword:xuyou"), XuyouEntity.TYPE);
}
