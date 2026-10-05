package com.blackmoss.thaumaturgesdelight.recipe;

import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import com.blackmoss.thaumaturgesdelight.registry.TDRecipeTypes;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeBookCategories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ArcaneCookingPotRecipe implements Recipe<RecipeWrapper> {
    public static final int INPUT_SLOTS = 6;
    public static final int DEFAULT_COOK_TIME = 200;
    public static final float DEFAULT_EXPERIENCE = 1.0F;

    public static final MapCodec<ArcaneCookingPotRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(ArcaneCookingPotRecipe::getGroup),
            CookingPotRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab", CookingPotRecipeBookTab.MISC).forGetter(ArcaneCookingPotRecipe::getRecipeBookTab),
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> List.copyOf(recipe.ingredients)),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(ArcaneCookingPotRecipe::resultTemplate),
            ItemStackTemplate.CODEC.optionalFieldOf("container").forGetter(ArcaneCookingPotRecipe::containerTemplate),
            Codec.FLOAT.optionalFieldOf("experience", DEFAULT_EXPERIENCE).forGetter(ArcaneCookingPotRecipe::getExperience),
            Codec.INT.optionalFieldOf("cookingtime", DEFAULT_COOK_TIME).forGetter(ArcaneCookingPotRecipe::getCookTime),
            AspectList.CODEC.optionalFieldOf("aspects", AspectList.EMPTY).forGetter(ArcaneCookingPotRecipe::aspects)
    ).apply(instance, ArcaneCookingPotRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ArcaneCookingPotRecipe> STREAM_CODEC = StreamCodec.of(ArcaneCookingPotRecipe::encode, ArcaneCookingPotRecipe::decode);

    public static final RecipeSerializer<ArcaneCookingPotRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final String group;
    private final CookingPotRecipeBookTab tab;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStackTemplate result;
    private final Optional<ItemStackTemplate> container;
    private final float experience;
    private final int cookTime;
    private final AspectList aspects;
    private CookingPotRecipe converted;

    public ArcaneCookingPotRecipe(String group, CookingPotRecipeBookTab tab, List<Ingredient> ingredients, ItemStackTemplate result, Optional<ItemStackTemplate> container, float experience, int cookTime, AspectList aspects) {
        this.group = group;
        this.tab = tab;
        this.ingredients = NonNullList.create();
        this.ingredients.addAll(ingredients);
        this.result = result;
        this.container = container;
        this.experience = experience;
        this.cookTime = cookTime;
        this.aspects = aspects;
    }

    public AspectList aspects() {
        return aspects;
    }

    public String getGroup() {
        return group;
    }

    public ItemStackTemplate resultTemplate() {
        return result;
    }

    public Optional<ItemStackTemplate> containerTemplate() {
        return container;
    }

    public float getExperience() {
        return experience;
    }

    public int getCookTime() {
        return cookTime;
    }

    public CookingPotRecipeBookTab getRecipeBookTab() {
        return tab;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public CookingPotRecipe asCookingPotRecipe() {
        if (converted == null) {
            converted = new CookingPotRecipe(group, tab, ingredients, result.create(), container.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY), experience, cookTime);
        }
        return converted;
    }

    @Override
    public boolean matches(@NonNull RecipeWrapper wrapper, @NonNull Level level) {
        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < INPUT_SLOTS; i++) {
            ItemStack stack = wrapper.getItem(i);
            if (!stack.isEmpty()) {
                inputs.add(stack);
            }
        }
        return inputs.size() == ingredients.size() && RecipeMatcher.findMatches(inputs, ingredients) != null;
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull RecipeWrapper wrapper) {
        return result.create();
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public @NonNull String group() {
        return group;
    }

    @Override
    public @NonNull RecipeSerializer<ArcaneCookingPotRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NonNull RecipeType<ArcaneCookingPotRecipe> getType() {
        return TDRecipeTypes.ARCANE_COOKING.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredients);
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return switch (tab == null ? CookingPotRecipeBookTab.MISC : tab) {
            case MEALS -> ModRecipeBookCategories.COOKING_MEALS.get();
            case DRINKS -> ModRecipeBookCategories.COOKING_DRINKS.get();
            case MISC -> ModRecipeBookCategories.COOKING_MISC.get();
        };
    }

    @Override
    public @NonNull List<RecipeDisplay> display() {
        return List.of(new ShapelessCraftingRecipeDisplay(ingredients.stream().map(Ingredient::display).toList(),
                new SlotDisplay.ItemStackSlotDisplay(result),
                new SlotDisplay.ItemSlotDisplay(TDBlocks.ARCANE_COOKING_POT.get().asItem())));
    }

    private static void encode(RegistryFriendlyByteBuf buffer, ArcaneCookingPotRecipe recipe) {
        buffer.writeUtf(recipe.group);
        buffer.writeUtf(recipe.tab == null ? "" : recipe.tab.toString());
        buffer.writeVarInt(recipe.ingredients.size());
        for (Ingredient ingredient : recipe.ingredients) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
        }
        ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
        buffer.writeBoolean(recipe.container.isPresent());
        recipe.container.ifPresent(template -> ItemStackTemplate.STREAM_CODEC.encode(buffer, template));
        buffer.writeFloat(recipe.experience);
        buffer.writeVarInt(recipe.cookTime);
        AspectList.STREAM_CODEC.encode(buffer, recipe.aspects);
    }

    private static ArcaneCookingPotRecipe decode(RegistryFriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        CookingPotRecipeBookTab tab = CookingPotRecipeBookTab.findByName(buffer.readUtf());
        int size = buffer.readVarInt();
        List<Ingredient> ingredients = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
        }
        ItemStackTemplate result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
        Optional<ItemStackTemplate> container = buffer.readBoolean() ? Optional.of(ItemStackTemplate.STREAM_CODEC.decode(buffer)) : Optional.empty();
        float experience = buffer.readFloat();
        int cookTime = buffer.readVarInt();
        AspectList aspects = AspectList.STREAM_CODEC.decode(buffer);
        return new ArcaneCookingPotRecipe(group, tab, ingredients, result, container, experience, cookTime, aspects);
    }
}
