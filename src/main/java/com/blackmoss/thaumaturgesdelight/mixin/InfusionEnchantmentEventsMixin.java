package com.blackmoss.thaumaturgesdelight.mixin;

import com.blackmoss.thaumaturgesdelight.item.BleedingEdgeResults;
import com.blackmoss.thaumaturgesdelight.registry.TDInfusionEnchantments;
import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantmentEvents;
import com.leclowndu93150.thaumaturge.content.equipment.InfusionEnchantmentHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(InfusionEnchantmentEvents.class)
public final class InfusionEnchantmentEventsMixin {
    @Unique
    private static final float BLEEDING_EDGE_CHANCE_PER_LEVEL = 0.125F;

    private InfusionEnchantmentEventsMixin() {
    }

    @Inject(method = "onLivingDrops", at = @At("HEAD"))
    private static void thaumaturgesdelight$cookDing(LivingDropsEvent event, CallbackInfo callback) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        ItemStack held = player.getMainHandItem();
        int rank = InfusionEnchantmentHelper.level(held, TDInfusionEnchantments.BLEEDING_EDGE);
        if (rank <= 0) {
            return;
        }

        Item chunk = BleedingEdgeResults.chunkFor(event.getEntity());
        if (chunk == null) {
            return;
        }

        ServerLevel level = (ServerLevel) player.level();
        float chance = (1 + rank) * BLEEDING_EDGE_CHANCE_PER_LEVEL;
        Collection<ItemEntity> drops = event.getDrops();
        List<ItemEntity> chunks = new ArrayList<>();
        for (ItemEntity drop : drops) {
            if (level.getRandom().nextFloat() <= chance) {
                chunks.add(new ItemEntity(level, drop.getX(), drop.getY(), drop.getZ(),
                        new ItemStack(chunk, drop.getItem().getCount())));
            }
        }

        if (!chunks.isEmpty()) {
            drops.addAll(chunks);
            level.playSound(
                    null,
                    event.getEntity().getX(),
                    event.getEntity().getY(),
                    event.getEntity().getZ(),
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS,
                    0.2F,
                    0.7F + level.getRandom().nextFloat() * 0.2F);
        }
    }
}
