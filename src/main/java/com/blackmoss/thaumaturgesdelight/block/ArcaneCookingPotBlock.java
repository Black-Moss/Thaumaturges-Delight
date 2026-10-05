package com.blackmoss.thaumaturgesdelight.block;

import com.blackmoss.thaumaturgesdelight.registry.TDBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.tag.ModTags;

public class ArcaneCookingPotBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {
    public static final MapCodec<ArcaneCookingPotBlock> CODEC = simpleCodec(ArcaneCookingPotBlock::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<CookingPotSupport> SUPPORT = EnumProperty.create("support", CookingPotSupport.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = Block.box(2.0F, 0.0F, 2.0F, 14.0F, 10.0F, 14.0F);
    private static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, Block.box(0.0F, -1.0F, 0.0F, 16.0F, 0.0F, 16.0F));


    public ArcaneCookingPotBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(SUPPORT, CookingPotSupport.NONE)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected @NonNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NonNull Builder<Block, BlockState> builder) {
        builder.add(FACING, SUPPORT, WATERLOGGED);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(SUPPORT, getTrayState(context.getLevel(), context.getClickedPos()))
                .setValue(WATERLOGGED, fluidState.is(Fluids.WATER));
    }

    @Override
    protected @NonNull BlockState updateShape(
            @NonNull BlockState state,
            @NonNull LevelReader level,
            @NonNull ScheduledTickAccess ticks,
            @NonNull BlockPos pos,
            @NonNull Direction direction,
            @NonNull BlockPos neighborPos,
            @NonNull BlockState neighborState,
            @NonNull RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (direction.getAxis() == Direction.Axis.Y && state.getValue(SUPPORT) != CookingPotSupport.HANDLE) {
            return state.setValue(SUPPORT, getTrayState(level, pos));
        }
        return state;
    }

    private CookingPotSupport getTrayState(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(ModTags.Blocks.TRAY_HEAT_SOURCES) ? CookingPotSupport.TRAY : CookingPotSupport.NONE;
    }

    @Override
    public @NonNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NonNull VoxelShape getShape(
            @NonNull BlockState state,
            @NonNull BlockGetter level,
            @NonNull BlockPos pos,
            @NonNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NonNull VoxelShape getCollisionShape(
            @NonNull BlockState state,
            @NonNull BlockGetter level,
            @NonNull BlockPos pos,
            @NonNull CollisionContext context) {
        return state.getValue(SUPPORT).equals(CookingPotSupport.TRAY)
                ? SHAPE_WITH_TRAY
                : SHAPE;
    }

    @Override
    protected @NonNull InteractionResult useItemOn(
            @NonNull ItemStack stack,
            @NonNull BlockState state,
            @NonNull Level level,
            @NonNull BlockPos pos,
            @NonNull Player player,
            @NonNull InteractionHand hand,
            @NonNull BlockHitResult hit) {
        if (stack.isEmpty() && player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                CookingPotSupport support = state.getValue(SUPPORT) == CookingPotSupport.HANDLE ? getTrayState(level, pos) : CookingPotSupport.HANDLE;
                level.setBlockAndUpdate(pos, state.setValue(SUPPORT, support));
                level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
            }
            return InteractionResult.SUCCESS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(pos) instanceof ArcaneCookingPotBlockEntity pot) {
            ItemStack serving = pot.useHeldItemOnMeal(stack);
            if (serving != ItemStack.EMPTY) {
                if (!player.getInventory().add(serving)) {
                    player.drop(serving, false);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.8F, 1.0F);
            } else {
                player.openMenu(pot, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NonNull BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new ArcaneCookingPotBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            @NonNull Level level,
            @NonNull BlockState state,
            @NonNull BlockEntityType<T> type) {
        if (type != TDBlockEntities.ARCANE_COOKING_POT.get()) {
            return null;
        }
        return level.isClientSide() ? null
                : (tickLevel, pos, tickState, blockEntity)
                -> ArcaneCookingPotBlockEntity.cookingTick(tickLevel, pos, tickState, (ArcaneCookingPotBlockEntity) blockEntity);
    }

    @Override
    public void animateTick(
            @NonNull BlockState state,
            @NonNull Level level,
            @NonNull BlockPos pos,
            @NonNull RandomSource random) {
        if (!(level.getBlockEntity(pos) instanceof ArcaneCookingPotBlockEntity pot) || !pot.isHeated()) {
            return;
        }
        if (random.nextFloat() < 0.12F) {
            level.addParticle(
                    ParticleTypes.SMOKE,
                    pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4,
                    0.0, 0.02, 0.0);
        }
        if (random.nextFloat() < 0.06F) {
            level.playLocalSound(
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.FURNACE_FIRE_CRACKLE,
                    SoundSource.BLOCKS, 0.4F,
                    1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F, false);
        }
    }

    @Override
    protected void affectNeighborsAfterRemoval(
            @NonNull BlockState state,
            ServerLevel level,
            @NonNull BlockPos pos,
            boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof ArcaneCookingPotBlockEntity pot) {
            for (ItemStack stack : pot.getDroppableInventory()) {
                if (!stack.isEmpty()) {
                    Block.popResource(level, pos, stack);
                }
            }
            pot.clearContent();
        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }
}
