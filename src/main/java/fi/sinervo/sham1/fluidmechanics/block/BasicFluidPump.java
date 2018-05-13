package fi.sinervo.sham1.fluidmechanics.block;

import fi.sinervo.sham1.fluidmechanics.FluidMechanics;
import fi.sinervo.sham1.fluidmechanics.block.tileentities.BasicFluidPumpTE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BasicFluidPump extends Block {

    public BasicFluidPump() {
        super(Material.IRON);
        setRegistryName(FluidMechanics.MODID, "basic_fluid_pump");
        setUnlocalizedName(getRegistryName().toString());
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new BasicFluidPumpTE();
    }
}
