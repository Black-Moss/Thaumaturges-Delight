package com.blackmoss.thaumaturgesdelight.item;

import com.blackmoss.thaumaturgesdelight.registry.TDTags;
import com.leclowndu93150.thaumaturge.registry.TCItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.List;

public final class BleedingEdgeResults {
    private static final List<Entry> ENTRIES;

    static {
        ENTRIES = List.of(
                new BleedingEdgeResults.Entry(TDTags.EntityTypes.COW, TCItems.CHUNK_BEEF.get()),
                new BleedingEdgeResults.Entry(TDTags.EntityTypes.CHICKENS, TCItems.CHUNK_CHICKEN.get()),
                new BleedingEdgeResults.Entry(TDTags.EntityTypes.PIGS, TCItems.CHUNK_PORK.get()),
                new BleedingEdgeResults.Entry(TDTags.EntityTypes.FISH, TCItems.CHUNK_FISH.get()),
                new BleedingEdgeResults.Entry(TDTags.EntityTypes.RABBITS, TCItems.CHUNK_RABBIT.get()),
                new BleedingEdgeResults.Entry(TDTags.EntityTypes.SHEEP, TCItems.CHUNK_MUTTON.get()));
    }

    private BleedingEdgeResults() {
    }

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
