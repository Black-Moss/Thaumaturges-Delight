package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.recipe.ArcaneCookingPotRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = ThaumaturgesDelight.MODID)
public final class TDRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ThaumaturgesDelight.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ArcaneCookingPotRecipe>> ARCANE_COOKING = RECIPE_TYPES.register("arcane_cooking",
            () -> new RecipeType<ArcaneCookingPotRecipe>() {
                @Override
                public String toString() {
                    return "thaumaturgesdelight:arcane_cooking";
                }
            });

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ArcaneCookingPotRecipe>> ARCANE_COOKING_SERIALIZER = RECIPE_SERIALIZERS.register("arcane_cooking",
            () -> ArcaneCookingPotRecipe.SERIALIZER);

    private TDRecipeTypes() {
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(ARCANE_COOKING.get());
    }
}
