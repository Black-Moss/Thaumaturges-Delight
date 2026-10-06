package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.item.EssentiaRockCandyItem;
import com.blackmoss.thaumaturgesdelight.item.PurifyCookieItem;
import com.leclowndu93150.thaumaturge.content.equipment.TCMaterials;
import com.leclowndu93150.thaumaturge.content.golem.ItemSealPlacer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.tag.ModTags;

public final class TDItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgesDelight.MODID);
    public static final DeferredItem<KnifeItem> BRASS_KNIFE = ITEMS.registerItem("brass_knife",
            props -> new KnifeItem(TDMaterials.TOOL_BRASS, props), props -> knife(props, TDMaterials.TOOL_BRASS));
    public static final DeferredItem<KnifeItem> THAUMIUM_KNIFE = ITEMS.registerItem("thaumium_knife",
            props -> new KnifeItem(TCMaterials.TOOL_THAUMIUM, props), props -> knife(props, TCMaterials.TOOL_THAUMIUM));
    public static final DeferredItem<KnifeItem> VOID_KNIFE = ITEMS.registerItem("void_knife",
            props -> new KnifeItem(TCMaterials.TOOL_VOID, props), props -> knife(props, TCMaterials.TOOL_VOID));
    public static final DeferredItem<KnifeItem> ELEMENTAL_KNIFE = ITEMS.registerItem("elemental_knife",
            props -> new KnifeItem(TCMaterials.TOOL_ELEMENTAL, props), props -> knife(props, TCMaterials.TOOL_ELEMENTAL));
    public static final DeferredItem<EssentiaRockCandyItem> ESSENTIA_ROCK_CANDY = ITEMS.registerItem(
            "essentia_rock_candy", EssentiaRockCandyItem::new, props -> props
                    .food(new FoodProperties(1, 0.5F, true),
                            Consumables.defaultFood().consumeSeconds(0.5F).build()));
    public static final DeferredItem<Item> SIXTUPLE_MEAT_TREAT = ITEMS.registerItem("sixfold_meat_treat",
            props -> new Item(props.food(new FoodProperties.Builder()
                            .nutrition(12)
                            .saturationModifier(1.6F)
                            .alwaysEdible()
                            .build(),
                    Consumables.defaultFood().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(
                            MobEffects.REGENERATION,
                            200, 0),
                            0.99F)).build())));

    public static final DeferredItem<PurifyCookieItem> PURIFY_COOKIE = ITEMS.registerItem("purify_cookie",
            PurifyCookieItem::new, props -> props.food(new FoodProperties(2, 0.5F, true)));

    public static final DeferredItem<BlockItem> ARCANE_COOKING_POT = ITEMS.registerSimpleBlockItem(TDBlocks.ARCANE_COOKING_POT);
    public static final DeferredItem<BlockItem> ARCANE_STOVE = ITEMS.registerSimpleBlockItem(TDBlocks.ARCANE_STOVE);

    public static final DeferredItem<ItemSealPlacer> SEAL_CUTTING = ITEMS.registerItem("seal_cutting",
            props -> new ItemSealPlacer(ThaumaturgesDelight.identifier("cutting"), props));
    public static final DeferredItem<ItemSealPlacer> SEAL_ADVANCED_CUTTING = ITEMS.registerItem("seal_advanced_cutting",
            props -> new ItemSealPlacer(ThaumaturgesDelight.identifier("advanced_cutting"), props));

    private TDItems() {
    }

    private static Item.Properties knife(Item.Properties properties, ToolMaterial material) {
        return material.applyToolProperties(
                properties.useItemDescriptionPrefix(),
                ModTags.Blocks.MINEABLE_WITH_KNIFE,
                0.5F,
                -2.0F,
                0.0F);
    }
}
