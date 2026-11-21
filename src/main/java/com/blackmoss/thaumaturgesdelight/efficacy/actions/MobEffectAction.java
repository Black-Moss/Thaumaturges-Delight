package com.blackmoss.thaumaturgesdelight.efficacy.actions;

import com.blackmoss.thaumaturgesdelight.efficacy.AspectEffect;
import com.blackmoss.thaumaturgesdelight.efficacy.EfficacyType;
import com.blackmoss.thaumaturgesdelight.registry.TDEfficacies;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record MobEffectAction(Holder<MobEffect> effect, int duration, int amplifier) implements AspectEffect {
    public static final MapCodec<MobEffectAction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    MobEffect.CODEC.fieldOf("effect").forGetter(MobEffectAction::effect),
                    Codec.INT.fieldOf("duration").forGetter(MobEffectAction::duration),
                    Codec.INT.optionalFieldOf("amplifier", 0).forGetter(MobEffectAction::amplifier)
            ).apply(instance, MobEffectAction::new));

    public MobEffectAction {
        if (duration < 1) {
            throw new IllegalArgumentException("duration must be positive");
        }
        if (amplifier < 0) {
            throw new IllegalArgumentException("amplifier must not be negative");
        }
    }

    @Override
    public EfficacyType<?> type() {
        return TDEfficacies.MOB_EFFECT.get();
    }

    @Override
    public void apply(Player player, Holder<IAspect> aspect, ItemStack stack) {
        AspectEffect.requireArgs(player, aspect, stack);
        player.addEffect(new MobEffectInstance(this.effect, this.duration, this.amplifier));
    }
}
