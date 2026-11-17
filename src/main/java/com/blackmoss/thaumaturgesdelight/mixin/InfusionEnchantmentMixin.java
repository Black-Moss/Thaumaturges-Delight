package com.blackmoss.thaumaturgesdelight.mixin;

import com.leclowndu93150.thaumaturge.api.items.InfusionEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InfusionEnchantment.class)
public enum InfusionEnchantmentMixin {
    THAUMATURGES_DELIGHT_BLEEDING_EDGE("bleeding_edge", 3, "weapon");

    @Shadow
    InfusionEnchantmentMixin(String serializedName, int maxLevel, String... toolClasses) {
    }
}
