package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TDParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HUNGER_LEG = PARTICLES.register(
            "hunger_leg", HungerLegParticleType::new);

    public static final class HungerLegParticleType extends SimpleParticleType {
        public HungerLegParticleType() {
            super(false);
        }
    }
}
