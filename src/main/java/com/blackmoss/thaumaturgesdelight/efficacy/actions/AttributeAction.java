package com.blackmoss.thaumaturgesdelight.efficacy.actions;

import com.blackmoss.thaumaturgesdelight.efficacy.AspectEffect;
import com.blackmoss.thaumaturgesdelight.efficacy.AttributeEfficacyHandler;
import com.blackmoss.thaumaturgesdelight.efficacy.EfficacyType;
import com.blackmoss.thaumaturgesdelight.registry.TDEfficacies;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record AttributeAction(
        Holder<Attribute> attribute,
        Identifier modifier,
        double amount,
        AttributeModifier.Operation operation,
        int duration) implements AspectEffect {
    public static final MapCodec<AttributeAction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Attribute.CODEC.fieldOf("attribute").forGetter(AttributeAction::attribute),
                    Identifier.CODEC.fieldOf("modifier").forGetter(AttributeAction::modifier),
                    Codec.DOUBLE.fieldOf("amount").forGetter(AttributeAction::amount),
                    AttributeModifier.Operation.CODEC
                            .optionalFieldOf("operation", AttributeModifier.Operation.ADD_VALUE)
                            .forGetter(AttributeAction::operation),
                    Codec.INT.fieldOf("duration").forGetter(AttributeAction::duration)
            ).apply(instance, AttributeAction::new));

    public AttributeAction {
        if (duration < 1) {
            throw new IllegalArgumentException("duration must be positive");
        }
    }

    @Override
    public EfficacyType<?> type() {
        return TDEfficacies.ATTRIBUTE.get();
    }

    @Override
    public void apply(Player player, Holder<IAspect> aspect, ItemStack stack) {
        AspectEffect.requireArgs(player, aspect, stack);
        AttributeInstance instance = player.getAttribute(this.attribute);
        if (instance == null) {
            return;
        }
        instance.removeModifier(this.modifier);
        instance.addTransientModifier(new AttributeModifier(this.modifier, this.amount, this.operation));
        AttributeEfficacyHandler.track(player, this.attribute, this.modifier, this.duration);
    }
}
