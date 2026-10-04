package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TDCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ThaumaturgesDelight.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> THAUMATURGES_DELIGHT = CREATIVE_MODE_TABS.register(ThaumaturgesDelight.MODID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.thaumaturgesdelight"))
                    .icon(() -> TDItems.THAUMIUM_KNIFE.get().getDefaultInstance())
                    .displayItems((_, output) -> {
                        output.accept(TDItems.BRASS_KNIFE.get());
                        output.accept(TDItems.THAUMIUM_KNIFE.get());
                        output.accept(TDItems.ELEMENTAL_KNIFE.get());
                        output.accept(TDItems.VOID_KNIFE.get());
//                        output.accept(TDItems.PRIMAL_VOID_KNIFE.get());
                    }).build());

}
