package com.vcwdfca.ecofor0.component;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** The same auxiliary-inventory route used by MMCE; recipe chance determines consumption. */
public final class CatalystFluids implements IFluidHandler {

    private final List<IFluidHandler> handlers = new ArrayList<>();

    public CatalystFluids(CatalystBank bank, IFluidHandler shared) {
        handlers.addAll(Arrays.asList(bank.fluids())); handlers.add(shared);
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        List<IFluidTankProperties> result = new ArrayList<>();
        for (IFluidHandler h : handlers) result.addAll(Arrays.asList(h.getTankProperties()));
        return result.toArray(new IFluidTankProperties[0]);
    }

    @Override
    public int fill(FluidStack value, boolean real) {
        if (value == null || value.amount <= 0) return 0;
        int filled = 0;
        for (IFluidHandler h : handlers) {
            filled += h.fill(new FluidStack(value, value.amount - filled), real); if (filled == value.amount) break;
        }
        return filled;
    }

    @Override
    public FluidStack drain(FluidStack value, boolean real) {
        if (value == null || value.amount <= 0) return null;
        int drained = 0;
        for (IFluidHandler h : handlers) {
            FluidStack part = h.drain(new FluidStack(value, value.amount - drained), real);
            if (part != null) drained += part.amount;
            if (drained == value.amount) break;
        }
        return drained == 0 ? null : new FluidStack(value, drained);
    }

    @Override
    public FluidStack drain(int maximum, boolean real) {
        if (maximum <= 0) return null;
        for (IFluidHandler h : handlers) {
            FluidStack value = h.drain(maximum, false);
            if (value != null) return drain(new FluidStack(value, maximum), real);
        }
        return null;
    }
}
