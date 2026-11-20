package com.blackmoss.thaumaturgesdelight.compat.farmersdelight;

import com.blackmoss.thaumaturgesdelight.recipe.ArcaneCookingPotRecipe;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspectIndex;
import com.leclowndu93150.thaumaturge.api.aspect.IAspectRecipeContributor;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class AspectContributor implements IAspectRecipeContributor {
    private static final float INGREDIENT_SHARE = 0.75F;
    private static final float ROUNDED_UP_FROM = 0.75F;
    private static final float ROUNDED_UP_BELOW = 1.0F;
    private static final int MIN_OUTPUT_COUNT = 1;

    private Map<Item, List<Candidate>> candidates = Map.of();

    private record Candidate(List<Ingredient> ingredients, AspectList cost, int count) {
    }

    @Override
    public void beginBuild(RecipeManager recipes, HolderLookup.Provider registries) {
        Objects.requireNonNull(recipes, "recipes");
        Objects.requireNonNull(registries, "registries");
        Map<Item, List<Candidate>> map = new HashMap<>();
        for (RecipeHolder<?> holder : recipes.getRecipes()) {
            if (holder.value() instanceof CookingPotRecipe cooking) {
                add(map, cooking.getIngredients(), AspectList.EMPTY, cooking.getResultItem());
            } else if (holder.value() instanceof ArcaneCookingPotRecipe arcane) {
                add(map, arcane.getIngredients(), arcane.aspects(), arcane.resultTemplate().create());
            } else if (holder.value() instanceof CuttingBoardRecipe cutting) {
                for (ChanceResult result : cutting.getRollableResults()) {
                    add(map, cutting.getIngredients(), AspectList.EMPTY, result.stack());
                }
            }
        }
        candidates = map;
    }

    @Override
    public Optional<AspectList> derive(Item item, RecipeManager recipes, HolderLookup.Provider registries, IAspectIndex partial) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(partial, "partial");
        List<Candidate> pool = candidates.get(item);
        if (pool == null || pool.isEmpty()) {
            return Optional.empty();
        }
        return pool.stream()
                .map(candidate -> aspectsOf(item, candidate, partial))
                .filter(aspects -> !aspects.isEmpty())
                .min(Comparator.comparingInt(AspectList::totalAmount));
    }

    private static void add(Map<Item, List<Candidate>> map, List<Ingredient> ingredients, AspectList cost, ItemStack output) {
        if (output.isEmpty()) {
            return;
        }
        Candidate candidate = new Candidate(List.copyOf(ingredients), cost, Math.max(MIN_OUTPUT_COUNT, output.getCount()));
        map.computeIfAbsent(output.getItem(), ignored -> new ArrayList<>()).add(candidate);
    }

    private static AspectList aspectsOf(Item item, Candidate candidate, IAspectIndex partial) {
        AspectList totals = AspectList.EMPTY;
        for (Ingredient ingredient : candidate.ingredients) {
            ItemStack representative = representative(ingredient);
            if (!representative.isEmpty() && !representative.is(item)) {
                totals = totals.add(partial.of(representative));
            }
        }
        return scale(totals, candidate.count).add(drain(candidate.cost, candidate.count));
    }

    private static ItemStack representative(Ingredient ingredient) {
        //noinspection deprecation
        return ingredient.items().findFirst().map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    private static AspectList scale(AspectList totals, int outputCount) {
        int count = Math.max(MIN_OUTPUT_COUNT, outputCount);
        AspectList result = AspectList.EMPTY;
        for (AspectInstance entry : totals.entries()) {
            float value = entry.amount() * INGREDIENT_SHARE / count;
            int amount = value >= ROUNDED_UP_FROM && value < ROUNDED_UP_BELOW ? 1 : (int) value;
            if (amount > 0) {
                result = result.add(entry.aspect(), amount);
            }
        }
        return result;
    }

    private static AspectList drain(AspectList cost, int outputCount) {
        int count = Math.max(MIN_OUTPUT_COUNT, outputCount);
        AspectList result = AspectList.EMPTY;
        for (AspectInstance entry : cost.entries()) {
            int amount = (int) (Math.sqrt(entry.amount()) / count);
            if (amount > 0) {
                result = result.add(entry.aspect(), amount);
            }
        }
        return result;
    }
}
