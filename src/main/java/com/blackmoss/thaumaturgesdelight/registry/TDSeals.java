package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.seal.CuttingBoardBehavior;
import com.leclowndu93150.thaumaturge.api.golems.seals.SealFilterMode;
import com.leclowndu93150.thaumaturge.api.golems.seals.SealType;
import com.leclowndu93150.thaumaturge.content.golem.seals.behavior.ItemMatchSettings;
import com.leclowndu93150.thaumaturge.registry.TCGolemTraits;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class TDSeals {
    public static final DeferredRegister<SealType> SEALS = DeferredRegister.create(SealType.REGISTRY_KEY, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<SealType, SealType> CUTTING = SEALS.register("cutting",
            () -> SealType.builder(CuttingBoardBehavior::new)
                    .placement(CuttingBoardBehavior.ON_CUTTING_BOARD)
                    .requires(TCGolemTraits.DEFT)
                    .placer(TDItems.SEAL_CUTTING)
                    .build());

    public static final DeferredHolder<SealType, SealType> ADVANCED_CUTTING = SEALS.register("advanced_cutting",
            () -> SealType.builder(CuttingBoardBehavior::new)
                    .filter(2, SealFilterMode.PLAIN)
                    .settings(ItemMatchSettings.ALL)
                    .showSettings()
                    .placement(CuttingBoardBehavior.ON_CUTTING_BOARD)
                    .requires(TCGolemTraits.DEFT)
                    .placer(TDItems.SEAL_ADVANCED_CUTTING)
                    .build());

    private TDSeals() {
    }
}
