package fi.sinervo.sham1.fluidmechanics.block;

import fi.sinervo.sham1.fluidmechanics.FluidMechanics;
import fi.sinervo.sham1.fluidmechanics.block.tileentities.BasicFluidPipeTE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicFluidPipe extends Block {
    public static final PropertyBool connectUp = PropertyBool.create("connect_up");
    public static final PropertyBool connectDown = PropertyBool.create("connect_down");
    public static final PropertyBool connectNorth = PropertyBool.create("connect_north");
    public static final PropertyBool connectSouth = PropertyBool.create("connect_south");
    public static final PropertyBool connectEast = PropertyBool.create("connect_east");
    public static final PropertyBool connectWest = PropertyBool.create("connect_west");

    private static final AxisAlignedBB UP_CONN_AABB =
            new AxisAlignedBB(0.25, 0.75, 0.25, 0.75, 1.0, 0.75);
    private static final AxisAlignedBB DOWN_CONN_AABB =
            new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 0.25, 0.75);
    private static final AxisAlignedBB NORTH_CONN_AABB =
            new AxisAlignedBB(0.25, 0.25, 0.0, 0.75, 0.75, 0.25);
    private static final AxisAlignedBB SOUTH_CONN_AABB =
            new AxisAlignedBB(0.25, 0.25, 0.75, 0.75, 0.75, 1.0);
    private static final AxisAlignedBB EAST_CONN_AABB =
            new AxisAlignedBB(0.75, 0.25, 0.25, 1.0, 0.75, 0.75);
    private static final AxisAlignedBB WEST_CONN_AABB =
            new AxisAlignedBB(0.0, 0.25, 0.25, 0.25, 0.75, 0.75);
    private static final AxisAlignedBB CORE_AABB =
            new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

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

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();

        AxisAlignedBB coreBB = CORE_AABB.offset(pos);
        if (entityBox.intersects(coreBB))
            collidingBoxes.add(coreBB);

        if (isActualState) {
            AxisAlignedBB eastBB = EAST_CONN_AABB.offset(pos);
            AxisAlignedBB westBB = WEST_CONN_AABB.offset(pos);
            AxisAlignedBB northBB = NORTH_CONN_AABB.offset(pos);
            AxisAlignedBB southBB = SOUTH_CONN_AABB.offset(pos);
            AxisAlignedBB upBB = UP_CONN_AABB.offset(pos);
            AxisAlignedBB downBB = DOWN_CONN_AABB.offset(pos);

            if (state.getValue(connectUp) && upBB.intersects(entityBox))
                collidingBoxes.add(upBB);
            if (state.getValue(connectDown) && downBB.intersects(entityBox))
                collidingBoxes.add(downBB);
            if (state.getValue(connectNorth) && northBB.intersects(entityBox))
                collidingBoxes.add(northBB);
            if (state.getValue(connectSouth) && southBB.intersects(entityBox))
                collidingBoxes.add(southBB);
            if (state.getValue(connectEast) && eastBB.intersects(entityBox))
                collidingBoxes.add(eastBB);
            if (state.getValue(connectWest) && westBB.intersects(entityBox))
                collidingBoxes.add(westBB);
        }
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(
            IBlockState blockState,
            @Nonnull World worldIn,
            @Nonnull BlockPos pos,
            @Nonnull Vec3d start,
            @Nonnull Vec3d end) {
        List<RayTraceResult> results = getAABBs(getActualState(blockState, worldIn, pos), BlockPos.ORIGIN).stream()
                .map(aabb -> rayTrace(pos, start, end, aabb))
                .collect(Collectors.toCollection(ArrayList::new));

        RayTraceResult ret = null;
        double currDist = 0.0D;

        for (RayTraceResult result : results) {
            if (result != null) {
                double thisDist = result.hitVec.squareDistanceTo(end);
                if (thisDist > currDist) {
                    ret = result;
                    currDist = thisDist;
                }
            }
        }

        return ret;
    }

    private List<AxisAlignedBB> getAABBs(IBlockState state, BlockPos pos) {
        List<AxisAlignedBB> ret = new ArrayList<>();

        AxisAlignedBB coreBB = CORE_AABB.offset(pos);
        AxisAlignedBB eastBB = EAST_CONN_AABB.offset(pos);
        AxisAlignedBB westBB = WEST_CONN_AABB.offset(pos);
        AxisAlignedBB northBB = NORTH_CONN_AABB.offset(pos);
        AxisAlignedBB southBB = SOUTH_CONN_AABB.offset(pos);
        AxisAlignedBB upBB = UP_CONN_AABB.offset(pos);
        AxisAlignedBB downBB = DOWN_CONN_AABB.offset(pos);

        ret.add(coreBB);
        if (state.getValue(connectUp)) ret.add(upBB);
        if (state.getValue(connectDown)) ret.add(downBB);
        if (state.getValue(connectEast)) ret.add(eastBB);
        if (state.getValue(connectWest)) ret.add(westBB);
        if (state.getValue(connectNorth)) ret.add(northBB);
        if (state.getValue(connectSouth)) ret.add(southBB);

        return ret;
    }

}
