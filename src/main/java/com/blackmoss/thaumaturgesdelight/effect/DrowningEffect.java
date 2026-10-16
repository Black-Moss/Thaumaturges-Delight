package com.blackmoss.thaumaturgesdelight.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

public class DrowningEffect extends MobEffect {
    private static final int DAMAGE_INTERVAL_TICKS = 20;
    private static final float DAMAGE_AMOUNT = 2.0F;

    public DrowningEffect() {
        super(MobEffectCategory.HARMFUL, 0x3A6EA5);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(
            @NonNull ServerLevel level,
            LivingEntity entity,
            int amplifier) {
        if (entity.getAirSupply() > 0) {
            entity.setAirSupply(0);
        }
        if (entity.tickCount % DAMAGE_INTERVAL_TICKS == 0) {
            //noinspection deprecation
            entity.hurt(entity.damageSources().drown(), DAMAGE_AMOUNT);
        }
        return true;
    }
}