package com.vcwdfca.ecofor0.component;

import com.mekeng.github.common.me.inventory.impl.GasInventory;
import github.kasuminova.mmce.common.util.GasInventoryHandler;
import mekanism.api.gas.GasStack;
import net.minecraft.nbt.NBTTagCompound;
import java.util.function.Function;

/** Uses the handler monitor for MMCE recipe insertion, ME export and persistence alike. */
public final class GasOutputBuffer extends GasInventoryHandler {

    private static final int SLOTS = 9;
    private final GasInventory tanks;

    public GasOutputBuffer(Runnable changed) {
        this(new GasInventory(SLOTS, Integer.MAX_VALUE, (inventory, slot) -> changed.run()));
    }

    private GasOutputBuffer(GasInventory tanks) {
        super(tanks);
        this.tanks = tanks;
    }

    public synchronized boolean flush(Function<GasStack, GasStack> insert) {
        boolean changed = false;
        for (int slot = 0; slot < tanks.size(); slot++) {
            GasStack stored = tanks.getGasStack(slot);
            if (stored == null) continue;
            GasStack left = insert.apply(stored.copy());
            if ((left == null ? 0 : left.amount) != stored.amount) {
                tanks.setGas(slot, left);
                changed = true;
            }
        }
        return changed;
    }

    public synchronized NBTTagCompound save() {
        return tanks.save();
    }

    public synchronized AssemblyResource resource(int slot) {
        return AssemblyResource.of(tanks.getGasStack(slot));
    }

    public synchronized void load(NBTTagCompound tag) {
        // Missing gas data in a 1.0.0 item/save means empty; never retain stale contents.
        for (int slot = 0; slot < tanks.size(); slot++) {
            tanks.setGas(slot, null);
        }
        tanks.load(tag);
        tanks.setCap(Integer.MAX_VALUE);
    }

    public synchronized long pending() {
        long count = 0;
        for (int slot = 0; slot < tanks.size(); slot++) {
            GasStack gas = tanks.getGasStack(slot);
            if (gas != null) {
                count += gas.amount;
            }
        }
        return count;
    }
}
