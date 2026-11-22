package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.lazycat.api.LazyIdentifier;
import com.blackmoss.lazycat.thaumaturge.research.LazyCatResearchProvider;
import com.blackmoss.lazycat.thaumaturge.research.ResearchEntryBuilder;
import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.blackmoss.thaumaturgesdelight.registry.TDResearchCategories;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
import com.leclowndu93150.thaumaturge.api.research.ResearchEntryMeta;
import com.leclowndu93150.thaumaturge.api.research.TTResearchCategories;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.registry.ModItems;

public final class ModResearchProvider extends LazyCatResearchProvider {
    public ModResearchProvider(PackOutput output) {
        super(output, ThaumaturgesDelight.MODID);
    }

    private static JsonObject aspect(ResourceKey<IAspect> aspect) {
        JsonObject component = new JsonObject();
        JsonObject value = new JsonObject();
        value.addProperty("aspect", aspect.identifier().toString());
        component.add("thaumaturge:crystal_aspect", value);
        return component;
    }

    @Override
    protected void addResearch() {
        context.entry(TTResearchCategories.BASICS, "unlock_culinary_magic")
                .parent("thaumaturge:unlock_alchemy")
                .parent("thaumaturge:unlock_artifice")
                .position(2, 2)
                .icon("thaumaturgesdelight:textures/research/cat_culinary_magic.png")
                .meta(ResearchEntryMeta.ROUND, ResearchEntryMeta.SPIKY, ResearchEntryMeta.HIDDEN)

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.ALCHEMY)
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.ARTIFICE)
                .end()

                .stage()
                .end()

                .noteAspect(TTAspects.AER)
                .noteAspect(TTAspects.IGNIS)
                .noteAspect(TTAspects.HERBA)
                .complexity(1)
                .save();

        context.category("culinary_magic")
                .background(ThaumaturgesDelight.fd("textures/block/canvas_rug.png"))
                .formula(TTAspects.AURAM)
                .formula(TTAspects.VITREUS, 20)
                .formula(TTAspects.AER)
                .formula(TTAspects.IGNIS)
                .formula(TTAspects.HERBA)
                .index(7)
                .requiredResearch("thaumaturgesdelight:unlock_culinary_magic")
                .save();

        tdResearch("base_culinary_magic")
                .parent("thaumaturgesdelight:unlock_culinary_magic")
                .position(0, -2)
                .icon("thaumaturgesdelight:textures/research/cat_culinary_magic.png")
                .meta(ResearchEntryMeta.ROUND, ResearchEntryMeta.HIDDEN)

                .stage()
                .recipes("thaumaturgesdelight:brass_knife",
                        "thaumaturgesdelight:thaumium_knife")
                .end()

                .noteAspect(TTAspects.AER)
                .noteAspect(TTAspects.IGNIS)
                .noteAspect(TTAspects.HERBA)
                .complexity(1)
                .save();

        tdResearch(TDItems.AURA_RICH_SOIL)
                .parent("thaumaturgesdelight:base_culinary_magic")
                .position(0, -4)

                .stage()
                .craft(ModItems.RICH_SOIL.get())
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .recipes("thaumaturgesdelight:dust_trigger/aura_rich_soil",
                        "thaumaturgesdelight:dust_trigger/aura_rich_soil_farmland")
                .end()

                .noteAspect(TTAspects.TERRA)
                .noteAspect(TTAspects.HERBA)
                .noteAspect(TTAspects.AURAM)
                .complexity(1)
                .save();

        tdResearch(TDItems.ESSENTIA_ROCK_CANDY)
                .parent("thaumaturgesdelight:magic_kitchen")
                .position(-3, -1)

                .stage()
                .obtain(TTItems.MANA_BEAN)
                .obtain(TTItems.ESSENTIA_CRYSTAL, 1, aspect(TTAspects.VITREUS))
                .obtain(TTItems.ESSENTIA_CRYSTAL, 1, aspect(TTAspects.GELUM))
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .recipe("thaumaturgesdelight:crucible/essentia_rock_candy/aer")
                .end()

                .noteAspect(TTAspects.AER)
                .noteAspect(TTAspects.IGNIS)
                .noteAspect(TTAspects.AQUA)
                .noteAspect(TTAspects.TERRA)
                .noteAspect(TTAspects.ORDO)
                .noteAspect(TTAspects.PERDITIO)
                .complexity(1)
                .save();

        tdResearch(TDItems.TAINTED_MILK_BUCKET)
                .parent("thaumaturgesdelight:magic_kitchen")
                .position(3, -1)

                .stage()
                .obtain(Items.MILK_BUCKET)
                .requiredKnowledge(KnowledgeType.OBSERVATION, TTResearchCategories.BASICS)
                .end()

                .stage()
                .end()

                .noteAspect(TTAspects.VITIUM)
                .noteAspect(TTAspects.ORDO)
                .complexity(1)
                .save();

        tdResearch(TDItems.MANA_BEAN_DOUGH)
                .parent("thaumaturgesdelight:magic_kitchen")
                .position(-1, 3)

                .stage()
                .obtain(TTItems.MANA_BEAN)
                .obtain(TTItems.MANA_BEAN)
                .obtain(TTItems.MANA_BEAN)
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
//                .recipe("thaumaturgesdelight:mana_bean_dough")
//                .recipe("thaumaturgesdelight:mana_bean_dough_form_egg")
                .end()

                .stage()
//                .recipe("thaumaturgesdelight:mana_bean_dough")
//                .recipe("thaumaturgesdelight:mana_bean_dough_form_egg")
//                .recipe("thaumaturgesdelight:smelting/mana_bread/aer")
                .end()

                .noteAspect(TTAspects.AER)
                .noteAspect(TTAspects.IGNIS)
                .noteAspect(TTAspects.AQUA)
                .noteAspect(TTAspects.TERRA)
                .noteAspect(TTAspects.ORDO)
                .noteAspect(TTAspects.PERDITIO)
                .complexity(2)
                .save();

        tdResearch(TDItems.ETHEREAL_PETAL)
                .parent("thaumaturgesdelight:magic_kitchen")
                .position(1, 3)

                .stage()
                .obtain(TTItems.ETHEREAL_BLOOM)
                .requiredKnowledge(KnowledgeType.OBSERVATION, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .recipe("thaumaturgesdelight:ethereal_petal")
                .end()

                .noteAspect(TTAspects.AURAM)
                .noteAspect(TTAspects.VITIUM)
                .complexity(1)
                .save();

        tdResearch("magic_kitchen")
                .parent("thaumaturgesdelight:base_culinary_magic")
                .parent("thaumaturge:essentia_smelter")
                .parent("thaumaturge:thaumatorium")
                .position(0, 0)
                .icon(TDItems.ARCANE_STOVE)
                .meta(ResearchEntryMeta.SPIKY)

                .stage()
                .obtain(ModItems.COOKING_POT.get())
                .obtain(ModItems.STOVE.get())
                .obtain(TTItems.SMELTER_BASIC)
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.ALCHEMY)
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .recipe("thaumaturgesdelight:dust_trigger/magic_kitchen")
                .end()

                .noteAspect(TTAspects.INSTRUMENTUM)
                .noteAspect(TTAspects.AER)
                .noteAspect(TTAspects.IGNIS)
                .noteAspect(TTAspects.HERBA)
                .complexity(2)
                .save();

        tdResearch(TDItems.ELEMENTAL_KNIFE)
                .parent("thaumaturgesdelight:magic_kitchen")
                .parent("thaumaturge:elemental_tools")
                .parent("thaumaturge:infusion_enchantment")
                .position(-2, -2)

                .stage()
                .craft(TDItems.THAUMIUM_KNIFE)
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.INFUSION)
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .recipes("thaumaturgesdelight:infusion/elemental_knife",
                        "thaumaturge:infusion_enchantment/bleeding_edge")
                .end()

                .noteAspect(TTAspects.INSTRUMENTUM)
                .noteAspect(TTAspects.AER)
                .noteAspect(TTAspects.IGNIS)
                .noteAspect(TTAspects.HERBA)
                .complexity(2)
                .save();

        tdResearch(TDItems.CUTTING_SEAL)
                .parent("thaumaturgesdelight:magic_kitchen")
                .parent("thaumaturge:seal_harvest")
                .parent("thaumaturge:seal_use")
                .position(2, -2)

                .stage()
                .obtain(ModItems.CUTTING_BOARD.get())
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.GOLEMANCY)
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .recipe("thaumaturgesdelight:infusion/seal_cutting")
                .end()

                .addendum()
                .recipe("thaumaturgesdelight:infusion/seal_advanced_cutting")
                .requiredResearch("thaumaturge:mind_biothaumic")
                .end()

                .noteAspect(TTAspects.HERBA, 3)
                .noteAspect(TTAspects.INSTRUMENTUM, 3)
                .noteAspect(TTAspects.MACHINA, 2)
                .complexity(1)
                .save();

        tdResearch(TDItems.KETCHUP_CAP)
                .parent("thaumaturgesdelight:magic_kitchen")
                .position(2, 2)

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.AUROMANCY)
                .end()

                .stage()
                .recipe("thaumaturgesdelight:infusion/ketchup_wand_cap")
                .end()

                .noteAspect(TTAspects.VITREUS)
                .noteAspect(TTAspects.HERBA)
                .complexity(2)
                .save();

        tdResearch(TDItems.FRENCH_FRY_ROD)
                .parent("thaumaturgesdelight:magic_kitchen")
                .position(-2, 2)

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .requiredKnowledge(KnowledgeType.THEORY, TTResearchCategories.AUROMANCY)
                .end()

                .stage()
                .recipe("thaumaturgesdelight:infusion/french_fry_wand_rod")
                .end()

                .noteAspect(TTAspects.VITREUS)
                .noteAspect(TTAspects.HERBA)
                .complexity(2)
                .save();

        tdResearch(TDItems.SEXTUPLE_MEAT_SURPRISE)
                .parent("thaumaturgesdelight:magic_kitchen")
                .position(-3, 1)

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .recipe("thaumaturgesdelight:arcane_cooking/sextuple_meat_surprise")
                .end()

                .noteAspect(TTAspects.PERMUTATIO)
                .noteAspect(TTAspects.VITREUS)
                .complexity(1)
                .save();

        tdResearch(TDItems.DROWNEDS_TEA)
                .parent("thaumaturgesdelight:magic_kitchen")
                .position(3, 1)

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .recipe("thaumaturgesdelight:arcane_cooking/drowneds_tea")
                .end()

                .noteAspect(TTAspects.AQUA)
                .noteAspect(TTAspects.VITREUS)
                .complexity(1)
                .save();
    }

    private ResearchEntryBuilder tdResearch(ItemLike item) {
        return tdResearch(LazyIdentifier.getItemPath(item))
                .icon(item);
    }

    private ResearchEntryBuilder tdResearch(String id) {
        return context.entry(TDResearchCategories.CULINARY_MAGIC, id);
    }
}
