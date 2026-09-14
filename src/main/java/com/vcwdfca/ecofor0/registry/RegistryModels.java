package com.vcwdfca.ecofor0.registry;

import com.vcwdfca.ecofor0.Tags;
import com.vcwdfca.ecofor0.block.BlockSuperPatternAssembly;
import com.vcwdfca.ecofor0.block.calculator.CalculatorController;
import com.vcwdfca.ecofor0.block.calculator.CalculatorCell;
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
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

/**
 * Registers client item models and runs only on the client event bus.
 * <p>
 * Matches Core's {@code RegistryBlocks#registerBlockModel} behavior by
 * targeting {@code <registryName>#inventory}. The blockstate inventory
 * variants provide the item appearance; no standalone item model files are
 * needed for these blocks.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Tags.MOD_ID)
public final class RegistryModels {

    private RegistryModels() {
    }

    @SubscribeEvent
    public static void onRegisterModels(final ModelRegistryEvent event) {
        registerInventoryModel(StorageEnergyCell.L13);
        registerInventoryModel(CalculatorCell.L13);
        registerInventoryModel(StorageCellItem.L13);
        registerInventoryModel(StorageCellFluid.L13);
        registerInventoryModel(StorageCellGas.L13);

        registerInventoryModel(CalculatorThreadCore.L13);
        registerInventoryModel(CalculatorThreadCoreHyper.L13);
        registerInventoryModel(CalculatorParallelProc.L13);
        registerInventoryModel(CalculatorTail.L13);

        registerInventoryModel(FabricatorParallelProc.L13);
        registerInventoryModel(FabricatorTail.L13);

        registerInventoryModel(StorageController.L13);
        registerInventoryModel(CalculatorController.L13);
        registerInventoryModel(FabricatorController.L13);

        registerInventoryModel(BlockSuperPatternAssembly.INSTANCE, "normal");
    }

    private static void registerInventoryModel(final Block block, String variantIn) {
        final Item item = Item.getItemFromBlock(block);
        if (item == Items.AIR) {
            throw new IllegalStateException("No ItemBlock registered for " + block.getRegistryName());
        }
        final ResourceLocation registryName = Objects.requireNonNull(
            block.getRegistryName(),
            "Block registryName must not be null for model registration: " + block.getClass().getName());
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(registryName, variantIn));
    }

    private static void registerInventoryModel(final Block block) {
        registerInventoryModel(block, "inventory");
    }

    private static void registerInventoryModel(final Item item) {
        final ResourceLocation registryName = Objects.requireNonNull(
                item.getRegistryName(),
                "Item registryName must not be null for model registration: " + item.getClass().getName());
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(registryName, "inventory"));
    }
}
