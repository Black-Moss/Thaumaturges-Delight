package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

public final class TDEffectTags {
    public static final TagKey<MobEffect> ROCK_CANDY_EXCLUDED = effectTag("rock_candy_excluded");

    private static TagKey<MobEffect> effectTag(String path) {
        return TagKey.create(Registries.MOB_EFFECT, ThaumaturgesDelight.identifier(path));
    }
}
