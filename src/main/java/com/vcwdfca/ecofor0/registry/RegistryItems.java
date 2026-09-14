package com.vcwdfca.ecofor0.registry;

import com.vcwdfca.ecofor0.Tags;
import com.vcwdfca.ecofor0.block.calculator.CalculatorCell;
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
import com.vcwdfca.ecofor0.block.storage.StorageCellFluid;
import com.vcwdfca.ecofor0.block.storage.StorageCellGas;
import com.vcwdfca.ecofor0.block.storage.StorageCellItem;
import com.vcwdfca.ecofor0.block.BlockSuperPatternAssembly;
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorParallelProc;
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorThreadCore;
import github.kasuminova.novaeng.common.block.ecotech.efabricator.BlockEFabricatorParallelProc;
import github.kasuminova.novaeng.common.item.ecalculator.ItemECalculatorController;
import github.kasuminova.novaeng.common.item.ecalculator.ItemECalculatorParallelProc;
import github.kasuminova.novaeng.common.item.ecalculator.ItemECalculatorThreadCore;
import github.kasuminova.novaeng.common.item.efabriactor.ItemEFabricatorController;
import github.kasuminova.novaeng.common.item.efabriactor.ItemEFabricatorParallelProc;
import github.kasuminova.novaeng.common.item.estorage.ItemEStorageController;
import hellfirepvp.modularmachinery.common.block.BlockController;
import hellfirepvp.modularmachinery.common.item.ItemBlockController;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Constructor;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class RegistryItems {

    private RegistryItems() {
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(createItemBlock(StorageEnergyCell.L13));
        event.getRegistry().register(CalculatorCell.L13);
        event.getRegistry().register(StorageCellItem.L13);
        event.getRegistry().register(StorageCellFluid.L13);
        event.getRegistry().register(StorageCellGas.L13);
        event.getRegistry().register(BlockSuperPatternAssembly.ITEM_INSTANCE);

        registerThreadCoreWithItem(event, CalculatorThreadCore.L13);
        registerThreadCoreWithItem(event, CalculatorThreadCoreHyper.L13);
        event.getRegistry().register(createCalculatorParallelProcItem(CalculatorParallelProc.L13));
        event.getRegistry().register(createItemBlock(CalculatorTail.L13));

        event.getRegistry().register(createFabricatorParallelProcItem(FabricatorParallelProc.L13));
        event.getRegistry().register(createItemBlock(FabricatorTail.L13));

        event.getRegistry().register(createControllerItemBlock(ItemEStorageController.class, StorageController.L13));
        event.getRegistry().register(createControllerItemBlock(ItemECalculatorController.class, CalculatorController.L13));
        event.getRegistry().register(createControllerItemBlock(ItemEFabricatorController.class, FabricatorController.L13));
    }

    private static ItemBlock createItemBlock(final Block block) {
        final ResourceLocation registryName = Objects.requireNonNull(
                block.getRegistryName(), "block registryName must not be null");
        final ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(registryName);
        return itemBlock;
    }

    @SuppressWarnings("SameParameterValue")
    private static ItemECalculatorParallelProc createCalculatorParallelProcItem(
            final BlockECalculatorParallelProc block) {
        final ResourceLocation registryName = Objects.requireNonNull(
                block.getRegistryName(), "block registryName must not be null");
        final ItemECalculatorParallelProc itemBlock = new ItemECalculatorParallelProc(block);
        itemBlock.setRegistryName(registryName);
        return itemBlock;
    }

    @SuppressWarnings("SameParameterValue")
    private static ItemEFabricatorParallelProc createFabricatorParallelProcItem(
            final BlockEFabricatorParallelProc block) {
        final ResourceLocation registryName = Objects.requireNonNull(
                block.getRegistryName(), "block registryName must not be null");
        final ItemEFabricatorParallelProc itemBlock = new ItemEFabricatorParallelProc(block);
        itemBlock.setRegistryName(registryName);
        return itemBlock;
    }

    /**
     * Thread cores must use {@link ItemECalculatorThreadCore} and call
     * {@code block.setItem}. Otherwise, {@code BlockECalculatorThreadCore#breakBlock}
     * drops air because {@code this.item == null}.
     */
    private static void registerThreadCoreWithItem(final RegistryEvent.Register<Item> event,
                                                    final BlockECalculatorThreadCore block) {
        final ItemECalculatorThreadCore itemBlock = CalculatorThreadCore.createItemBlock(block);
        block.setItem(itemBlock);
        event.getRegistry().register(itemBlock);
    }

    private static <C extends ItemBlockController> ItemBlockController createControllerItemBlock(Class<C> ctrl, final BlockController block) {
        final ResourceLocation registryName = Objects.requireNonNull(
                block.getRegistryName(), "controller registryName must not be null");
        C itemBlock;
        try {
            Constructor<C> constructor = ctrl.getConstructor(BlockController.class);
            itemBlock = constructor.newInstance(block);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create controller item block", e);
        }
        itemBlock.setRegistryName(registryName);
        return itemBlock;
    }
}
