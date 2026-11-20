package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class ModBlockLootSubProvider extends BlockLootSubProvider {
    public ModBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected @NonNull Iterable<Block> getKnownBlocks() {
        return TDBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.value()).toList();
    }

    @Override
    protected void generate() {
        dropSelf(TDBlocks.ARCANE_COOKING_POT.get());
        dropSelf(TDBlocks.ARCANE_STOVE.get());
        dropSelf(TDBlocks.AURA_RICH_SOIL.get());
        dropOther(TDBlocks.AURA_RICH_SOIL_FARMLAND.get(), TDBlocks.AURA_RICH_SOIL.get());
//        dropSelf(TDBlocks.DEATH_DAY_CAKE.get());
    }
}
