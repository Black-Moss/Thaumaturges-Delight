package com.blackmoss.thaumaturgesdelight.recipe;

import com.google.common.base.Preconditions;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.data.recipe.builders.SimpleRecipeBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArcaneCookingPotRecipeBuilder extends SimpleRecipeBuilder {
    private static final String RECIPE_FOLDER = "arcane_cooking/";

    private final List<Ingredient> ingredients = new ArrayList<>();
    private final HolderGetter<IAspect> aspectsGetter;
    private @Nullable ItemStackTemplate container;
    private int cookTime = ArcaneCookingPotRecipe.DEFAULT_COOK_TIME;
    private float experience = ArcaneCookingPotRecipe.DEFAULT_EXPERIENCE;
    private CookingPotRecipeBookTab tab = CookingPotRecipeBookTab.MISC;
    private AspectList aspects;

    public ArcaneCookingPotRecipeBuilder(HolderGetter<IAspect> aspectsGetter, RecipeCategory category, ItemStackTemplate result) {
        super(result, category);
        this.aspects = AspectList.EMPTY;
        this.aspectsGetter = aspectsGetter;
    }

    public ArcaneCookingPotRecipeBuilder ingredient(Ingredient ingredient) {
        return this.ingredient(ingredient, 1);
    }

    public ArcaneCookingPotRecipeBuilder ingredient(Ingredient ingredient, int amount) {
        Preconditions.checkNotNull(ingredient, "The ingredient must not be null !");
        Preconditions.checkArgument(amount > 0, "The amount of ingredient must be positive !");
        for (int i = 0; i < amount; i++) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    public ArcaneCookingPotRecipeBuilder ingredient(ItemLike item) {
        return this.ingredient(Ingredient.of(item), 1);
    }

    public ArcaneCookingPotRecipeBuilder ingredient(ItemLike item, int amount) {
        return this.ingredient(Ingredient.of(item), amount);
    }

    public ArcaneCookingPotRecipeBuilder ingredient(HolderSet<Item> tag) {
        return this.ingredient(Ingredient.of(tag), 1);
    }

    public ArcaneCookingPotRecipeBuilder container(ItemLike container) {
        this.container = new ItemStackTemplate(container.asItem());
        return this;
    }

    public ArcaneCookingPotRecipeBuilder tab(CookingPotRecipeBookTab tab) {
        Preconditions.checkNotNull(tab, "The recipe book tab must not be null !");
        this.tab = tab;
        return this;
    }

    public ArcaneCookingPotRecipeBuilder cookTime(int cookTime) {
        Preconditions.checkArgument(cookTime > 0, "The cooking time must be positive !");
        this.cookTime = cookTime;
        return this;
    }

    public ArcaneCookingPotRecipeBuilder experience(float experience) {
        Preconditions.checkArgument(experience >= 0.0F, "The experience must not be negative !");
        this.experience = experience;
        return this;
    }

    public ArcaneCookingPotRecipeBuilder aspect(ResourceKey<IAspect> aspect, int amount) {
        Preconditions.checkNotNull(aspect, "The aspect must not be null !");
        Preconditions.checkArgument(amount > 0, "The amount of aspect must be positive !");
        this.aspects = this.aspects.add(new AspectInstance(this.aspectsGetter.getOrThrow(aspect), amount));
        return this;
    }

    public ArcaneCookingPotRecipeBuilder aspects(AspectList aspects) {
        Preconditions.checkNotNull(aspects, "The aspects must not be null !");
        this.aspects = aspects;
        return this;
    }

    @Override
    public void save(RecipeOutput output, @NonNull ResourceKey<Recipe<?>> key) {
        Preconditions.checkState(!this.ingredients.isEmpty(), "Arcane cooking recipe has no ingredients");
        Preconditions.checkState(this.ingredients.size() <= ArcaneCookingPotRecipe.INPUT_SLOTS, "Arcane cooking recipe has too many ingredients");
        ArcaneCookingPotRecipe recipe = new ArcaneCookingPotRecipe("", this.tab, this.ingredients, this.result, Optional.ofNullable(this.container), this.experience, this.cookTime, this.aspects);
        output.accept(key, recipe, this.advancementBuilder.build(output, key, this.category));
    }

    @Override
    public @NonNull ResourceKey<Recipe<?>> defaultId() {
        return ResourceKey.create(Registries.RECIPE, this.result.typeHolder().unwrapKey().orElseThrow().identifier().withPrefix(RECIPE_FOLDER));
    }
}
