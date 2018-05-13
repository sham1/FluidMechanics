package fi.sinervo.sham1.fluidmechanics.block;

import fi.sinervo.sham1.fluidmechanics.block.tileentities.BasicFluidPipeTE;
import fi.sinervo.sham1.fluidmechanics.block.tileentities.BasicFluidPumpTE;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public final class Blocks {
    public static final Block BASIC_FLUID_PIPE = new BasicFluidPipe();
    public static final Block BASIC_FLUID_PUMP = new BasicFluidPump();

    public static final ItemBlock BASIC_FLUID_PIPE_ITEM = new ItemBlock(BASIC_FLUID_PIPE);
    public static final ItemBlock BASIC_FLUID_PUMP_ITEM = new ItemBlock(BASIC_FLUID_PUMP);

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                BASIC_FLUID_PIPE,
                BASIC_FLUID_PUMP
        );
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerBlockItems(RegistryEvent.Register<Item> event) {
        BASIC_FLUID_PIPE_ITEM.setRegistryName(Objects.requireNonNull(BASIC_FLUID_PIPE.getRegistryName()));
        BASIC_FLUID_PUMP_ITEM.setRegistryName(Objects.requireNonNull(BASIC_FLUID_PUMP.getRegistryName()));
        event.getRegistry().registerAll(BASIC_FLUID_PIPE_ITEM, BASIC_FLUID_PUMP_ITEM);
    }

    public static void registerTileEntities() {
        TileEntity.register(BASIC_FLUID_PIPE.getRegistryName().toString(), BasicFluidPipeTE.class);
        TileEntity.register(BASIC_FLUID_PUMP.getRegistryName().toString(), BasicFluidPumpTE.class);
    }
}
