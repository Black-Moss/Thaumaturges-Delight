package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.client.color.CrystalAspectTint;
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

    private static void flatItem(@NonNull ItemModelGenerators itemModels, @NonNull Item item) {
        ModelTemplates.FLAT_ITEM.create(getItemModelId(item), TextureMapping.layer0(item), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(getItemModelId(item)));
    }

    private static void essentiaRockCandy(@NonNull ItemModelGenerators itemModels, @NonNull Item item) {
        ModelTemplates.FLAT_ITEM.create(getItemModelId(item), TextureMapping.layer0(item), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(getItemModelId(item), new CrystalAspectTint(16777215)));
    }

    private static Identifier getItemModelId(Item item) {
        Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
        return ThaumaturgesDelight.identifier("item/" + itemId.getPath());
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        flatItem(itemModels, TDItems.BRASS_KNIFE.get());
        flatItem(itemModels, TDItems.THAUMIUM_KNIFE.get());
        flatItem(itemModels, TDItems.VOID_KNIFE.get());
        flatItem(itemModels, TDItems.ELEMENTAL_KNIFE.get());
        flatItem(itemModels, TDItems.SIXFOLD_MEAT_TREAT.get());
        flatItem(itemModels, TDItems.PURIFY_COOKIE.get());

        essentiaRockCandy(itemModels, TDItems.ESSENTIA_ROCK_CANDY.get());
    }
}
