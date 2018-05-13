package fi.sinervo.sham1.fluidmechanics.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public final class FluidExtras {
    public static Fluid getFluidAt(World world, BlockPos pos) {
        return FluidRegistry.lookupFluidForBlock(world.getBlockState(pos).getBlock());
    }

    public static boolean areFluidsEqual(Fluid fluid1, Fluid fluid2) {
        if (fluid1 == null || fluid2 == null) return fluid1 == fluid2;
        return fluid1.getName().equals(fluid2.getName());
    }
}
