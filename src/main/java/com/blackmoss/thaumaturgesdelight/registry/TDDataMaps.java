package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.efficacy.AspectEfficacies;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = ThaumaturgesDelight.MODID)
public final class TDDataMaps {
    public static final DataMapType<IAspect, AspectEfficacies> ASPECT_EFFICACY =
            DataMapType.builder(
                            ThaumaturgesDelight.identifier("aspect_efficacy"),
                            IAspect.REGISTRY_KEY,
                            AspectEfficacies.CODEC)
                    .synced(AspectEfficacies.CODEC, false)
                    .build();

    @SubscribeEvent
    public static void onRegister(RegisterDataMapTypesEvent event) {
        event.register(ASPECT_EFFICACY);
    }
}
