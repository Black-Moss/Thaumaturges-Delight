package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.block.ArcaneCookingPotBlock;
import com.blackmoss.thaumaturgesdelight.block.ArcaneStoveBlock;
import com.blackmoss.thaumaturgesdelight.block.AuraRichSoilBlock;
import com.blackmoss.thaumaturgesdelight.block.AuraRichSoilFarmlandBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public final class TDBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ThaumaturgesDelight.MODID);

    public static final DeferredBlock<ArcaneCookingPotBlock> ARCANE_COOKING_POT = BLOCKS.registerBlock(
            "arcane_cooking_pot", ArcaneCookingPotBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(ModBlocks.COOKING_POT.get()));


    public static final DeferredBlock<ArcaneStoveBlock> ARCANE_STOVE = BLOCKS.registerBlock(
            "arcane_stove", ArcaneStoveBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(ModBlocks.STOVE.get()));

    public static final DeferredBlock<AuraRichSoilBlock> AURA_RICH_SOIL = BLOCKS.registerBlock(
            "aura_rich_soil", AuraRichSoilBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(ModBlocks.RICH_SOIL.get())
                    .randomTicks());

    public static final DeferredBlock<AuraRichSoilFarmlandBlock> AURA_RICH_SOIL_FARMLAND = BLOCKS.registerBlock(
            "aura_rich_soil_farmland", AuraRichSoilFarmlandBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(ModBlocks.RICH_SOIL_FARMLAND.get())
                    .randomTicks());

//    public static final DeferredBlock<DeathDayCakeBlock> DEATH_DAY_CAKE = BLOCKS.register(
//            "death_day_cake", () -> new DeathDayCakeBlock(BlockBehaviour.Properties
//                    .ofFullCopy(Blocks.CAKE),
//                    TDItems.DEATH_DAY_CAKE_SLICE, true));
}
