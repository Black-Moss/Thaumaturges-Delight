package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TTAspects;
import com.leclowndu93150.thaumaturge.content.aspect.Aspect;
import java.util.List;
import java.util.Optional;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

public final class TDAspects {
    public static final ResourceKey<IAspect> CUPPEDIA = ResourceKey.create(IAspect.REGISTRY_KEY, ThaumaturgesDelight.identifier("cuppedia"));

    public static void bootstrap(BootstrapContext<IAspect> ctx) {
        var lookup = ctx.lookup(IAspect.REGISTRY_KEY);
        ctx.register(CUPPEDIA, new Aspect("cuppedia", 0xFDE030,
                List.of(lookup.getOrThrow(TTAspects.DESIDERIUM), lookup.getOrThrow(TTAspects.VICTUS)),
                Optional.empty(),
                ThaumaturgesDelight.identifier("textures/aspects/cuppedia.png"),
                Aspect.DEFAULT_BLEND));
    }

    private TDAspects() {
    }
}