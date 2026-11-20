package com.blackmoss.thaumaturgesdelight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.NonNull;

public final class HungerLegParticle extends SingleQuadParticle {
    private static final int LIFETIME_BASE = 20;
    private static final int LIFETIME_RANGE = 10;
    private static final float SIZE_BASE = 0.35F;
    private static final float SIZE_SPREAD = 0.15F;
    private static final float RISE = 0.01F;

    private HungerLegParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, RandomSource random) {
        super(level, x, y, z, sprites.first());
        this.lifetime = LIFETIME_BASE + random.nextInt(LIFETIME_RANGE);
        this.quadSize = SIZE_BASE + random.nextFloat() * SIZE_SPREAD;
        this.yd = RISE;
        this.hasPhysics = false;
        this.gravity = 0.0F;
        this.friction = 1.0F;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0F - (float) this.age / this.lifetime;
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static final class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(
                @NonNull SimpleParticleType options,
                @NonNull ClientLevel level,
                double x, double y, double z,
                double vx, double vy, double vz,
                @NonNull RandomSource random) {
            return new HungerLegParticle(level, x, y, z, sprites, random);
        }
    }
}
