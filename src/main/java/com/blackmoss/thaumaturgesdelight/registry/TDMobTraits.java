package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.trait.GluttonousTrait;
import com.leclowndu93150.thaumaturge.api.entity.trait.MobTrait;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TDMobTraits {
    public static final DeferredRegister<MobTrait> MOB_TRAITS = DeferredRegister.create(MobTrait.REGISTRY_KEY, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<MobTrait, GluttonousTrait> GLUTTONOUS = MOB_TRAITS.register(
            "gluttonous", GluttonousTrait::new);
}
