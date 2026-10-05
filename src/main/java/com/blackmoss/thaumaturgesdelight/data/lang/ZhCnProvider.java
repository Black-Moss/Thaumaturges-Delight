package com.blackmoss.thaumaturgesdelight.data.lang;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
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
        add("container.thaumaturgesdelight.arcane_cooking_pot", "奥术厨锅");
        add("container.thaumaturgesdelight.arcane_cooking_pot.heated", "已加热");
        add("container.thaumaturgesdelight.arcane_cooking_pot.not_heated", "需要下方热源");
        add("container.thaumaturgesdelight.arcane_cooking_pot.served_on", "盛放于：%s");

        add("recipe.type.arcane_cooking", "奥术烹饪");

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

        addResearch("magic_kitchen", "魔法厨房",
                "厨锅相较来说太过于脆弱了，根本不能承受源质，也就是说我没法用源质做菜！除了升级厨锅，我还得升级炉灶，不然我没法往锅里面注入源质，参考源质冶炼厂的原理做一个就行。",
                "给炉灶和厨锅撒上一些世界盐，我就成功得以让他们得以引导和使用源质了。" +
                        "<BR>不同于坩埚，奥术厨锅并不能把事物融化成源质，它只能将你的食材和源质混合在一起煮成一道菜；与此同时，奥术炉灶会在做菜时产生一定的吸力，将需要的源质吸到厨锅中进行烹饪。" +
                        "<BR>当然这个Pro版厨锅也支持普通厨锅的配方！");

        addResearch("essentia_rock_candy", "源质冰糖",
                "吃过魔豆后我就觉得这东西真的上瘾，但是很明显的问题就是魔豆的效果太随机了，如果去掉魔豆的形态直接食用源质是不是更好？不过源质这种东西应该冰起来才能吃吧，那就从冰冻下手。",
                "隆重介绍：源质冰糖！这东西只会给你随机的正面或中性效果，绝对不会给你带来灾难！<BR>将糖泡入你的（食品级）坩埚中即可获得，只需要5点该要素、1点寒冰和1点水晶！");

        add("enchantment.thaumaturge.cook_ding", "庖丁");

        addBlock(TDBlocks.ARCANE_COOKING_POT, "奥术厨锅");
        addBlock(TDBlocks.ARCANE_STOVE, "奥术炉灶");

        addItem(TDItems.BRASS_KNIFE, "黄铜刀");
        addItem(TDItems.THAUMIUM_KNIFE, "神秘刀");
        addItem(TDItems.VOID_KNIFE, "虚空刀");
        addItem(TDItems.ELEMENTAL_KNIFE, "庖丁刀");

        addItem(TDItems.ESSENTIA_ROCK_CANDY, "%s 源质冰糖");
        add("item.thaumaturgesdelight.essentia_rock_candy.unknown", "未知源质冰糖");

        addItem(TDItems.SIXFOLD_MEAT_TREAT, "六层肉饼");
        addItem(TDItems.PURIFY_COOKIE, "净化曲奇");
    }

    private void addResearch(String researchId, String title, String... stage) {
        add("research.%s.%s.title".formatted(ThaumaturgesDelight.MODID, researchId), title);
        for (int i = 0; i < stage.length; i++) {
            add("research.%s.%s.stage_%d".formatted(ThaumaturgesDelight.MODID, researchId, i), stage[i]);
        }
    }
}
