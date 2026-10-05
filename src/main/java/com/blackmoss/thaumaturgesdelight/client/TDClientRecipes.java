package com.blackmoss.thaumaturgesdelight.client;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import net.minecraft.world.item.crafting.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.List;

@EventBusSubscriber(modid = ThaumaturgesDelight.MODID, value = Dist.CLIENT)
public final class TDClientRecipes {
    private static RecipeMap recipeMap = RecipeMap.EMPTY;

    private TDClientRecipes() {
    }

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        recipeMap = event.getRecipeMap();
    }

    public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> byType(RecipeType<T> type) {
        return List.copyOf(recipeMap.byType(type));
    }
}
