package com.vcwdfca.ecofor0.component;

import com.glodblock.github.common.item.fake.FakeItemRegister;
import com.glodblock.github.common.item.fake.FakeFluids;
import com.glodblock.github.integration.mek.FakeGases;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.IGasItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

/** A typed quantity. NBT stores counts as ints, never ItemStack's signed byte Count. */
public final class AssemblyResource {

    public final int kind, amount;
    private final ItemStack item;
    private final FluidStack fluid;
    private final GasStack gas;

    private AssemblyResource(int kind, int amount, ItemStack item, FluidStack fluid, GasStack gas) {
        this.kind = kind;
        this.amount = amount;
        this.item = item;
        this.fluid = fluid;
        this.gas = gas;
    }

    public static AssemblyResource of(ItemStack value) {
        return value == null || value.isEmpty() ? null : new AssemblyResource(0, value.getCount(), value.copy(), null, null);
    }

    public static AssemblyResource of(FluidStack value) {
        return value == null || value.amount <= 0 ? null : new AssemblyResource(1, value.amount, null, value.copy(), null);
    }

    public static AssemblyResource of(GasStack value) {
        return value == null || value.amount <= 0 ? null : new AssemblyResource(2, value.amount, null, null, value.copy());
    }

    public ItemStack item() {
        ItemStack v = item.copy();
        v.setCount(amount);
        return v;
    }

    public FluidStack fluid() {
        return new FluidStack(fluid, amount);
    }

    public GasStack gas() {
        GasStack v = gas.copy();
        v.amount = amount;
        return v;
    }

    public AssemblyResource sized(int count) {
        return count <= 0 ? null : new AssemblyResource(kind, count, item, fluid, gas);
    }

    public boolean same(AssemblyResource other) {
        if (other == null || kind != other.kind) {
            return false;
        }

        if (kind == 0) {
            return item.isItemEqual(other.item) && ItemStack.areItemStackTagsEqual(item, other.item);
        }

        return kind == 1 ? fluid.isFluidEqual(other.fluid) : gas.isGasEqual(other.gas);
    }

    public NBTTagCompound save() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("kind", kind);
        tag.setInteger("amount", amount);
        tag.setTag("value", kind == 0 ? sized(1).item().writeToNBT(new NBTTagCompound())
                : kind == 1 ? fluid().writeToNBT(new NBTTagCompound()) : gas().write(new NBTTagCompound()));
        return tag;
    }

    public static AssemblyResource load(NBTTagCompound tag) {
        NBTTagCompound value = tag.getCompoundTag("value");
        int kind = tag.getInteger("kind"), count = tag.getInteger("amount");
        AssemblyResource resource = kind == 0 ? of(new ItemStack(value)) : kind == 1
                ? of(FluidStack.loadFluidStackFromNBT(value)) : kind == 2 ? of(GasStack.readFromNBT(value)) : null;
        return resource == null ? null : resource.sized(count);
    }

    public static AssemblyResource fromHeld(int kind, ItemStack held) {
        if (held.isEmpty()) {
            return null;
        }
        if (kind == 0) {
            return FakeItemRegister.isFakeItem(held) ? null : of(held);
        }
        Object value = FakeItemRegister.isFakeItem(held) ? FakeItemRegister.getStack(held) : null;
        if (kind == 1) {
            return of(value instanceof FluidStack ? (FluidStack) value : FluidUtil.getFluidContained(held));
        }
        if (value instanceof GasStack) {
            return of((GasStack) value);
        }
        return held.getItem() instanceof IGasItem ? of(((IGasItem) held.getItem()).getGas(held)) : null;
    }

    public String name() {
        return kind == 0 ? item.getDisplayName() : kind == 1 ? fluid.getLocalizedName() : gas.getGas().getLocalizedName();
    }

    public static AssemblyResource fromIngredient(int kind, Object value) {
        if (value instanceof ItemStack){
            return fromHeld(kind, (ItemStack) value);
        }
        if (kind == 1 && value instanceof FluidStack){
            return of((FluidStack) value);
        }

        return kind == 2 && value instanceof GasStack ? of((GasStack) value) : null;
    }

    public ItemStack icon() {
        if (kind == 0) return sized(1).item();
        // AE2FC's drop handlers deliberately reject displayStack. Its display helpers
        // use display-only packets; the real quantity stays in this resource.
        return kind == 1 ? FakeFluids.displayFluid(fluid()) : FakeGases.displayGas(gas());
    }
}
