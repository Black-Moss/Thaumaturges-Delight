package com.blackmoss.thaumaturgesdelight.data.tag;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDTags;
import com.leclowndu93150.thaumaturge.registry.TTMobEffects;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ModMobEffectTagsProvider implements DataProvider {
    private final KeyTagProvider<MobEffect> mobEffectTags;

    public ModMobEffectTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
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
                        .add(TTMobEffects.BLURRED_VISION.getKey())
                        .add(TTMobEffects.DEATH_GAZE.getKey())
                        .add(TTMobEffects.FLUX_TAINT.getKey())
                        .add(TTMobEffects.INFECTIOUS_VIS_EXHAUST.getKey())
                        .add(TTMobEffects.SUN_SCORNED.getKey())
                        .add(TTMobEffects.THAUMARHIA.getKey())
                        .add(TTMobEffects.UNNATURAL_HUNGER.getKey())
                        .add(TTMobEffects.VIS_EXHAUST.getKey());
            }
        };
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        return CompletableFuture.allOf(mobEffectTags.run(cache));
    }

    @Override
    public @NonNull String getName() {
        return "Thaumaturge's Delight Mob Effect Tags";
    }
}
