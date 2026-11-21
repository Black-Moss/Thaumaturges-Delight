package com.blackmoss.lazycat.thaumaturgesdelight.recipe;

import com.blackmoss.lazycat.api.recipe.AbstractRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.RecipeContext;
import com.blackmoss.lazycat.api.recipe.thaumaturge.AbstractThaumaturgeRecipeBuilder;
import com.blackmoss.lazycat.api.recipe.thaumaturge.ThaumaturgeUtils;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.recipe.ResearchGate;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class AspectFoodBuilder {
    @FunctionalInterface
    public interface RecipeFactory {
        AbstractRecipeBuilder<?> create(AspectFoodContext food);
    }

    public record AspectFoodContext(
            Holder<IAspect> aspect,
            ResourceKey<IAspect> aspectKey,
            ItemStackTemplate result,
            String id) {
        public AspectFoodContext {
            Objects.requireNonNull(aspect, "aspect");
            Objects.requireNonNull(aspectKey, "aspectKey");
            Objects.requireNonNull(result, "result");
            Objects.requireNonNull(id, "id");
        }
    }

    private final RecipeContext context;
    private final ItemLike item;
    private final String idPrefix;
    private @Nullable ResearchGate gate;

    public AspectFoodBuilder(RecipeContext context, ItemLike item, String idPrefix) {
        this.context = Objects.requireNonNull(context, "context");
        this.item = Objects.requireNonNull(item, "item");
        this.idPrefix = Objects.requireNonNull(idPrefix, "idPrefix");
    }

    public AspectFoodBuilder gate(ResearchGate gate) {
        this.gate = Objects.requireNonNull(gate, "gate");
        return this;
    }

    public void save(RecipeFactory factory) {
        Objects.requireNonNull(factory, "factory");
        for (Holder<IAspect> aspect : this.context.registries().lookupOrThrow(IAspect.REGISTRY_KEY).listElements().toList()) {
            ResourceKey<IAspect> key = aspect.unwrapKey().orElseThrow();
            String id = this.idPrefix + "/" + key.identifier().getPath();
            AspectFoodContext food = new AspectFoodContext(
                    aspect, key, ThaumaturgeUtils.aspectItem(aspect, this.item), id);
            AbstractRecipeBuilder<?> builder = Objects.requireNonNull(factory.create(food), "recipe builder");
            if (this.gate != null && builder instanceof AbstractThaumaturgeRecipeBuilder<?> gated) {
                gated.gate(this.gate);
            }
            builder.id(id);
            builder.save();
        }
    }
}
