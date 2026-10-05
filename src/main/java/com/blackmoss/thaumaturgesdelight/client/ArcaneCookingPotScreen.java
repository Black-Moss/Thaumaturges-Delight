package com.blackmoss.thaumaturgesdelight.client;

import com.blackmoss.thaumaturgesdelight.block.ArcaneCookingPotBlockEntity;
import com.blackmoss.thaumaturgesdelight.menu.ArcaneCookingPotMenu;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class ArcaneCookingPotScreen extends AbstractRecipeBookScreen<ArcaneCookingPotMenu> {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath("farmersdelight", "textures/gui/cooking_pot.png");
    private static final Rectangle HEAT_ICON = new Rectangle(47, 55, 17, 15);
    private static final Rectangle PROGRESS_ARROW = new Rectangle(89, 25, 0, 17);
    private static final int TEXTURE_SIZE = 256;

    private final ArcaneCookingPotRecipeBookComponent recipeBookComponent;

    public ArcaneCookingPotScreen(ArcaneCookingPotMenu menu, Inventory playerInventory, Component title) {
        this(menu, new ArcaneCookingPotRecipeBookComponent(menu), playerInventory, title);
    }

    private ArcaneCookingPotScreen(ArcaneCookingPotMenu menu, ArcaneCookingPotRecipeBookComponent recipeBookComponent, Inventory playerInventory, Component title) {
        super(menu, recipeBookComponent, playerInventory, title);
        this.recipeBookComponent = recipeBookComponent;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 28;
    }

    @Override
    protected @NonNull ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(leftPos + 5, height / 2 - 49);
    }

    @Override
    protected void onRecipeBookButtonClick() {
        super.onRecipeBookButtonClick();
        this.leftPos = recipeBookComponent.updateScreenPosition(width, imageWidth);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE,
                leftPos, topPos,
                0.0F, 0.0F,
                imageWidth, imageHeight,
                TEXTURE_SIZE, TEXTURE_SIZE);

        if (menu.isHeated()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE,
                            leftPos + HEAT_ICON.x, topPos + HEAT_ICON.y,
                            176.0F, 0.0F,
                            HEAT_ICON.width, HEAT_ICON.height,
                    TEXTURE_SIZE, TEXTURE_SIZE);
        }

        int cookProgressionScaled = menu.getCookProgressionScaled();
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE,
                leftPos + PROGRESS_ARROW.x, topPos + PROGRESS_ARROW.y,
                176.0F, 15.0F,
                cookProgressionScaled + 1,
                PROGRESS_ARROW.height, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    @Override
    protected void extractTooltip(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (isHovering(HEAT_ICON.x, HEAT_ICON.y, HEAT_ICON.width, HEAT_ICON.height, mouseX, mouseY)) {
            Component heat = Component.translatable(menu.isHeated()
                    ? "container.thaumaturgesdelight.arcane_cooking_pot.heated"
                    : "container.thaumaturgesdelight.arcane_cooking_pot.not_heated");
            graphics.setTooltipForNextFrame(font, heat, mouseX, mouseY);
        } else if (minecraft.player != null && menu.getCarried().isEmpty() && hoveredSlot != null && hoveredSlot.hasItem() && hoveredSlot.index == ArcaneCookingPotBlockEntity.MEAL_DISPLAY_SLOT) {
            List<Component> tooltip = new ArrayList<>();
            ItemStack meal = hoveredSlot.getItem();
            tooltip.add(Component.empty().append(meal.getHoverName()).withStyle(meal.getRarity().getStyleModifier()));
            ItemStack container = menu.getBlockEntity().getContainer();
            if (!container.isEmpty()) {
                tooltip.add(Component.translatable("container.thaumaturgesdelight.arcane_cooking_pot.served_on", container.getHoverName().getString())
                        .withStyle(ChatFormatting.GRAY));
            }
            graphics.setComponentTooltipForNextFrame(font, tooltip, mouseX, mouseY, meal);
        } else {
            super.extractTooltip(graphics, mouseX, mouseY);
        }
    }

    @Override
    protected boolean isBiggerResultSlot() {
        return false;
    }
}
