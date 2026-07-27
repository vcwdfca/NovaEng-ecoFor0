package com.vcwdfca.ecofor0.storage;

import github.kasuminova.novaeng.common.block.ecotech.estorage.prop.DriveStorageLevel;
import github.kasuminova.novaeng.common.item.estorage.EStorageCellItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class StorageCellItem extends EStorageCellItem {

    public static final StorageCellItem L13 =
            new StorageCellItem(DriveStorageLevel.C, 1024, 256);

    protected StorageCellItem(final DriveStorageLevel level, final int totalMegabytes, final int byteMultiplier) {
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

final class StorageCellL13Tooltip {

    private StorageCellL13Tooltip() {
    }

    static void replaceLevelTooltip(final List<String> tooltip) {
        tooltip.remove(net.minecraft.client.resources.I18n.format("novaeng.estorage_cell.l9.tip"));
        tooltip.add(net.minecraft.client.resources.I18n.format("novaeng.estorage_cell.l13.tip"));
    }
}
