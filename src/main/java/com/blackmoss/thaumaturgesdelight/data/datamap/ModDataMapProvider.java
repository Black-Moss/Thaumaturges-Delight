package com.blackmoss.thaumaturgesdelight.data.datamap;

import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.datamap.LazyCatDataMapProvider;
import com.blackmoss.lazycat.datamap.thaumaturge.BaseAspectBuilder;
import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDAspects;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public final class ModDataMapProvider extends LazyCatDataMapProvider {
    public ModDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ThaumaturgesDelight.MODID);
    }

    @Override
    protected void buildDataMaps(DataMapContext context) {
        new BaseAspectBuilder(context, Items.BREAD)
                .aspect(TDAspects.CUPPEDIA, 1)
                .save();
        new BaseAspectBuilder(context, Tags.Items.FOODS_COOKED_MEAT)
                .aspect(TDAspects.CUPPEDIA, 1)
                .save();
        new BaseAspectBuilder(context, ModTags.Items.SWEETS)
                .aspect(TDAspects.CUPPEDIA, 2)
                .save();
        new BaseAspectBuilder(context, ModItems.CANVAS.get())
                .aspect(TTAspects.HERBA, 2)
                .save();
        new BaseAspectBuilder(context, CommonTags.Items.FOODS_CABBAGE)
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.VICTUS)
                .save();
        new BaseAspectBuilder(context, CommonTags.Items.FOODS_TOMATO)
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.VICTUS)
                .save();
        new BaseAspectBuilder(context, CommonTags.Items.FOODS_ONION)
                .aspect(TTAspects.HERBA)
                .aspect(TTAspects.VICTUS)
                .aspect(TTAspects.ALKIMIA, 1)
                .save();
        new BaseAspectBuilder(context, ModItems.ROPE.get())
                .aspect(TTAspects.INSTRUMENTUM, 3)
                .aspect(TTAspects.AVERSIO, 2)
                .save();
    }
}
