package com.blackmoss.thaumaturgesdelight.data.tag;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ThaumaturgesDelight.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(TDEntityTypeTags.COW)
                .add(EntityType.COW);

        tag(TDEntityTypeTags.CHICKENS)
                .add(EntityType.CHICKEN);

        tag(TDEntityTypeTags.PIGS)
                .add(EntityType.PIG)
                .add(EntityType.PIGLIN)
                .add(EntityType.PIGLIN_BRUTE)
                .add(EntityType.ZOMBIFIED_PIGLIN);

        tag(TDEntityTypeTags.FISH)
                .add(EntityType.COD)
                .add(EntityType.SALMON)
                .add(EntityType.PUFFERFISH)
                .add(EntityType.TROPICAL_FISH);

        tag(TDEntityTypeTags.RABBITS)
                .add(EntityType.RABBIT);

        tag(TDEntityTypeTags.SHEEP)
                .add(EntityType.SHEEP)
                .add(EntityType.GOAT);
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge's Delight Entity Type Tags";
    }
}
