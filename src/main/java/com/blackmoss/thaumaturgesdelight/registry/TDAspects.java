package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import net.minecraft.resources.ResourceKey;

public final class TDAspects {
    public static final ResourceKey<IAspect> CUPPEDIA = ResourceKey.create(IAspect.REGISTRY_KEY, ThaumaturgesDelight.identifier("cuppedia"));

    private TDAspects() {
    }
}
