package com.blackmoss.thaumaturgesdelight;

import com.blackmoss.thaumaturgesdelight.data.ModBlockLootSubProvider;
import com.blackmoss.thaumaturgesdelight.data.ModModelProvider;
import com.blackmoss.thaumaturgesdelight.data.ModRecipeProvider;
import com.blackmoss.thaumaturgesdelight.data.ModTagsProvider;
import com.blackmoss.thaumaturgesdelight.data.lang.EnUsProvider;
import com.blackmoss.thaumaturgesdelight.data.lang.ZhCnProvider;
import com.blackmoss.thaumaturgesdelight.registry.*;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.data.worldgen.aspect.AspectBootstrap;
import com.mojang.logging.LogUtils;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = ThaumaturgesDelight.MODID)
@Mod(ThaumaturgesDelight.MODID)
public class ThaumaturgesDelight {
    public static final String MODID = "thaumaturgesdelight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ThaumaturgesDelight(IEventBus modEventBus, ModContainer modContainer) {
        TDBlocks.BLOCKS.register(modEventBus);
        TDBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        TDRecipeTypes.RECIPE_TYPES.register(modEventBus);
        TDRecipeTypes.RECIPE_SERIALIZERS.register(modEventBus);
        TDMenus.MENUS.register(modEventBus);
        TDItems.ITEMS.register(modEventBus);
        TDSeals.SEALS.register(modEventBus);
        TDCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    public static Identifier fdIdentifier(String path) {
        return Identifier.fromNamespaceAndPath("farmersdelight", path);
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        event.createProvider(EnUsProvider::new);
        event.createProvider(ZhCnProvider::new);

        RegistrySetBuilder registries = new RegistrySetBuilder()
                .add(IAspect.REGISTRY_KEY, AspectBootstrap::bootstrap);
        event.createDatapackRegistryObjects(registries);

        event.createProvider(ModModelProvider::new);
        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModTagsProvider::new);

        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(
                        ModBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider));
    }
}
