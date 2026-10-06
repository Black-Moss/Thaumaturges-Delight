package com.blackmoss.thaumaturgesdelight.seal;

import com.leclowndu93150.thaumaturge.api.golems.IGolemAPI;
import com.leclowndu93150.thaumaturge.api.golems.seals.ISealBehavior;
import com.leclowndu93150.thaumaturge.api.golems.seals.ISealEntity;
import com.leclowndu93150.thaumaturge.api.golems.seals.ISealFilter;
import com.leclowndu93150.thaumaturge.api.golems.seals.SealPlacement;
import com.leclowndu93150.thaumaturge.api.golems.tasks.Task;
import com.leclowndu93150.thaumaturge.api.items.InvHelper;
import com.leclowndu93150.thaumaturge.content.golem.seals.behavior.ItemMatchSettings;
import com.leclowndu93150.thaumaturge.content.golem.tasks.TaskBoard;
import com.leclowndu93150.thaumaturge.server.TCFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.List;

public final class CuttingBoardBehavior implements ISealBehavior {
    public static final SealPlacement ON_CUTTING_BOARD = (level, pos, _) -> level.getBlockState(pos).is(ModBlocks.CUTTING_BOARD.get());
    public static final int SLOT_TARGET = 0;
    public static final int SLOT_KNIFE = 1;
    private static final int SCAN_PERIOD = 5;

    private int counter;
    private int pendingTask = Integer.MIN_VALUE;

    private static boolean boardReady(Level level, ISealEntity seal) {
        ItemStack onBoard = boardItem(level, seal);
        return !onBoard.isEmpty() && targetAllowed(seal, onBoard);
    }

    private static ItemStack boardItem(Level level, ISealEntity seal) {
        BlockPos pos = seal.pos().pos();
        return level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board ? board.getStoredItem() : ItemStack.EMPTY;
    }

    private static boolean targetAllowed(ISealEntity seal, ItemStack onBoard) {
        ISealFilter filter = seal.filter().orElse(null);
        if (filter == null) {
            return true;
        }
        ItemStack wanted = filter.stack(SLOT_TARGET);
        if (wanted.isEmpty()) {
            return true;
        }
        return InvHelper.matchesFilters(List.of(wanted), filter.isBlacklist(), onBoard, ItemMatchSettings.of(seal));
    }

    private static ItemStack knifeInHand(ISealEntity seal, IGolemAPI golem) {
        ISealFilter filter = seal.filter().orElse(null);
        ItemStack wanted = filter == null ? ItemStack.EMPTY : filter.stack(SLOT_KNIFE);
        InvHelper.InvFilter match = ItemMatchSettings.of(seal);
        for (ItemStack stack : golem.hands().contents()) {
            if (!stack.is(ModTags.Items.KNIVES)) {
                continue;
            }
            if (filter != null && (wanted.isEmpty() || InvHelper.matchesFilters(List.of(wanted), filter.isBlacklist(), stack, match))) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void tick(ServerLevel level, ISealEntity seal) {
        if (++counter % SCAN_PERIOD == 0) {
            Task pending = TaskBoard.of(level).find(pendingTask);
            if ((pending == null || pending.isSuspended() || pending.isCompleted()) && boardReady(level, seal)) {
                Task task = Task.atBlock(seal.pos(), seal.pos().pos());
                task.setPriority(seal.priority());
                TaskBoard.of(level).post(task);
                this.pendingTask = task.id();
            }
        }
    }

    @Override
    public boolean canPerform(ISealEntity seal, IGolemAPI golem, Task task) {
        return boardReady(golem.level(), seal) && !knifeInHand(seal, golem).isEmpty();
    }

    @Override
    public boolean completeTask(ServerLevel level, ISealEntity seal, IGolemAPI golem, Task task) {
        if (!boardReady(level, seal) || !(level.getBlockEntity(task.pos()) instanceof CuttingBoardBlockEntity board)) {
            task.suspend();
            return true;
        }
        ItemStack knife = golem.hands().release(knifeInHand(seal, golem));
        if (knife.isEmpty()) {
            task.suspend();
            return true;
        }
        if (board.processStoredItemUsingTool(knife, TCFakePlayer.GOLEM.at(level, golem.asEntity()))) {
            golem.swingArm();
            golem.addRankXp(1);
        }
        ItemStack leftover = golem.hands().hold(knife);
        if (!leftover.isEmpty()) {
            InvHelper.dropItemAtEntity(level, leftover, golem.asEntity());
        }
        task.suspend();
        return true;
    }
}
