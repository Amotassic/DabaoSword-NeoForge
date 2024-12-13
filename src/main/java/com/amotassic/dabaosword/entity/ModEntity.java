package com.amotassic.dabaosword.entity;

import com.amotassic.dabaosword.DabaoSword;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntity {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, DabaoSword.MODID);
    public static final Supplier<EntityType<XuyouEntity>> XUYOU = ENTITIES.register("xuyou", () -> XuyouEntity.TYPE);
}
