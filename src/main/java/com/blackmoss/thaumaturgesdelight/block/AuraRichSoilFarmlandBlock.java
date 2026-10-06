package com.blackmoss.thaumaturgesdelight.block;

import com.blackmoss.thaumaturgesdelight.registry.TDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.RichSoilFarmlandBlock;

public class AuraRichSoilFarmlandBlock extends RichSoilFarmlandBlock {
    public AuraRichSoilFarmlandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static void turnToAuraSoil(@Nullable Entity entity, BlockState state, Level level, BlockPos pos) {
        BlockState soil = pushEntitiesUp(state, TDBlocks.AURA_RICH_SOIL.get().defaultBlockState(), level, pos);
        level.setBlockAndUpdate(pos, soil);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, state));
    }

    @Override
    public void randomTick(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        super.randomTick(state, level, pos, random);
        BlockPos cropPos = pos.above();
        BlockState cropState = level.getBlockState(cropPos);
        AuraRichSoilBlock.boostPlantWithAura(level, pos, cropState, cropPos);
    }

    @Override
    public void tick(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            turnToAuraSoil(null, state, level, pos);
        }
    }

    @Override
    public @NonNull BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        if (!defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos())) {
            return TDBlocks.AURA_RICH_SOIL.get().defaultBlockState();
        }
        return super.getStateForPlacement(context);
    }
}
