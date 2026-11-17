package com.blackmoss.thaumaturgesdelight.block;

import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.api.warp.IPlayerWarp;
import com.leclowndu93150.thaumaturge.api.warp.WarpHelper;
import com.leclowndu93150.thaumaturge.api.warp.WarpType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.common.block.FeastBlock;

import java.util.function.Supplier;

public class DeathDayCakeBlock extends FeastBlock {
    public static final int MAX_SERVINGS = 7;
    public static final IntegerProperty CAKE_SERVINGS = IntegerProperty.create("servings", 0, MAX_SERVINGS);

    private static final double WARP_GROWTH_MULTIPLIER = 1.5D;
    private static final double WARP_TO_FLUX_RATIO = 0.33D;
    private static final int AREA_RADIUS_CHUNKS = 2;

    public static final VoxelShape[] CAKE_SHAPES = cakeShapes();

    public DeathDayCakeBlock(BlockBehaviour.Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
        super(properties, servingItem, hasLeftovers, true);
    }

    private static VoxelShape[] cakeShapes() {
        VoxelShape[] shapes = new VoxelShape[MAX_SERVINGS + 1];
        for (int servings = 0; servings <= MAX_SERVINGS; servings++) {
            shapes[servings] = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D + servings, 15.0D);
        }
        return shapes;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CAKE_SERVINGS);
    }

    @Override
    public IntegerProperty getServingsProperty() {
        return CAKE_SERVINGS;
    }

    @Override
    public int getMaxServings() {
        return MAX_SERVINGS;
    }

    @Override
    public @NonNull VoxelShape getShape(
            BlockState state,
            @NonNull BlockGetter level,
            @NonNull BlockPos pos,
            @NonNull CollisionContext context) {
        return CAKE_SHAPES[state.getValue(CAKE_SERVINGS)];
    }

    @Override
    protected int getAnalogOutputSignal(
            BlockState state,
            @NonNull Level level,
            @NonNull BlockPos pos,
            @NonNull Direction direction) {
        return state.getValue(CAKE_SERVINGS);
    }

    @Override
    protected InteractionResult takeServing(
            @NonNull LevelAccessor level,
            @NonNull BlockPos pos,
            BlockState state,
            @NonNull Player player,
            @NonNull InteractionHand hand) {
        int before = state.getValue(getServingsProperty());
        if (before <= 0) {
            return super.takeServing(level, pos, state, player, hand);
        }
        InteractionResult result = super.takeServing(level, pos, state, player, hand);
        if (player instanceof ServerPlayer serverPlayer) {
            multiplyWarp(serverPlayer);
            if (before == 1) {
                detonate(serverPlayer);
            }
        }
        return result;
    }

    private void multiplyWarp(ServerPlayer player) {
        IPlayerWarp warp = WarpHelper.getWarp(player);
        for (WarpType type : WarpType.values()) {
            int current = warp.get(type);
            if (current <= 0) {
                continue;
            }
            int grow = Math.max(1, (int) Math.round(current * (WARP_GROWTH_MULTIPLIER - 1.0D)));
            WarpHelper.addWarp(player, grow, type);
        }
    }

    private void detonate(ServerPlayer player) {
        ServerLevel serverLevel = player.level();
        IPlayerWarp warp = WarpHelper.getWarp(player);
        int normal = warp.get(WarpType.NORMAL);
        int temporary = warp.get(WarpType.TEMPORARY);
        int flux = (int) Math.floor((normal + temporary) * WARP_TO_FLUX_RATIO);

        if (flux > 0) {
            int fromNormal = Math.min(flux, normal);
            int fromTemporary = flux - fromNormal;
            if (fromNormal > 0) {
                WarpHelper.addWarp(player, -fromNormal, WarpType.NORMAL);
            }
            if (fromTemporary > 0) {
                WarpHelper.addWarp(player, -fromTemporary, WarpType.TEMPORARY);
            }
        }

        BlockPos deathPos = player.blockPosition();
        player.hurtServer(serverLevel, player.damageSources().genericKill(), Float.MAX_VALUE);

        if (flux > 0) {
            spreadFlux(serverLevel, deathPos, flux);
        }
    }

    private void spreadFlux(ServerLevel level, BlockPos origin, int flux) {
        ChunkPos center = ChunkPos.containing(origin);
        int chunks = (AREA_RADIUS_CHUNKS * 2 + 1) * (AREA_RADIUS_CHUNKS * 2 + 1);
        int perChunk = flux / chunks;
        int remainder = flux % chunks;
        for (int dx = -AREA_RADIUS_CHUNKS; dx <= AREA_RADIUS_CHUNKS; dx++) {
            for (int dz = -AREA_RADIUS_CHUNKS; dz <= AREA_RADIUS_CHUNKS; dz++) {
                int amount = perChunk + (dx == 0 && dz == 0 ? remainder : 0);
                if (amount <= 0) {
                    continue;
                }
                ChunkPos chunk = new ChunkPos(center.x() + dx, center.z() + dz);
                AuraHelper.addFlux(level, chunk.getMiddleBlockPosition(origin.getY()), amount);
            }
        }
    }
}
