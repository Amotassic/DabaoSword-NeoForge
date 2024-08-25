package com.amotassic.dabaosword.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class Tags {
    public static void Tag() {}

    public static final TagKey<Item> CARD = createTag("card");
    public static final TagKey<Item> BASIC_CARD = createTag("basic_card");
    public static final TagKey<Item> ARMOURY_CARD = createTag("armoury_card");
    public static final TagKey<Item> SKILL = createTag("skills");
    public static final TagKey<Item> SHA = createTag("sha");
    public static final TagKey<Item> RECOVER = createTag("recover");
    public static final TagKey<Item> LOCK_SKILL = createTag("lock_skill");

    private static TagKey<Item> createTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("dabaosword", name));
    }
}
