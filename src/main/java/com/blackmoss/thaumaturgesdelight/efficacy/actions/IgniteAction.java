package com.blackmoss.thaumaturgesdelight.efficacy.actions;

import com.blackmoss.thaumaturgesdelight.efficacy.AspectEffect;
import com.blackmoss.thaumaturgesdelight.efficacy.EfficacyType;
import com.blackmoss.thaumaturgesdelight.registry.TDEfficacies;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record IgniteAction(int ticks) implements AspectEffect {
    public static final MapCodec<IgniteAction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("ticks").forGetter(IgniteAction::ticks)
            ).apply(instance, IgniteAction::new));

    public IgniteAction {
        if (ticks < 1) {
            throw new IllegalArgumentException("ticks must be positive");
        }
    }

    @Override
    public EfficacyType<?> type() {
        return TDEfficacies.IGNITE.get();
    }

    @Override
    public void apply(Player player, Holder<IAspect> aspect, ItemStack stack) {
        AspectEffect.requireArgs(player, aspect, stack);
        player.igniteForTicks(this.ticks);
    }
}
