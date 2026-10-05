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

@EventBusSubscriber(modid = ThaumaturgesDelight.MODID)
public class ArcaneStoveBlockEntity extends AbstractStoveBlockEntity implements IEssentiaTransport {
    private static final int SUCTION = 128;
    private static final int WORK_INTERVAL = 5;
    private static final int INVENTORY_SLOTS = 6;

    private AspectList needs = AspectList.EMPTY;
    private AspectList gathered = AspectList.EMPTY;
    private int counter;

    public ArcaneStoveBlockEntity(BlockPos pos, BlockState state) {
        super(TDBlockEntities.ARCANE_STOVE.get(), pos, state, RecipeType.CAMPFIRE_COOKING);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ArcaneStoveBlockEntity stove) {
        AbstractStoveBlockEntity.serverTick(level, pos, state, stove);
        if (++stove.counter % WORK_INTERVAL == 0) {
            stove.fetchFromNeighbours(level, pos);
        }
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

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(EssentiaCapabilities.TRANSPORT, TDBlockEntities.ARCANE_STOVE.get(), (blockEntity, side) -> blockEntity);
    }

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

    public void consumeRequirement() {
        needs = AspectList.EMPTY;
        gathered = AspectList.EMPTY;
        setChanged();
        syncToClient();
    }

    public AspectList needs() {
        return needs;
    }

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
        return false;
    }

    @Override
    public void setSuction(@Nullable Holder<IAspect> aspect, int amount) {
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

    @Override
    protected int getInventorySlotCount() {
        return INVENTORY_SLOTS;
    }

    @Override
    public Vec2 getStoveItemOffset(int index) {
        Vec2[] offsets = new Vec2[]{
                new Vec2(0.3F, 0.2F), new Vec2(0.0F, 0.2F), new Vec2(-0.3F, 0.2F),
                new Vec2(0.3F, -0.2F), new Vec2(0.0F, -0.2F), new Vec2(-0.3F, -0.2F)};
        return offsets[index];
    }

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
}
