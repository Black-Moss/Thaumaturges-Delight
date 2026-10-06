package com.blackmoss.thaumaturgesdelight.data;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
import com.blackmoss.thaumaturgesdelight.registry.TDTags;
import com.leclowndu93150.thaumaturge.registry.TCBlockTags;
import com.leclowndu93150.thaumaturge.registry.TCMobEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

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
                tag(Tags.Items.FOODS_COOKIE)
                        .add(TDItems.PURIFY_COOKIE.get());
            }
        };

        //noinspection deprecation
        this.blockTags = new IntrinsicHolderTagsProvider<>(output, Registries.BLOCK, lookup,
                block -> block.builtInRegistryHolder().key(), ThaumaturgesDelight.MODID) {
            @SuppressWarnings("unchecked")
            @Override
            protected void addTags(HolderLookup.@NonNull Provider provider) {
                tag(ModTags.Blocks.HEAT_SOURCES)
                        .addTags(TCBlockTags.CRUCIBLE_HEAT_SOURCES);
                tag(TCBlockTags.CRUCIBLE_HEAT_SOURCES)
                        .add(TDBlocks.ARCANE_STOVE.get());
                tag(BlockTags.MINEABLE_WITH_SHOVEL)
                        .add(TDBlocks.AURA_RICH_SOIL.get())
                        .add(TDBlocks.AURA_RICH_SOIL_FARMLAND.get());
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
                //noinspection DataFlowIssue
                tag(TDTags.Effects.ROCK_CANDY_EXCLUDED)
                        .add(MobEffects.SLOWNESS.getKey())
                        .add(MobEffects.MINING_FATIGUE.getKey())
                        .add(MobEffects.INSTANT_DAMAGE.getKey())
                        .add(MobEffects.NAUSEA.getKey())
                        .add(MobEffects.BLINDNESS.getKey())
                        .add(MobEffects.HUNGER.getKey())
                        .add(MobEffects.WEAKNESS.getKey())
                        .add(MobEffects.POISON.getKey())
                        .add(MobEffects.WITHER.getKey())
                        .add(MobEffects.UNLUCK.getKey())
                        .add(MobEffects.BAD_OMEN.getKey())
                        .add(MobEffects.DARKNESS.getKey())
                        .add(MobEffects.TRIAL_OMEN.getKey())
                        .add(MobEffects.WIND_CHARGED.getKey())
                        .add(MobEffects.WEAKNESS.getKey())
                        .add(MobEffects.OOZING.getKey())
                        .add(MobEffects.INFESTED.getKey())
                        .add(MobEffects.RAID_OMEN.getKey())
                        .add(TCMobEffects.BLURRED_VISION.getKey())
                        .add(TCMobEffects.DEATH_GAZE.getKey())
                        .add(TCMobEffects.FLUX_TAINT.getKey())
                        .add(TCMobEffects.INFECTIOUS_VIS_EXHAUST.getKey())
                        .add(TCMobEffects.SUN_SCORNED.getKey())
                        .add(TCMobEffects.THAUMARHIA.getKey())
                        .add(TCMobEffects.UNNATURAL_HUNGER.getKey())
                        .add(TCMobEffects.VIS_EXHAUST.getKey());
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
