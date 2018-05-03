package fi.sinervo.sham1.fluidmechanics.block;

import fi.sinervo.sham1.fluidmechanics.FluidMechanics;
import fi.sinervo.sham1.fluidmechanics.block.tileentities.BasicFluidPipeTE;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.Objects;

public final class Blocks {
    public static final Block BASIC_FLUID_PIPE = new BasicFluidPipe();

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                BASIC_FLUID_PIPE
        );
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerBlockItems(RegistryEvent.Register<Item> event) {
        Arrays.stream(new Block[] {
                BASIC_FLUID_PIPE
        }).map(ItemBlock::new)
                .map(item -> item.setRegistryName(Objects.requireNonNull(item.getBlock().getRegistryName())))
                .forEach(item -> event.getRegistry().register(item));
    }

    public static void registerTileEntities() {
        TileEntity.register(String.format("%s:%s", FluidMechanics.MODID, "basic_fluid_pipe"), BasicFluidPipeTE.class);
    }
}
