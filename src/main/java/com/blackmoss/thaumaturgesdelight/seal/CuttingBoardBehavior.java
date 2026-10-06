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
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;

/**
 * 切菜印记：傀儡用刀把砧板上的东西切开。
 *
 * <p>只负责"切"——切完的碎屑会像玩家切菜一样掉在砧板旁，由玩家或收集印记处理。
 * 切制本身完全交给农夫乐事的 {@link CuttingBoardBlockEntity#processStoredItemUsingTool}，
 * 所以配方、音效、粒子都是原版行为。
 *
 * <p><b>刀从哪来</b>：优先用傀儡手上拿着的刀（要走一遍"取出→切→放回"，耐久也会正常扣在真刀上）；
 * 如果手上没有，则看它是不是装了<b>菜刀臂</b>——那种傀儡自带一把刀，不占手也不会磨损，
 * 代价是它身上带着 CLUMSY（笨手），搬不了东西。
 *
 * <p>基础印记没有过滤器，放上去就切砧板上的任何东西；高级印记带两格过滤器：
 * <ul>
 *   <li>第 1 格（{@link #SLOT_TARGET}）：要切什么，留空 = 什么都切；</li>
 *   <li>第 2 格（{@link #SLOT_KNIFE}）：必须用哪把刀，留空 = 任意一把菜刀（菜刀臂自带的刀也要过这一关）。</li>
 * </ul>
 */
public final class CuttingBoardBehavior implements ISealBehavior {
    /** 印记只能贴在砧板上。 */
    public static final SealPlacement ON_CUTTING_BOARD = (level, pos, face) -> level.getBlockState(pos).is(ModBlocks.CUTTING_BOARD.get());
    /** 过滤器第 1 格：要切什么。 */
    public static final int SLOT_TARGET = 0;
    /** 过滤器第 2 格：用哪把刀。 */
    public static final int SLOT_KNIFE = 1;
    /** 每 5 tick 扫一次，节奏和"使用"印记一致。 */
    private static final int SCAN_PERIOD = 5;

    // Thaumaturge 的 SealClock 是包内私有，addon 用自己的计数器即可
    private int counter;
    private int pendingTask = Integer.MIN_VALUE;

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
        // 砧板上有想切的东西，且这傀儡拿得出一把刀（手上的，或者菜刀臂自带的）
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
            // 菜刀臂：刀长在手臂上，不占手也不磨损，直接用
            ItemStack builtIn = builtInKnife(seal, golem);
            if (!builtIn.isEmpty()) {
                cut(level, golem, board, builtIn);
            }
        }
        task.suspend();
        return true;
    }

    /**
     * 用手里拿着的刀切：取出 → 切 → 放回。
     *
     * <p>不要改成"借假玩家右键"（官方 UseBehavior 那套）：那样耐久是扣在假玩家手里的副本上，
     * 真刀要等 golemClick 内部的 returnItems 塞回来，傀儡手被占住时刀会被丢在地上甚至凭空消失。
     */
    private static void cutWithHeldKnife(ServerLevel level, IGolemAPI golem, CuttingBoardBlockEntity board, ItemStack carried) {
        ItemStack knife = golem.hands().release(carried);
        if (knife.isEmpty()) {
            return;
        }
        cut(level, golem, board, knife);
        // 手是刚刚腾出来的，一定放得回；真放不下也只丢在傀儡脚下，绝不会蒸发
        ItemStack leftover = golem.hands().hold(knife);
        if (!leftover.isEmpty()) {
            InvHelper.dropItemAtEntity(level, leftover, golem.asEntity());
        }
    }

    /** 真正的切制：交给农夫乐事，它会生成碎屑、放音效粒子、清空砧板，并扣 1 点刀的耐久。 */
    private static void cut(ServerLevel level, IGolemAPI golem, CuttingBoardBlockEntity board, ItemStack knife) {
        if (board.processStoredItemUsingTool(knife, TCFakePlayer.GOLEM.at(level, golem.asEntity()))) {
            golem.swingArm();
            golem.addRankXp(1);
        }
    }

    private static boolean hasKnife(ISealEntity seal, IGolemAPI golem) {
        return !carriedKnife(seal, golem).isEmpty() || !builtInKnife(seal, golem).isEmpty();
    }

    /** 砧板上有东西，且符合"要切什么"的过滤。 */
    private static boolean boardReady(Level level, ISealEntity seal) {
        ItemStack onBoard = boardItem(level, seal);
        return !onBoard.isEmpty() && targetAllowed(seal, onBoard);
    }

    private static ItemStack boardItem(Level level, ISealEntity seal) {
        BlockPos pos = seal.pos().pos();
        return level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board ? board.getStoredItem() : ItemStack.EMPTY;
    }

    /** 第 1 格过滤：留空 = 什么都切；否则要符合它（走官方匹配，认 NBT/耐久/标签等设置）。 */
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

    /** 傀儡手上拿着的刀：必须是农夫乐事菜刀，且满足第 2 格过滤（留空 = 任意菜刀）。 */
    private static ItemStack carriedKnife(ISealEntity seal, IGolemAPI golem) {
        ISealFilter filter = seal.filter().orElse(null);
        ItemStack wanted = filter == null ? ItemStack.EMPTY : filter.stack(SLOT_KNIFE);
        InvHelper.InvFilter match = ItemMatchSettings.of(seal);
        for (ItemStack stack : golem.hands().contents()) {
            if (!stack.is(ModTags.Items.KNIVES)) {
                continue;
            }
            if (wanted.isEmpty() || InvHelper.matchesFilters(List.of(wanted), filter.isBlacklist(), stack, match)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    /** 菜刀臂自带的刀：同样要过第 2 格过滤（玩家指定了别的刀时，这把不算数）。 */
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
}
