package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.api.wands.WandCap;
import com.leclowndu93150.thaumaturge.api.wands.WandRod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class TDWandParts {
    public static final DeferredRegister<WandCap> CAPS = DeferredRegister.create(WandCap.REGISTRY_KEY, ThaumaturgesDelight.MODID);
    public static final DeferredRegister<WandRod> RODS = DeferredRegister.create(WandRod.REGISTRY_KEY, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<WandCap, WandCap> KETCHUP_CAP = CAPS.register("ketchup", () ->
            new WandCap(1.0F, List.of(), 0.0F, 1,
                    ThaumaturgesDelight.identifier("textures/models/ketchup_wand_cap.png")));
    public static final DeferredHolder<WandRod, WandRod> FRENCH_FRY_ROD = RODS.register("french_fry", () ->
            new WandRod(25, 1,
                    ThaumaturgesDelight.identifier("textures/models/french_fry_wand_rod.png"),
                    null, false, false, false,
                    ThaumaturgesDelight.identifier("rod_french_fry")));

    public TDWandParts() {
    }
}
