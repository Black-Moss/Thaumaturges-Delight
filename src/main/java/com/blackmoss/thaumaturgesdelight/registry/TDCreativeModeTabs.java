package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.item.EssentiaRockCandyFactory;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantmentHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Comparator;

public class TDCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ThaumaturgesDelight.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THAUMATURGES_DELIGHT = CREATIVE_MODE_TABS.register(ThaumaturgesDelight.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thaumaturgesdelight"))
                    .icon(() -> TDItems.ELEMENTAL_KNIFE.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(TDItems.BRASS_KNIFE.get());
                        output.accept(TDItems.THAUMIUM_KNIFE.get());

                        ItemStack elementalKnife = new ItemStack(TDItems.ELEMENTAL_KNIFE.get());
                        InfusionEnchantmentHelper.add(elementalKnife, TDInfusionEnchantments.BLEEDING_EDGE, 1);
                        output.accept(elementalKnife);

                        output.accept(TDItems.VOID_KNIFE.get());

                        HolderLookup.RegistryLookup<IAspect> aspectRegistry = parameters.holders().lookupOrThrow(IAspect.REGISTRY_KEY);

                        output.accept(TDItems.ARCANE_COOKING_POT.get());
                        output.accept(TDItems.ARCANE_STOVE.get());

                        output.accept(TDItems.SEXTUPLE_MEAT_TREAT.get());
                        output.accept(TDItems.PURIFY_COOKIE.get());
                        output.accept(TDItems.SEAL_CUTTING.get());
                        output.accept(TDItems.SEAL_ADVANCED_CUTTING.get());
                        output.accept(TDItems.CHUNKS_FRIED_RICE);

                        for (Holder<IAspect> aspect : aspectRegistry
                                .listElements()
                                .sorted(Comparator.comparing((h) -> !h.value().isPrimal()))
                                .toList()) {
                            output.accept(EssentiaRockCandyFactory.of(aspect));
                        }
                    }).build());

}
