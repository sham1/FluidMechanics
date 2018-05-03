package fi.sinervo.sham1.fluidmechanics;

import fi.sinervo.sham1.fluidmechanics.block.Blocks;
import fi.sinervo.sham1.fluidmechanics.common.Proxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = FluidMechanics.MODID, name = "Fluid Mechanics", version = "1.12-0.0.1.0")
public class FluidMechanics {

    public static final String MODID = "fluidmechanics";

    @Mod.Instance(value = "fluidmechanics")
    public static FluidMechanics INSTANCE;

    @SidedProxy(modId = "fluidmechanics",
            clientSide = "fi.sinervo.sham1.fluidmechanics.client.Proxy",
            serverSide = "fi.sinervo.sham1.fluidmechanics.server.Proxy")
    public static Proxy proxy;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new Blocks());
        Blocks.registerTileEntities();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent event) {
        proxy.setupItemModels();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void postInit(FMLPostInitializationEvent event) {

    }
}
