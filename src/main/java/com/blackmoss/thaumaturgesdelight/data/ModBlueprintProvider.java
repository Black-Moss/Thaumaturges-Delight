package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.lazycat.api.thaumaturge.blueprint.BlueprintContext;
import com.blackmoss.lazycat.thaumaturge.blueprint.LazyCatBlueprintProvider;
import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import net.minecraft.data.PackOutput;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public final class ModBlueprintProvider extends LazyCatBlueprintProvider {
    public ModBlueprintProvider(PackOutput output) {
        super(output, ThaumaturgesDelight.MODID);
    }

    @Override
    protected void addBlueprints(BlueprintContext context) {
        context.blueprint("magic_kitchen")
                .key('C', ModBlocks.COOKING_POT.get(), TDBlocks.ARCANE_COOKING_POT.get())
                .key('S', ModBlocks.STOVE.get(), TDBlocks.ARCANE_STOVE.get())
                .layer("C")
                .layer("S")
                .save();
    }
}
