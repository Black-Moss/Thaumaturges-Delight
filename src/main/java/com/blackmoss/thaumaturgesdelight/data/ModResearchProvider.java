package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.lazycat.thaumaturge.research.LazyCatResearchProvider;
import com.blackmoss.lazycat.thaumaturge.research.ResearchEntryBuilder;
import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.blackmoss.thaumaturgesdelight.registry.TDResearchCategories;
import com.google.gson.JsonObject;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeType;
import com.leclowndu93150.thaumaturge.api.research.ResearchEntryMeta;
import com.leclowndu93150.thaumaturge.api.research.TTResearchCategories;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import net.minecraft.data.PackOutput;
import vectorwing.farmersdelight.common.registry.ModItems;

public final class ModResearchProvider extends LazyCatResearchProvider {
    public ModResearchProvider(PackOutput output) {
        super(output, ThaumaturgesDelight.MODID);
    }

    private static JsonObject crystalAspect(String aspect) {
        JsonObject component = new JsonObject();
        JsonObject value = new JsonObject();
        value.addProperty("aspect", aspect);
        component.add("thaumaturge:crystal_aspect", value);
        return component;
    }

    @Override
    protected void addResearch() {
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

        tdResearch("aura_rich_soil")
                .parent("thaumaturgesdelight:base_culinary_magic")
                .parent("thaumaturge:elemental_tools")
                .parent("thaumaturge:infusion_enchantment")
                .position(-3, -3)
                .icon(TDItems.AURA_RICH_SOIL)

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

        tdResearch("elemental_knife")
                .parent("thaumaturgesdelight:base_culinary_magic")
                .parent("thaumaturge:elemental_tools")
                .parent("thaumaturge:infusion_enchantment")
                .position(-2, 0)
                .icon(TDItems.ELEMENTAL_KNIFE)

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

        tdResearch("essentia_rock_candy")
                .parent("thaumaturgesdelight:base_culinary_magic")
                .position(3, -3)
                .icon(TDItems.ESSENTIA_ROCK_CANDY)

                .stage()
                .obtain(TTItems.MANA_BEAN)
                .obtain(TTItems.ESSENTIA_CRYSTAL, 1, crystalAspect("thaumaturge:vitreus"))
                .obtain(TTItems.ESSENTIA_CRYSTAL, 1, crystalAspect("thaumaturge:gelum"))
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .recipe("thaumaturgesdelight:crucible/essentia_rock_candy/aer")
                .end()

                .noteAspect(TTAspects.AER)
                .noteAspect(TTAspects.IGNIS)
                .noteAspect(TTAspects.HERBA)
                .complexity(1)
                .save();

        tdResearch("magic_kitchen")
                .parent("thaumaturgesdelight:base_culinary_magic")
                .parent("thaumaturge:essentia_smelter")
                .parent("thaumaturge:thaumatorium")
                .position(0, 0)
                .icon(TDItems.ARCANE_STOVE)

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

        tdResearch("cutting_seal")
                .parent("thaumaturgesdelight:base_culinary_magic")
                .parent("thaumaturge:seal_harvest")
                .parent("thaumaturge:seal_use")
                .position(2, 0)
                .icon(TDItems.CUTTING_SEAL)

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

        context.entry(TTResearchCategories.ARTIFICE, "unlock_culinary_magic")
                .parent("thaumaturge:infernal_furnace")
                .position(5, -1)
                .icon("thaumaturgesdelight:textures/research/cat_culinary_magic.png")
                .meta(ResearchEntryMeta.ROUND, ResearchEntryMeta.SPIKY, ResearchEntryMeta.HIDDEN)

                .stage()
                .requiredKnowledge(KnowledgeType.THEORY, TDResearchCategories.CULINARY_MAGIC)
                .end()

                .stage()
                .end()

                .noteAspect(TTAspects.AER)
                .noteAspect(TTAspects.IGNIS)
                .noteAspect(TTAspects.HERBA)
                .complexity(1)
                .save();
    }

    private ResearchEntryBuilder tdResearch(String id) {
        return context.entry(TDResearchCategories.CULINARY_MAGIC, id);
    }
}
