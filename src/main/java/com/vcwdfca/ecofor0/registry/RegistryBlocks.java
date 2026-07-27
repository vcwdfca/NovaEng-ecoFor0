package com.vcwdfca.ecofor0.registry;

import com.vcwdfca.ecofor0.Tags;
import com.vcwdfca.ecofor0.calculator.CalculatorController;
import com.vcwdfca.ecofor0.calculator.CalculatorParallelProc;
import com.vcwdfca.ecofor0.calculator.CalculatorTail;
import com.vcwdfca.ecofor0.calculator.CalculatorThreadCore;
import com.vcwdfca.ecofor0.calculator.CalculatorThreadCoreHyper;
import com.vcwdfca.ecofor0.fabricator.FabricatorController;
import com.vcwdfca.ecofor0.fabricator.FabricatorParallelProc;
import com.vcwdfca.ecofor0.fabricator.FabricatorTail;
import com.vcwdfca.ecofor0.storage.StorageController;
import com.vcwdfca.ecofor0.storage.StorageEnergyCell;
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
