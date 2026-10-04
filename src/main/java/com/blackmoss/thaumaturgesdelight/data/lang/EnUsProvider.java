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
        addItem(TDItems.BRASS_KNIFE, "Brass Knife");
        addItem(TDItems.THAUMIUM_KNIFE, "Thaumium Knife");
        addItem(TDItems.VOID_KNIFE, "Void Knife");
        addItem(TDItems.ELEMENTAL_KNIFE, "xxx Knife");
    }
}
