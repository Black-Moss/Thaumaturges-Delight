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

        add("enchantment.thaumaturge.cook_ding", "Cook Ding");

        addItem(TDItems.BRASS_KNIFE, "Brass Knife");
        addItem(TDItems.THAUMIUM_KNIFE, "Thaumium Knife");
        addItem(TDItems.VOID_KNIFE, "Void Knife");
        addItem(TDItems.ELEMENTAL_KNIFE, "Knife of Butcher");
    }

    private void addResearch(String researchId, String title, String... stage) {
        add("research.%s.%s.title".formatted(ThaumaturgesDelight.MODID, researchId), title);
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(ThaumaturgesDelight.MODID, researchId, i), stage[i]);
        }
    }
}