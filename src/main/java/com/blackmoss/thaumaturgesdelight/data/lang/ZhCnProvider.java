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
        add("research_category.thaumaturgesdelight.culinary_magic", "食品魔法");

        addResearch("unlock_culinary_magic", "解锁：食品魔法",
                "虽然作为一名神秘使知道各种常人所不知的秘闻，但我吃的却很单调，一点都比不上那些凡人，我想我是时候在这一点上突破了……",
                "我认为食物中也会蕴藏各种魔法，这可是他们那些凡人所不知的。");

        add("enchantment.thaumaturge.cook_ding", "庖丁");

        addItem(TDItems.BRASS_KNIFE, "黄铜刀");
        addItem(TDItems.THAUMIUM_KNIFE, "神秘刀");
        addItem(TDItems.VOID_KNIFE, "虚空刀");
        addItem(TDItems.ELEMENTAL_KNIFE, "庖丁刀");

        addItem(TDItems.ESSENTIA_ROCK_CANDY, "%s 源质冰糖");
        add("item.thaumaturgesdelight.essentia_rock_candy.unknown", "未知源质冰糖");

        addItem(TDItems.SIXFOLD_MEAT_TREAT, "六层肉饼");
    }

    private void addResearch(String researchId, String title, String... stage) {
        add("research.%s.%s.title".formatted(ThaumaturgesDelight.MODID, researchId), title);
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(ThaumaturgesDelight.MODID, researchId, i), stage[i]);
        }
    }
}
