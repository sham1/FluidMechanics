package fi.sinervo.sham1.fluidmechanics.client;

import fi.sinervo.sham1.fluidmechanics.block.Blocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

import java.util.Objects;

public class Proxy extends fi.sinervo.sham1.fluidmechanics.common.Proxy {
    @Override
    public void setupItemModels() {
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(Blocks.BASIC_FLUID_PIPE),
                0,
                new ModelResourceLocation(
                        Objects.requireNonNull(Blocks.BASIC_FLUID_PIPE.getRegistryName()),
                        "inventory"));
    }
}
