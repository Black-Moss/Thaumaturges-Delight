package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.aspect.TCAspects;
import com.leclowndu93150.thaumaturge.content.aspect.Aspect;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.Optional;

public final class TDAspects {
    public static final ResourceKey<IAspect> CUPPEDIA = ResourceKey.create(IAspect.REGISTRY_KEY, ThaumaturgesDelight.identifier("cuppedia"));

    public TDAspects() {
    }

    public static void bootstrap(BootstrapContext<IAspect> ctx) {
        Holder<IAspect> a = ctx.lookup(IAspect.REGISTRY_KEY).getOrThrow(TCAspects.DESIDERIUM);
        Holder<IAspect> b = ctx.lookup(IAspect.REGISTRY_KEY).getOrThrow(TCAspects.FABRICO);
        ctx.register(TDAspects.CUPPEDIA, new Aspect(TDAspects.CUPPEDIA.identifier().getPath(),
                16638000,
                List.of(a, b),
                Optional.empty(),
                ThaumaturgesDelight.identifier("textures/aspects/cuppedia.png"),
                1));
    }
}
