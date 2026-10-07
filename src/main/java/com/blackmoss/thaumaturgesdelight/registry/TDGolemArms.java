package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemArm;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemComponent;
import com.leclowndu93150.thaumaturge.api.golems.parts.GolemPartModel;
import com.leclowndu93150.thaumaturge.registry.TCGolemTraits;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class TDGolemArms {
    public static final DeferredRegister<GolemArm> GOLEM_ARMS = DeferredRegister.create(GolemArm.REGISTRY_KEY, ThaumaturgesDelight.MODID);

    public static final DeferredHolder<GolemArm, GolemArm> KNIFE_ARMS = GOLEM_ARMS.register("knife",
            () -> new GolemArm(
                    List.of(ThaumaturgesDelight.identifier("seal_cutting")),
                    partIcon("knife_arms"),
                    new GolemPartModel(obj("golem_knife_arms"), null, GolemPartModel.AttachPoint.ARMS),
                    List.of(GolemComponent.of(() -> new ItemStack(TDItems.BRASS_KNIFE.get(), 2)),
                            GolemComponent.base(),
                            GolemComponent.mechanism()),
                    null,
                    List.of(TCGolemTraits.CLUMSY)));

    private TDGolemArms() {
    }

    private static Identifier partIcon(String name) {
        return ThaumaturgesDelight.identifier("textures/misc/golem/" + name + ".png");
    }

    private static Identifier obj(String name) {
        return ThaumaturgesDelight.identifier("models/mesh/" + name + ".tcmesh");
    }
}
