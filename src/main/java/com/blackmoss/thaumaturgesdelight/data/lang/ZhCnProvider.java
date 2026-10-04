package com.blackmoss.thaumaturgesdelight.data.lang;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class ZhCnProvider extends LanguageProvider {
    public ZhCnProvider(PackOutput output) {
        super(output, ThaumaturgesDelight.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.thaumaturgesdelight", "神秘乐事");

        add("enchantment.thaumaturge.cook_ding", "庖丁");

        addItem(TDItems.BRASS_KNIFE, "黄铜刀");
        addItem(TDItems.THAUMIUM_KNIFE, "神秘刀");
        addItem(TDItems.VOID_KNIFE, "虚空刀");
        addItem(TDItems.ELEMENTAL_KNIFE, "庖丁刀");
    }
}
