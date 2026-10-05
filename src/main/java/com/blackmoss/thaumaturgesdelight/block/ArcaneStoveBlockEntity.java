package com.blackmoss.thaumaturgesdelight.block;

import com.blackmoss.thaumaturgesdelight.ThaumaturgesDelight;
import com.blackmoss.thaumaturgesdelight.registry.TDBlockEntities;
import com.leclowndu93150.thaumaturge.api.aspect.AspectInstance;
import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import com.leclowndu93150.thaumaturge.api.aspect.IAspect;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaAccess;
import com.leclowndu93150.thaumaturge.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaTransport;
import com.leclowndu93150.thaumaturge.content.essentia.EssentiaTransportHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec2;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;

import java.util.Objects;

/**
 * 奥术灶台：默认点燃，没有源质时它就是个普通灶台（加热厨锅、烧灶面上的食物）。
 *
 * <p>源质方面参考炼金塔：<b>不常驻存储，只在需要的时候才需求</b>。
 * 厨锅在下锅前把整张配方的源质清单交给 {@link #request}，灶台随即对周围能输出源质的容器/管道张开吸力
 * （{@link #SUCTION}），按清单里的顺序一次吸 1 点、凑齐前不做菜；出锅时由厨锅 {@link #consumeRequirement} 结账。
 * 对外不暴露任何存量（{@link #getEssentiaAmount} 恒为 0），也永远不往外给源质（{@link #canOutputTo} 恒为 false）。
 *
 * <p>注意需求是<b>整单</b>而不是一次一种：多源质配方如果一次只讨要一种，凑齐 A 再去要 B 时会把 A 顶掉，
 * 回头再要 A 就死循环了。
 */
@EventBusSubscriber(modid = ThaumaturgesDelight.MODID)
public class ArcaneStoveBlockEntity extends AbstractStoveBlockEntity implements IEssentiaTransport {
    /** 索取时的吸力（炼金塔同款）。 */
    private static final int SUCTION = 128;
    /** 每隔多少 tick 去周围取一次源质（炼金塔同款）。 */
    private static final int WORK_INTERVAL = 5;
    private static final int INVENTORY_SLOTS = 6;

    /** 当前这一单需要哪些源质。 */
    private AspectList needs = AspectList.EMPTY;
    /** 已经凑到手的部分（暂存，出锅时整单清空，不作为容器对外可见）。 */
    private AspectList gathered = AspectList.EMPTY;
    private int counter;

    public ArcaneStoveBlockEntity(BlockPos pos, BlockState state) {
        super(TDBlockEntities.ARCANE_STOVE.get(), pos, state, RecipeType.CAMPFIRE_COOKING);
    }

    // --------------------------------------------------------------- 主循环

    public static void serverTick(Level level, BlockPos pos, BlockState state, ArcaneStoveBlockEntity stove) {
        // 农夫乐事灶台逻辑：灶面上的食物照常按营火配方烹饪
        AbstractStoveBlockEntity.serverTick(level, pos, state, stove);
        if (++stove.counter % WORK_INTERVAL == 0) {
            stove.fetchFromNeighbours(level, pos);
        }
    }

    // ----------------------------------------------------------- 需求 API

    /**
     * 声明这一单需要哪些源质（可以每 tick 重复调用）。
     * 内容不变就保留已凑到的进度；换了配方则重新开始，只留下仍然需要的那部分。
     */
    public void request(AspectList needs) {
        if (needs.isEmpty()) {
            return;
        }
        if (!Objects.equals(this.needs, needs)) {
            this.needs = needs;
            this.gathered = keepOnlyGathered(gathered, needs);
        }
        setChanged();
        syncToClient();
    }

    /** 整单是否已经备齐。 */
    public boolean isReady() {
        if (needs.isEmpty()) {
            return false;
        }
        for (AspectInstance need : needs.entries()) {
            if (gathered.amountOf(need.aspect()) < need.amount()) {
                return false;
            }
        }
        return true;
    }

    /** 出锅结账：整单清空。 */
    public void consumeRequirement() {
        needs = AspectList.EMPTY;
        gathered = AspectList.EMPTY;
        setChanged();
        syncToClient();
    }

    public AspectList needs() {
        return needs;
    }

    /** 还缺什么（没有需求时为空）。 */
    public AspectList missing() {
        if (needs.isEmpty()) {
            return AspectList.EMPTY;
        }
        AspectList missing = AspectList.EMPTY;
        for (AspectInstance need : needs.entries()) {
            int gap = need.amount() - gathered.amountOf(need.aspect());
            if (gap > 0) {
                missing = missing.add(need.aspect(), gap);
            }
        }
        return missing;
    }

    /** 当前正在吸的那一种：清单里第一个还缺的源质。 */
    private @Nullable ResourceKey<IAspect> currentSuctionKey() {
        if (needs.isEmpty()) {
            return null;
        }
        for (AspectInstance need : needs.sortedByTag()) {
            if (gathered.amountOf(need.aspect()) < need.amount()) {
                return need.aspect().unwrapKey().orElse(null);
            }
        }
        return null;
    }

    private static AspectList keepOnlyGathered(AspectList gathered, AspectList needs) {
        AspectList kept = AspectList.EMPTY;
        for (AspectInstance entry : gathered.entries()) {
            int wanted = needs.amountOf(entry.aspect());
            if (wanted > 0) {
                kept = kept.add(entry.aspect(), Math.min(wanted, entry.amount()));
            }
        }
        return kept;
    }

    // --------------------------------------------------------------- 取源质

    /** 向四周能输出源质的容器/管道一次取 1 点（炼金塔 fill() 同款判定）。 */
    private void fetchFromNeighbours(Level level, BlockPos pos) {
        ResourceKey<IAspect> suctionKey = currentSuctionKey();
        if (suctionKey == null) {
            return;
        }
        Holder<IAspect> wanted = EssentiaTransportHelper.resolve(level, suctionKey);
        if (wanted == null) {
            return;
        }
        for (Direction dir : Direction.values()) {
            IEssentiaTransport neighbour = EssentiaAccess.transport(level, pos.relative(dir), dir.getOpposite());
            if (neighbour == null || !neighbour.canOutputTo(dir.getOpposite())) {
                continue;
            }
            if (neighbour.getEssentiaAmount(dir.getOpposite()) <= 0) {
                continue;
            }
            if (neighbour.getSuctionAmount(dir.getOpposite()) >= getSuctionAmount(dir)) {
                continue;
            }
            if (getSuctionAmount(dir) < neighbour.getMinimumSuction()) {
                continue;
            }
            int taken = neighbour.takeEssentia(wanted, 1, dir.getOpposite());
            if (taken > 0) {
                acceptEssentia(wanted, taken);
                return;
            }
        }
    }

    /** 只收"这一单"要用、且没超过缺口的那种源质；别的一律不收。 */
    private int acceptEssentia(Holder<IAspect> aspect, int amount) {
        if (amount <= 0) {
            return 0;
        }
        int gap = needs.amountOf(aspect) - gathered.amountOf(aspect);
        if (gap <= 0) {
            return 0;
        }
        int added = Math.min(gap, amount);
        gathered = gathered.add(aspect, added);
        setChanged();
        syncToClient();
        return added;
    }

    // ------------------------------------------------------ IEssentiaTransport

    @Override
    public boolean isConnectable(Direction face) {
        return true;
    }

    @Override
    public boolean canInputFrom(Direction face) {
        return true;
    }

    @Override
    public boolean canOutputTo(Direction face) {
        // 炼金塔式：只进不出，它不是一个源质容器
        return false;
    }

    @Override
    public void setSuction(@Nullable Holder<IAspect> aspect, int amount) {
        // 吸力由当前需求决定，不接受外部设定
    }

    @Override
    public @Nullable Holder<IAspect> getSuctionType(Direction face) {
        ResourceKey<IAspect> key = currentSuctionKey();
        return key == null ? null : EssentiaTransportHelper.resolve(level, key);
    }

    @Override
    public int getSuctionAmount(Direction face) {
        return currentSuctionKey() != null ? SUCTION : 0;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public @Nullable Holder<IAspect> getEssentiaType(Direction face) {
        return null;
    }

    @Override
    public int getEssentiaAmount(Direction face) {
        return 0;
    }

    @Override
    public int takeEssentia(Holder<IAspect> aspect, int amount, Direction face) {
        return 0;
    }

    @Override
    public int addEssentia(Holder<IAspect> incoming, int amount, Direction face) {
        return canInputFrom(face) ? acceptEssentia(incoming, amount) : 0;
    }

    @Override
    public int spaceFor(Holder<IAspect> aspect, Direction face) {
        return Math.max(0, needs.amountOf(aspect) - gathered.amountOf(aspect));
    }

    // --------------------------------------------------------------- 灶台自身

    @Override
    protected int getInventorySlotCount() {
        return INVENTORY_SLOTS;
    }

    @Override
    public Vec2 getStoveItemOffset(int index) {
        Vec2[] offsets = new Vec2[] {
                new Vec2(0.3F, 0.2F), new Vec2(0.0F, 0.2F), new Vec2(-0.3F, 0.2F),
                new Vec2(0.3F, -0.2F), new Vec2(0.0F, -0.2F), new Vec2(-0.3F, -0.2F)};
        return offsets[index];
    }

    /** 灶面空着就不用冒烟了。 */
    public void addSmokeParticles() {
        if (this.level == null) {
            return;
        }
        ItemStackHandler items = this.getItems();
        for (int i = 0; i < items.getSlots(); i++) {
            if (items.getStackInSlot(i).isEmpty() || this.level.getRandom().nextFloat() >= 0.2F) {
                continue;
            }
            Vec2 itemOffset = this.getStoveItemOffset(i);
            Direction direction = this.getBlockState().getValue(AbstractStoveBlock.FACING);
            if (direction.get2DDataValue() % 2 != 0) {
                //noinspection SuspiciousNameCombination
                itemOffset = new Vec2(itemOffset.y, itemOffset.x);
            }
            double x = this.worldPosition.getX() + 0.5 + direction.getStepX() * itemOffset.x + direction.getClockWise().getStepX() * itemOffset.y;
            double y = this.worldPosition.getY() + 1.0;
            double z = this.worldPosition.getZ() + 0.5 + direction.getStepZ() * itemOffset.x + direction.getClockWise().getStepZ() * itemOffset.y;
            for (int k = 0; k < 3; k++) {
                this.level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
            }
        }
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            BlockState state = getBlockState();
            level.sendBlockUpdated(getBlockPos(), state, state, Block.UPDATE_ALL);
        }
    }

    // ------------------------------------------------------------- 存档 / 能力

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        needs = input.read("Needs", AspectList.CODEC).orElse(AspectList.EMPTY);
        gathered = input.read("Gathered", AspectList.CODEC).orElse(AspectList.EMPTY);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        if (!needs.isEmpty()) {
            output.store("Needs", AspectList.CODEC, needs);
        }
        if (!gathered.isEmpty()) {
            output.store("Gathered", AspectList.CODEC, gathered);
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(EssentiaCapabilities.TRANSPORT, TDBlockEntities.ARCANE_STOVE.get(), (blockEntity, side) -> blockEntity);
    }
}
