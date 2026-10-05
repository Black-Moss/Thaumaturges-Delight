package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.block.ArcaneCookingPotBlock;
import com.blackmoss.thaumaturgesdelight.block.ArcaneStoveBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public final class TDBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ThaumaturgesDelight.MODID);

    public static final DeferredBlock<ArcaneCookingPotBlock> ARCANE_COOKING_POT = BLOCKS.register("arcane_cooking_pot",
            () -> new ArcaneCookingPotBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.COOKING_POT.get())
                    .setId(ResourceKey.create(Registries.BLOCK, ThaumaturgesDelight.identifier("arcane_cooking_pot")))));

    public static final DeferredBlock<ArcaneStoveBlock> ARCANE_STOVE = BLOCKS.register("arcane_stove",
            () -> new ArcaneStoveBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.STOVE.get())
                    .setId(ResourceKey.create(Registries.BLOCK, ThaumaturgesDelight.identifier("arcane_stove")))));
}
