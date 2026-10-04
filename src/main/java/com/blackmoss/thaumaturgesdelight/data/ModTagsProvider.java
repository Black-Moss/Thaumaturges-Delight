package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.blackmoss.thaumaturgesdelight.registry.TDTags;
import com.leclowndu93150.thaumaturge.registry.TCEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public final class ModTagsProvider implements DataProvider {
    private final IntrinsicHolderTagsProvider<Item> itemTags;
    private final IntrinsicHolderTagsProvider<Block> blockTags;
    private final IntrinsicHolderTagsProvider<EntityType<?>> entityTypeTags;


    public ModTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        //noinspection deprecation
        this.itemTags = new IntrinsicHolderTagsProvider<>(output, Registries.ITEM, lookup,
                item -> item.builtInRegistryHolder().key(), ThaumaturgesDelight.MODID) {
            @Override
            protected void addTags(HolderLookup.@NonNull Provider provider) {
                tag(CommonTags.Items.TOOLS_KNIFE)
                        .add(TDItems.BRASS_KNIFE.get())
                        .add(TDItems.THAUMIUM_KNIFE.get())
                        .add(TDItems.VOID_KNIFE.get())
                        .add(TDItems.ELEMENTAL_KNIFE.get());
                tag(ModTags.Items.KNIVES)
                        .add(TDItems.BRASS_KNIFE.get())
                        .add(TDItems.THAUMIUM_KNIFE.get())
                        .add(TDItems.VOID_KNIFE.get())
                        .add(TDItems.ELEMENTAL_KNIFE.get());
            }
        };

        //noinspection deprecation
        this.blockTags = new IntrinsicHolderTagsProvider<>(output, Registries.BLOCK, lookup,
                block -> block.builtInRegistryHolder().key(), ThaumaturgesDelight.MODID) {
            @Override
            protected void addTags(HolderLookup.@NonNull Provider provider) {
            }
        };

        //noinspection deprecation
        this.entityTypeTags = new IntrinsicHolderTagsProvider<>(output, Registries.ENTITY_TYPE, lookup,
                entityType -> entityType.builtInRegistryHolder().key(), ThaumaturgesDelight.MODID) {
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
        };
    }


    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        return CompletableFuture.allOf(itemTags.run(cache), blockTags.run(cache), entityTypeTags.run(cache));
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge's Delight Tags";
    }
}
