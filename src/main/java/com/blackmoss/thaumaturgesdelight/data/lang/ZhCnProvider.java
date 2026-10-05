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

        addResearch("base_culinary_magic", "食品魔法",
                "吃饱了才有力气搞研究！");

        addResearch("elemental_knife", "元素刀",
                "自从将元始要素成功注入工具之后，我一直在想能不能把要素注入进刀里面，虽然这听起来有点荒谬……<BR>老样子，先用神秘工具“开刀”吧。",
                "如果计算没有失误，而且我没有吃饱了撑着的话，这把庖丁刀可以在击杀动物时额外掉落一些肉粒。虽然和它那些兄弟姐妹们比不上多高级，但是总归能多吃点，是吧？" +
                        "<BR>当然这项新的元素工具属性不可能是仅供庖丁刀的：" +
                        "<PAGE>§o庖丁§r" +
                        "<DIV>这项附魔工具所击杀的动物会额外掉落一些肉块。" +
                        "<DIV>§l附魔等级§r：3" +
                        "<DIV>§l附魔对象§r：刀");

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
