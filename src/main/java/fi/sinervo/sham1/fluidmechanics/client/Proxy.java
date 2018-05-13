package fi.sinervo.sham1.fluidmechanics.client;

import fi.sinervo.sham1.fluidmechanics.block.Blocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class Proxy extends fi.sinervo.sham1.fluidmechanics.common.Proxy {
    @Override
    public void setupItemModels() {
        ModelLoader.setCustomModelResourceLocation(
                Blocks.BASIC_FLUID_PIPE_ITEM,
                0,
                new ModelResourceLocation(
                        Blocks.BASIC_FLUID_PIPE_ITEM.getRegistryName(),
                        "inventory"));
        ModelLoader.setCustomModelResourceLocation(
                Blocks.BASIC_FLUID_PUMP_ITEM,
                0,
                new ModelResourceLocation(
                        Blocks.BASIC_FLUID_PUMP_ITEM.getRegistryName(),
                        "inventory"));
    }
}
