package com.blackmoss.thaumaturgesdelight.recipe;

import com.blackmoss.thaumaturgesdelight.registry.TDRecipeTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.Optional;

public final class ArcaneCookingLookup {
    private ArcaneCookingLookup() {
    }

    public static Optional<RecipeHolder<CookingPotRecipe>> find(ServerLevel level, RecipeWrapper wrapper) {
        Optional<RecipeHolder<CookingPotRecipe>> vanilla = Holder.COOKING.getRecipeFor(wrapper, level);
        if (vanilla.isPresent()) {
            return vanilla;
        }
        return Holder.ARCANE.getRecipeFor(wrapper, level).map(holder -> new RecipeHolder<>(holder.id(), holder.value().asCookingPotRecipe()));
    }

    private static final class Holder {
        private static final RecipeManager.CachedCheck<RecipeWrapper, CookingPotRecipe> COOKING = RecipeManager.createCheck(ModRecipeTypes.COOKING.get());
        private static final RecipeManager.CachedCheck<RecipeWrapper, ArcaneCookingPotRecipe> ARCANE = RecipeManager.createCheck(TDRecipeTypes.ARCANE_COOKING.get());
    }
}
