package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.lazycat.model.LazyCatModelProvider;
import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.block.ArcaneCookingPotBlock;
import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.client.color.CrystalAspectTint;
import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmlandBlock;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;

import java.util.Optional;

public class ModModelProvider extends LazyCatModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ThaumaturgesDelight.MODID);
    }

    private static MultiVariant potVariant(Identifier model, Quadrant rotation) {
        return BlockModelGenerators.plainVariant(model).with(VariantMutator.Y_ROT.withValue(rotation));
    }

    private static MultiVariant stoveVariant(Identifier model, Quadrant rotation) {
        return BlockModelGenerators.plainVariant(model).with(VariantMutator.Y_ROT.withValue(rotation));
    }

    private static Material stoveMaterial(String path) {
        return new Material(ThaumaturgesDelight.identifier("block/" + path));
    }

    private void aspectItem(Item item) {
        ModelTemplates.FLAT_ITEM.create(getItemModelId(item), TextureMapping.layer0(item), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(item, ItemModelUtils.tintedModel(getItemModelId(item), new CrystalAspectTint(16777215)));
    }

    private TextureMapping stoveTextures(boolean lit) {
        return new TextureMapping()
                .put(TextureSlot.BOTTOM, new Material(ThaumaturgesDelight.fd("block/stove_bottom")))
                .put(TextureSlot.TOP, stoveMaterial(lit ? "arcane_stove_top_on" : "arcane_stove_top"))
                .put(TextureSlot.SIDE, stoveMaterial("arcane_stove_side"))
                .put(TextureSlot.FRONT, stoveMaterial(lit ? "arcane_stove_front_on" : "arcane_stove_front"));
    }

    @Override
    protected void addModels() {
        arcaneCookingPot(TDBlocks.ARCANE_COOKING_POT.get());
        arcaneStove(TDBlocks.ARCANE_STOVE.get());
        auraRichSoil(TDBlocks.AURA_RICH_SOIL.get(), TDBlocks.AURA_RICH_SOIL_FARMLAND.get());
//        deathDayCake(TDBlocks.DEATH_DAY_CAKE.get());

        itemFlat(TDItems.BRASS_KNIFE);
        itemFlat(TDItems.THAUMIUM_KNIFE);
        itemFlat(TDItems.VOID_KNIFE);
        itemFlat(TDItems.ELEMENTAL_KNIFE);
        itemFlat(TDItems.SEXTUPLE_MEAT_SURPRISE);
        itemFlat(TDItems.PURIFY_COOKIE);
        itemFlat(TDItems.CUTTING_SEAL);
        itemFlat(TDItems.ADVANCED_CUTTING_SEAL);
        itemFlat(TDItems.CHUNKS_FRIED_RICE);
        itemFlat(TDItems.DROWNEDS_TEA);
//        itemFlat(TDItems.DEATH_DAY_CAKE_SLICE);
        itemFlat(TDItems.TAINTED_MILK_BUCKET);
        itemFlat(TDItems.KETCHUP_CAP);
        itemFlat(TDItems.FRENCH_FRY_ROD);
        itemFlat(TDItems.ETHEREAL_PETAL);

        aspectItem(TDItems.ESSENTIA_ROCK_CANDY.get());
        aspectItem(TDItems.MANA_BEAN_DOUGH.get());
        aspectItem(TDItems.MANA_BREAD.get());
    }

//    private void deathDayCake(Block block) {
//        Identifier modelId = getBlockModelId(block);
//        ModelTemplates.CUBE_ALL.create(modelId, new TextureMapping()
//                .put(TextureSlot.ALL, new Material(ThaumaturgesDelight.identifier("block/death_day_cake"))), blockModels.modelOutput);
//
//        PropertyDispatch<MultiVariant> dispatch = PropertyDispatch.initial(FeastBlock.FACING)
//                .select(Direction.NORTH, BlockModelGenerators.plainVariant(modelId))
//                .select(Direction.EAST, BlockModelGenerators.plainVariant(modelId).with(VariantMutator.Y_ROT.withValue(Quadrant.R90)))
//                .select(Direction.SOUTH, BlockModelGenerators.plainVariant(modelId).with(VariantMutator.Y_ROT.withValue(Quadrant.R180)))
//                .select(Direction.WEST, BlockModelGenerators.plainVariant(modelId).with(VariantMutator.Y_ROT.withValue(Quadrant.R270)));
//
//        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(dispatch));
//        blockModels.registerSimpleItemModel(block, modelId);
//    }

    private void arcaneCookingPot(Block block) {
        var potModel = getBlockModelId(block);
        var potHandleModel = ThaumaturgesDelight.identifier("block/arcane_cooking_pot_handle");
        var potTrayModel = ThaumaturgesDelight.identifier("block/arcane_cooking_pot_tray");
        PropertyDispatch<MultiVariant> dispatch = PropertyDispatch.initial(ArcaneCookingPotBlock.FACING, ArcaneCookingPotBlock.SUPPORT)
                .select(Direction.NORTH, CookingPotSupport.NONE, potVariant(potModel, Quadrant.R0))
                .select(Direction.NORTH, CookingPotSupport.HANDLE, potVariant(potHandleModel, Quadrant.R0))
                .select(Direction.NORTH, CookingPotSupport.TRAY, potVariant(potTrayModel, Quadrant.R0))
                .select(Direction.EAST, CookingPotSupport.NONE, potVariant(potModel, Quadrant.R90))
                .select(Direction.EAST, CookingPotSupport.HANDLE, potVariant(potHandleModel, Quadrant.R90))
                .select(Direction.EAST, CookingPotSupport.TRAY, potVariant(potTrayModel, Quadrant.R90))
                .select(Direction.SOUTH, CookingPotSupport.NONE, potVariant(potModel, Quadrant.R180))
                .select(Direction.SOUTH, CookingPotSupport.HANDLE, potVariant(potHandleModel, Quadrant.R180))
                .select(Direction.SOUTH, CookingPotSupport.TRAY, potVariant(potTrayModel, Quadrant.R180))
                .select(Direction.WEST, CookingPotSupport.NONE, potVariant(potModel, Quadrant.R270))
                .select(Direction.WEST, CookingPotSupport.HANDLE, potVariant(potHandleModel, Quadrant.R270))
                .select(Direction.WEST, CookingPotSupport.TRAY, potVariant(potTrayModel, Quadrant.R270));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(dispatch));
        blockModels.registerSimpleItemModel(block, potModel);
    }

    private void arcaneStove(Block block) {
        Identifier stoveModel = getBlockModelId(block);
        Identifier stoveOnModel = ThaumaturgesDelight.identifier("block/arcane_stove_on");

        new ModelTemplate(
                Optional.of(Identifier.withDefaultNamespace("block/orientable_with_bottom")),
                Optional.empty(),
                TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.FRONT)
                .create(stoveModel, stoveTextures(false), blockModels.modelOutput);
        new ModelTemplate(
                Optional.of(Identifier.withDefaultNamespace("block/orientable_with_bottom")),
                Optional.empty(),
                TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.FRONT)
                .create(stoveOnModel, stoveTextures(true), blockModels.modelOutput);

        PropertyDispatch<MultiVariant> dispatch = PropertyDispatch.initial(AbstractStoveBlock.FACING, AbstractStoveBlock.LIT)
                .select(Direction.NORTH, false, stoveVariant(stoveModel, Quadrant.R0))
                .select(Direction.NORTH, true, stoveVariant(stoveOnModel, Quadrant.R0))
                .select(Direction.EAST, false, stoveVariant(stoveModel, Quadrant.R90))
                .select(Direction.EAST, true, stoveVariant(stoveOnModel, Quadrant.R90))
                .select(Direction.SOUTH, false, stoveVariant(stoveModel, Quadrant.R180))
                .select(Direction.SOUTH, true, stoveVariant(stoveOnModel, Quadrant.R180))
                .select(Direction.WEST, false, stoveVariant(stoveModel, Quadrant.R270))
                .select(Direction.WEST, true, stoveVariant(stoveOnModel, Quadrant.R270));

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(dispatch));
        blockModels.registerSimpleItemModel(block, stoveModel);
    }

    private void auraRichSoil(Block block, Block farmlandBlock) {
        var modelId = getBlockModelId(block);
        var farmlandModelId = getBlockModelId(farmlandBlock);
        var farmlandMoistModelId = ThaumaturgesDelight.identifier("block/aura_rich_soil_farmland_moist");

        ModelTemplates.CUBE_ALL.create(modelId, new TextureMapping()
                .put(TextureSlot.ALL, new Material(modelId)), blockModels.modelOutput);
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(modelId)));
        blockModels.registerSimpleItemModel(block, modelId);

        new ModelTemplate(
                Optional.of(ThaumaturgesDelight.fd("block/template_farmland_custom")), Optional.empty(),
                TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.TOP)
                .create(farmlandModelId, new TextureMapping()
                        .put(TextureSlot.BOTTOM, new Material(modelId))
                        .put(TextureSlot.SIDE, new Material(modelId))
                        .put(TextureSlot.TOP, new Material(farmlandModelId)), blockModels.modelOutput);
        new ModelTemplate(
                Optional.of(ThaumaturgesDelight.fd("block/template_farmland_custom")), Optional.empty(),
                TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.TOP)
                .create(farmlandMoistModelId, new TextureMapping()
                        .put(TextureSlot.BOTTOM, new Material(modelId))
                        .put(TextureSlot.SIDE, new Material(ThaumaturgesDelight.identifier("block/aura_rich_soil_farmland_moist_side")))
                        .put(TextureSlot.TOP, new Material(farmlandMoistModelId)), blockModels.modelOutput);

        MultiVariant dry = BlockModelGenerators.plainVariant(farmlandModelId);
        MultiVariant moist = BlockModelGenerators.plainVariant(farmlandMoistModelId);
        PropertyDispatch<MultiVariant> dispatch = PropertyDispatch.initial(FarmlandBlock.MOISTURE)
                .select(0, dry)
                .select(1, dry)
                .select(2, dry)
                .select(3, dry)
                .select(4, dry)
                .select(5, dry)
                .select(6, dry)
                .select(7, moist);

        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(farmlandBlock).with(dispatch));
        blockModels.registerSimpleItemModel(farmlandBlock, farmlandModelId);
    }
}
