package com.blackmoss.thaumaturgesdelight.data.tag;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDItemTags;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.registry.TTItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public final class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ThaumaturgesDelight.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(CommonTags.Items.TOOLS_KNIFE)
                .add(TDItems.BRASS_KNIFE.get())
                .add(TDItems.THAUMIUM_KNIFE.get())
                .add(TDItems.VOID_KNIFE.get())
                .add(TDItems.ELEMENTAL_KNIFE.get());
        tag(ModTags.Items.KNIVES)
                .add(TDItems.BRASS_KNIFE.get())
                .add(TDItems.THAUMIUM_KNIFE.get())
                .add(TDItems.VOID_KNIFE.get())
                .add(TDItems.ELEMENTAL_KNIFE.get());
        tag(Tags.Items.FOODS_COOKIE)
                .add(TDItems.PURIFY_COOKIE.get());

        tag(TDItemTags.FOODS_DOUGH_MANA_BEAN)
                .add(TDItems.MANA_BEAN_DOUGH.get());

        tag(TTItemTags.WAND_CAPS)
                .add(TDItems.KETCHUP_CAP.get());
        tag(TTItemTags.WAND_RODS)
                .add(TDItems.FRENCH_FRY_ROD.get());

        tag(TDItemTags.FOODS_BREAD_MANA)
                .add(TDItems.MANA_BREAD.get());
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge's Delight Item Tags";
    }
}
