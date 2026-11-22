package com.blackmoss.thaumaturgesdelight.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class TDItemTags {
    public static final TagKey<Item> FOODS_DOUGH_MANA_BEAN = commonTag("foods/dough/mana_bean");
    public static final TagKey<Item> FOODS_BREAD_MANA = commonTag("foods/bread/mana");

    private static TagKey<Item> commonTag(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", path));
    }
}
