package com.vcwdfca.ecofor0.block;

import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorController;

/** L13 计算控制器。L13 的等级校验由 addon Mixin 单独处理。 */
public class CalculatorController extends BlockECalculatorController {
    public static final BlockECalculatorController L13 = new CalculatorController("l13");

    public CalculatorController(String level) {
        super(level);
        REGISTRY.put(this.registryName, this);
    }
}
