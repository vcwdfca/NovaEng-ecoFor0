package com.vcwdfca.ecofor0.block.storage;

import github.kasuminova.novaeng.common.block.ecotech.estorage.prop.DriveStorageLevel;
import github.kasuminova.novaeng.common.item.estorage.EStorageCellFluid;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class StorageCellFluid extends EStorageCellFluid {

    public static final StorageCellFluid L13 =
            new StorageCellFluid(DriveStorageLevel.C, 1024, 256);

    protected StorageCellFluid(final DriveStorageLevel level, final int totalMegabytes, final int byteMultiplier) {
        super(level, totalMegabytes, byteMultiplier);
    }

    @Override
    protected void addCheckedInformation(final ItemStack stack,
                                         final World world,
                                         final List<String> tooltip,
                                         final ITooltipFlag flag) {
        super.addCheckedInformation(stack, world, tooltip, flag);
        StorageCellL13Tooltip.replaceLevelTooltip(tooltip);
    }
}
