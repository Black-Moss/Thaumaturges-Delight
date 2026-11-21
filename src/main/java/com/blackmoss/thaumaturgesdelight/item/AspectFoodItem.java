package com.blackmoss.thaumaturgesdelight.item;

import com.blackmoss.thaumaturgesdelight.registry.TDEffectTags;
import com.leclowndu93150.thaumaturge.api.aspect.AspectComponents;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.registry.TTDataComponents;
import com.leclowndu93150.thaumaturge.registry.TTEffectTags;
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

public class AspectFoodItem extends Item {
    protected int effectAmplifier = 2;
    protected double instantHealthFactor = 3.0;
    protected int effectBaseDuration = 200;
    protected int effectExtraDuration = 8;

    public AspectFoodItem(Properties properties) {
        super(properties);
    }

    public static @Nullable Holder<IAspect> aspectOf(ItemStack stack) {
        AspectInstance stored = (AspectInstance) stack.get((DataComponentType<?>) TTDataComponents.CRYSTAL_ASPECT.get());
        return stored == null ? null : stored.aspect();
    }

    protected static void assignRandomAspect(ItemStack stack, ServerLevel level) {
        if (stack.has(TTDataComponents.CRYSTAL_ASPECT.get())) {
            return;
        }
        List<Holder.Reference<IAspect>> aspects = level.registryAccess()
                .lookupOrThrow(IAspect.REGISTRY_KEY)
                .listElements()
                .toList();
        if (!aspects.isEmpty()) {
            Holder<IAspect> aspect = aspects.get(level.getRandom().nextInt(aspects.size()));
            stack.set(TTDataComponents.CRYSTAL_ASPECT.get(), new AspectInstance(aspect, 1));
        }
    }

    @Override
    public @NonNull Component getName(@NonNull ItemStack stack) {
        Holder<IAspect> aspect = aspectOf(stack);
        return aspect == null
                ? Component.translatable(this.descriptionId, Component.translatable("tooltip.thaumaturge.aspect.unknown"))
                : Component.translatable(this.descriptionId, AspectComponents.name(aspect));
    }

    @Override
    public @NonNull ItemStack finishUsingItem(
            @NonNull ItemStack stack,
            @NonNull Level level,
            @NonNull LivingEntity entity) {
        if (level instanceof ServerLevel serverLevel && entity instanceof ServerPlayer player) {
            Holder<IAspect> aspect = aspectOf(stack);
            if (aspect != null) {
                applyAspectEffect(serverLevel, player, stack, aspect);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    protected void applyAspectEffect(
            ServerLevel level,
            ServerPlayer player,
            ItemStack stack,
            Holder<IAspect> aspect) {
        RandomSource random = level.getRandom();
        Optional<HolderSet.Named<MobEffect>> pool = level.registryAccess()
                .lookupOrThrow(TTEffectTags.MANA_BEAN_EFFECTS.registry())
                .get(TTEffectTags.MANA_BEAN_EFFECTS);
        if (pool.isEmpty()) {
            return;
        }

        List<Holder<MobEffect>> candidates = pool.get().stream()
                .filter(effect -> !effect.is(TDEffectTags.RANDOM_EFFECT_EXCLUDED))
                .toList();
        if (candidates.isEmpty()) {
            return;
        }

        Holder<MobEffect> effect = candidates.get(random.nextInt(candidates.size()));
        if (effect.value().isInstantenous()) {
            effect.value().applyInstantenousEffect(
                    level, player, player, player, this.effectAmplifier, this.instantHealthFactor);
        } else {
            player.addEffect(new MobEffectInstance(
                    effect, this.effectBaseDuration + random.nextInt(this.effectExtraDuration), 0));
        }
    }
}
