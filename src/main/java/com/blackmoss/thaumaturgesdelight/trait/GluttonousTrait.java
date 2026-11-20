package com.blackmoss.thaumaturgesdelight.trait;

import com.blackmoss.thaumaturgesdelight.registry.TDParticles;
import com.leclowndu93150.thaumaturge.content.entity.champion.trait.AbstractChampionTrait;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class GluttonousTrait extends AbstractChampionTrait {
    private static final int MAX_FOOD_PER_BITE = 4;
    private static final float HEALTH_PER_FOOD = 2.0F;
    private static final float MINIMUM_HEAL = 1.0F;
    private static final float PARTICLE_CHANCE = 0.2F;
    private static final double PARTICLE_RISE = 0.02;

    @SuppressWarnings("unused")
    public static void gluttonous(LivingEntity mob, Level level, RandomSource random, double x, double y, double z) {
        if (random.nextFloat() < PARTICLE_CHANCE) {
            level.addParticle(TDParticles.HUNGER_LEG.get(), x, y, z, 0.0, PARTICLE_RISE, 0.0);
        }
    }

    @Override
    public float onAttack(LivingEntity mob, LivingEntity target, DamageSource source, float amount) {
        if (!(target instanceof Player player)) {
            return amount;
        }

        int food = player.getFoodData().getFoodLevel();
        if (food <= 0) {
            return amount;
        }

        int drained = Math.min(MAX_FOOD_PER_BITE, food);
        player.getFoodData().setFoodLevel(food - drained);
        mob.heal(Math.max(MINIMUM_HEAL, drained * HEALTH_PER_FOOD));

        return amount;
    }
}
