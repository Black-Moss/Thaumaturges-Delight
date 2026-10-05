package com.blackmoss.thaumaturgesdelight.compat.jei.category;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.recipe.ArcaneCookingPotRecipe;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.blackmoss.thaumaturgesdelight.registry.TDRecipeTypes;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.compat.jei.ingredient.AspectIngredientRenderer;
import com.leclowndu93150.thaumaturge.compat.jei.ingredient.AspectIngredientType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ArcaneCookingRecipeCategory implements IRecipeCategory<RecipeHolder<ArcaneCookingPotRecipe>> {
    public static final IRecipeHolderType<ArcaneCookingPotRecipe> RECIPE_TYPE = IRecipeHolderType.create(TDRecipeTypes.ARCANE_COOKING.get());

    private static final Identifier JEI_TEXTURE = ThaumaturgesDelight.fdIdentifier("textures/gui/jei/cooking_pot.png");
    private static final Identifier POT_TEXTURE = ThaumaturgesDelight.fdIdentifier("textures/gui/cooking_pot.png");

    private static final int WIDTH = 116;
    private static final int BACKGROUND_HEIGHT = 56;
    private static final int HEIGHT = 82;
    private static final int SLOT_SIZE = 18;
    private static final int ASPECT_Y = 60;
    private static final int ASPECT_SPACING = 22;
    private static final int OUTPUT_X = 95;
    private static final int OUTPUT_Y = 10;
    private static final int CONTAINER_X = 63;
    private static final int CONTAINER_Y = 39;
    private static final int SERVED_Y = 39;
    private static final int TIMER_X = 61;
    private static final int TIMER_Y = 2;
    private static final int TIMER_WIDTH = 22;
    private static final int TIMER_HEIGHT = 28;

    protected final IDrawable heatIndicator;
    protected final IDrawable timeIcon;
    protected final IDrawable expIcon;
    protected final IDrawableAnimated arrow;
    private final Component title = Component.translatable("recipe.type.arcane_cooking");
    private final IDrawable background;
    private final IDrawable icon;

    public ArcaneCookingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(JEI_TEXTURE, 0, 0, WIDTH, BACKGROUND_HEIGHT);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(TDItems.ARCANE_COOKING_POT.get()));
        this.heatIndicator = guiHelper.createDrawable(POT_TEXTURE, 176, 0, 17, 15);
        this.timeIcon = guiHelper.createDrawable(POT_TEXTURE, 176, 32, 8, 11);
        this.expIcon = guiHelper.createDrawable(POT_TEXTURE, 176, 43, 9, 9);
        this.arrow = guiHelper.drawableBuilder(POT_TEXTURE, 176, 15, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public @NonNull IRecipeType<RecipeHolder<ArcaneCookingPotRecipe>> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NonNull Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NonNull IRecipeLayoutBuilder builder, @NonNull RecipeHolder<ArcaneCookingPotRecipe> holder, @NonNull IFocusGroup focuses) {
        ArcaneCookingPotRecipe recipe = holder.value();
        List<Ingredient> ingredients = recipe.getIngredients();
        ItemStack result = recipe.resultTemplate().create();
        ItemStack container = recipe.containerTemplate().map(ItemStackTemplate::create).orElse(ItemStack.EMPTY);

        for (int i = 0; i < ingredients.size() && i < ArcaneCookingPotRecipe.INPUT_SLOTS; i++) {
            int col = i % 3;
            int row = i / 3;
            builder.addSlot(RecipeIngredientRole.INPUT, col * SLOT_SIZE + 1, row * SLOT_SIZE + 1).add(ingredients.get(i));
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y).add(result);
        if (!container.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, CONTAINER_X, CONTAINER_Y).add(container);
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, SERVED_Y).add(result);

        AspectList aspects = recipe.aspects();
        if (!aspects.isEmpty()) {
            List<AspectInstance> costs = aspects.sortedByAmount();
            int startX = (WIDTH - costs.size() * ASPECT_SPACING) / 2;
            for (int i = 0; i < costs.size(); i++) {
                builder.addInputSlot(startX + i * ASPECT_SPACING, ASPECT_Y)
                        .setCustomRenderer(AspectIngredientType.INSTANCE, AspectIngredientRenderer.INSTANCE)
                        .add(AspectIngredientType.INSTANCE, costs.get(i));
            }
        }
    }

    @Override
    public void draw(@NonNull RecipeHolder<ArcaneCookingPotRecipe> holder, @NonNull IRecipeSlotsView recipeSlotsView, @NonNull GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        background.draw(graphics);
        arrow.draw(graphics, 60, 9);
        heatIndicator.draw(graphics, 18, 39);
        timeIcon.draw(graphics, 64, 2);
        if (holder.value().getExperience() > 0.0F) {
            expIcon.draw(graphics, 63, 21);
        }
    }

    @Override
    public void getTooltip(@NonNull ITooltipBuilder tooltip, @NonNull RecipeHolder<ArcaneCookingPotRecipe> holder, @NonNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= TIMER_X && mouseX < TIMER_X + TIMER_WIDTH && mouseY >= TIMER_Y && mouseY < TIMER_Y + TIMER_HEIGHT) {
            int cookTime = holder.value().getCookTime();
            if (cookTime > 0) {
                tooltip.add(Component.translatable("gui.jei.category.smelting.time.seconds", cookTime / 20));
            }
            float experience = holder.value().getExperience();
            if (experience > 0.0F) {
                tooltip.add(Component.translatable("gui.jei.category.smelting.experience", experience));
            }
        }
    }
}
