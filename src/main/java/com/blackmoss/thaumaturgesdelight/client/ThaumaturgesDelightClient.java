package com.blackmoss.thaumaturgesdelight.client;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.client.hud.ArcaneCookingPotScreen;
import com.blackmoss.thaumaturgesdelight.client.particle.HungerLegParticle;
import com.blackmoss.thaumaturgesdelight.registry.TDMenus;
import com.blackmoss.thaumaturgesdelight.registry.TDMobTraits;
import com.blackmoss.thaumaturgesdelight.registry.TDParticles;
import com.blackmoss.thaumaturgesdelight.trait.GluttonousTrait;
import com.leclowndu93150.thaumaturge.client.trait.MobTraitVisuals;
import net.minecraft.world.item.crafting.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.List;

@EventBusSubscriber(modid = ThaumaturgesDelight.MODID, value = Dist.CLIENT)
public final class ThaumaturgesDelightClient {
    private static RecipeMap recipeMap = RecipeMap.EMPTY;

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(TDMenus.ARCANE_COOKING_POT.get(), ArcaneCookingPotScreen::new);
    }

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        recipeMap = event.getRecipeMap();
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(TDParticles.HUNGER_LEG.get(), HungerLegParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModList.get()
                .getModContainerById(ThaumaturgesDelight.MODID)
                .ifPresent(container ->
                        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new));

        event.enqueueWork(() -> MobTraitVisuals.registerParticles(
                TDMobTraits.GLUTTONOUS.getKey(), GluttonousTrait::gluttonous));
    }

    public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> byType(RecipeType<T> type) {
        return List.copyOf(recipeMap.byType(type));
    }
}
