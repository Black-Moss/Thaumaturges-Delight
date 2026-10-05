package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.block.ArcaneCookingPotBlock;
import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.client.color.CrystalAspectTint;
import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;

import java.util.Optional;

public class ModModelProvider extends ModelProvider {
    private static final Identifier POT_MODEL = ThaumaturgesDelight.identifier("block/arcane_cooking_pot");
    private static final Identifier POT_HANDLE_MODEL = ThaumaturgesDelight.identifier("block/arcane_cooking_pot_handle");
    private static final Identifier POT_TRAY_MODEL = ThaumaturgesDelight.identifier("block/arcane_cooking_pot_tray");

    private static final ModelTemplate STOVE_TEMPLATE = new ModelTemplate(
            Optional.of(Identifier.withDefaultNamespace("block/orientable_with_bottom")), Optional.empty(),
            TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.FRONT);
    private static final Identifier STOVE_MODEL = ThaumaturgesDelight.identifier("block/arcane_stove");
    private static final Identifier STOVE_ON_MODEL = ThaumaturgesDelight.identifier("block/arcane_stove_on");
    private static final TextureMapping STOVE_TEXTURES = stoveTextures(false);
    private static final TextureMapping STOVE_ON_TEXTURES = stoveTextures(true);

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
        arcaneCookingPot(blockModels);
        arcaneStove(blockModels);

        flatItem(itemModels, TDItems.BRASS_KNIFE.get());
        flatItem(itemModels, TDItems.THAUMIUM_KNIFE.get());
        flatItem(itemModels, TDItems.VOID_KNIFE.get());
        flatItem(itemModels, TDItems.ELEMENTAL_KNIFE.get());
        flatItem(itemModels, TDItems.SIXFOLD_MEAT_TREAT.get());
        flatItem(itemModels, TDItems.PURIFY_COOKIE.get());

        essentiaRockCandy(itemModels, TDItems.ESSENTIA_ROCK_CANDY.get());
    }

    private void arcaneCookingPot(@NonNull BlockModelGenerators blockModels) {
        Block block = TDBlocks.ARCANE_COOKING_POT.get();
        PropertyDispatch<MultiVariant> dispatch = PropertyDispatch.initial(ArcaneCookingPotBlock.FACING, ArcaneCookingPotBlock.SUPPORT)
                .select(Direction.NORTH, CookingPotSupport.NONE, potVariant(POT_MODEL, Quadrant.R0))
                .select(Direction.NORTH, CookingPotSupport.HANDLE, potVariant(POT_HANDLE_MODEL, Quadrant.R0))
                .select(Direction.NORTH, CookingPotSupport.TRAY, potVariant(POT_TRAY_MODEL, Quadrant.R0))
                .select(Direction.EAST, CookingPotSupport.NONE, potVariant(POT_MODEL, Quadrant.R90))
                .select(Direction.EAST, CookingPotSupport.HANDLE, potVariant(POT_HANDLE_MODEL, Quadrant.R90))
                .select(Direction.EAST, CookingPotSupport.TRAY, potVariant(POT_TRAY_MODEL, Quadrant.R90))
                .select(Direction.SOUTH, CookingPotSupport.NONE, potVariant(POT_MODEL, Quadrant.R180))
                .select(Direction.SOUTH, CookingPotSupport.HANDLE, potVariant(POT_HANDLE_MODEL, Quadrant.R180))
                .select(Direction.SOUTH, CookingPotSupport.TRAY, potVariant(POT_TRAY_MODEL, Quadrant.R180))
                .select(Direction.WEST, CookingPotSupport.NONE, potVariant(POT_MODEL, Quadrant.R270))
                .select(Direction.WEST, CookingPotSupport.HANDLE, potVariant(POT_HANDLE_MODEL, Quadrant.R270))
                .select(Direction.WEST, CookingPotSupport.TRAY, potVariant(POT_TRAY_MODEL, Quadrant.R270));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(dispatch));
        blockModels.registerSimpleItemModel(block, POT_MODEL);
    }

    private static MultiVariant potVariant(Identifier model, Quadrant rotation) {
        return BlockModelGenerators.plainVariant(model).with(VariantMutator.Y_ROT.withValue(rotation));
    }

    private void arcaneStove(@NonNull BlockModelGenerators blockModels) {
        Block block = TDBlocks.ARCANE_STOVE.get();
        STOVE_TEMPLATE.create(STOVE_MODEL, STOVE_TEXTURES, blockModels.modelOutput);
        STOVE_TEMPLATE.create(STOVE_ON_MODEL, STOVE_ON_TEXTURES, blockModels.modelOutput);

        PropertyDispatch<MultiVariant> dispatch = PropertyDispatch.initial(AbstractStoveBlock.FACING, AbstractStoveBlock.LIT)
                .select(Direction.NORTH, false, stoveVariant(STOVE_MODEL, Quadrant.R0))
                .select(Direction.NORTH, true, stoveVariant(STOVE_ON_MODEL, Quadrant.R0))
                .select(Direction.EAST, false, stoveVariant(STOVE_MODEL, Quadrant.R90))
                .select(Direction.EAST, true, stoveVariant(STOVE_ON_MODEL, Quadrant.R90))
                .select(Direction.SOUTH, false, stoveVariant(STOVE_MODEL, Quadrant.R180))
                .select(Direction.SOUTH, true, stoveVariant(STOVE_ON_MODEL, Quadrant.R180))
                .select(Direction.WEST, false, stoveVariant(STOVE_MODEL, Quadrant.R270))
                .select(Direction.WEST, true, stoveVariant(STOVE_ON_MODEL, Quadrant.R270));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(dispatch));
        blockModels.registerSimpleItemModel(block, STOVE_MODEL);
    }

    private static MultiVariant stoveVariant(Identifier model, Quadrant rotation) {
        return BlockModelGenerators.plainVariant(model).with(VariantMutator.Y_ROT.withValue(rotation));
    }

    private static TextureMapping stoveTextures(boolean lit) {
        return new TextureMapping()
                .put(TextureSlot.BOTTOM, stoveMaterial("stove_bottom"))
                .put(TextureSlot.TOP, stoveMaterial(lit ? "stove_top_on" : "stove_top"))
                .put(TextureSlot.SIDE, stoveMaterial("stove_side"))
                .put(TextureSlot.FRONT, stoveMaterial(lit ? "stove_front_on" : "stove_front"));
    }

    private static Material stoveMaterial(String path) {
        return new Material(Identifier.fromNamespaceAndPath("farmersdelight", "block/" + path));
    }
}
