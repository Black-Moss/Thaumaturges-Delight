package com.blackmoss.thaumaturgesdelight.compat.jei;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.client.ThaumaturgesDelightClient;
import com.blackmoss.thaumaturgesdelight.client.hud.ArcaneCookingPotScreen;
import com.blackmoss.thaumaturgesdelight.compat.jei.category.ArcaneCookingRecipeCategory;
import com.blackmoss.thaumaturgesdelight.menu.ArcaneCookingPotMenu;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.blackmoss.thaumaturgesdelight.registry.TDMenus;
import com.blackmoss.thaumaturgesdelight.registry.TDRecipeTypes;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ThaumaturgesDelightJEIPlugin implements IModPlugin {
    private static final Identifier PLUGIN_UID = ThaumaturgesDelight.identifier("jei_plugin");

    private static List<RecipeHolder<CraftingRecipe>> doughDisplayRecipes() {
        List<RecipeHolder<CraftingRecipe>> loaded = ThaumaturgesDelightClient.byType(RecipeType.CRAFTING);
        boolean waterDough = isLoaded(loaded, "mana_bean_dough");
        boolean eggDough = isLoaded(loaded, "mana_bean_dough_from_egg");
        List<RecipeHolder<CraftingRecipe>> displays = new ArrayList<>();
        if (!waterDough && !eggDough) {
            return displays;
        }

        Registry<IAspect> aspects = null;
        if (Minecraft.getInstance().level != null) {
            aspects = Minecraft.getInstance().level.registryAccess().lookupOrThrow(IAspect.REGISTRY_KEY);
        }
        if (aspects != null) {
            for (Holder.Reference<IAspect> aspect : aspects.listElements().toList()) {
                if (waterDough) {
                    displays.add(doughDisplay("mana_bean_dough", aspect, 1,
                            List.of(
                                    Ingredient.of(TTItems.MANA_BEAN.get()),
                                    tagIngredient(Tags.Items.BUCKETS_WATER)),
                            List.of(
                                    List.of(beanStack(aspect)),
                                    tagStacks(Tags.Items.BUCKETS_WATER))));
                }
                if (eggDough) {
                    displays.add(doughDisplay("mana_bean_dough_from_egg", aspect, 3,
                            List.of(
                                    Ingredient.of(TTItems.MANA_BEAN.get()),
                                    Ingredient.of(TTItems.MANA_BEAN.get()),
                                    Ingredient.of(TTItems.MANA_BEAN.get()),
                                    tagIngredient(Tags.Items.EGGS)),
                            List.of(
                                    List.of(beanStack(aspect)),
                                    List.of(beanStack(aspect)),
                                    List.of(beanStack(aspect)),
                                    tagStacks(Tags.Items.EGGS))));
                }
            }
        }

        return displays;
    }

    private static boolean isLoaded(List<RecipeHolder<CraftingRecipe>> loaded, String path) {
        Identifier id = ThaumaturgesDelight.identifier(path);
        return loaded.stream().anyMatch(holder -> holder.id().identifier().equals(id));
    }

    private static Ingredient tagIngredient(TagKey<Item> tag) {
        Registry<Item> items = null;
        if (Minecraft.getInstance().level != null) {
            items = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ITEM);
        }
        List<Holder<Item>> holders = new ArrayList<>();
        if (items != null) {
            items.getTagOrEmpty(tag).forEach(holders::add);
        }
        return Ingredient.of(HolderSet.direct(holders));
    }

    private static RecipeHolder<CraftingRecipe> doughDisplay(
            String path,
            Holder<IAspect> aspect,
            int count,
            List<Ingredient> ingredients,
            List<List<ItemStack>> jeiInputs) {
        ItemStack output = new ItemStack(TDItems.MANA_BEAN_DOUGH.get(), count);
        output.set(TTDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(aspect, 1));
        ItemStackTemplate result = new ItemStackTemplate(output.typeHolder(), output.getCount(), output.getComponentsPatch());
        DoughDisplayRecipe display = new DoughDisplayRecipe(
                new Recipe.CommonInfo(true),
                new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, path),
                result,
                ingredients,
                jeiInputs,
                output);
        Identifier id = ThaumaturgesDelight.identifier("jei/" + path + "/" + aspect.unwrapKey().orElseThrow().identifier().getPath());
        //noinspection NullableProblems
        ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, id);
        return new RecipeHolder<>(key, display);
    }

    private static ItemStack beanStack(Holder<IAspect> aspect) {
        ItemStack stack = new ItemStack(TTItems.MANA_BEAN.get());
        stack.set(TTDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(aspect, 1));
        return stack;
    }

    private static List<ItemStack> tagStacks(TagKey<Item> tag) {
        Registry<Item> items = null;
        if (Minecraft.getInstance().level != null) {
            items = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.ITEM);
        }
        List<ItemStack> stacks = new ArrayList<>();
        if (items != null) {
            items.getTagOrEmpty(tag).forEach(holder -> stacks.add(new ItemStack(holder.value())));
        }
        return stacks;
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
        registration.addRecipes(ArcaneCookingRecipeCategory.RECIPE_TYPE, ThaumaturgesDelightClient.byType(TDRecipeTypes.ARCANE_COOKING.get()));
        registration.addRecipes(RecipeTypes.CRAFTING, doughDisplayRecipes());
    }

    @Override
    public void registerRecipeCatalysts(@NonNull IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(ArcaneCookingRecipeCategory.RECIPE_TYPE, TDItems.ARCANE_COOKING_POT.get());
        registration.addCraftingStation(FDRecipeTypes.COOKING, TDItems.ARCANE_COOKING_POT.get());
    }

    @Override
    public void registerItemSubtypes(@NonNull ISubtypeRegistration registration) {
        registration.registerFromDataComponentTypes(TDItems.ESSENTIA_ROCK_CANDY.get(), TTDataComponents.CRYSTAL_ASPECT.get());
        registration.registerFromDataComponentTypes(TDItems.MANA_BEAN_DOUGH.get(), TTDataComponents.CRYSTAL_ASPECT.get());
    }

    @Override
    public void registerGuiHandlers(@NonNull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ArcaneCookingPotScreen.class, 89, 25, 24, 17, ArcaneCookingRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(@NonNull IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ArcaneCookingPotMenu.class, TDMenus.ARCANE_COOKING_POT.get(), ArcaneCookingRecipeCategory.RECIPE_TYPE, 0, 6, 9, 36);
    }

    @Override
    public void registerVanillaCategoryExtensions(@NonNull IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(DoughDisplayRecipe.class, DoughDisplayExtension.INSTANCE);
    }
}
