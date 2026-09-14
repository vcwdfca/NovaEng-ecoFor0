package com.vcwdfca.ecofor0.component;

import com.glodblock.github.common.item.fake.FakeFluids;
import com.glodblock.github.common.item.fake.FakeItemRegister;
import com.glodblock.github.integration.mek.FakeGases;
import github.kasuminova.mmce.common.util.InfItemFluidHandler;
import mekanism.api.gas.GasStack;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/** Simulate the entire delivery with long arithmetic before MMCE mutates any int-sized stack. */
public final class InputCapacity {

    private InputCapacity() {}

    public static boolean fits(InfItemFluidHandler target, InventoryCrafting table) {
        List<ItemStack> items = new ArrayList<>(target.getItemStackList());
        List<FluidStack> fluids = new ArrayList<>(target.getFluidStackList());
        List<Object> gases = new ArrayList<>(target.getGasStackList());
        for (int slot = 0; slot < table.getSizeInventory(); slot++) {
            ItemStack incoming = table.getStackInSlot(slot);
            if (incoming.isEmpty()) continue;
            if (FakeFluids.isFluidFakeItem(incoming)) {
                FluidStack fluid = FakeItemRegister.getStack(incoming);
                if (fluid == null || fluid.amount <= 0) return false;
                long amount = fluid.amount;
                for (FluidStack stored : fluids) {
                    if (stored != null && stored.isFluidEqual(fluid)) amount += stored.amount;
                }
                if (amount > Integer.MAX_VALUE) return false;
                fluids.add(fluid);
            } else if (FakeGases.isGasFakeItem(incoming)) {
                GasStack gas = FakeItemRegister.getStack(incoming);
                if (gas == null || gas.amount <= 0) return false;
                long amount = gas.amount;
                for (Object value : gases) {
                    if (value instanceof GasStack && ((GasStack) value).isGasEqual(gas)) amount += ((GasStack) value).amount;
                }
                if (amount > Integer.MAX_VALUE) return false;
                gases.add(gas);
            } else {
                long amount = incoming.getCount();
                for (ItemStack stored : items) {
                    if (!stored.isEmpty() && stored.isItemEqual(incoming)
                            && ItemStack.areItemStackTagsEqual(stored, incoming)) amount += stored.getCount();
                }
                if (amount > Integer.MAX_VALUE) return false;
                items.add(incoming);
            }
        }
        return true;
    }
}
