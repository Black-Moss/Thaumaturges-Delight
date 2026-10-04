package com.blackmoss.thaumaturgesdelight.registry;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

public class TDTags {
    public static class EntityTypes {
        public static final TagKey<EntityType<?>> COW = modEntityTag("cow");
        public static final TagKey<EntityType<?>> CHICKENS = modEntityTag("chickens");
        public static final TagKey<EntityType<?>> PIGS = modEntityTag("pigs");
        public static final TagKey<EntityType<?>> FISH = modEntityTag("fish");
        public static final TagKey<EntityType<?>> RABBITS = modEntityTag("rabbits");
        public static final TagKey<EntityType<?>> SHEEP = modEntityTag("sheep");

        public EntityTypes() {
        }
    }

    public final class Effects {
        public static final TagKey<MobEffect> ROCK_CANDY_EXCLUDED = effectTag("rock_candy_excluded");

        private Effects() {
        }
    }

    private static TagKey<EntityType<?>> modEntityTag(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, ThaumaturgesDelight.identifier(path));
    }

    private static TagKey<MobEffect> effectTag(String path) {
        return TagKey.create(Registries.MOB_EFFECT, ThaumaturgesDelight.identifier(path));
    }
}
