package com.vcwdfca.ecofor0.block;

import com.vcwdfca.ecofor0.util.IPatternAddition;
import github.kasuminova.novaeng.common.block.ecotech.estorage.BlockEStorageController;

public class StorageController extends BlockEStorageController implements IPatternAddition {

    public static final StorageController L13 = new StorageController("l13");

    public StorageController(String level) {
        super(level);
        BlockEStorageController.REGISTRY.put(this.registryName, this);
    }

    @Override //TODO
    public void setMainPattern() {

    }

    @Override
    public void setDynamicPattern() {

    }
}
