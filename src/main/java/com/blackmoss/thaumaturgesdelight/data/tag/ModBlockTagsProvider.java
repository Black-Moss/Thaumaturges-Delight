package com.blackmoss.thaumaturgesdelight.data.tag;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import com.leclowndu93150.thaumaturge.registry.TCBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public final class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ThaumaturgesDelight.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        //noinspection unchecked
        tag(ModTags.Blocks.HEAT_SOURCES)
                .addTags(TCBlockTags.CRUCIBLE_HEAT_SOURCES);
        tag(TCBlockTags.CRUCIBLE_HEAT_SOURCES)
                .add(TDBlocks.ARCANE_STOVE.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(TDBlocks.AURA_RICH_SOIL.get())
                .add(TDBlocks.AURA_RICH_SOIL_FARMLAND.get());
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge's Delight Block Tags";
    }
}
