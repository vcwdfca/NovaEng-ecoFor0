package com.vcwdfca.ecofor0.block.calculator;

import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorParallelProc;

public class CalculatorParallelProc extends BlockECalculatorParallelProc {
    /**
     * L13 uses 131072 (2^17) parallelism instead of 1048576 (2^20).
     * ECalculatorController accumulates parallelism in an int field, so
     * 1048576 overflows int (2^31) at about 2048 blocks. 131072 remains
     * eight times L9's 16384 while allowing about 16384 blocks safely.
     */
    public static final BlockECalculatorParallelProc L13 = new CalculatorParallelProc("l13", 131072);

    protected CalculatorParallelProc(String level, int parallelism) {
        super(level, parallelism);
    }
}
