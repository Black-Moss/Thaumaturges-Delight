package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ThaumaturgesDelight.MODID);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        generateFlatItem(itemModels, TDItems.BRASS_KNIFE.get());
        generateFlatItem(itemModels, TDItems.THAUMIUM_KNIFE.get());
        generateFlatItem(itemModels, TDItems.VOID_KNIFE.get());
        generateFlatItem(itemModels, TDItems.ELEMENTAL_KNIFE.get());
    }

    private static void generateFlatItem(@NonNull ItemModelGenerators itemModels, @NonNull Item item) {
        Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
        Identifier modelId = ThaumaturgesDelight.identifier("item/" + itemId.getPath());
        ModelTemplates.FLAT_ITEM.create(modelId, TextureMapping.layer0(item), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(modelId));
    }
}
