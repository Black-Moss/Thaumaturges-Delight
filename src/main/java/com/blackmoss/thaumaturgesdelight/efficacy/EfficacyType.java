package com.blackmoss.thaumaturgesdelight.efficacy;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public record EfficacyType<T extends AspectEffect>(MapCodec<T> codec) {
    public static final ResourceKey<Registry<EfficacyType<?>>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(ThaumaturgesDelight.identifier("efficacy_type"));
}
