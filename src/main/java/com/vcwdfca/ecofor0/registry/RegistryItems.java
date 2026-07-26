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
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorThreadCore;
import github.kasuminova.novaeng.common.item.ecalculator.ItemECalculatorThreadCore;
import hellfirepvp.modularmachinery.common.block.BlockController;
import hellfirepvp.modularmachinery.common.item.ItemBlockController;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class RegistryItems {

    private RegistryItems() {
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(createItemBlock(StorageEnergyCell.L13));

        registerThreadCoreWithItem(event, CalculatorThreadCore.L13);
        registerThreadCoreWithItem(event, CalculatorThreadCoreHyper.L13);
        event.getRegistry().register(createItemBlock(CalculatorParallelProc.L13));
        event.getRegistry().register(createItemBlock(CalculatorTail.L13));

        event.getRegistry().register(createItemBlock(FabricatorParallelProc.L13));
        event.getRegistry().register(createItemBlock(FabricatorTail.L13));

        event.getRegistry().register(createControllerItemBlock(StorageController.L13));
        event.getRegistry().register(createControllerItemBlock(CalculatorController.L13));
        event.getRegistry().register(createControllerItemBlock(FabricatorController.L13));
    }

    private static ItemBlock createItemBlock(final Block block) {
        final ResourceLocation registryName = Objects.requireNonNull(
                block.getRegistryName(), "block registryName must not be null");
        final ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(registryName);
        itemBlock.setTranslationKey(block.getTranslationKey());
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

    private static ItemBlockController createControllerItemBlock(final BlockController block) {
        final ResourceLocation registryName = Objects.requireNonNull(
                block.getRegistryName(), "controller registryName must not be null");
        final ItemBlockController itemBlock = new ItemBlockController(block) {
            @Override
            public @NotNull String getItemStackDisplayName(final @NotNull ItemStack stack) {
                return I18n.format(getTranslationKey(stack) + ".name");
            }
        };
        itemBlock.setRegistryName(registryName);
        itemBlock.setTranslationKey(block.getTranslationKey());
        return itemBlock;
    }
}
