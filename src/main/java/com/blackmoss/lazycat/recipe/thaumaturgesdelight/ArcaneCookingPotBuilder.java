package com.blackmoss.lazycat.recipe.thaumaturgesdelight;

import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.blackmoss.thaumaturgesdelight.recipe.ArcaneCookingPotRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeUnlockAdvancementBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public final class ArcaneCookingPotBuilder extends AbstractThaumaturgeRecipeBuilder<ArcaneCookingPotBuilder> {
    private final List<Object> ingredients = new ArrayList<>();
    private Float experience;
    private Integer time;
    private ItemLike container;
    private CookingPotRecipeBookTab recipeBookTab = CookingPotRecipeBookTab.MISC;

    public ArcaneCookingPotBuilder(RecipeContext context, Object result) {
        super(context, result);
    }

    @Override
    protected ArcaneCookingPotBuilder self() {
        return this;
    }

    @Override
    protected String defaultFolder() {
        return "arcane_cooking";
    }

    public ArcaneCookingPotBuilder exp(float experience) {
        this.experience = experience;
        return this;
    }

    public ArcaneCookingPotBuilder time(int time) {
        this.time = time;
        return this;
    }

    public ArcaneCookingPotBuilder ingredient(Object source) {
        return ingredient(source, 1);
    }

    public ArcaneCookingPotBuilder ingredient(Object source, int amount) {
        Objects.requireNonNull(source, "source");
        if (amount < 1) {
            throw new IllegalArgumentException("amount must be positive");
        }
        defaultUnlock(context().ingredient(source));
        for (int i = 0; i < amount; i++) {
            ingredients.add(source);
        }
        return this;
    }

    public ArcaneCookingPotBuilder container(ItemLike container) {
        this.container = Objects.requireNonNull(container, "container");
        return this;
    }

    public ArcaneCookingPotBuilder category(CookingPotRecipeBookTab tab) {
        this.recipeBookTab = Objects.requireNonNull(tab, "category");
        return this;
    }

    @Override
    protected void validate() {
        super.validate();
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("Arcane cooking recipe requires at least one ingredient");
        }
        if (ingredients.size() > ArcaneCookingPotRecipe.INPUT_SLOTS) {
            throw new IllegalStateException("Arcane cooking recipe has too many ingredients");
        }
        if (experience != null && experience < 0.0F) {
            throw new IllegalArgumentException("experience must not be negative");
        }
        if (time != null && time <= 0) {
            throw new IllegalArgumentException("time must be positive");
        }
        requireAspects();
    }

    @Override
    protected void build(RecipeOutput output, ResourceKey<Recipe<?>> key) {
        NonNullList<Ingredient> inputs = NonNullList.create();
        for (Object source : ingredients) {
            inputs.add(toIngredient(source));
        }
        Optional<ItemStackTemplate> containerTemplate = container == null
                ? Optional.empty()
                : Optional.of(new ItemStackTemplate(container.asItem()));
        ArcaneCookingPotRecipe recipe = new ArcaneCookingPotRecipe(
                group() != null ? group() : "",
                recipeBookTab,
                inputs,
                result(),
                containerTemplate,
                experience != null ? experience : ArcaneCookingPotRecipe.DEFAULT_EXPERIENCE,
                time != null ? time : ArcaneCookingPotRecipe.DEFAULT_COOK_TIME,
                buildAspects());
        RecipeUnlockAdvancementBuilder advancement = new RecipeUnlockAdvancementBuilder();
        applyCriteria(advancement::unlockedBy);
        output.accept(key, recipe, advancement.build(output, key, category()));
    }
}
