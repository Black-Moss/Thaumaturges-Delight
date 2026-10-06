package com.blackmoss.thaumaturgesdelight.block;

import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.RichSoilBlock;
import vectorwing.farmersdelight.common.tag.ModTags;

public class AuraRichSoilBlock extends RichSoilBlock {
    public static final float VIS_COST = 1.0F;

    public AuraRichSoilBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    private static boolean boostWithAura(ServerLevel level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        if (!aboveState.is(ModTags.Blocks.PLANTED_FROM_BELOW) && boostPlantWithAura(level, pos, aboveState, abovePos)) {
            return true;
        }
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        return belowState.is(ModTags.Blocks.PLANTED_FROM_BELOW)
                && boostPlantWithAura(level, pos, belowState, belowPos);
    }

    public static boolean boostPlantWithAura(ServerLevel level, BlockPos auraPos, BlockState plantState, BlockPos plantPos) {
        if (AuraHelper.drainVis(level, auraPos, VIS_COST, true) < VIS_COST) {
            return false;
        }
        if (!RichSoilBlock.boostPlant(plantState, plantPos, level)) {
            return false;
        }
        AuraHelper.drainVis(level, auraPos, VIS_COST, false);
        return true;
    }

    @Override
    public void randomTick(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        BlockPos abovePos = pos.above();
        if (this.convertMushroomToColony(level.getBlockState(abovePos), abovePos, level)) {
            return;
        }
        if (boostWithAura(level, pos)) {
            return;
        }
        RichSoilBlock.tryBoostingPlantsAboveAndBelow(level, pos, random);
    }

    /**
     * 锄头把它变成<strong>灵气</strong>沃土耕地，而不是像普通沃土那样变成乐事的沃土耕地（那样会丢灵气）。
     */
    @Override
    public @Nullable BlockState getToolModifiedState(@NonNull BlockState state, @NonNull UseOnContext context,
                                                     @NonNull ItemAbility toolAction, boolean simulate) {
        return toolAction.equals(ItemAbilities.HOE_TILL)
                && context.getLevel().getBlockState(context.getClickedPos().above()).isAir()
                ? TDBlocks.AURA_RICH_SOIL_FARMLAND.get().defaultBlockState()
                : null;
    }
}
