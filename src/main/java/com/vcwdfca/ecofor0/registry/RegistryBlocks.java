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
import github.kasuminova.novaeng.common.item.ecalculator.ItemECalculatorThreadCore;
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

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        // 第一期：存储
        event.getRegistry().register(createItemBlock(StorageEnergyCell.L13));

        // 第二期：计算
        registerThreadCoreWithItem(event, CalculatorThreadCore.L13);
        registerThreadCoreWithItem(event, CalculatorThreadCoreHyper.L13);
        event.getRegistry().register(createItemBlock(CalculatorParallelProc.L13));
        event.getRegistry().register(createItemBlock(CalculatorTail.L13));

        // 第三期：合成
        event.getRegistry().register(createItemBlock(FabricatorParallelProc.L13));
        event.getRegistry().register(createItemBlock(FabricatorTail.L13));

        // 第四期：Controller 主机（使用 ItemBlockController）
        event.getRegistry().register(createControllerItemBlock(StorageController.L13));
        event.getRegistry().register(createControllerItemBlock(CalculatorController.L13));
        event.getRegistry().register(createControllerItemBlock(FabricatorController.L13));
    }

    /**
     * 为方块创建配套 ItemBlock，并从方块复制 registryName / translationKey。
     * 能源仓在 Core 中也是走普通 {@link ItemBlock}（非自定义 ItemBlock），此处保持一致。
     */
    private static ItemBlock createItemBlock(final Block block) {
        final ResourceLocation registryName = Objects.requireNonNull(
                block.getRegistryName(), "block registryName must not be null");
        final ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(registryName);
        itemBlock.setTranslationKey(block.getTranslationKey());
        return itemBlock;
    }

    /**
     * ThreadCore 特殊处理：必须使用 {@link ItemECalculatorThreadCore} 并调用 {@code block.setItem}。
     * 否则破坏方块时 {@code BlockECalculatorThreadCore#breakBlock} 会因 {@code this.item == null} 掉落空气。
     */
    private static void registerThreadCoreWithItem(final RegistryEvent.Register<Item> event,
                                                    final github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorThreadCore block) {
        final ItemECalculatorThreadCore itemBlock = CalculatorThreadCore.createItemBlock(block);
        block.setItem(itemBlock);
        event.getRegistry().register(itemBlock);
    }

    /**
     * Controller 方块使用 {@link ItemBlockController}（继承自 modularmachinery）。
     */
    private static ItemBlockController createControllerItemBlock(final hellfirepvp.modularmachinery.common.block.BlockController block) {
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
