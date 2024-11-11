package com.amotassic.dabaosword.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;

public class Tags {
    public static void Tag() {}

    public static final TagKey<Item> BASIC_CARD = createTag("basic_card");
    public static final TagKey<Item> ARMOURY_CARD = createTag("armoury_card");
    public static final TagKey<Item> LOCK_SKILL = createTag("lock_skill");
    public static final TagKey<Item> TRIGGER_WUXIE = createTag("trigger_wuxie");

    private static TagKey<Item> createTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("dabaosword", name));
    }

    public static final TagKey<DamageType> TRIGGER_TIESUO = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("dabaosword", "trigger_tiesuo"));
}
