package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemArm;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemComponent;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPartModel;
import com.leclowndu93150.thaumaturge.registry.TTGolemTraits;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class TDGolemParts {
    public static final DeferredRegister<GolemArm> GOLEM_ARMS = DeferredRegister.create(GolemArm.REGISTRY_KEY, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<GolemArm, GolemArm> KNIFE_ARMS = GOLEM_ARMS.register("knife",
            () -> new GolemArm(
                    List.of(ThaumaturgesDelight.identifier("seal_cutting")),
                    ThaumaturgesDelight.identifier("textures/misc/golem/knife_arms.png"),
                    new GolemPartModel(ThaumaturgesDelight.identifier("models/mesh/golem_knife_arms.ttmesh"), null, GolemPartModel.AttachPoint.ARMS),
                    List.of(GolemComponent.of(() -> new ItemStack(TDItems.BRASS_KNIFE.get(), 2)),
                            GolemComponent.base(),
                            GolemComponent.mechanism()),
                    null,
                    List.of(TTGolemTraits.CLUMSY)));

    public TDGolemParts() {
    }
}
