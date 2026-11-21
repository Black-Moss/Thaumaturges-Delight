package com.blackmoss.thaumaturgesdelight.recipe;

import com.blackmoss.thaumaturgesdelight.item.AspectFoodItem;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.research.pool.AspectPoolAccess;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

public class ManaBeanDoughRecipe extends CustomRecipe {
    public static final MapCodec<ManaBeanDoughRecipe> CODEC = CraftingBookCategory.CODEC
            .optionalFieldOf("category", CraftingBookCategory.MISC)
            .xmap(ManaBeanDoughRecipe::new, ManaBeanDoughRecipe::category);
    public static final StreamCodec<RegistryFriendlyByteBuf, ManaBeanDoughRecipe> STREAM_CODEC = CraftingBookCategory.STREAM_CODEC
            .map(ManaBeanDoughRecipe::new, ManaBeanDoughRecipe::category).cast();
    public static final RecipeSerializer<ManaBeanDoughRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
    private final CraftingBookCategory category;

    public ManaBeanDoughRecipe(CraftingBookCategory category) {
        this.category = category;
    }

    public static boolean knownByCrafter(ItemStack manaBean) {
        Player player = PlayerCraftingContext.get();
        if (player == null) {
            return true;
        }

        Holder<IAspect> aspect = AspectFoodItem.aspectOf(manaBean);
        return aspect == null || AspectPoolAccess.isDiscovered(player, aspect);
    }

    public @NonNull CraftingBookCategory category() {
        return this.category;
    }

    public boolean matches(CraftingInput container, @NonNull Level level) {
        ItemStack manaBeanStack = ItemStack.EMPTY;
        ItemStack waterStack = ItemStack.EMPTY;

        for (int index = 0; index < container.size(); ++index) {
            ItemStack selectedStack = container.getItem(index);
            if (!selectedStack.isEmpty()) {
                if (selectedStack.is(TTItems.MANA_BEAN.get())) {
                    if (!manaBeanStack.isEmpty()) {
                        return false;
                    }

                    manaBeanStack = selectedStack;
                } else {
                    if (!selectedStack.is(Tags.Items.BUCKETS_WATER)) {
                        return false;
                    }

                    waterStack = selectedStack;
                }
            }
        }

        if (manaBeanStack.isEmpty() || waterStack.isEmpty()) {
            return false;
        }

        return knownByCrafter(manaBeanStack);
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull CraftingInput container) {
        ItemStack result = new ItemStack(TDItems.MANA_BEAN_DOUGH.get());
        for (int index = 0; index < container.size(); ++index) {
            ItemStack selectedStack = container.getItem(index);
            if (selectedStack.is(TTItems.MANA_BEAN.get())) {
                Holder<IAspect> aspect = AspectFoodItem.aspectOf(selectedStack);
                if (aspect != null) {
                    result.set(TTDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(aspect, 1));
                }
                break;
            }
        }
        return result;
    }

    public @NonNull NonNullList<ItemStack> getRemainingItems(CraftingInput container) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(container.size(), ItemStack.EMPTY);

        for (int index = 0; index < remainders.size(); ++index) {
            ItemStack selectedStack = container.getItem(index);
            if (selectedStack.is(Tags.Items.BUCKETS_WATER)) {
                remainders.set(index, selectedStack.copy());
            }
        }

        return remainders;
    }

    @Override
    public @NonNull RecipeSerializer<ManaBeanDoughRecipe> getSerializer() {
        return SERIALIZER;
    }
}
