package fi.sinervo.sham1.fluidmechanics.block.tileentities;

import com.google.common.collect.ImmutableList;
import fi.sinervo.sham1.fluidmechanics.utils.FluidExtras;
import jdk.nashorn.internal.ir.annotations.Immutable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class BasicFluidPumpTE extends TileEntity implements ITickable {

    private static final int MAX_RANGE_SQUARED = 4096;
    private Queue<BlockPos> suckQueue;
    private Map<BlockPos, List<BlockPos>> pathToPos;

    private BlockPos currentPosition;

    private static final EnumFacing ALLOWED_TRAVERSAL_DIRS[] = new EnumFacing[] {
            EnumFacing.UP,
            EnumFacing.NORTH,
            EnumFacing.SOUTH,
            EnumFacing.EAST,
            EnumFacing.WEST,
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound.getCompoundTag("tank"));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound tankTag = new NBTTagCompound();
        tank.writeToNBT(tankTag);
        compound.setTag("tank", tankTag);
        return compound;
    }

    private FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME * 16) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            markDirty();
        }
    };

    @Override
    public boolean hasCapability(@Nullable Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nullable Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP)
            return (T)tank;
        return super.getCapability(capability, facing);
    }

    public BasicFluidPumpTE() {
        this.suckQueue = new ArrayDeque<>();
        this.pathToPos = new HashMap<>();
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (currentPosition != null) {
                world.setBlockToAir(currentPosition);
                nextPosition();
            } else {
                buildSuctionQueue();
                nextPosition();
            }
        }
    }

    private void nextPosition() {
        while (!suckQueue.isEmpty()) {
            currentPosition = ((ArrayDeque<BlockPos>) suckQueue).removeLast();
            if (!world.isAirBlock(currentPosition)) return;
        }
        currentPosition = null;
    }

    private void buildSuctionQueue() {
        suckQueue.clear();
        pathToPos.clear();
        Fluid targetFluid = null;
        Set<BlockPos> checkedPositions = new HashSet<>();
        List<BlockPos> positionsToCheck = new ArrayList<>();
        for (BlockPos cursor = pos.down(); cursor.getY() > 0; cursor = cursor.down()) {
            if (FluidExtras.getFluidAt(world, cursor) != null) {
                targetFluid = FluidExtras.getFluidAt(world, cursor);
                positionsToCheck.add(cursor);
                pathToPos.put(cursor, Collections.singletonList(cursor));
                checkedPositions.add(cursor);

                suckQueue.add(cursor);
            } else if (!world.isAirBlock(cursor)) {
                break;
            }
        }

        if (positionsToCheck.isEmpty()) return;

        while (!positionsToCheck.isEmpty()) {
            List<BlockPos> posToCheckCopy = new ArrayList<>(positionsToCheck);
            positionsToCheck.clear();
            for (BlockPos current : posToCheckCopy) {
                for (EnumFacing offset : ALLOWED_TRAVERSAL_DIRS) {
                    BlockPos cursor = current.offset(offset);
                    if (cursor.distanceSq(pos) > MAX_RANGE_SQUARED) continue;

                    if (checkedPositions.add(cursor)) {
                        if (FluidExtras.areFluidsEqual(FluidExtras.getFluidAt(world, cursor), targetFluid)) {
                            ImmutableList.Builder<BlockPos> pathBuilder = new ImmutableList.Builder<>();
                            pathBuilder.addAll(pathToPos.get(current));
                            pathBuilder.add(cursor);
                            pathToPos.put(cursor, pathBuilder.build());

                            suckQueue.add(cursor);
                            positionsToCheck.add(cursor);
                        }
                    }
                }
            }
        }
    }

}
