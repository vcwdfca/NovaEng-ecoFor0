package com.vcwdfca.ecofor0.registry;

import com.vcwdfca.ecofor0.Tags;
import com.vcwdfca.ecofor0.block.calculator.CalculatorController;
import com.vcwdfca.ecofor0.block.calculator.CalculatorParallelProc;
import com.vcwdfca.ecofor0.block.calculator.CalculatorTail;
import com.vcwdfca.ecofor0.block.calculator.CalculatorThreadCore;
import com.vcwdfca.ecofor0.block.calculator.CalculatorThreadCoreHyper;
import com.vcwdfca.ecofor0.block.fabricator.FabricatorController;
import com.vcwdfca.ecofor0.block.fabricator.FabricatorParallelProc;
import com.vcwdfca.ecofor0.block.fabricator.FabricatorTail;
import com.vcwdfca.ecofor0.block.storage.StorageController;
import com.vcwdfca.ecofor0.block.storage.StorageEnergyCell;
import com.vcwdfca.ecofor0.block.BlockSuperPatternAssembly;
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

        event.getRegistry().register(BlockSuperPatternAssembly.INSTANCE);
    }

}
