package com.vcwdfca.ecofor0.block.calculator;

import com.vcwdfca.ecofor0.util.IPatternAddition;
import com.vcwdfca.ecofor0.util.IControllerLevelDisplay;
import github.kasuminova.mmce.common.util.DynamicPattern;
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorCasing;
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorCellDrive;
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorController;
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorMEChannel;
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.BlockECalculatorTransmitterBus;
import hellfirepvp.modularmachinery.common.lib.BlocksMM;
import hellfirepvp.modularmachinery.common.machine.TaggedPositionBlockArray;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;

import static com.vcwdfca.ecofor0.util.Util.info;

/** L13 calculator controller with an add-on-specific display tier. */
public class CalculatorController extends BlockECalculatorController implements IPatternAddition, IControllerLevelDisplay {

    private static final BlockArray.BlockInformation CASING = info(BlockECalculatorCasing.INSTANCE);
    public static final CalculatorController L13 = new CalculatorController("l13");

    public CalculatorController(String level) {
        super(level);
        BlockECalculatorController.REGISTRY.put(this.registryName, this);
    }

    @Override
    public String getDisplayLevel() {
        return "L13";
    }

    @Override
    public void setMainPattern() {
        this.parentMachine = this.getParentMachine();
        TaggedPositionBlockArray parts = this.parentMachine.getPattern();
        parts.addBlock(1, 0, 1, info(BlockECalculatorMEChannel.INSTANCE));
        parts.addBlock(1, 1, 1, info(BlocksMM.fluidInputHatch, BlocksMM.meFluidInputBus));
        parts.addBlock(1, -1, 1, info(BlocksMM.fluidOutputHatch, BlocksMM.meFluidOutputBus));

        parts.addBlock(-1, 0, 1, CASING);
        parts.addBlock(-1, 0, 0, CASING);
        parts.addBlock(1, 0, 0, CASING);
        parts.addBlock(0, 0, 1, CASING);
        parts.addBlock(1, 1, 0, CASING);
        parts.addBlock(-1, -1, 1, CASING);
        parts.addBlock(0, -1, 1, CASING);
        parts.addBlock(0, -1, 0, CASING);
        parts.addBlock(1, -1, 0, CASING);
        parts.addBlock(-1, 1, 1, CASING);
        parts.addBlock(-1, 1, 0, CASING);
        parts.addBlock(0, 1, 1, CASING);
        parts.addBlock(0, 1, 0, CASING);
        parts.addBlock(-1, -1, 0, CASING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setDynamicPattern() {
        DynamicPattern pattern = new DynamicPattern("workers");
        pattern.addFaces(Collections.singleton(EnumFacing.WEST));
        pattern.setMinSize(1);
        pattern.setMaxSize(12);
        pattern.setStructureSizeOffsetStart(new BlockPos(-2, 0, 0));
        pattern.setStructureSizeOffset(new BlockPos(-1, 0, 0));

        TaggedPositionBlockArray parts = new TaggedPositionBlockArray();
        parts.addBlock(0, -1, 1, info(CalculatorParallelProc.L13));
        parts.addBlock(0, -1, 0, info(BlockECalculatorCellDrive.INSTANCE.getStateFromMeta(2)));
        parts.addBlock(0, 0, 0, info(BlockECalculatorTransmitterBus.INSTANCE.getStateFromMeta(2)));
        parts.addBlock(0, 0, 1, info(CalculatorThreadCore.L13, CalculatorThreadCoreHyper.L13));
        parts.addBlock(0, 1, 1, info(CalculatorParallelProc.L13));
        parts.addBlock(0, 1, 0, info(BlockECalculatorCellDrive.INSTANCE.getStateFromMeta(2)));
        pattern.setPattern(parts);

        TaggedPositionBlockArray partsEnd = new TaggedPositionBlockArray();
        partsEnd.addBlock(0, -1, 1, CASING);
        partsEnd.addBlock(0, -1, 0, CASING);
        partsEnd.addBlock(0, 0, 0, info(CalculatorTail.L13.getStateFromMeta(2)));
        partsEnd.addBlock(0, 0, 1, CASING);
        partsEnd.addBlock(0, 1, 1, CASING);
        partsEnd.addBlock(0, 1, 0, info(BlockECalculatorCasing.INSTANCE.getStateFromMeta(2)));
        pattern.setPatternEnd(partsEnd);

        this.parentMachine.addDynamicPattern("workers", pattern);
    }
}
