package com.blackmoss.thaumaturgesdelight.item;

import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.content.research.pool.AspectPools;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class EssentiaRockCandyItem extends AspectFoodItem {
    private static final float GRANT_CHANCE = 0.25F;

    public EssentiaRockCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyAspectEffect(ServerLevel level, ServerPlayer player, ItemStack stack, Holder<IAspect> aspect) {
        super.applyAspectEffect(level, player, stack, aspect);
        if (level.getRandom().nextFloat() < GRANT_CHANCE) {
            AspectPools.grant(player, aspect, 1);
        }
    }
}
