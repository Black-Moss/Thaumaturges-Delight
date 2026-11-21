package com.blackmoss.thaumaturgesdelight.compat.jei;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.List;

public class DoughDisplayRecipe extends ShapelessRecipe {
    private final List<List<ItemStack>> jeiInputs;
    private final ItemStack jeiOutput;

    public DoughDisplayRecipe(
            Recipe.CommonInfo info,
            CraftingRecipe.CraftingBookInfo bookInfo,
            ItemStackTemplate result, List<Ingredient> ingredients,
            List<List<ItemStack>> jeiInputs,
            ItemStack jeiOutput) {
        super(info, bookInfo, result, ingredients);
        this.jeiInputs = jeiInputs;
        this.jeiOutput = jeiOutput;
    }

    public List<List<ItemStack>> jeiInputs() {
        return this.jeiInputs;
    }

    public ItemStack jeiOutput() {
        return this.jeiOutput;
    }
}
