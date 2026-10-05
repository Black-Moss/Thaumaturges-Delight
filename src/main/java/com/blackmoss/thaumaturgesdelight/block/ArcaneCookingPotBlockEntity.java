package com.blackmoss.thaumaturgesdelight.block;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.menu.ArcaneCookingPotMenu;
import com.blackmoss.thaumaturgesdelight.recipe.ArcaneCookingLookup;
import com.blackmoss.thaumaturgesdelight.recipe.ArcaneCookingPotRecipe;
import com.blackmoss.thaumaturgesdelight.registry.TDBlockEntities;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;
import vectorwing.farmersdelight.common.block.entity.inventory.CookingPotItemHandler;
import vectorwing.farmersdelight.common.block.entity.inventory.LegacyItemHandlerResourceHandler;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("deprecation")
@EventBusSubscriber(modid = ThaumaturgesDelight.MODID)
public class ArcaneCookingPotBlockEntity extends BlockEntity implements MenuProvider, HeatableBlockEntity, Nameable, RecipeCraftingHolder, Clearable {
    public static final int INPUT_SLOTS = 6;
    public static final int MEAL_DISPLAY_SLOT = 6;
    public static final int CONTAINER_SLOT = 7;
    public static final int OUTPUT_SLOT = 8;
    public static final int INVENTORY_SIZE = 9;
    public static final int DATA_COUNT = 2;

    private static final Map<Item, Item> INGREDIENT_REMAINDER_OVERRIDES = Map.ofEntries(
            Map.entry(Items.POWDER_SNOW_BUCKET, Items.BUCKET),
            Map.entry(Items.AXOLOTL_BUCKET, Items.BUCKET),
            Map.entry(Items.COD_BUCKET, Items.BUCKET),
            Map.entry(Items.PUFFERFISH_BUCKET, Items.BUCKET),
            Map.entry(Items.SALMON_BUCKET, Items.BUCKET),
            Map.entry(Items.TROPICAL_FISH_BUCKET, Items.BUCKET),
            Map.entry(Items.SUSPICIOUS_STEW, Items.BOWL),
            Map.entry(Items.MUSHROOM_STEW, Items.BOWL),
            Map.entry(Items.RABBIT_STEW, Items.BOWL),
            Map.entry(Items.BEETROOT_SOUP, Items.BOWL),
            Map.entry(Items.POTION, Items.GLASS_BOTTLE),
            Map.entry(Items.SPLASH_POTION, Items.GLASS_BOTTLE),
            Map.entry(Items.LINGERING_POTION, Items.GLASS_BOTTLE),
            Map.entry(Items.EXPERIENCE_BOTTLE, Items.GLASS_BOTTLE));

    private final ItemStackHandler inventory = createHandler();
    private final ContainerData cookingPotData = createIntArray();
    private final Object2IntOpenHashMap<ResourceKey<Recipe<?>>> usedRecipeTracker = new Object2IntOpenHashMap<>();
    private int cookTime;
    private int cookTimeTotal;
    private ItemStack mealContainerStack = ItemStack.EMPTY;
    private @Nullable Component customName;

    public ArcaneCookingPotBlockEntity(BlockPos pos, BlockState state) {
        super(TDBlockEntities.ARCANE_COOKING_POT.get(), pos, state);
    }

    public static void cookingTick(Level level, BlockPos pos, BlockState state, ArcaneCookingPotBlockEntity pot) {
        boolean isHeated = pot.isHeated(level, pos);
        boolean didInventoryChange = false;
        if (isHeated && pot.hasInput()) {
            Optional<RecipeHolder<CookingPotRecipe>> recipe = pot.getMatchingRecipe();
            if (recipe.isPresent() && pot.canCook(recipe.get().value())) {
                didInventoryChange = pot.processCooking(recipe.get());
            } else {
                pot.cookTime = Mth.clamp(pot.cookTime - 2, 0, pot.cookTimeTotal);
            }
        } else if (pot.cookTime > 0) {
            pot.cookTime = Mth.clamp(pot.cookTime - 2, 0, pot.cookTimeTotal);
        }

        ItemStack mealStack = pot.getMeal();
        if (!mealStack.isEmpty()) {
            if (!pot.doesMealHaveContainer(mealStack)) {
                pot.moveMealToOutput();
                didInventoryChange = true;
            } else if (!pot.inventory.getStackInSlot(CONTAINER_SLOT).isEmpty()) {
                pot.useStoredContainersOnMeal();
                didInventoryChange = true;
            }
        }

        if (didInventoryChange) {
            pot.inventoryChanged();
        }
    }

    private static ItemStack getCraftingRemainder(ItemStack stack) {
        ItemStackTemplate remainder = stack.getItem().getCraftingRemainder(stack);
        return remainder == null ? ItemStack.EMPTY : remainder.create();
    }

    private static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
        float total = craftedAmount * experience;
        int xp = Mth.floor(total);
        if (xp < total && level.getRandom().nextFloat() < total - xp) {
            xp++;
        }
        while (xp > 0) {
            int split = ExperienceOrb.getExperienceValue(xp);
            xp -= split;
            level.addFreshEntity(new ExperienceOrb(level, pos.x, pos.y + 0.5, pos.z, split));
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, TDBlockEntities.ARCANE_COOKING_POT.get(), (blockEntity, side) -> {
            ItemStackHandler handler = blockEntity.getInventory();
            Direction direction = side == Direction.UP ? Direction.UP : Direction.DOWN;
            return new LegacyItemHandlerResourceHandler(new CookingPotItemHandler(handler, direction), handler::setStackInSlot);
        });
    }

    public static void animationTick(Level level, BlockPos pos, BlockState state, ArcaneCookingPotBlockEntity pot) {
        if (!pot.isHeated(level, pos)) {
            return;
        }
        RandomSource random = level.getRandom();
        if (random.nextFloat() < 0.2F) {
            double x = pos.getX() + 0.5 + random.nextDouble() * 0.6 - 0.3;
            double y = pos.getY() + 0.7;
            double z = pos.getZ() + 0.5 + random.nextDouble() * 0.6 - 0.3;
            level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0.0, 0.0, 0.0);
        }
        if (random.nextFloat() < 0.05F) {
            double x = pos.getX() + 0.5 + random.nextDouble() * 0.4 - 0.2;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5 + random.nextDouble() * 0.4 - 0.2;
            double rise = random.nextBoolean() ? 0.015 : 0.005;
            level.addParticle(ModParticleTypes.STEAM.get(), x, y, z, 0.0, rise, 0.0);
        }
    }

    private Optional<RecipeHolder<CookingPotRecipe>> getMatchingRecipe() {
        if (!(level instanceof ServerLevel serverLevel) || !hasInput()) {
            return Optional.empty();
        }
        return ArcaneCookingLookup.find(serverLevel, new RecipeWrapper(inventory));
    }

    private boolean hasInput() {
        for (int i = 0; i < INPUT_SLOTS; i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    protected boolean canCook(CookingPotRecipe recipe) {
        if (level == null || !hasInput()) {
            return false;
        }
        ItemStack resultStack = recipe.assemble(new RecipeWrapper(inventory));
        if (resultStack.isEmpty()) {
            return false;
        }
        ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
        if (mealStack.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItemSameComponents(mealStack, resultStack)) {
            return false;
        }
        int total = mealStack.getCount() + resultStack.getCount();
        return total <= mealStack.getMaxStackSize() && total <= resultStack.getMaxStackSize();
    }

    private boolean processCooking(RecipeHolder<CookingPotRecipe> recipe) {
        if (level == null) {
            return false;
        }
        if (!hasEssentiaFor(recipe)) {
            cookTime = 0;
            return false;
        }
        cookTime++;
        cookTimeTotal = recipe.value().getCookTime();
        if (cookTime < cookTimeTotal) {
            return false;
        }
        cookTime = 0;
        consumeEssentiaFor(recipe);
        mealContainerStack = recipe.value().getOutputContainer();
        ItemStack resultStack = recipe.value().assemble(new RecipeWrapper(inventory));
        ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
        if (mealStack.isEmpty()) {
            inventory.setStackInSlot(MEAL_DISPLAY_SLOT, resultStack.copy());
        } else if (ItemStack.isSameItemSameComponents(mealStack, resultStack)) {
            mealStack.grow(resultStack.getCount());
        }
        setRecipeUsed(recipe);
        for (int i = 0; i < INPUT_SLOTS; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            ItemStack remainder = getCraftingRemainder(stack);
            if (!remainder.isEmpty()) {
                ejectIngredientRemainder(remainder);
            } else if (INGREDIENT_REMAINDER_OVERRIDES.containsKey(stack.getItem())) {
                ejectIngredientRemainder(INGREDIENT_REMAINDER_OVERRIDES.get(stack.getItem()).getDefaultInstance());
            }
            if (!stack.isEmpty()) {
                stack.shrink(1);
            }
        }
        return true;
    }

    private boolean hasEssentiaFor(RecipeHolder<CookingPotRecipe> recipe) {
        ArcaneCookingPotRecipe arcane = arcaneRecipeFor(recipe);
        if (arcane == null || arcane.aspects().isEmpty()) {
            return true;
        }
        BlockEntity entity = null;
        if (level != null) {
            entity = level.getBlockEntity(worldPosition.below());
        }
        if (!(entity instanceof ArcaneStoveBlockEntity stove)) {
            return false;
        }
        stove.request(arcane.aspects());
        return stove.isReady();
    }

    private void consumeEssentiaFor(RecipeHolder<CookingPotRecipe> recipe) {
        ArcaneCookingPotRecipe arcane = arcaneRecipeFor(recipe);
        if (arcane == null || arcane.aspects().isEmpty() || level == null) {
            return;
        }
        if (level.getBlockEntity(worldPosition.below()) instanceof ArcaneStoveBlockEntity stove) {
            stove.consumeRequirement();
        }
    }

    private @Nullable ArcaneCookingPotRecipe arcaneRecipeFor(RecipeHolder<CookingPotRecipe> recipe) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return null;
        }
        return ArcaneCookingLookup.findArcane(serverLevel, new RecipeWrapper(inventory))
                .filter(holder -> holder.id().equals(recipe.id()))
                .map(RecipeHolder::value)
                .orElse(null);
    }

    protected void ejectIngredientRemainder(ItemStack remainderStack) {
        Direction direction = getBlockState().getValue(ArcaneCookingPotBlock.FACING).getCounterClockWise();
        double x = worldPosition.getX() + 0.5 + direction.getStepX() * 0.25;
        double y = worldPosition.getY() + 0.7;
        double z = worldPosition.getZ() + 0.5 + direction.getStepZ() * 0.25;
        if (level != null) {
            ItemUtils.spawnItemEntity(
                    level, remainderStack,
                    x, y, z,
                    direction.getStepX() * 0.08F, 0.25F, direction.getStepZ() * 0.08F);
        }
    }

    public ItemStack getMeal() {
        return inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
    }

    public ItemStack getContainer() {
        ItemStack mealStack = getMeal();
        if (mealStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return mealContainerStack.isEmpty() ? getCraftingRemainder(mealStack) : mealContainerStack;
    }

    private boolean doesMealHaveContainer(ItemStack meal) {
        return !mealContainerStack.isEmpty() || !getCraftingRemainder(meal).isEmpty();
    }

    public boolean isContainerValid(ItemStack containerStack) {
        if (containerStack.isEmpty() || mealContainerStack.isEmpty()) {
            return false;
        }
        return ItemStack.isSameItem(mealContainerStack, containerStack);
    }

    public ItemStack useHeldItemOnMeal(ItemStack container) {
        if (isContainerValid(container) && !getMeal().isEmpty()) {
            container.shrink(1);
            inventoryChanged();
            return getMeal().split(1);
        }
        return ItemStack.EMPTY;
    }

    private void moveMealToOutput() {
        ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
        int count = Math.min(mealStack.getCount(), mealStack.getMaxStackSize() - outputStack.getCount());
        if (outputStack.isEmpty()) {
            inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(count));
        } else if (ItemStack.isSameItemSameComponents(mealStack, outputStack)) {
            mealStack.shrink(count);
            outputStack.grow(count);
        }
    }

    private void useStoredContainersOnMeal() {
        ItemStack mealStack = inventory.getStackInSlot(MEAL_DISPLAY_SLOT);
        ItemStack containerStack = inventory.getStackInSlot(CONTAINER_SLOT);
        ItemStack outputStack = inventory.getStackInSlot(OUTPUT_SLOT);
        if (isContainerValid(containerStack) && outputStack.getCount() < outputStack.getMaxStackSize()) {
            int available = Math.min(mealStack.getCount(), containerStack.getCount());
            int count = Math.min(available, mealStack.getMaxStackSize() - outputStack.getCount());
            if (outputStack.isEmpty()) {
                containerStack.shrink(count);
                inventory.setStackInSlot(OUTPUT_SLOT, mealStack.split(count));
            } else if (ItemStack.isSameItemSameComponents(outputStack, mealStack)) {
                mealStack.shrink(count);
                containerStack.shrink(count);
                outputStack.grow(count);
            }
        }
    }

    public boolean isHeated() {
        return level != null && isHeated(level, worldPosition);
    }

    public NonNullList<ItemStack> getDroppableInventory() {
        NonNullList<ItemStack> drops = NonNullList.create();
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            drops.add(inventory.getStackInSlot(i));
        }
        return drops;
    }

    @Override
    public void clearContent() {
        ItemUtils.clearItems(inventory);
    }

    protected void inventoryChanged() {
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NonNull Inventory playerInventory, @NonNull Player player) {
        return new ArcaneCookingPotMenu(id, playerInventory, this, cookingPotData);
    }

    @Override
    public @NonNull Component getName() {
        return customName != null ? customName : Component.translatable("container.thaumaturgesdelight.arcane_cooking_pot");
    }

    @Override
    public @NonNull Component getDisplayName() {
        return getName();
    }

    @Override
    public @Nullable Component getCustomName() {
        return customName;
    }

    public void setCustomName(@Nullable Component customName) {
        this.customName = customName;
    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipe) {
        if (recipe != null) {
            usedRecipeTracker.addTo(recipe.id(), 1);
        }
    }

    public void awardUsedRecipes(Player player, @NonNull List<ItemStack> items) {
        player.awardRecipes(getUsedRecipesAndPopExperience(player.level(), player.position()));
        usedRecipeTracker.clear();
    }

    public List<RecipeHolder<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        List<RecipeHolder<?>> used = new ArrayList<>();
        if (!(level instanceof ServerLevel serverLevel)) {
            return used;
        }
        for (Object2IntMap.Entry<ResourceKey<Recipe<?>>> entry : usedRecipeTracker.object2IntEntrySet()) {
            for (RecipeHolder<?> holder : serverLevel.recipeAccess().getRecipes()) {
                if (!holder.id().equals(entry.getKey())) {
                    continue;
                }
                used.add(holder);
                if (holder.value() instanceof CookingPotRecipe cookingPotRecipe) {
                    splitAndSpawnExperience(serverLevel, pos, entry.getIntValue(), cookingPotRecipe.getExperience());
                }
                break;
            }
        }
        return used;
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        input.readChild("Inventory", inventory);
        cookTime = input.getIntOr("CookTime", 0);
        cookTimeTotal = input.getIntOr("CookTimeTotal", 0);
        mealContainerStack = input.read("Container", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        customName = input.read("CustomName", ComponentSerialization.CODEC).orElse(null);
        usedRecipeTracker.clear();
        ValueInput recipes = input.childOrEmpty("RecipesUsed");
        for (String key : recipes.keySet()) {
            usedRecipeTracker.put(ResourceKey.create(Registries.RECIPE, Identifier.parse(key)), recipes.getIntOr(key, 0));
        }
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("CookTime", cookTime);
        output.putInt("CookTimeTotal", cookTimeTotal);
        output.store("Container", ItemStack.OPTIONAL_CODEC, mealContainerStack);
        output.storeNullable("CustomName", ComponentSerialization.CODEC, customName);
        output.putChild("Inventory", inventory);
        ValueOutput recipes = output.child("RecipesUsed");
        usedRecipeTracker.forEach((id, amount) -> recipes.putInt(id.identifier().toString(), amount));
    }

    @Override
    protected void applyImplicitComponents(@NonNull DataComponentGetter components) {
        super.applyImplicitComponents(components);
        customName = components.get(DataComponents.CUSTOM_NAME);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.@NonNull Builder components) {
        super.collectImplicitComponents(components);
        if (customName != null) {
            components.set(DataComponents.CUSTOM_NAME, customName);
        }
    }

    @Override
    public void removeComponentsFromTag(@NonNull ValueOutput output) {
        super.removeComponentsFromTag(output);
        output.discard("CustomName");
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public ContainerData getCookingPotData() {
        return cookingPotData;
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            protected int getStackLimit(int slot, @NonNull ItemStack stack) {
                if (slot == MEAL_DISPLAY_SLOT) {
                    return Math.max(64, stack.getMaxStackSize());
                }
                return super.getStackLimit(slot, stack);
            }

            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    private ContainerData createIntArray() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> cookTime;
                    case 1 -> cookTimeTotal;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> cookTime = value;
                    case 1 -> cookTimeTotal = value;
                    default -> {
                    }
                }
            }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };
    }

    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != MEAL_DISPLAY_SLOT && slot != OUTPUT_SLOT;
    }
}
