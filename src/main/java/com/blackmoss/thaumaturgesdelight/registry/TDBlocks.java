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

    public static final DeferredBlock<ArcaneCookingPotBlock> ARCANE_COOKING_POT =
            BLOCKS.register("arcane_cooking_pot", () -> new ArcaneCookingPotBlock(BlockBehaviour.Properties
                    .ofFullCopy(ModBlocks.COOKING_POT.get())));

    public static final DeferredBlock<ArcaneStoveBlock> ARCANE_STOVE = BLOCKS.register(
            "arcane_stove", () -> new ArcaneStoveBlock(BlockBehaviour.Properties
                    .ofFullCopy(ModBlocks.STOVE.get())));

    public static final DeferredBlock<AuraRichSoilBlock> AURA_RICH_SOIL = BLOCKS.register(
            "aura_rich_soil", () -> new AuraRichSoilBlock(BlockBehaviour.Properties
                    .ofFullCopy(ModBlocks.RICH_SOIL.get())
                    .randomTicks()));

    public static final DeferredBlock<AuraRichSoilFarmlandBlock> AURA_RICH_SOIL_FARMLAND = BLOCKS.register(
            "aura_rich_soil_farmland", () -> new AuraRichSoilFarmlandBlock(BlockBehaviour.Properties
                    .ofFullCopy(ModBlocks.RICH_SOIL_FARMLAND.get())
                    .randomTicks()));

//    public static final DeferredBlock<DeathDayCakeBlock> DEATH_DAY_CAKE = BLOCKS.register(
//            "death_day_cake", () -> new DeathDayCakeBlock(BlockBehaviour.Properties
//                    .ofFullCopy(Blocks.CAKE),
//                    TDItems.DEATH_DAY_CAKE_SLICE, true));
}
