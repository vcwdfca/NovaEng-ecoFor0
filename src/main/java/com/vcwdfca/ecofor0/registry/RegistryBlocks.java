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

/**
 * addon 自持的注册入口。
 * <p>
 * 不复用 NovaEngineering-Core 的 {@code RegistryBlocks#registerBlock} /
 * {@code prepareItemBlockRegister} 静态入队方法：Core 会在自己的注册事件
 * （{@code priority = LOW}）中消费并 {@code clear()} 这些列表，addon 若入队存在
 * 时序风险。因此这里直接通过 {@code event.getRegistry().register(...)} 注册。
 * <p>
 * {@link StorageEnergyCell#L13} 继承 Core 构造函数，其 registryName 被强制写入
 * {@code novaeng_core} 命名空间；跨 modid 注册在 1.12.2 合法，对应资源须放在
 * {@code assets/novaeng_core/} 下。
 * <p>
 * TileEntity 复用 Core 已注册的 {@code estorage_energy_cell} 类型，无需重复注册。
 */
@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class RegistryBlocks {

    private RegistryBlocks() {
    }

    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event) {
        // 第一期：存储
        event.getRegistry().register(StorageEnergyCell.L13);

        // 第二期：计算
        event.getRegistry().register(CalculatorThreadCore.L13);
        event.getRegistry().register(CalculatorThreadCoreHyper.L13);
        event.getRegistry().register(CalculatorParallelProc.L13);
        event.getRegistry().register(CalculatorTail.L13);

        // 第三期：合成
        event.getRegistry().register(FabricatorParallelProc.L13);
        event.getRegistry().register(FabricatorTail.L13);

        // 第四期：Controller 主机
        event.getRegistry().register(StorageController.L13);
        event.getRegistry().register(CalculatorController.L13);
        event.getRegistry().register(FabricatorController.L13);
    }

}
