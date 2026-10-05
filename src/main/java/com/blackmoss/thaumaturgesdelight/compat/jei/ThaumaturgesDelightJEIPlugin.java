package com.blackmoss.thaumaturgesdelight.compat.jei;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.client.ArcaneCookingPotScreen;
import com.blackmoss.thaumaturgesdelight.client.TDClientRecipes;
import com.blackmoss.thaumaturgesdelight.compat.jei.category.ArcaneCookingRecipeCategory;
import com.blackmoss.thaumaturgesdelight.menu.ArcaneCookingPotMenu;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.blackmoss.thaumaturgesdelight.registry.TDMenus;
import com.blackmoss.thaumaturgesdelight.registry.TDRecipeTypes;
import com.leclowndu93150.thaumaturge.registry.TCDataComponents;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

@JeiPlugin
public class ThaumaturgesDelightJEIPlugin implements IModPlugin {
    private static final Identifier PLUGIN_UID = ThaumaturgesDelight.identifier("jei_plugin");

    public ThaumaturgesDelightJEIPlugin() {
    }

    @Override
    public @NonNull Identifier getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(@NonNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new ArcaneCookingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NonNull IRecipeRegistration registration) {
        registration.addRecipes(ArcaneCookingRecipeCategory.RECIPE_TYPE, TDClientRecipes.byType(TDRecipeTypes.ARCANE_COOKING.get()));
    }

    @Override
    public void registerRecipeCatalysts(@NonNull IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(ArcaneCookingRecipeCategory.RECIPE_TYPE, TDItems.ARCANE_COOKING_POT.get());
        registration.addCraftingStation(FDRecipeTypes.COOKING, TDItems.ARCANE_COOKING_POT.get());
    }

    @Override
    public void registerItemSubtypes(@NonNull ISubtypeRegistration registration) {
        registration.registerFromDataComponentTypes(TDItems.ESSENTIA_ROCK_CANDY.get(), TCDataComponents.CRYSTAL_ASPECT.get());
    }

    @Override
    public void registerGuiHandlers(@NonNull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ArcaneCookingPotScreen.class, 89, 25, 24, 17, ArcaneCookingRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(@NonNull IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ArcaneCookingPotMenu.class, TDMenus.ARCANE_COOKING_POT.get(), ArcaneCookingRecipeCategory.RECIPE_TYPE, 0, 6, 9, 36);
    }
}
