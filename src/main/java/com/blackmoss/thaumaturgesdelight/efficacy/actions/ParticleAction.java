package com.blackmoss.thaumaturgesdelight.efficacy.actions;

import com.blackmoss.thaumaturgesdelight.efficacy.AspectEffect;
import com.blackmoss.thaumaturgesdelight.efficacy.EfficacyType;
import com.blackmoss.thaumaturgesdelight.registry.TDEfficacies;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record ParticleAction(Identifier particleId, int count, double speed, double spread) implements AspectEffect {
    public static final MapCodec<ParticleAction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("particle").forGetter(ParticleAction::particleId),
                    Codec.INT.fieldOf("count").forGetter(ParticleAction::count),
                    Codec.DOUBLE.optionalFieldOf("speed", 0.02).forGetter(ParticleAction::speed),
                    Codec.DOUBLE.optionalFieldOf("spread", 0.3).forGetter(ParticleAction::spread)
            ).apply(instance, ParticleAction::new));

    public ParticleAction {
        if (count < 1) {
            throw new IllegalArgumentException("count must be positive");
        }
    }

    @Override
    public EfficacyType<?> type() {
        return TDEfficacies.PARTICLE.get();
    }

    @Override
    public void apply(Player player, Holder<IAspect> aspect, ItemStack stack) {
        AspectEffect.requireArgs(player, aspect, stack);
        Holder<ParticleType<?>> holder = BuiltInRegistries.PARTICLE_TYPE.get(this.particleId).orElse(null);
        if (holder == null || !(holder.value() instanceof ParticleOptions options)) {
            return;
        }

        Level level = player.level();
        for (int i = 0; i < this.count; i++) {
            double dx = (level.getRandom().nextDouble() - 0.5) * this.spread;
            double dy = level.getRandom().nextDouble() * this.spread;
            double dz = (level.getRandom().nextDouble() - 0.5) * this.spread;
            level.addParticle(options, player.getX() + dx, player.getY() + player.getEyeHeight() + dy,
                    player.getZ() + dz, 0.0, this.speed, 0.0);
        }
    }
}
