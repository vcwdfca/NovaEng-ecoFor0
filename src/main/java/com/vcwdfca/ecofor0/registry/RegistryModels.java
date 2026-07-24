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
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

/**
 * 客户端物品模型注册。仅在客户端事件总线触发。
 * <p>
 * 与 Core 的 {@code RegistryBlocks#registerBlockModel} 做法一致：把物品模型指向
 * {@code <registryName>#inventory}。L13 走静态物品模型（见 {@code assets/novaeng_core/models/item/}），
 * 暂不复刻 Core 的按电量动态渲染（{@code EStorageEnergyCellItemRenderer}）。
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Tags.MOD_ID)
public final class RegistryModels {

    private RegistryModels() {
    }

    @SubscribeEvent
    public static void onRegisterModels(final ModelRegistryEvent event) {
        // 第一期：存储
        registerInventoryModel(StorageEnergyCell.L13);

        // 第二期：计算
        registerInventoryModel(CalculatorThreadCore.L13);
        registerInventoryModel(CalculatorThreadCoreHyper.L13);
        registerInventoryModel(CalculatorParallelProc.L13);
        registerInventoryModel(CalculatorTail.L13);

        // 第三期：合成
        registerInventoryModel(FabricatorParallelProc.L13);
        registerInventoryModel(FabricatorTail.L13);

        // 第四期：Controller 主机
        registerInventoryModel(StorageController.L13);
        registerInventoryModel(CalculatorController.L13);
        registerInventoryModel(FabricatorController.L13);
    }

    private static void registerInventoryModel(final Block block) {
        final Item item = Item.getItemFromBlock(block);
        final ResourceLocation registryName = Objects.requireNonNull(
                block.getRegistryName(),
                "Block registryName must not be null for model registration: " + block.getClass().getName());
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(registryName, "inventory"));
    }
}
