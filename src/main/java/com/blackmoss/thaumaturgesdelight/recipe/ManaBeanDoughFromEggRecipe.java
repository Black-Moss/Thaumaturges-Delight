package com.blackmoss.thaumaturgesdelight.recipe;

import com.blackmoss.thaumaturgesdelight.item.AspectFoodItem;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

public class ManaBeanDoughFromEggRecipe extends CustomRecipe {
    public static final int BEAN_COUNT = 3;
    public static final int RESULT_COUNT = 3;

    public static final MapCodec<ManaBeanDoughFromEggRecipe> CODEC = CraftingBookCategory.CODEC
            .optionalFieldOf("category", CraftingBookCategory.MISC)
            .xmap(ManaBeanDoughFromEggRecipe::new, ManaBeanDoughFromEggRecipe::category);
    public static final StreamCodec<RegistryFriendlyByteBuf, ManaBeanDoughFromEggRecipe> STREAM_CODEC = CraftingBookCategory.STREAM_CODEC
            .map(ManaBeanDoughFromEggRecipe::new, ManaBeanDoughFromEggRecipe::category).cast();
    public static final RecipeSerializer<ManaBeanDoughFromEggRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

    private final CraftingBookCategory category;

    public ManaBeanDoughFromEggRecipe(CraftingBookCategory category) {
        this.category = category;
    }

    public @NonNull CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public boolean matches(CraftingInput container, @NonNull Level level) {
        Holder<IAspect> commonAspect = null;
        ItemStack firstBean = ItemStack.EMPTY;
        int beans = 0;
        boolean egg = false;

        for (int index = 0; index < container.size(); ++index) {
            ItemStack selectedStack = container.getItem(index);
            if (selectedStack.isEmpty()) {
                continue;
            }

            if (selectedStack.is(TTItems.MANA_BEAN.get())) {
                Holder<IAspect> current = AspectFoodItem.aspectOf(selectedStack);
                if (current == null) {
                    return false;
                }

                if (commonAspect == null) {
                    commonAspect = current;
                    firstBean = selectedStack;
                } else if (!commonAspect.unwrapKey().equals(current.unwrapKey())) {
                    return false;
                }

                ++beans;
            } else if (selectedStack.is(Tags.Items.EGGS)) {
                if (egg) {
                    return false;
                }

                egg = true;
            } else {
                return false;
            }
        }

        if (beans != BEAN_COUNT || !egg) {
            return false;
        }

        return ManaBeanDoughRecipe.knownByCrafter(firstBean);
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull CraftingInput container) {
        ItemStack result = new ItemStack(TDItems.MANA_BEAN_DOUGH.get(), RESULT_COUNT);

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

    @Override
    public @NonNull RecipeSerializer<ManaBeanDoughFromEggRecipe> getSerializer() {
        return SERIALIZER;
    }
}
