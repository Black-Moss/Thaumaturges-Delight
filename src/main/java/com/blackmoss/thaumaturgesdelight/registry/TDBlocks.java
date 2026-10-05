package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.block.ArcaneCookingPotBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public final class TDBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ThaumaturgesDelight.MODID);

    // 方块属性直接抄原版厨锅（硬度、音效、不遮挡光照等）；
    // ofFullCopy 造出来的是新实例，不带方块 id，必须自己 setId，否则注册时抛 "Block id not set"
    public static final DeferredBlock<ArcaneCookingPotBlock> ARCANE_COOKING_POT = BLOCKS.register("arcane_cooking_pot",
            () -> new ArcaneCookingPotBlock(BlockBehaviour.Properties.ofFullCopy(ModBlocks.COOKING_POT.get())
                    .setId(ResourceKey.create(Registries.BLOCK, ThaumaturgesDelight.identifier("arcane_cooking_pot")))));
}
