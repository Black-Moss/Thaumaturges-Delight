package com.blackmoss.thaumaturgesdelight.efficacy;

import com.blackmoss.thaumaturgesdelight.registry.TDDataMaps;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;

public record AspectEfficacies(List<AspectEffect> entries) {
    public static final Codec<AspectEfficacies> CODEC = AspectEffect.CODEC.listOf(1, 16)
            .xmap(AspectEfficacies::new, AspectEfficacies::entries);

    public AspectEfficacies {
        entries = List.copyOf(Objects.requireNonNull(entries, "entries"));
    }

    public static void apply(Player player, Holder<IAspect> aspect, ItemStack stack) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(aspect, "aspect");
        Objects.requireNonNull(stack, "stack");

        AspectEfficacies mapped = aspect.getData(TDDataMaps.ASPECT_EFFICACY);
        if (mapped == null || mapped.entries.isEmpty()) {
            RandomEfficacy.apply(player);
            return;
        }

        for (AspectEffect entry : mapped.entries) {
            entry.apply(player, aspect, stack);
        }
    }
}
