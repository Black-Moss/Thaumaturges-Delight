package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.item.EssentiaRockCandyItem;
import com.leclowndu93150.thaumaturge.content.equipment.TCMaterials;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Consumables;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.tag.ModTags;

public final class TDItems {
    private static final float KNIFE_ATTACK_DAMAGE = 0.5F;
    private static final float KNIFE_ATTACK_SPEED = -2.0F;
    private static final float KNIFE_TOOL_DAMAGE = 0.0F;

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgesDelight.MODID);

    public static final DeferredItem<KnifeItem> BRASS_KNIFE = ITEMS.registerItem("brass_knife",
            props -> new KnifeItem(TDMaterials.TOOL_BRASS, props), props -> knife(props, TDMaterials.TOOL_BRASS));
    public static final DeferredItem<KnifeItem> THAUMIUM_KNIFE = ITEMS.registerItem("thaumium_knife",
            props -> new KnifeItem(TCMaterials.TOOL_THAUMIUM, props), props -> knife(props, TCMaterials.TOOL_THAUMIUM));
    public static final DeferredItem<KnifeItem> VOID_KNIFE = ITEMS.registerItem("void_knife",
            props -> new KnifeItem(TCMaterials.TOOL_VOID, props), props -> knife(props, TCMaterials.TOOL_VOID));
    public static final DeferredItem<KnifeItem> ELEMENTAL_KNIFE = ITEMS.registerItem("elemental_knife",
            props -> new KnifeItem(TCMaterials.TOOL_ELEMENTAL, props), props -> knife(props, TCMaterials.TOOL_ELEMENTAL));
//    public static final DeferredItem<KnifeItem> PRIMAL_VOID_KNIFE = ITEMS.registerItem("primal_knife",
//            props -> new KnifeItem(TCMaterials.TOOL_PRIMAL_VOID, props), props -> knife(props, TCMaterials.TOOL_PRIMAL_VOID));

    public static final DeferredItem<EssentiaRockCandyItem> ESSENTIA_ROCK_CANDY = ITEMS.registerItem(
            "essentia_rock_candy", EssentiaRockCandyItem::new, props -> props
                    .food(new FoodProperties(1, 0.5F, true),
                            Consumables.defaultFood().consumeSeconds(0.5F).build()));

    private TDItems() {
    }

    private static Item.Properties knife(Item.Properties properties, ToolMaterial material) {
        return material.applyToolProperties(
                properties.useItemDescriptionPrefix(),
                ModTags.Blocks.MINEABLE_WITH_KNIFE,
                KNIFE_ATTACK_DAMAGE,
                KNIFE_ATTACK_SPEED,
                KNIFE_TOOL_DAMAGE);
    }
}
