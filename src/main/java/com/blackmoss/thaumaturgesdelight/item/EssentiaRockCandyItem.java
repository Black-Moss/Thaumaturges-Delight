package com.blackmoss.thaumaturgesdelight.item;

import com.blackmoss.thaumaturgesdelight.registry.TDTags;
import com.leclowndu93150.thaumaturge.api.aspect.AspectComponents;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.research.pool.AspectPools;
import com.leclowndu93150.thaumaturge.registry.TCDataComponents;
import com.leclowndu93150.thaumaturge.registry.TCEffectTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EssentiaRockCandyItem extends Item {
    private static final int EFFECT_AMPLIFIER = 2;
    private static final double INSTANT_HEALTH_FACTOR = 3.0;
    private static final int EFFECT_BASE_DURATION = 160;
    private static final int EFFECT_EXTRA_DURATION = 80;
    private static final float GRANT_CHANCE = 0.25F;

    public EssentiaRockCandyItem(Properties properties) {
        super(properties);
    }

    public static @Nullable Holder<IAspect> aspectOf(ItemStack stack) {
        AspectInstance stored = (AspectInstance) stack.get((DataComponentType<?>) TCDataComponents.CRYSTAL_ASPECT.get());
        return stored == null ? null : stored.aspect();
    }

    public static int colorOf(ItemStack stack) {
        Holder<IAspect> aspect = aspectOf(stack);
        return aspect == null ? 16777215 : aspect.value().color();
    }

    public @NonNull Component getName(@NonNull ItemStack stack) {
        Holder<IAspect> aspect = aspectOf(stack);
        return aspect == null
                ? Component.translatable("item.thaumaturgesdelight.essentia_rock_candy.unknown")
                : Component.translatable("item.thaumaturgesdelight.essentia_rock_candy", AspectComponents.name(aspect));
    }

    public @NonNull ItemStack finishUsingItem(
            @NonNull ItemStack stack,
            @NonNull Level level,
            @NonNull LivingEntity entity) {
        if (level instanceof ServerLevel serverLevel) {
            if (entity instanceof ServerPlayer player) {
                RandomSource random = serverLevel.getRandom();
                Optional<HolderSet.Named<MobEffect>> pool = serverLevel
                        .registryAccess()
                        .lookupOrThrow(TCEffectTags.MANA_BEAN_EFFECTS.registry())
                        .get(TCEffectTags.MANA_BEAN_EFFECTS);
                if (pool.isPresent()) {
                    List<Holder<MobEffect>> candidates = pool.get().stream()
                            .filter(effect -> !effect.is(TDTags.Effects.ROCK_CANDY_EXCLUDED))
                            .toList();
                    if (!candidates.isEmpty()) {
                        Holder<MobEffect> effect = candidates.get(random.nextInt(candidates.size()));
                        if (effect.value().isInstantenous()) {
                            effect.value().applyInstantenousEffect(serverLevel, player, player, player, EFFECT_AMPLIFIER, INSTANT_HEALTH_FACTOR);
                        } else {
                            player.addEffect(new MobEffectInstance(effect, EFFECT_BASE_DURATION + random.nextInt(EFFECT_EXTRA_DURATION), 0));
                        }
                    }
                }

                Holder<IAspect> aspect = aspectOf(stack);
                if (aspect != null && random.nextFloat() < GRANT_CHANCE) {
                    AspectPools.grant(player, aspect, 1);
                }
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }
}
