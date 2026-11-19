package com.blackmoss.thaumaturgesdelight;

import com.blackmoss.thaumaturgesdelight.config.ThaumaturgesDelightClientConfig;
import com.blackmoss.thaumaturgesdelight.config.ThaumaturgesDelightCommonConfig;
import com.blackmoss.thaumaturgesdelight.data.*;
import com.blackmoss.thaumaturgesdelight.data.lang.EnUsProvider;
import com.blackmoss.thaumaturgesdelight.data.lang.ZhCnProvider;
import com.blackmoss.thaumaturgesdelight.data.tag.ModBlockTagsProvider;
import com.blackmoss.thaumaturgesdelight.data.tag.ModEntityTypeTagsProvider;
import com.blackmoss.thaumaturgesdelight.data.tag.ModItemTagsProvider;
import com.blackmoss.thaumaturgesdelight.data.tag.ModMobEffectTagsProvider;
import com.blackmoss.thaumaturgesdelight.registry.*;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.data.worldgen.aspect.AspectBootstrap;
import com.mojang.logging.LogUtils;
import net.minecraft.SharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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
        TDMobEffects.MOB_EFFECTS.register(modEventBus);
        TDSeals.SEALS.register(modEventBus);
        TDGolemParts.GOLEM_ARMS.register(modEventBus);
        TDCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        TDWandParts.CAPS.register(modEventBus);
        TDWandParts.RODS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.CLIENT, ThaumaturgesDelightClientConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, ThaumaturgesDelightCommonConfig.SPEC);

    }

    public static Identifier identifier(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

    public static Identifier fd(String path) {
        return Identifier.fromNamespaceAndPath("farmersdelight", path);
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        RegistrySetBuilder registries = new RegistrySetBuilder()
                .add(IAspect.REGISTRY_KEY, ctx -> {
                    AspectBootstrap.bootstrap(ctx);
                    TDAspects.bootstrap(ctx);
                });
        event.createDatapackRegistryObjects(registries);

        event.createProvider(EnUsProvider::new);
        event.createProvider(ZhCnProvider::new);

        event.createProvider(ModModelProvider::new);
        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModResearchProvider::new);
        event.createProvider(ModBlueprintProvider::new);

        event.createProvider(ModBlockTagsProvider::new);
        event.createProvider(ModEntityTypeTagsProvider::new);
        event.createProvider(ModItemTagsProvider::new);
        event.createProvider(ModMobEffectTagsProvider::new);

        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(
                        ModBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider));
    }

    private static HolderLookup.Provider withPlaceholders(HolderLookup.Provider parent) {
        return new HolderLookup.Provider() {
            @Override
            public Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
                return parent.listRegistryKeys();
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> Optional<HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> key) {
                return parent.lookup(key).map(found -> (HolderLookup.RegistryLookup<T>) withPlaceholders((HolderLookup.RegistryLookup<T>) found));
            }
        };
    }

    private static <T> HolderLookup.RegistryLookup<T> withPlaceholders(HolderLookup.RegistryLookup<T> parent) {
        return new HolderLookup.RegistryLookup.Delegate<T>() {
            @Override
            public HolderLookup.RegistryLookup<T> parent() {
                return parent;
            }

            @Override
            public Optional<Holder.Reference<T>> get(ResourceKey<T> key) {
                return Optional.of(parent.get(key).orElseGet(() -> Holder.Reference.createStandAlone(parent, key)));
            }
        };
    }
}
