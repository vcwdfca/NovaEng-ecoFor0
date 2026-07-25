package com.vcwdfca.ecofor0.block;

import com.vcwdfca.ecofor0.util.IPatternAddition;
import github.kasuminova.novaeng.common.block.ecotech.efabricator.BlockEFabricatorController;

public class FabricatorController extends BlockEFabricatorController implements IPatternAddition {

    /** Core stops at L9 (16x); L13 continues the same doubling progression. */
    public static final int L13_QUEUE_DEPTH_MULTIPLIER = 32;
    public static final int L13_ENERGY_USAGE_MULTIPLIER = 32;

    public static final FabricatorController L13 = new FabricatorController("l13");

    public FabricatorController(String level) {
        super(level);
        BlockEFabricatorController.REGISTRY.put(this.registryName, this);
    }

    @Override //TODO
    public void setMainPattern() {

    }

    @Override
    public void setDynamicPattern() {

    }
}
