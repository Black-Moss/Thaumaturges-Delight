package com.blackmoss.thaumaturgesdelight.client;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.block.ArcaneCookingPotBlockEntity;
import com.blackmoss.thaumaturgesdelight.menu.ArcaneCookingPotMenu;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.client.recipebook.RecipeCategories;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeBookCategories;

import java.util.List;
import java.util.Optional;

public class ArcaneCookingPotRecipeBookComponent extends RecipeBookComponent<ArcaneCookingPotMenu> {
    private static final WidgetSprites RECIPE_BOOK_BUTTONS = new WidgetSprites(
            ThaumaturgesDelight.fdIdentifier("recipe_book/cooking_pot_enabled"),
            ThaumaturgesDelight.fdIdentifier("recipe_book/cooking_pot_disabled"),
            ThaumaturgesDelight.fdIdentifier("recipe_book/cooking_pot_enabled_highlighted"),
            ThaumaturgesDelight.fdIdentifier("recipe_book/cooking_pot_disabled_highlighted"));

    private static final List<TabInfo> TABS = List.of(
            new TabInfo(new ItemStack(Items.COMPASS), Optional.empty(), RecipeCategories.COOKING_SEARCH),
            new TabInfo(ModItems.BEEF_STEW.get(), ModRecipeBookCategories.COOKING_MEALS.get()),
            new TabInfo(ModItems.APPLE_CIDER.get(), ModRecipeBookCategories.COOKING_DRINKS.get()),
            new TabInfo(ModItems.FRUIT_SALAD.get(), ModRecipeBookCategories.COOKING_MISC.get()));

    public ArcaneCookingPotRecipeBookComponent(ArcaneCookingPotMenu menu) {
        super(menu, TABS);
    }

    @Override
    protected @NonNull WidgetSprites getFilterButtonTextures() {
        return RECIPE_BOOK_BUTTONS;
    }

    public void hide() {
        setVisible(false);
    }

    @Override
    protected @NonNull Component getRecipeFilterName() {
        return Component.translatable("container.farmersdelight.recipe_book.cookable");
    }

    @Override
    protected boolean isCraftingSlot(@NonNull Slot slot) {
        return slot.index <= ArcaneCookingPotBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    protected void fillGhostRecipe(@NonNull GhostSlots ghost, @NonNull RecipeDisplay display, @NonNull ContextMap context) {
        ghost.setResult(menu.slots.get(ArcaneCookingPotBlockEntity.MEAL_DISPLAY_SLOT), context, display.result());
        if (display instanceof ShapelessCraftingRecipeDisplay shapeless) {
            int count = Math.min(shapeless.ingredients().size(), ArcaneCookingPotBlockEntity.INPUT_SLOTS);
            for (int i = 0; i < count; i++) {
                ghost.setInput(menu.slots.get(i), context, shapeless.ingredients().get(i));
            }
        }
    }

    @Override
    protected void selectMatchingRecipes(@NonNull RecipeCollection collection, @NonNull StackedItemContents contents) {
        collection.selectRecipes(contents, display -> display instanceof ShapelessCraftingRecipeDisplay shapeless && shapeless.ingredients().size() <= ArcaneCookingPotBlockEntity.INPUT_SLOTS);
    }
}
