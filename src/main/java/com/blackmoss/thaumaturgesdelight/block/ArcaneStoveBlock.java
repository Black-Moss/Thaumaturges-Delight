package com.blackmoss.thaumaturgesdelight.block;

import com.blackmoss.thaumaturgesdelight.registry.TDBlockEntities;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaTransferFeedback;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaItemStorage;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaStreamPort;
import com.leclowndu93150.thaumaturge.api.essentia.ItemEssentiaTransferResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.StoveBlock;

public class ArcaneStoveBlock extends StoveBlock implements IEssentiaStreamPort {
    public static final MapCodec<ArcaneStoveBlock> CODEC = simpleCodec(ArcaneStoveBlock::new);

    private static final double PORT_ANCHOR_Y = 0.75;
    private static final double PORT_CLEARANCE_Y = 1.25;

    public ArcaneStoveBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new ArcaneStoveBlockEntity(pos, state);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(LIT, true);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return state.getValue(LIT)
                    ? createTickerHelper(type, TDBlockEntities.ARCANE_STOVE.get(),
                    (_, _, _, stove) -> stove.addSmokeParticles())
                    : null;
        }
        return createTickerHelper(type, TDBlockEntities.ARCANE_STOVE.get(), ArcaneStoveBlockEntity::serverTick);
    }

    @Override
    public StreamPort essentiaStreamPort(BlockGetter level, BlockPos pos, BlockState state, Vec3 farEnd, boolean outgoing) {
        Vec3 center = Vec3.atCenterOf(pos);
        return new StreamPort(center.add(0.0, PORT_ANCHOR_Y, 0.0), center.add(0.0, PORT_CLEARANCE_Y, 0.0));
    }

    @Override
    public @NonNull InteractionResult useItemOn(
            @NonNull ItemStack stack,
            @NonNull BlockState state,
            @NonNull Level level,
            @NonNull BlockPos pos,
            @NonNull Player player,
            @NonNull InteractionHand hand,
            @NonNull BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof ArcaneStoveBlockEntity stove) {
            InteractionResult result = depositEssentia(stove, stack, level, player, hand);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    private static InteractionResult depositEssentia(
            ArcaneStoveBlockEntity stove,
            ItemStack stack,
            Level level,
            Player player,
            InteractionHand hand) {
        IEssentiaItemStorage itemStorage = stack.getCapability(EssentiaCapabilities.ITEM_STORAGE);
        if (itemStorage == null) {
            return InteractionResult.PASS;
        }
        AspectList contents = itemStorage.contents();
        if (contents.isEmpty()) {
            return InteractionResult.PASS;
        }
        AspectInstance first = contents.entries().getFirst();
        int needed = stove.spaceFor(first.aspect(), Direction.UP);
        if (needed <= 0) {
            return InteractionResult.PASS;
        }
        ItemEssentiaTransferResult result = itemStorage.extract(first.aspect(), Math.min(needed, first.amount()));
        if (result.amountMoved() <= 0) {
            return InteractionResult.PASS;
        }
        int accepted = stove.addEssentia(first.aspect(), result.amountMoved(), Direction.UP);
        if (accepted <= 0) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        player.setItemInHand(hand, result.resultingStack());
        EssentiaTransferFeedback.playDrain(player, result.resultingStack(), accepted);
        return InteractionResult.SUCCESS_SERVER;
    }

    public static boolean isLit(BlockState state) {
        BooleanProperty lit = LIT;
        return state.hasProperty(lit) && state.getValue(lit);
    }
}
