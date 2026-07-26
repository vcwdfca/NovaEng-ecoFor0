package com.vcwdfca.ecofor0.block;

import com.vcwdfca.ecofor0.util.IPatternAddition;
import github.kasuminova.mmce.common.util.DynamicPattern;
import github.kasuminova.novaeng.common.block.ecotech.estorage.BlockEStorageCasing;
import github.kasuminova.novaeng.common.block.ecotech.estorage.BlockEStorageCellDrive;
import github.kasuminova.novaeng.common.block.ecotech.estorage.BlockEStorageController;
import github.kasuminova.novaeng.common.block.ecotech.estorage.BlockEStorageMEChannel;
import github.kasuminova.novaeng.common.block.ecotech.estorage.BlockEStorageVent;
import hellfirepvp.modularmachinery.common.machine.TaggedPositionBlockArray;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;

import static com.vcwdfca.ecofor0.util.Util.info;

public class StorageController extends BlockEStorageController implements IPatternAddition {

    private static final BlockArray.BlockInformation CASING = info(BlockEStorageCasing.INSTANCE);
    public static final StorageController L13 = new StorageController("l13");

    public StorageController(String level) {
        super(level);
        BlockEStorageController.REGISTRY.put(this.registryName, this);
    }

    @Override
    public void setMainPattern() {
        this.parentMachine = this.getParentMachine();
        TaggedPositionBlockArray parts = this.parentMachine.getPattern();
        parts.addBlock(1, 0, 1, info(BlockEStorageMEChannel.INSTANCE));

        parts.addBlock(1, 0, 0, CASING);
        parts.addBlock(0, 0, 1, CASING);
        parts.addBlock(1, 1, 0, CASING);
        parts.addBlock(1, 1, 1, CASING);
        parts.addBlock(0, -1, 1, CASING);
        parts.addBlock(0, -1, 0, CASING);
        parts.addBlock(1, -1, 1, CASING);
        parts.addBlock(1, -1, 0, CASING);
        parts.addBlock(0, 1, 1, CASING);
        parts.addBlock(0, 1, 0, CASING);
    }

    @Override
    public void setDynamicPattern() {
        DynamicPattern pattern = new DynamicPattern("drives");
        pattern.addFaces(Collections.singleton(EnumFacing.WEST));
        pattern.setMinSize(1);
        pattern.setMaxSize(12);
        pattern.setStructureSizeOffsetStart(new BlockPos(-1, 0, 0));
        pattern.setStructureSizeOffset(new BlockPos(-1, 0, 0));

        TaggedPositionBlockArray parts = new TaggedPositionBlockArray();
        parts.addBlock(0, -1, 1, info(StorageEnergyCell.L13));
        parts.addBlock(0, -1, 0, info(BlockEStorageCellDrive.INSTANCE.getStateFromMeta(2)));
        parts.addBlock(0, 0, 0, info(BlockEStorageCellDrive.INSTANCE.getStateFromMeta(2)));
        parts.addBlock(0, 0, 1, info(BlockEStorageVent.INSTANCE.getStateFromMeta(2)));
        parts.addBlock(0, 1, 1, info(StorageEnergyCell.L13));
        parts.addBlock(0, 1, 0, info(BlockEStorageCellDrive.INSTANCE.getStateFromMeta(2)));
        pattern.setPattern(parts);

        TaggedPositionBlockArray partsEnd = new TaggedPositionBlockArray();
        partsEnd.addBlock(0, -1, 1, CASING);
        partsEnd.addBlock(0, -1, 0, CASING);
        partsEnd.addBlock(0, 0, 0, CASING);
        partsEnd.addBlock(0, 0, 1, CASING);
        partsEnd.addBlock(0, 1, 1, CASING);
        partsEnd.addBlock(0, 1, 0, CASING);
        pattern.setPatternEnd(partsEnd);

        this.parentMachine.addDynamicPattern("drives", pattern);
    }
}
