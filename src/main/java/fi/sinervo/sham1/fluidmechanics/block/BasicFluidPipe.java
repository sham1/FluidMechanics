package fi.sinervo.sham1.fluidmechanics.block;

import fi.sinervo.sham1.fluidmechanics.FluidMechanics;
import fi.sinervo.sham1.fluidmechanics.block.tileentities.BasicFluidPipeTE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BasicFluidPipe extends Block {

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    public static final PropertyBool connectUp = PropertyBool.create("connect_up");
    public static final PropertyBool connectDown = PropertyBool.create("connect_down");
    public static final PropertyBool connectNorth = PropertyBool.create("connect_north");
    public static final PropertyBool connectSouth = PropertyBool.create("connect_south");
    public static final PropertyBool connectEast = PropertyBool.create("connect_east");
    public static final PropertyBool connectWest = PropertyBool.create("connect_west");


    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,
                connectUp, connectDown, connectNorth, connectSouth, connectEast, connectWest);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile;
        boolean canConnectUp = ((tile = worldIn.getTileEntity(pos.up())) != null &&
                tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN));

        boolean canConnectDown = ((tile = worldIn.getTileEntity(pos.down())) != null &&
                tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP));

        boolean canConnectNorth = ((tile = worldIn.getTileEntity(pos.north())) != null &&
                tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.SOUTH));

        boolean canConnectSouth = ((tile = worldIn.getTileEntity(pos.south())) != null &&
                tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH));

        boolean canConnectEast = ((tile = worldIn.getTileEntity(pos.east())) != null &&
                tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.WEST));

        boolean canConnectWest = ((tile = worldIn.getTileEntity(pos.west())) != null &&
                tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.EAST));

        return state
                .withProperty(connectUp, canConnectUp).withProperty(connectDown, canConnectDown)
                .withProperty(connectNorth, canConnectNorth).withProperty(connectSouth, canConnectSouth)
                .withProperty(connectEast, canConnectEast).withProperty(connectWest, canConnectWest);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new BasicFluidPipeTE();
    }

    BasicFluidPipe() {
        super(Material.GLASS);
        setRegistryName(new ResourceLocation(FluidMechanics.MODID, "basic_fluid_pipe"));
        setUnlocalizedName(getRegistryName().toString());
    }
}
