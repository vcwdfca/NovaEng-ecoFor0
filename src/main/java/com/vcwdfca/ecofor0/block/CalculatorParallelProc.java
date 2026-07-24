package com.vcwdfca.ecofor0.block;

import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorParallelProc;

public class CalculatorParallelProc extends BlockECalculatorParallelProc {
    /**
     * L13 并行度取 131072 (2^17) 而非 1048576 (2^20)。
     * 原因：ECalculatorController 累加 parallelism 到 int 字段，1048576 约 2048 块即溢出 int(2^31)。
     * 131072 提供约 16384 块的安全余量，仍是 L9(16384) 的 8 倍。
     */
    public static final BlockECalculatorParallelProc L13 = new CalculatorParallelProc("l13", 131072);

    protected CalculatorParallelProc(String level, int parallelism) {
        super(level, parallelism);
    }
}
