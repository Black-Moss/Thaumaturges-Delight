package com.blackmoss.thaumaturgesdelight.item;

import com.blackmoss.thaumaturgesdelight.registry.TDEntityTypeTags;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.List;

public final class BleedingEdgeResults {
    private static final List<Entry> ENTRIES = List.of(
            new BleedingEdgeResults.Entry(TDEntityTypeTags.COW, TTItems.CHUNK_BEEF.get()),
            new BleedingEdgeResults.Entry(TDEntityTypeTags.CHICKENS, TTItems.CHUNK_CHICKEN.get()),
            new BleedingEdgeResults.Entry(TDEntityTypeTags.PIGS, TTItems.CHUNK_PORK.get()),
            new BleedingEdgeResults.Entry(TDEntityTypeTags.FISH, TTItems.CHUNK_FISH.get()),
            new BleedingEdgeResults.Entry(TDEntityTypeTags.RABBITS, TTItems.CHUNK_RABBIT.get()),
            new BleedingEdgeResults.Entry(TDEntityTypeTags.SHEEP, TTItems.CHUNK_MUTTON.get()));

    public static Item chunkFor(LivingEntity entity) {
        for (Entry entry : ENTRIES) {
            //noinspection deprecation
            if (entity.getType().builtInRegistryHolder().is(entry.entity())) {
                return entry.chunk;
            }
        }

        return null;
    }

    private record Entry(TagKey<EntityType<?>> entity, Item chunk) {
    }
}
