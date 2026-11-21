package com.blackmoss.thaumaturgesdelight.efficacy.actions;

import com.blackmoss.thaumaturgesdelight.efficacy.AspectEffect;
import com.blackmoss.thaumaturgesdelight.efficacy.EfficacyType;
import com.blackmoss.thaumaturgesdelight.registry.TDEfficacies;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.wands.ItemWand;
import com.leclowndu93150.thaumaturge.content.wands.WandVisHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record RestoreVisAction(int centivis) implements AspectEffect {
    public static final MapCodec<RestoreVisAction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("centivis").forGetter(RestoreVisAction::centivis)
            ).apply(instance, RestoreVisAction::new));

    public RestoreVisAction {
        if (centivis < 1) {
            throw new IllegalArgumentException("centivis must be positive");
        }
    }

    @Override
    public EfficacyType<?> type() {
        return TDEfficacies.RESTORE_VIS.get();
    }

    @Override
    public void apply(Player player, Holder<IAspect> aspect, ItemStack stack) {
        AspectEffect.requireArgs(player, aspect, stack);
        ResourceKey<IAspect> key = aspect.unwrapKey().orElse(null);
        if (key == null) {
            return;
        }

        ItemStack wand = player.getMainHandItem();
        if (!(wand.getItem() instanceof ItemWand)) {
            wand = player.getOffhandItem();
        }
        if (!(wand.getItem() instanceof ItemWand)) {
            return;
        }

        WandVisHelper.topUpCentivis(wand, key, this.centivis, true);
    }
}
