package com.blackmoss.thaumaturgesdelight.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jspecify.annotations.NonNull;

import java.util.List;

public final class DoughDisplayExtension implements ICraftingCategoryExtension<DoughDisplayRecipe> {
    public static final DoughDisplayExtension INSTANCE = new DoughDisplayExtension();

    private static final int GRID_WIDTH = 3;
    private static final int GRID_HEIGHT = 3;
    private static final int RECIPE_WIDTH = 2;

    @Override
    public @NonNull List<SlotDisplay> getIngredients(@NonNull RecipeHolder<DoughDisplayRecipe> holder) {
        if (holder.value().display().getFirst() instanceof ShapelessCraftingRecipeDisplay display) {
            return display.ingredients();
        }
        return List.of();
    }

    @Override
    public int getWidth(@NonNull RecipeHolder<DoughDisplayRecipe> holder) {
        return GRID_WIDTH;
    }

    @Override
    public int getHeight(@NonNull RecipeHolder<DoughDisplayRecipe> holder) {
        return GRID_HEIGHT;
    }

    @Override
    public void setRecipe(
            @NonNull RecipeHolder<DoughDisplayRecipe> holder,
            @NonNull IRecipeLayoutBuilder builder,
            @NonNull ICraftingGridHelper gridHelper,
            @NonNull IFocusGroup focuses) {
        DoughDisplayRecipe recipe = holder.value();
        int rows = Math.max(1, (recipe.jeiInputs().size() + RECIPE_WIDTH - 1) / RECIPE_WIDTH);
        gridHelper.createAndSetInputs(builder, recipe.jeiInputs(), RECIPE_WIDTH, rows);
        gridHelper.createAndSetOutputs(builder, List.of(recipe.jeiOutput()));
    }
}
