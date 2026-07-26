package com.vcwdfca.ecofor0.registry;

import com.vcwdfca.ecofor0.Tags;
import com.vcwdfca.ecofor0.block.CalculatorController;
import com.vcwdfca.ecofor0.block.CalculatorParallelProc;
import com.vcwdfca.ecofor0.block.CalculatorTail;
import com.vcwdfca.ecofor0.block.CalculatorThreadCore;
import com.vcwdfca.ecofor0.block.CalculatorThreadCoreHyper;
import com.vcwdfca.ecofor0.block.FabricatorController;
import com.vcwdfca.ecofor0.block.FabricatorParallelProc;
import com.vcwdfca.ecofor0.block.FabricatorTail;
import com.vcwdfca.ecofor0.block.StorageController;
import com.vcwdfca.ecofor0.block.StorageEnergyCell;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class RegistryBlocks {

    private RegistryBlocks() {
    }

    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(StorageEnergyCell.L13);

        event.getRegistry().register(CalculatorThreadCore.L13);
        event.getRegistry().register(CalculatorThreadCoreHyper.L13);
        event.getRegistry().register(CalculatorParallelProc.L13);
        event.getRegistry().register(CalculatorTail.L13);

        event.getRegistry().register(FabricatorParallelProc.L13);
        event.getRegistry().register(FabricatorTail.L13);

        event.getRegistry().register(StorageController.L13);
        event.getRegistry().register(CalculatorController.L13);
        event.getRegistry().register(FabricatorController.L13);
    }

}
