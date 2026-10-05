package com.blackmoss.thaumaturgesdelight.menu;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.block.ArcaneCookingPotBlockEntity;
import com.blackmoss.thaumaturgesdelight.registry.TDMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.recipebook.ServerPlaceRecipe.CraftingMenuAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.List;

public class ArcaneCookingPotMenu extends RecipeBookMenu {
    public static final Identifier EMPTY_CONTAINER_SLOT_BOWL = ThaumaturgesDelight.fdIdentifier("item/empty_container_slot_bowl");

    private static final int BE_SLOT_COUNT = 9;
    private static final int PLAYER_INV_START = BE_SLOT_COUNT;
    private static final int PLAYER_INV_END = BE_SLOT_COUNT + 36;

    private final ArcaneCookingPotBlockEntity blockEntity;
    private final ItemStackHandler inventory;
    private final ContainerData data;

    public ArcaneCookingPotMenu(int id, Inventory playerInventory, ArcaneCookingPotBlockEntity blockEntity, ContainerData data) {
        super(TDMenus.ARCANE_COOKING_POT.get(), id);
        this.blockEntity = blockEntity;
        this.inventory = blockEntity.getInventory();
        this.data = data;
        addCookingPotSlots();
        addPlayerSlots(playerInventory);
        addDataSlots(data);
    }

    public ArcaneCookingPotMenu(int id, Inventory playerInventory, RegistryFriendlyByteBuf buffer) {
        this(id, playerInventory, resolveBlockEntity(playerInventory, buffer.readBlockPos()), new SimpleContainerData(ArcaneCookingPotBlockEntity.DATA_COUNT));
    }

    private static ArcaneCookingPotBlockEntity resolveBlockEntity(Inventory playerInventory, BlockPos pos) {
        if (playerInventory.player.level().getBlockEntity(pos) instanceof ArcaneCookingPotBlockEntity pot) {
            return pot;
        }
        throw new IllegalStateException("Block entity is not correct! " + pos);
    }

    private void addCookingPotSlots() {
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new SlotItemHandler(inventory, row * 3 + col, 30 + col * 18, 17 + row * 18));
            }
        }
        addSlot(new SlotItemHandler(inventory, ArcaneCookingPotBlockEntity.MEAL_DISPLAY_SLOT, 124, 26) {
            @Override
            public boolean mayPlace(@NonNull ItemStack stack) {
                return false;
            }
        });
        addSlot(new SlotItemHandler(inventory, ArcaneCookingPotBlockEntity.CONTAINER_SLOT, 92, 55) {
            @Override
            public Identifier getNoItemIcon() {
                return EMPTY_CONTAINER_SLOT_BOWL;
            }
        });
        addSlot(new SlotItemHandler(inventory, ArcaneCookingPotBlockEntity.OUTPUT_SLOT, 124, 55) {
            @Override
            public boolean mayPlace(@NonNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NonNull Player player, @NonNull ItemStack stack) {
                blockEntity.awardUsedRecipes(player, List.of(stack));
                super.onTake(player, stack);
            }
        });
    }

    private void addPlayerSlots(Inventory playerInventory) {
        int inventoryY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, inventoryY + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < BE_SLOT_COUNT) {
            if (!moveItemStackTo(stack, PLAYER_INV_START, PLAYER_INV_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, 0, ArcaneCookingPotBlockEntity.INPUT_SLOTS, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return original;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        if (blockEntity == null || blockEntity.getLevel() != player.level()) {
            return false;
        }
        return player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos())) <= 64.0;
    }

    public ContainerData getData() {
        return data;
    }

    public boolean isHeated() {
        return blockEntity.isHeated();
    }

    public int getCookProgressionScaled() {
        int cookTime = data.get(0);
        int cookTimeTotal = data.get(1);
        return cookTime != 0 && cookTimeTotal != 0 ? cookTime * 24 / cookTimeTotal : 0;
    }

    public ArcaneCookingPotBlockEntity getBlockEntity() {
        return blockEntity;
    }

    @Override
    public void fillCraftSlotsStackedContents(@NonNull StackedItemContents contents) {
        for (int i = 0; i < BE_SLOT_COUNT; i++) {
            contents.accountSimpleStack(inventory.getStackInSlot(i));
        }
    }

    public void clearCraftingContent() {
        for (int i = 0; i < ArcaneCookingPotBlockEntity.INPUT_SLOTS; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean matchesAnyRecipe(@NonNull RecipeHolder<?> recipe) {
        Recipe<?> generic = recipe.value();
        if (blockEntity.getLevel() != null) {
            return ((Recipe<RecipeWrapper>) generic).matches(new RecipeWrapper(inventory), blockEntity.getLevel());
        }
        return false;
    }

    @Override
    public RecipeBookMenu.@NonNull PostPlaceAction handlePlacement(
            boolean placeAll,
            boolean creative,
            @NonNull RecipeHolder<?> recipe,
            @NonNull ServerLevel level,
            @NonNull Inventory playerInventory) {
        List<Slot> craftingSlots = slots.subList(0, ArcaneCookingPotBlockEntity.INPUT_SLOTS);
        @SuppressWarnings("unchecked")
        RecipeHolder<CookingPotRecipe> holder = (RecipeHolder<CookingPotRecipe>) recipe;
        CraftingMenuAccess<CookingPotRecipe> access = new CraftingMenuAccess<>() {
            @Override
            public void fillCraftSlotsStackedContents(@NonNull StackedItemContents contents) {
                ArcaneCookingPotMenu.this.fillCraftSlotsStackedContents(contents);
            }

            @Override
            public void clearCraftingContent() {
                ArcaneCookingPotMenu.this.clearCraftingContent();
            }

            @Override
            public boolean recipeMatches(@NonNull RecipeHolder<CookingPotRecipe> matched) {
                return ArcaneCookingPotMenu.this.matchesAnyRecipe(matched);
            }
        };
        return ServerPlaceRecipe.placeRecipe(access, 3, 2, craftingSlots, craftingSlots, playerInventory, holder, placeAll, creative);
    }

    @Override
    public @NonNull RecipeBookType getRecipeBookType() {
        return RecipeBookType.valueOf("FARMERSDELIGHT_COOKING");
    }

    public int getResultSlotIndex() {
        return ArcaneCookingPotBlockEntity.CONTAINER_SLOT;
    }

    public int getGridWidth() {
        return 3;
    }

    public int getGridHeight() {
        return 2;
    }

    public int getSize() {
        return ArcaneCookingPotBlockEntity.INPUT_SLOTS + 1;
    }

    public boolean shouldMoveToInventory(int index) {
        return index != getResultSlotIndex();
    }
}
