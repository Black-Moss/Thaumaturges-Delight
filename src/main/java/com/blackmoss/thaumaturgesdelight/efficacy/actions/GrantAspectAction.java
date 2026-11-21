package com.blackmoss.thaumaturgesdelight.efficacy.actions;

import com.blackmoss.thaumaturgesdelight.efficacy.AspectEffect;
import com.blackmoss.thaumaturgesdelight.efficacy.EfficacyType;
import com.blackmoss.thaumaturgesdelight.registry.TDEfficacies;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.research.pool.AspectPools;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record GrantAspectAction(int amount) implements AspectEffect {
    public static final MapCodec<GrantAspectAction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("amount").forGetter(GrantAspectAction::amount)
            ).apply(instance, GrantAspectAction::new));

    public GrantAspectAction {
        if (amount < 1) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }

    @Override
    public EfficacyType<?> type() {
        return TDEfficacies.GRANT_ASPECT.get();
    }

    @Override
    public void apply(Player player, Holder<IAspect> aspect, ItemStack stack) {
        AspectEffect.requireArgs(player, aspect, stack);
        if (player instanceof ServerPlayer serverPlayer) {
            AspectPools.grant(serverPlayer, aspect, this.amount);
        }
    }
}
