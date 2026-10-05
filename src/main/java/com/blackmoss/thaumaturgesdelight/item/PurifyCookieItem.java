package com.blackmoss.thaumaturgesdelight.item;

import com.leclowndu93150.thaumaturge.api.warp.IPlayerWarp;
import com.leclowndu93150.thaumaturge.api.warp.WarpHelper;
import com.leclowndu93150.thaumaturge.api.warp.WarpType;
import com.leclowndu93150.thaumaturge.registry.TCBlocks;
import com.leclowndu93150.thaumaturge.registry.TCMobEffects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class PurifyCookieItem extends Item {
    public PurifyCookieItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onUseTick(
            @NonNull Level level,
            @NonNull LivingEntity entity,
            @NonNull ItemStack stack,
            int remainingTicks) {
        if (level.isClientSide()) {
            if (level.getRandom().nextFloat() < 0.2F) {
                level.playLocalSound(
                        entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.CHORUS_FLOWER_DEATH,
                        SoundSource.PLAYERS,
                        0.1F, 1.5F + level.getRandom().nextFloat() * 0.2F,
                        false);
            }
            spawnBubbles(level, entity, 10, 1.0F);
        }
    }

    @Override
    public boolean releaseUsing(
            @NonNull ItemStack stack,
            @NonNull Level level,
            @NonNull LivingEntity entity,
            int timeLeft) {
        int used = this.getUseDuration(stack, entity) - timeLeft;
        if (used > 95 && entity instanceof Player) {
            stack.shrink(1);
            if (!level.isClientSide() && entity instanceof ServerPlayer player) {
                IPlayerWarp warp = WarpHelper.getWarp(player);
                int amount = 1;
                if (player.hasEffect(TCMobEffects.WARP_WARD)) {
                    ++amount;
                }

                if (level.getBlockState(player.blockPosition()).is(TCBlocks.PURIFYING_FLUID.get())) {
                    ++amount;
                }

                if (warp.get(WarpType.NORMAL) > 0) {
                    WarpHelper.addWarp(player, -amount, WarpType.NORMAL);
                }

                if (warp.get(WarpType.TEMPORARY) > 0) {
                    WarpHelper.addWarp(player, -warp.get(WarpType.TEMPORARY), WarpType.TEMPORARY);
                }
            } else if (level.isClientSide()) {
                spawnBubbles(level, entity, 40, 1.5F);
            }

            return true;
        } else {
            return false;
        }
    }


    private static void spawnBubbles(Level level, LivingEntity entity, int count, float spread) {
        for (int a = 0; a < count; ++a)
            level.addParticle(
                    ParticleTypes.BUBBLE_POP,
                    entity.getX() - 0.5F + level.getRandom().nextFloat() * spread,
                    entity.getBoundingBox().minY + level.getRandom().nextFloat() * entity.getBbHeight(),
                    entity.getZ() - 0.5F + level.getRandom().nextFloat() * spread,
                    0.0F, 0.02, 0.0F);
    }
}
