package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.seal.CuttingBoardSealBehavior;
import com.leclowndu93150.thaumaturge.api.golems.seals.SealFilterMode;
import com.leclowndu93150.thaumaturge.api.golems.seals.SealType;
import com.leclowndu93150.thaumaturge.content.golem.seals.behavior.ItemMatchSettings;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("unused")
public final class TDSeals {
    public static final DeferredRegister<SealType> SEALS = DeferredRegister.create(SealType.REGISTRY_KEY, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<SealType, SealType> CUTTING = SEALS.register("cutting",
            () -> SealType.builder(CuttingBoardSealBehavior::new)
                    .placement(CuttingBoardSealBehavior.ON_CUTTING_BOARD)
                    .placer(TDItems.CUTTING_SEAL)
                    .build());

    public static final DeferredHolder<SealType, SealType> ADVANCED_CUTTING = SEALS.register("advanced_cutting",
            () -> SealType.builder(CuttingBoardSealBehavior::new)
                    .filter(2, SealFilterMode.PLAIN)
                    .settings(ItemMatchSettings.ALL)
                    .showSettings()
                    .placement(CuttingBoardSealBehavior.ON_CUTTING_BOARD)
                    .placer(TDItems.ADVANCED_CUTTING_SEAL)
                    .build());
}
