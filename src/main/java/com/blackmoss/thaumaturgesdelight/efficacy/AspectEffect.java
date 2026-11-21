package com.blackmoss.thaumaturgesdelight.efficacy;

import com.blackmoss.thaumaturgesdelight.registry.TDEfficacies;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public interface AspectEffect {
    Codec<AspectEffect> CODEC = Codec.lazyInitialized(() ->
            TDEfficacies.REGISTRY
                    .byNameCodec()
                    .dispatch("type", AspectEffect::type, EfficacyType::codec));

    static void requireArgs(Player player, Holder<IAspect> aspect, ItemStack stack) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(aspect, "aspect");
        Objects.requireNonNull(stack, "stack");
    }

    EfficacyType<?> type();

    void apply(Player player, Holder<IAspect> aspect, ItemStack stack);
}
