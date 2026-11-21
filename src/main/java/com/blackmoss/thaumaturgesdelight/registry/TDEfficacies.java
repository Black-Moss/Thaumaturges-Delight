package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.efficacy.AspectEffect;
import com.blackmoss.thaumaturgesdelight.efficacy.EfficacyType;
import com.blackmoss.thaumaturgesdelight.efficacy.actions.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TDEfficacies {
    public static final DeferredRegister<EfficacyType<?>> EFFICACIES =
            DeferredRegister.create(EfficacyType.REGISTRY_KEY, ThaumaturgesDelight.MODID);
    public static final Registry<EfficacyType<?>> REGISTRY = EFFICACIES.makeRegistry(
            builder -> builder.sync(false));

    public static final DeferredHolder<EfficacyType<?>, EfficacyType<MobEffectAction>> MOB_EFFECT = register(
            "mob_effect", MobEffectAction.CODEC);
    public static final DeferredHolder<EfficacyType<?>, EfficacyType<AttributeAction>> ATTRIBUTE = register(
            "attribute", AttributeAction.CODEC);
    public static final DeferredHolder<EfficacyType<?>, EfficacyType<IgniteAction>> IGNITE = register(
            "ignite", IgniteAction.CODEC);
    public static final DeferredHolder<EfficacyType<?>, EfficacyType<GrantAspectAction>> GRANT_ASPECT = register(
            "grant_aspect", GrantAspectAction.CODEC);
    public static final DeferredHolder<EfficacyType<?>, EfficacyType<RestoreVisAction>> RESTORE_VIS = register(
            "restore_vis", RestoreVisAction.CODEC);
    public static final DeferredHolder<EfficacyType<?>, EfficacyType<ParticleAction>> PARTICLE = register(
            "particle", ParticleAction.CODEC);

    private static <T extends AspectEffect> DeferredHolder<EfficacyType<?>, EfficacyType<T>> register(
            String name, MapCodec<T> codec) {
        return EFFICACIES.register(name, () -> new EfficacyType<>(codec));
    }
}
