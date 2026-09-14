package com.vcwdfca.ecofor0.block.calculator;

import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorThreadCoreHyper;

public class CalculatorThreadCoreHyper extends BlockECalculatorThreadCoreHyper {
    public static final BlockECalculatorThreadCoreHyper L13 = new CalculatorThreadCoreHyper("l13", 2, 32);

    protected CalculatorThreadCoreHyper(String level, int threads, int hyperThreads) {
        super(level, threads, hyperThreads);
    }
}
