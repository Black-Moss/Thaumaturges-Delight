package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.content.essentia.item.ComponentEssentia;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = ThaumaturgesDelight.MODID)
public final class TDCapabilities {
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                EssentiaCapabilities.CONTAINER,
                (stack, _) -> ComponentEssentia.crystal(stack),
                TDItems.MANA_BEAN_DOUGH.get());
        event.registerItem(
                EssentiaCapabilities.CONTAINER,
                (stack, _) -> ComponentEssentia.crystal(stack),
                TDItems.ESSENTIA_ROCK_CANDY.get());
    }
}
