package com.blackmoss.thaumaturgesdelight.item;

import com.blackmoss.thaumaturgesdelight.efficacy.AspectEfficacies;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public final class ManaBeanDoughItem extends AspectFoodItem {
    public ManaBeanDoughItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyAspectEffect(ServerLevel level, ServerPlayer player, ItemStack stack, Holder<IAspect> aspect) {
        AspectEfficacies.apply(player, aspect, stack);
    }

    @Override
    public void inventoryTick(@NonNull ItemStack stack, @NonNull ServerLevel level, @NonNull Entity entity, EquipmentSlot slot) {
        assignRandomAspect(stack, level);
    }
}
