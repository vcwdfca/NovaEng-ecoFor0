package com.vcwdfca.ecofor0.block;

import github.kasuminova.novaeng.common.block.ecotech.estorage.BlockEStorageController;

public class StorageController extends BlockEStorageController {
    public static final BlockEStorageController L13 = new StorageController("l13");

    public StorageController(String level) {
        super(level);
        BlockEStorageController.REGISTRY.put(this.registryName, this);
    }
}
