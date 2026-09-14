package com.vcwdfca.ecofor0.component;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

/**
 *  Physical catalysts: nine items and nine 16-bucket tanks, following GTL's per-pattern bank.
 */
public final class CatalystBank {

    private final ItemStackHandler items;
    private final FluidTank[] fluids = new FluidTank[9];

    public CatalystBank(Runnable changed) {
        items = new ItemStackHandler(9) {
            @Override protected void onContentsChanged(int slot) {
                changed.run();
            }
        };
        for (int i = 0; i < fluids.length; i++) {
            fluids[i] = new FluidTank(16000) {
                @Override
                protected void onContentsChanged() {
                    changed.run();
                }
            };
        }
    }

    public NBTTagCompound save() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("items", items.serializeNBT());
        NBTTagList tanks = new NBTTagList();
        for (FluidTank tank : fluids) {
            tanks.appendTag(tank.writeToNBT(new NBTTagCompound()));
        }
        tag.setTag("fluids", tanks);
        return tag;
    }

    public void load(NBTTagCompound tag) {
        NBTTagCompound inv = tag.getCompoundTag("items").copy();
        inv.setInteger("Size", 9);
        items.deserializeNBT(inv);
        NBTTagList tanks = tag.getTagList("fluids", 10);
        for (int i = 0; i < fluids.length; i++) {
            fluids[i].setFluid(null);
            if (i < tanks.tagCount()) {
                fluids[i].readFromNBT(tanks.getCompoundTagAt(i));
            }
        }
    }

    public ItemStackHandler items() {
        return this.items;
    }

    public FluidTank[] fluids() {
        return this.fluids;
    }

    public boolean isNotEmpty() {
        for (int i = 0; i < 9; i++) {
            if (!items.getStackInSlot(i).isEmpty() || fluids[i].getFluidAmount() > 0) {
                return true;
            }
        }
        return false;
    }
}
