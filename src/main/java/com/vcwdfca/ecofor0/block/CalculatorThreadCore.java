package com.vcwdfca.ecofor0.block;

import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorThreadCore;
import github.kasuminova.novaeng.common.item.ecalculator.ItemECalculatorThreadCore;

import java.util.Objects;

public class CalculatorThreadCore extends BlockECalculatorThreadCore {
    public static final BlockECalculatorThreadCore L13 = new CalculatorThreadCore("l13", 16, 0);

    protected CalculatorThreadCore(String level, int threads, int hyperThreads) {
        super(level, threads, hyperThreads);
    }

    /**
     * 为 L13 创建配套的 ItemBlock。
     * 注意：ThreadCore 破坏时掉落 {@code this.item}，必须在注册后调用 {@code block.setItem(itemBlock)}。
     */
    public static ItemECalculatorThreadCore createItemBlock(BlockECalculatorThreadCore block) {
        ItemECalculatorThreadCore itemBlock = new ItemECalculatorThreadCore(block);
        itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName(),
                "block registryName must not be null"));
        itemBlock.setTranslationKey(block.getTranslationKey());
        return itemBlock;
    }
}
