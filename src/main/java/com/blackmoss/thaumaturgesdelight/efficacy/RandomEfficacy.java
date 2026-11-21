package com.blackmoss.thaumaturgesdelight.efficacy;

import com.blackmoss.thaumaturgesdelight.registry.TDEffectTags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Objects;

public final class RandomEfficacy {
    private static final int BASE_DURATION = 160;
    private static final int EXTRA_DURATION = 80;
    private static final int AMPLIFIER = 0;
    private static final double INSTANT_HEALTH_FACTOR = 1.0;

    public static void apply(Player player) {
        Objects.requireNonNull(player, "player");
        List<? extends Holder<MobEffect>> candidates = player.registryAccess()
                .lookupOrThrow(Registries.MOB_EFFECT)
                .listElements()
                .filter(effect -> !effect.is(TDEffectTags.RANDOM_EFFECT_EXCLUDED))
                .toList();
        if (candidates.isEmpty()) {
            return;
        }

        Holder<MobEffect> effect = candidates.get(player.getRandom().nextInt(candidates.size()));
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        if (effect.value().isInstantenous()) {
            effect.value().applyInstantenousEffect(serverLevel, player, player, player, AMPLIFIER, INSTANT_HEALTH_FACTOR);
        } else {
            player.addEffect(new MobEffectInstance(
                    effect, BASE_DURATION + player.getRandom().nextInt(EXTRA_DURATION), AMPLIFIER));
        }
    }
}
