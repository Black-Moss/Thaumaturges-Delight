package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.content.equipment.TCMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.tag.ModTags;

public final class TDItems {
    private static final float KNIFE_ATTACK_DAMAGE = 0.5F;
    private static final float KNIFE_ATTACK_SPEED = -2.0F;
    private static final float KNIFE_TOOL_DAMAGE = 0.0F;

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ThaumaturgesDelight.MODID);

    public static final DeferredItem<Item> BRASS_KNIFE = ITEMS.registerItem("brass_knife",
            Item::new, props -> knife(props, TDMaterials.TOOL_BRASS));
    public static final DeferredItem<Item> THAUMIUM_KNIFE = ITEMS.registerItem("thaumium_knife",
            Item::new, props -> knife(props, TCMaterials.TOOL_THAUMIUM));
    public static final DeferredItem<Item> VOID_KNIFE = ITEMS.registerItem("void_knife",
            Item::new, props -> knife(props, TCMaterials.TOOL_VOID));
    public static final DeferredItem<Item> ELEMENTAL_KNIFE = ITEMS.registerItem("elemental_knife",
            Item::new, props -> knife(props, TCMaterials.TOOL_ELEMENTAL));
//    public static final DeferredItem<Item> PRIMAL_VOID_KNIFE = ITEMS.registerItem("primal_knife",
//            Item::new, props -> knife(props, TCMaterials.TOOL_PRIMAL_VOID));

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
