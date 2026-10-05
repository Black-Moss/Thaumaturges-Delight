package com.blackmoss.thaumaturgesdelight.client;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = ThaumaturgesDelight.MODID, value = Dist.CLIENT)
public final class TDClientSetup {
    private TDClientSetup() {
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TDMenus.ARCANE_COOKING_POT.get(), ArcaneCookingPotScreen::new);
    }
}
