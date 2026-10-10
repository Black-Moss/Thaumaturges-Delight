package com.blackmoss.thaumaturgesdelight.data.lang;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class EnUsProvider extends LanguageProvider {
    public EnUsProvider(PackOutput output) {
        super(output, ThaumaturgesDelight.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.thaumaturgesdelight", "Thaumaturge's Delight");

        add("container.thaumaturgesdelight.arcane_cooking_pot", "Arcane Cooking Pot");
        add("container.thaumaturgesdelight.arcane_cooking_pot.heated", "Heated");
        add("container.thaumaturgesdelight.arcane_cooking_pot.not_heated", "Needs heat from below");
        add("container.thaumaturgesdelight.arcane_cooking_pot.served_on", "Served on: %s");

        add("recipe.type.arcane_cooking", "Arcane Cooking");

        add("research_category.thaumaturgesdelight.culinary_magic", "Culinary Magic");

        add("golem.arm.thaumaturgesdelight.knife", "Knife Arms");
        add("golem.arm.text.thaumaturgesdelight.knife",
                "These arms end in a pair of kitchen knives. The golem therefore always has a knife to hand and never needs to carry one — but its grip is taken up by the blades, so it cannot carry anything else.");

        add("aspect.thaumaturgesdelight.cuppedia", "Cuppedia");
        add("aspect.thaumaturgesdelight.cuppedia.desc", "Delicious, Tasty");
        add("aspect.thaumaturgesdelight.cuppedia.help", "Delicious food");

        addResearch("unlock_culinary_magic", "Unlock: Culinary Magic",
                "Despite learning much about the details of the world throughout my journey as a Thaumaturge," +
                        "<BR>I have noticed that my diet has yet to be effected by my efforts. I think it is about time that changes.",
                "As with most things, I expect that food is defined by magic, and can be improved by it.");

        addResearch("base_culinary_magic", "Culinary Magic",
                "You simply cannot do important research on an empty stomach!");

        addResearch("elemental_knife", "Elemental Knife",
                "Ever since I succeeded in infusing primal elements into tools, I have wondered whether one could be infused into a knife as well — absurd as that may sound..." +
                        "<BR>As ever, let a thaumium tool be the first to 'go under the knife.'",
                "If my calculations hold — and I have not simply eaten myself into foolishness — the Knife of the Butcher does not simply cut, It is the essence of bifurcation. The knife forces materials to split at the molecular level, allowing me to more effectively get meat from animals." +
                        "<BR>While nowhere near as grand as its siblings, not needing to go hunting as often is a suitable bonus." +
                        "Additionally, I have found that it is possible to apply a similar (yet less potent) enchantment to other knives." +
                        "<PAGE>§oBleeding Edge§r" +
                        "<DIV>When you kill an animal with a tool enchanted with Bleeding Edge, it has a chance of dropping a few additional chunks of meat. Increasing the rank of this enchantment improves the chance." +
                        "<DIV>§oRanks§r: 1-3" +
                        "<DIV>§oTarget§r: Knife");

        addResearch("magic_kitchen", "Magic Kitchen",
                "A standard cooking pot is far too fragile to contain raw essentia, it needs a more robust counterpart. — An upgraded stove to supply the essentia to the upgraded pot must also be created. The essentia smelter has already shown me the principle, so building a stove with similar properties should not be difficult.",
                "A sprinkling of Salis Mundus over both the stove and the pot should be enough to let them channel and make use of essentia." +
                        "<BR>Unlike a crucible, the Arcane Cooking Pot cannot break things down into essentia; all it does is combine your ingredients with essentia and simmer them into a single dish. The Arcane Stove, meanwhile, builds up a certain suction while the dish is cooking, drawing the essentia it needs up into the pot to take part in it." +
                        "<BR>And of course this Pro-grade pot can still handle any recipe an ordinary cooking pot does!");

        addResearch("seal_cutting", "Control Seal: Cutting",
                "Sadly, an ordinary Use seal is no good for preparing food. I should design a Control Seal made for the kitchen instead — and of course it ought to be cheaper than a plain Use seal.",
                "The Cutting seal exists to cut things on a cutting board, and it can only be placed on one. When a golem has a knife to hand and whatever sits on the board can be cut, it will set to work." +
                        "<BR>Note: this seal only cuts, it does not gather — whatever the golem chops will be flung every which way!" +
                        "<BR>§9This research also unlocks additional golem parts§0.");

        addResearchAddenda("seal_cutting",
                "The advanced version of the Cutting seal can be filtered: the first slot filters what is to be cut, the second filters which knife is used.");

        addResearch("aura_rich_soil", "Aura-Rich Soil",
                "Well, ordinary rich soil grows far too slowly — I'd have to wait a whole day just for a single dish. It seems I'll have to resort to magic...",
                "Sprinkle some Salis Mundus onto rich soil and the aura will seep in, becoming nourishment for your crops; each time a crop advances a stage, a point of aura is drained from the current chunk." +
                        "<BR>Don't plant too much of it — you're not here to grow Flux Rifts!");

        addResearch("essentia_rock_candy", "Essentia Rock Candy",
                "After eating my first mana bean I found myself properly addicted — though the flaw is only too obvious: the bean's effect is far too random. Would it not be better to consume the essentia directly, without carrying a bean around? Essentia presumably has to be frozen before it will keep, though... so let us start with freezing.",
                "Introducing: Essentia Rock Candy! It grants a random beneficial or neutral effect — and never, ever a disaster!" +
                        "<BR>Simply dissolve one in your (food-grade) crucible; all it takes is 5 of the aspect you fancy, 1 Gelum and 1 Vitreous!");

        add("enchantment.thaumaturge.bleeding_edge", "Bleeding Edge");

        addBlock(TDBlocks.ARCANE_COOKING_POT, "Arcane Cooking Pot");
        addBlock(TDBlocks.ARCANE_STOVE, "Arcane Stove");
        addBlock(TDBlocks.AURA_RICH_SOIL, "Aura-Rich Soil");
        addBlock(TDBlocks.AURA_RICH_SOIL_FARMLAND, "Aura-Rich Soil Farmland");

        addItem(TDItems.BRASS_KNIFE, "Brass Knife");
        addItem(TDItems.THAUMIUM_KNIFE, "Thaumium Knife");
        addItem(TDItems.VOID_KNIFE, "Void Knife");
        addItem(TDItems.ELEMENTAL_KNIFE, "Knife of the Butcher");

        addItem(TDItems.ESSENTIA_ROCK_CANDY, "%s Essentia Rock Candy");
        add("item.thaumaturgesdelight.essentia_rock_candy.unknown", "Unknown Essentia Rock Candy");

        addItem(TDItems.SEXTUPLE_MEAT_SURPRISE, "Sextuple Meat Surprise");
        addItem(TDItems.PURIFY_COOKIE, "Cookie of Purification");
        addItem(TDItems.SEAL_CUTTING, "Control Seal: Cutting");
        addItem(TDItems.SEAL_ADVANCED_CUTTING, "Advanced Control Seal: Cutting");
        addItem(TDItems.CHUNKS_FRIED_RICE, "Chunks Fried Rice");
    }

    private void addResearch(String researchId, String title, String... stage) {
        add("research.%s.%s.title".formatted(ThaumaturgesDelight.MODID, researchId), title);
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(ThaumaturgesDelight.MODID, researchId, i), stage[i]);
        }
    }

    private void addResearchAddenda(String researchId, String... stage) {
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.addendum_%d".formatted(ThaumaturgesDelight.MODID, researchId, i), stage[i]);
        }
    }
}
