package com.blackmoss.thaumaturgesdelight.mixin;

import com.leclowndu93150.thaumaturge.api.items.InfusionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(InfusionEnchantment.class)
public enum InfusionEnchantmentMixin {
    THAUMATURGES_DELIGHT_BLEEDING_EDGE("bleeding_edge", Set.of("weapon"), 3);

    @Shadow
    InfusionEnchantmentMixin(String name, Set<String> toolClasses, int maxLevel) {
    }
}
