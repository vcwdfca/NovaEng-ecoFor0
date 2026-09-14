package com.vcwdfca.ecofor0.component;

import appeng.api.storage.data.IAEFluidStack;
import github.kasuminova.mmce.common.util.AEFluidInventoryUpgradeable;
import hellfirepvp.modularmachinery.common.util.IOInventory;
import net.minecraft.item.ItemStack;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;

/** Network calls run under the same locks that MMCE uses when writing recipe outputs. */
public final class OutputTransfer {

    private OutputTransfer() {}

    public static boolean items(IOInventory buffer, Function<ItemStack, ItemStack> insert) {
        boolean changed = false;
        Lock lock = buffer.getRWLock().writeLock();
        lock.lock();
        try {
            for (int slot = 0; slot < buffer.getSlots(); slot++) {
                ItemStack stored = buffer.getStackInSlot(slot);
                if (stored.isEmpty()) continue;
                ItemStack left = insert.apply(stored.copy());
                if (left.getCount() != stored.getCount()) {
                    buffer.setStackInSlot(slot, left);
                    changed = true;
                }
            }
        } finally { lock.unlock(); }
        return changed;
    }

    public static boolean fluids(AEFluidInventoryUpgradeable buffer, Function<IAEFluidStack, IAEFluidStack> insert) {
        boolean changed = false;
        Lock lock = buffer.getRWLock().writeLock();
        lock.lock();
        try {
            for (int slot = 0; slot < buffer.getSlots(); slot++) {
                IAEFluidStack stored = buffer.getFluidInSlot(slot);
                if (stored == null) continue;
                IAEFluidStack left = insert.apply(stored.copy());
                if ((left == null ? 0 : left.getStackSize()) != stored.getStackSize()) {
                    buffer.setFluidInSlot(slot, left);
                    changed = true;
                }
            }
        } finally { lock.unlock(); }
        return changed;
    }
}
