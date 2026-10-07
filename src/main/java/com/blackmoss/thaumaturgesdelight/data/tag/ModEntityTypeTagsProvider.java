package com.blackmoss.thaumaturgesdelight.data.tag;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ThaumaturgesDelight.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(TDTags.EntityTypes.COW)
                .add(EntityType.COW);

        tag(TDTags.EntityTypes.CHICKENS)
                .add(EntityType.CHICKEN);

        tag(TDTags.EntityTypes.PIGS)
                .add(EntityType.PIG)
                .add(EntityType.PIGLIN)
                .add(EntityType.PIGLIN_BRUTE)
                .add(EntityType.ZOMBIFIED_PIGLIN);

        tag(TDTags.EntityTypes.FISH)
                .add(EntityType.COD)
                .add(EntityType.SALMON)
                .add(EntityType.PUFFERFISH)
                .add(EntityType.TROPICAL_FISH);

        tag(TDTags.EntityTypes.RABBITS)
                .add(EntityType.RABBIT);

        tag(TDTags.EntityTypes.SHEEP)
                .add(EntityType.SHEEP)
                .add(EntityType.GOAT);
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge's Delight Entity Type Tags";
    }
}
