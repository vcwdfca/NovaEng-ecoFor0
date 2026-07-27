package com.vcwdfca.ecofor0.calculator;

import github.kasuminova.novaeng.common.block.ecotech.ecalculator.prop.DriveStorageLevel;
import github.kasuminova.novaeng.common.item.ecalculator.ECalculatorCell;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/** L13 flash crystal array using the highest Core storage level as its drive type. */
public class CalculatorCell extends ECalculatorCell {

    public static final CalculatorCell L13 =
            new CalculatorCell(DriveStorageLevel.C, 262_144L);

    protected CalculatorCell(final DriveStorageLevel level, final long totalMegabytes) {
        super(level, totalMegabytes);
    }

    @Override
    public void addInformation(final @NotNull ItemStack stack,
                               final World world,
                               final List<String> tooltip,
                               final @NotNull ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(I18n.format("novaeng.ecalculator_cell.l13.tip"));
    }
}
