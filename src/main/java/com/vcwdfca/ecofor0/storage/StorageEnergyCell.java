package com.vcwdfca.ecofor0.storage;

import github.kasuminova.novaeng.common.block.ecotech.estorage.BlockEStorageEnergyCell;

public class StorageEnergyCell extends BlockEStorageEnergyCell {
    public static final BlockEStorageEnergyCell L13 = new StorageEnergyCell("l13", 13_000_000_000D);

    protected StorageEnergyCell(String level, double maxEnergyStore) {
        super(level, maxEnergyStore);
    }
}
