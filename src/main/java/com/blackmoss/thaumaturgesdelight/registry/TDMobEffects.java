package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.effect.DrowningEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TDMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, ThaumaturgesDelight.MODID);

    public static final Holder<MobEffect> DROWNING = MOB_EFFECTS.register("drowning", DrowningEffect::new);

    private TDMobEffects() {
    }
}
