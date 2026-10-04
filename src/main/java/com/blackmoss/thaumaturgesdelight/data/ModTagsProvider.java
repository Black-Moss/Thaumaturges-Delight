package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.blackmoss.thaumaturgesdelight.registry.TDTags;
import com.leclowndu93150.thaumaturge.registry.TCMobEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class ModTagsProvider implements DataProvider {
    private final IntrinsicHolderTagsProvider<Item> itemTags;
    private final IntrinsicHolderTagsProvider<Block> blockTags;
    private final IntrinsicHolderTagsProvider<EntityType<?>> entityTypeTags;
    private final KeyTagProvider<MobEffect> mobEffectTags;


    public ModTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        //noinspection deprecation
        this.itemTags = new IntrinsicHolderTagsProvider<>(output, Registries.ITEM, lookup,
                item -> item.builtInRegistryHolder().key(), ThaumaturgesDelight.MODID) {
            @Override
            protected void addTags(HolderLookup.@NonNull Provider provider) {
                tag(CommonTags.Items.TOOLS_KNIFE)
                        .add(TDItems.BRASS_KNIFE.get())
                        .add(TDItems.THAUMIUM_KNIFE.get())
                        .add(TDItems.VOID_KNIFE.get())
                        .add(TDItems.ELEMENTAL_KNIFE.get());
                tag(ModTags.Items.KNIVES)
                        .add(TDItems.BRASS_KNIFE.get())
                        .add(TDItems.THAUMIUM_KNIFE.get())
                        .add(TDItems.VOID_KNIFE.get())
                        .add(TDItems.ELEMENTAL_KNIFE.get());
            }
        };

        //noinspection deprecation
        this.blockTags = new IntrinsicHolderTagsProvider<>(output, Registries.BLOCK, lookup,
                block -> block.builtInRegistryHolder().key(), ThaumaturgesDelight.MODID) {
            @Override
            protected void addTags(HolderLookup.@NonNull Provider provider) {
            }
        };

        //noinspection deprecation
        this.entityTypeTags = new IntrinsicHolderTagsProvider<>(output, Registries.ENTITY_TYPE, lookup,
                entityType -> entityType.builtInRegistryHolder().key(), ThaumaturgesDelight.MODID) {
            @Override
            protected void addTags(HolderLookup.@NonNull Provider provider) {
                tag(TDTags.EntityTypes.COW)
                        .add(EntityType.COW);

                tag(TDTags.EntityTypes.CHICKENS)
                        .add(EntityType.CHICKEN);

                tag(TDTags.EntityTypes.PIGS)
                        .add(EntityType.PIG)
                        .add(EntityType.PIGLIN)
                        .add(EntityType.PIGLIN_BRUTE)
                        .add(EntityType.ZOMBIFIED_PIGLIN);

                tag(TDTags.EntityTypes.FISH)
                        .add(EntityType.COD)
                        .add(EntityType.SALMON)
                        .add(EntityType.PUFFERFISH)
                        .add(EntityType.TROPICAL_FISH);

                tag(TDTags.EntityTypes.RABBITS)
                        .add(EntityType.RABBIT);

                tag(TDTags.EntityTypes.SHEEP)
                        .add(EntityType.SHEEP)
                        .add(EntityType.GOAT);
            }
        };

        this.mobEffectTags = new KeyTagProvider<>(output, Registries.MOB_EFFECT, lookup, ThaumaturgesDelight.MODID) {
            @Override
            protected void addTags(HolderLookup.@NonNull Provider provider) {
                tag(TDTags.Effects.ROCK_CANDY_EXCLUDED)
                        .add(Objects.requireNonNull(MobEffects.SLOWNESS.getKey()))
                        .add(Objects.requireNonNull(MobEffects.MINING_FATIGUE.getKey()))
                        .add(Objects.requireNonNull(MobEffects.INSTANT_DAMAGE.getKey()))
                        .add(Objects.requireNonNull(MobEffects.NAUSEA.getKey()))
                        .add(Objects.requireNonNull(MobEffects.BLINDNESS.getKey()))
                        .add(Objects.requireNonNull(MobEffects.HUNGER.getKey()))
                        .add(Objects.requireNonNull(MobEffects.WEAKNESS.getKey()))
                        .add(Objects.requireNonNull(MobEffects.POISON.getKey()))
                        .add(Objects.requireNonNull(MobEffects.WITHER.getKey()))
                        .add(Objects.requireNonNull(MobEffects.UNLUCK.getKey()))
                        .add(Objects.requireNonNull(MobEffects.BAD_OMEN.getKey()))
                        .add(Objects.requireNonNull(MobEffects.DARKNESS.getKey()))
                        .add(Objects.requireNonNull(MobEffects.TRIAL_OMEN.getKey()))
                        .add(Objects.requireNonNull(MobEffects.WIND_CHARGED.getKey()))
                        .add(MobEffects.WEAKNESS.getKey())
                        .add(Objects.requireNonNull(MobEffects.OOZING.getKey()))
                        .add(Objects.requireNonNull(MobEffects.INFESTED.getKey()))
                        .add(Objects.requireNonNull(MobEffects.RAID_OMEN.getKey()))
                        .add(Objects.requireNonNull(TCMobEffects.BLURRED_VISION.getKey()))
                        .add(Objects.requireNonNull(TCMobEffects.DEATH_GAZE.getKey()))
                        .add(Objects.requireNonNull(TCMobEffects.FLUX_TAINT.getKey()))
                        .add(Objects.requireNonNull(TCMobEffects.INFECTIOUS_VIS_EXHAUST.getKey()))
                        .add(Objects.requireNonNull(TCMobEffects.SUN_SCORNED.getKey()))
                        .add(Objects.requireNonNull(TCMobEffects.THAUMARHIA.getKey()))
                        .add(Objects.requireNonNull(TCMobEffects.UNNATURAL_HUNGER.getKey()))
                        .add(Objects.requireNonNull(TCMobEffects.VIS_EXHAUST.getKey()));
            }
        };
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        return CompletableFuture.allOf(itemTags.run(cache), blockTags.run(cache), entityTypeTags.run(cache), mobEffectTags.run(cache));
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge's Delight Tags";
    }
}
