package com.blackmoss.thaumaturgesdelight.block;

import com.leclowndu93150.thaumaturge.api.aura.AuraHelper;
import com.leclowndu93150.thaumaturge.api.warp.IPlayerWarp;
import com.leclowndu93150.thaumaturge.api.warp.WarpHelper;
import com.leclowndu93150.thaumaturge.api.warp.WarpType;
import java.util.function.Supplier;
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
import vectorwing.farmersdelight.common.block.FeastBlock;

// 死日蛋糕（盛宴方块），共 7 份：
// 每取下一份（吃掉）扭曲 ×1.5；只有把最后一份也吃下去（即吃满 7 份）才会瞬间死亡，
// 并把 33% 的扭曲转化成死亡点周围 5x5 区块的咒波。
//
// 注意：FD 的 RotatedFeastBlock 内部用的是静态 SERVINGS（0-4），且形状数组是私有的，
// 做不到 7 份。因此这里直接继承 FeastBlock，自己管状态属性与形状。
public class DeathDayCakeBlock extends FeastBlock {
    public static final int MAX_SERVINGS = 7;
    // FD 自带的 SERVINGS 只有 0-4，蛋糕要 7 份，故自定义同名属性
    public static final IntegerProperty CAKE_SERVINGS = IntegerProperty.create("servings", 0, MAX_SERVINGS);

    private static final double WARP_GROWTH_MULTIPLIER = 1.5D;
    private static final double WARP_TO_FLUX_RATIO = 0.33D;
    private static final int AREA_RADIUS_CHUNKS = 2;

    // 索引即剩余份数：0 份只剩一层薄底盘，蛋糕高度随剩余份数递增
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return CAKE_SHAPES[state.getValue(CAKE_SERVINGS)];
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return state.getValue(CAKE_SERVINGS);
    }

    @Override
    protected InteractionResult takeServing(LevelAccessor level, BlockPos pos, BlockState state, Player player, InteractionHand hand) {
        int before = state.getValue(getServingsProperty());
        if (before <= 0) {
            return super.takeServing(level, pos, state, player, hand);
        }
        InteractionResult result = super.takeServing(level, pos, state, player, hand);
        if (player instanceof ServerPlayer serverPlayer) {
            multiplyWarp(serverPlayer);
            // 刚刚取走的是最后一份（1 → 0），说明这一整个蛋糕的 7 份已吃满
            if (before == 1) {
                detonate(serverPlayer);
            }
        }
        return result;
    }

    // 扭曲 ×1.5：各池分别乘，至少 +1 保证有增长
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

    // 先按 33% 扣掉扭曲，再瞬间致死，最后把等量咒波铺到死亡点周围 5x5 区块。
    private void detonate(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        IPlayerWarp warp = WarpHelper.getWarp(player);
        int normal = warp.get(WarpType.NORMAL);
        int temporary = warp.get(WarpType.TEMPORARY);
        int flux = (int) Math.floor((normal + temporary) * WARP_TO_FLUX_RATIO);

        if (flux > 0) {
            // 永久扭曲设定上无法移除，故只从普通池与临时池扣
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
        // genericKill 无视护甲与无敌，保证是瞬死
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
                // 整除不下的余数补给正中心区块
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
