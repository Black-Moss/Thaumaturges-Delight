package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.registry.TCItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ModRecipeProvider extends RecipeProvider {
    private ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        knife(TDItems.BRASS_KNIFE, TCItems.INGOT_BRASS, TCItems.NUGGET_BRASS);
        knife(TDItems.THAUMIUM_KNIFE, TCItems.INGOT_THAUMIUM, TCItems.NUGGET_THAUMIUM);
    }

    public void knife(ItemLike knife, ItemLike material, ItemLike materialNugget) {
        shaped(RecipeCategory.TOOLS, knife)
                .pattern("M")
                .pattern("S")
                .define('M', material)
                .define('S', Tags.Items.RODS_WOODEN)
                .unlockedBy("has", has(knife))
                .save(output);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(knife), RecipeCategory.MISC, CookingBookCategory.MISC, materialNugget, 0.1F, 200);
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(knife), RecipeCategory.MISC, CookingBookCategory.MISC, materialNugget, 0.1F, 100);
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput output) {
            return new ModRecipeProvider(provider, output);
        }

        @Override
        public @NonNull String getName() {
            return "Thaumaturge's Delight Recipes";
        }
    }
}
