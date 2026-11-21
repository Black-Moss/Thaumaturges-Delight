package com.blackmoss.lazycat.thaumaturgesdelight.efficacy;

import com.blackmoss.lazycat.api.datamap.AbstractDataMapBuilder;
import com.blackmoss.lazycat.api.datamap.DataMapContext;
import com.blackmoss.lazycat.api.datamap.DataMapTargets;
import com.blackmoss.thaumaturgesdelight.efficacy.AspectEffect;
import com.blackmoss.thaumaturgesdelight.efficacy.AspectEfficacies;
import com.blackmoss.thaumaturgesdelight.efficacy.actions.AttributeAction;
import com.blackmoss.thaumaturgesdelight.efficacy.actions.GrantAspectAction;
import com.blackmoss.thaumaturgesdelight.efficacy.actions.IgniteAction;
import com.blackmoss.thaumaturgesdelight.efficacy.actions.MobEffectAction;
import com.blackmoss.thaumaturgesdelight.efficacy.actions.ParticleAction;
import com.blackmoss.thaumaturgesdelight.efficacy.actions.RestoreVisAction;
import com.blackmoss.thaumaturgesdelight.registry.TDDataMaps;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public final class EfficacyDataMapBuilder extends AbstractDataMapBuilder<IAspect, AspectEfficacies> {
    private static final int DEFAULT_DURATION = 30;
    private final List<AspectEffect> effects = new ArrayList<>();

    public EfficacyDataMapBuilder(DataMapContext context, ResourceKey<IAspect> aspect) {
        this(context, DataMapTargets.entry(Objects.requireNonNull(aspect, "aspect")));
    }

    private EfficacyDataMapBuilder(
            DataMapContext context,
            BiConsumer<DataMapProvider.Builder<AspectEfficacies, IAspect>, AspectEfficacies> target) {
        super(context, TDDataMaps.ASPECT_EFFICACY, target);
    }

    public EfficacyDataMapBuilder effect(AspectEffect effect) {
        this.effects.add(Objects.requireNonNull(effect, "effect"));
        return this;
    }

    public EfficacyDataMapBuilder effects(AspectEffect... effects) {
        Objects.requireNonNull(effects, "effects");
        for (AspectEffect effect : effects) {
            effect(effect);
        }
        return this;
    }

    public EfficacyDataMapBuilder effects(Holder<MobEffect> effect) {
        return effects(effect, DEFAULT_DURATION, 0);
    }

    public EfficacyDataMapBuilder effects(Holder<MobEffect> effect, int duration) {
        return effects(effect, duration, 0);
    }

    public EfficacyDataMapBuilder effects(Holder<MobEffect> effect, int duration, int amplifier) {
        return effect(new MobEffectAction(effect, duration, amplifier));
    }

    public EfficacyDataMapBuilder particle(Identifier particleId, int count, double speed, double spread) {
        return effect(new ParticleAction(particleId, count, speed, spread));
    }

    public EfficacyDataMapBuilder attribute(Holder<Attribute> attribute, Identifier modifier, double amount) {
        return attribute(attribute, modifier, amount, AttributeModifier.Operation.ADD_VALUE, DEFAULT_DURATION);
    }

    public EfficacyDataMapBuilder attribute(
            Holder<Attribute> attribute,
            Identifier modifier,
            double amount,
            int duration) {
        return attribute(attribute, modifier, amount, AttributeModifier.Operation.ADD_VALUE, duration);
    }

    public EfficacyDataMapBuilder attribute(
            Holder<Attribute> attribute,
            Identifier modifier,
            double amount,
            AttributeModifier.Operation operation,
            int duration) {
        return effect(new AttributeAction(attribute, modifier, amount, operation, duration));
    }

    public EfficacyDataMapBuilder ignite(int ticks) {
        return effect(new IgniteAction(ticks));
    }

    public EfficacyDataMapBuilder restoreVis(int centivis) {
        return effect(new RestoreVisAction(centivis));
    }

    public EfficacyDataMapBuilder grantAspect(int amount) {
        return effect(new GrantAspectAction(amount));
    }

    @Override
    protected AspectEfficacies build() {
        if (this.effects.isEmpty()) {
            throw new IllegalStateException("efficacy entry needs at least one effect");
        }
        return new AspectEfficacies(List.copyOf(this.effects));
    }
}
