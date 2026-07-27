package com.vcwdfca.ecofor0.calculator;

import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorThreadCore;
import github.kasuminova.novaeng.common.item.ecalculator.ItemECalculatorThreadCore;

import java.util.Objects;

public class CalculatorThreadCore extends BlockECalculatorThreadCore {
    public static final BlockECalculatorThreadCore L13 = new CalculatorThreadCore("l13", 16, 0);

    protected CalculatorThreadCore(String level, int threads, int hyperThreads) {
        super(level, threads, hyperThreads);
    }

    /**
     * Creates the L13 ItemBlock. Thread cores drop {@code this.item} when
     * broken, so registration must subsequently call {@code block.setItem(itemBlock)}.
     */
    public static ItemECalculatorThreadCore createItemBlock(BlockECalculatorThreadCore block) {
        ItemECalculatorThreadCore itemBlock = new ItemECalculatorThreadCore(block);
        itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName(),
                "block registryName must not be null"));
        return itemBlock;
    }
}
