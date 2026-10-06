package com.blackmoss.thaumaturgesdelight.seal;

import com.blackmoss.thaumaturgesdelight.registry.TDGolemArms;
import com.blackmoss.thaumaturgesdelight.registry.TDItems;
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
    public static final SealPlacement ON_CUTTING_BOARD = (level, pos, face) -> level.getBlockState(pos).is(ModBlocks.CUTTING_BOARD.get());
    public static final int SLOT_TARGET = 0;
    public static final int SLOT_KNIFE = 1;
    private static final int SCAN_PERIOD = 5;

    private int counter;
    private int pendingTask = Integer.MIN_VALUE;

    private static void cutWithHeldKnife(ServerLevel level, IGolemAPI golem, CuttingBoardBlockEntity board, ItemStack carried) {
        ItemStack knife = golem.hands().release(carried);
        if (knife.isEmpty()) {
            return;
        }
        cut(level, golem, board, knife);
        ItemStack leftover = golem.hands().hold(knife);
        if (!leftover.isEmpty()) {
            InvHelper.dropItemAtEntity(level, leftover, golem.asEntity());
        }
    }

    private static void cut(ServerLevel level, IGolemAPI golem, CuttingBoardBlockEntity board, ItemStack knife) {
        if (board.processStoredItemUsingTool(knife, TCFakePlayer.GOLEM.at(level, golem.asEntity()))) {
            golem.swingArm();
            golem.addRankXp(1);
        }
    }

    private static boolean hasKnife(ISealEntity seal, IGolemAPI golem) {
        return !carriedKnife(seal, golem).isEmpty() || !builtInKnife(seal, golem).isEmpty();
    }

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

    private static ItemStack carriedKnife(ISealEntity seal, IGolemAPI golem) {
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

    private static ItemStack builtInKnife(ISealEntity seal, IGolemAPI golem) {
        if (golem.properties().arms() != TDGolemArms.KNIFE_ARMS.get()) {
            return ItemStack.EMPTY;
        }
        ItemStack knife = new ItemStack(TDItems.THAUMIUM_KNIFE.get());
        ISealFilter filter = seal.filter().orElse(null);
        if (filter == null) {
            return knife;
        }
        ItemStack wanted = filter.stack(SLOT_KNIFE);
        return wanted.isEmpty() || InvHelper.matchesFilters(List.of(wanted), filter.isBlacklist(), knife, ItemMatchSettings.of(seal)) ? knife : ItemStack.EMPTY;
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
        return boardReady(golem.level(), seal) && hasKnife(seal, golem);
    }

    @Override
    public boolean completeTask(ServerLevel level, ISealEntity seal, IGolemAPI golem, Task task) {
        if (!boardReady(level, seal) || !(level.getBlockEntity(task.pos()) instanceof CuttingBoardBlockEntity board)) {
            task.suspend();
            return true;
        }
        ItemStack carried = carriedKnife(seal, golem);
        if (!carried.isEmpty()) {
            cutWithHeldKnife(level, golem, board, carried);
        } else {
            ItemStack builtIn = builtInKnife(seal, golem);
            if (!builtIn.isEmpty()) {
                cut(level, golem, board, builtIn);
            }
        }
        task.suspend();
        return true;
    }
}
