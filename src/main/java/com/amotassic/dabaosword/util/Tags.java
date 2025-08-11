package com.amotassic.dabaosword.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;

public class Tags {
    public static void Tag() {}

    private static TagKey<Item> createTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("dabaosword", name));
    }

    public static final TagKey<Item>
            SKILLS = curiosTag("skills"),
            WEAPON = curiosTag("weapon"),
            ARMOR = curiosTag("armor"),
            ATTACK = curiosTag("attack"),
            DEFEND = curiosTag("defend");

    private static TagKey<Item> curiosTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("curios", name));
    }

    public static final TagKey<DamageType> TRIGGER_TIESUO = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("dabaosword", "trigger_tiesuo"));

    public static final TagKey<DamageType> FROM_CARD = TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("dabaosword", "from_card"));
}
