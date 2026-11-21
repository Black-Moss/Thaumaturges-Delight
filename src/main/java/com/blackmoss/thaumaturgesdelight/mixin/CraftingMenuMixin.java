package com.blackmoss.thaumaturgesdelight.mixin;

import com.blackmoss.thaumaturgesdelight.recipe.PlayerCraftingContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingMenu.class)
public class CraftingMenuMixin {
    @Inject(method = "slotChangedCraftingGrid", at = @At("HEAD"))
    private static void thaumaturgesdelight$beginCrafting(
            AbstractContainerMenu menu,
            ServerLevel level,
            Player player,
            CraftingContainer container,
            ResultContainer resultSlots,
            RecipeHolder<CraftingRecipe> recipeHint,
            CallbackInfo ci) {
        PlayerCraftingContext.set(player);
    }

    @Inject(method = "slotChangedCraftingGrid", at = @At("RETURN"))
    private static void thaumaturgesdelight$endCrafting(
            AbstractContainerMenu menu,
            ServerLevel level,
            Player player,
            CraftingContainer container,
            ResultContainer resultSlots,
            RecipeHolder<CraftingRecipe> recipeHint,
            CallbackInfo ci) {
        PlayerCraftingContext.clear();
    }
}
