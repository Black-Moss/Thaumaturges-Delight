package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.block.ArcaneCookingPotBlockEntity;
import com.blackmoss.thaumaturgesdelight.block.ArcaneStoveBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public final class TDBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArcaneCookingPotBlockEntity>> ARCANE_COOKING_POT = BLOCK_ENTITIES.register(
            "arcane_cooking_pot", () -> new BlockEntityType<>(ArcaneCookingPotBlockEntity::new, Set.of(TDBlocks.ARCANE_COOKING_POT.get())));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArcaneStoveBlockEntity>> ARCANE_STOVE = BLOCK_ENTITIES.register(
            "arcane_stove", () -> new BlockEntityType<>(ArcaneStoveBlockEntity::new, Set.of(TDBlocks.ARCANE_STOVE.get())));
}
