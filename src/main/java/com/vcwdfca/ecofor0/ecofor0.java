package com.vcwdfca.ecofor0;

import com.vcwdfca.ecofor0.registry.RegistryMachine;
import com.vcwdfca.ecofor0.network.AssemblyNetwork;
import com.vcwdfca.ecofor0.block.BlockSuperPatternAssembly;
import com.vcwdfca.ecofor0.client.ClientProxy;
import com.vcwdfca.ecofor0.common.tile.TileSuperPatternAssembly;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies =
    "required-after:modularmachinery;" +
    "required-after:novaeng_core;" +
    "required-after:mekanism"
)
public class ecofor0 {

    @Mod.Instance(Tags.MOD_ID)
    public static ecofor0 INSTANCE;
    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);
    public static ClientProxy UI = new ClientProxy();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerTileEntity(TileSuperPatternAssembly.class, BlockSuperPatternAssembly.BLOCK_ID);
        AssemblyNetwork.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new GuiHandler());
        LOGGER.info("preInit From {}", Tags.MOD_NAME);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        RegistryMachine.register();
    }

    public static ResourceLocation id(String id) {
        return new ResourceLocation(Tags.MOD_ID, id);
    }

}
