package com.blackmoss.thaumaturgesdelight.data.lang;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
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
        add("research_category.thaumaturgesdelight.culinary_magic", "Culinary Magic");


        addResearch("unlock_culinary_magic", "Unlock: Culinary Magic",
                "Although as a thaumaturge envoy, I know all kinds of secrets that ordinary people don't know, what I eat is very monotonous and not at all as good as those mortals. I think it's time for me to break through in this regard...",
                "I think there are also various magic hidden in food, which ordinary people don't know.");

        addResearch("base_culinary_magic", "Culinary Magic",
                "Only when you are full can you have the strength to do research!");

        addResearch("elemental_knife", "Elemental Knife",
                "Ever since I succeeded in infusing primal elements into tools, I have wondered whether one could be infused into a knife as well — absurd as that may sound...<BR>As ever, let a thaumium tool be the first to go under the knife.",
                "If my calculations hold — and I have not simply eaten myself into foolishness — this Knife of Butcher yields a few extra scraps of meat whenever it fells an animal. Nowhere near as grand as its siblings, perhaps, but a little more on the plate is still a little more, is it not?<BR>" +
                        "And of course, an elemental property such as this could never belong to that one knife alone:" +
                        "<PAGE>§oCook Ding§r" +
                        "<DIV>When you kill an animal with a tool enchanted with this, it has a chance of dropping a few additional chunks of meat. Increasing the rank of this enchantment improves the chance." +
                        "<DIV>§oRanks§r: 1-3" +
                        "<BR>§oTarget§r: Knife");

        addResearch("essentia_rock_candy", "Essentia Rock Candy",
                "After eating my first mana bean I found myself properly addicted — though the flaw is only too obvious: the bean's effect is far too random. Would it not be better to consume the essentia directly, without carrying a bean around? Essentia presumably has to be frozen before it will keep, though... so let us start with freezing.",
                "Introducing: Essentia Rock Candy! It grants a random beneficial or neutral effect — and never, ever a disaster!<BR>Simply dissolve one in your (food-grade) crucible; all it takes is 5 of the aspect you fancy, 1 Gelum and 1 Vitreous!");

        add("enchantment.thaumaturge.cook_ding", "Cook Ding");

        addItem(TDItems.BRASS_KNIFE, "Brass Knife");
        addItem(TDItems.THAUMIUM_KNIFE, "Thaumium Knife");
        addItem(TDItems.VOID_KNIFE, "Void Knife");
        addItem(TDItems.ELEMENTAL_KNIFE, "Knife of Butcher");

        addItem(TDItems.ESSENTIA_ROCK_CANDY, "%s Essentia Rock Candy");
        add("item.thaumaturgesdelight.essentia_rock_candy.unknown", "Unknown Essentia Rock Candy");

        addItem(TDItems.SIXFOLD_MEAT_TREAT, "Sixfold Meat Treat");
    }

    private void addResearch(String researchId, String title, String... stage) {
        add("research.%s.%s.title".formatted(ThaumaturgesDelight.MODID, researchId), title);
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(ThaumaturgesDelight.MODID, researchId, i), stage[i]);
        }
    }
}