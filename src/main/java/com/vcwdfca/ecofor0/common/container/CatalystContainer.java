package com.vcwdfca.ecofor0.common.container;

import com.vcwdfca.ecofor0.network.AssemblyNetwork;
import com.vcwdfca.ecofor0.component.CatalystBank;
import com.vcwdfca.ecofor0.common.tile.TileSuperPatternAssembly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public final class CatalystContainer extends Container {
    private final TileSuperPatternAssembly assembly;
    private final int pattern;
    private final CatalystBank bank;
    NBTTagCompound snapshot = new NBTTagCompound();

    public CatalystContainer(TileSuperPatternAssembly tile, int pattern, EntityPlayer player) {
        assembly = tile; this.pattern = pattern; bank = tile.catalysts(pattern);
        for (int i = 0; i < 9; i++) addSlotToContainer(new SlotItemHandler(bank.items(), i, 8 + i % 3 * 18, 30 + i / 3 * 18));
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 112 + row * 18));
        for (int col = 0; col < 9; col++) addSlotToContainer(new Slot(player.inventory, col, 8 + col * 18, 170));
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer player) {
        return FrequencyContainer.allowed(assembly, player);
    }

    @Override
    @NotNull
    public ItemStack slotClick(int id, int drag, @NotNull ClickType type, @NotNull EntityPlayer player) {
        return canInteractWith(player) ? super.slotClick(id, drag, type, player) : ItemStack.EMPTY;
    }

    @Override
    @NotNull
    public ItemStack transferStackInSlot(@NotNull EntityPlayer player, int index) {
        if (!canInteractWith(player) || index < 0 || index >= inventorySlots.size()) {
            return ItemStack.EMPTY;
        }
        Slot slot = inventorySlots.get(index);
        ItemStack stack = slot.getStack();
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack old = stack.copy();
        if (!(index < 9 ? mergeItemStack(stack, 9, inventorySlots.size(), true) : mergeItemStack(stack, 0, 9, false))) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.putStack(ItemStack.EMPTY);
        } else {
            slot.onSlotChanged();
        }
        slot.onTake(player, stack);
        return old;
    }

    @Override
    public void addListener(@NotNull IContainerListener listener) {
        super.addListener(listener);
        if (listener instanceof EntityPlayerMP) {
            AssemblyNetwork.CHANNEL.sendTo(new AssemblyNetwork.Snapshot(windowId, bank.save()), (EntityPlayerMP) listener);
        }
    }

    @Override public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (assembly.getWorld().isRemote) {
            return;
        }
        NBTTagCompound next = bank.save();
        if (next.equals(snapshot)) {
            return;
        }
        snapshot = next;
        for (IContainerListener listener : listeners) {
            if (listener instanceof EntityPlayerMP) {
                AssemblyNetwork.CHANNEL.sendTo(new AssemblyNetwork.Snapshot(windowId, next), (EntityPlayerMP) listener);
            }
        }
    }

    public TileSuperPatternAssembly getAssembly() {
        return this.assembly;
    }

    public int getPattern() {
        return this.pattern;
    }

    public CatalystBank getBank() {
        return this.bank;
    }

    public void transferFluid(EntityPlayerMP player, int index, boolean intoTank) {
        if (!canInteractWith(player) || index < 0 || index >= 9) return;
        ItemStack held = player.inventory.getItemStack(); if (held.isEmpty()) return;
        ItemStack single = held.copy(); single.setCount(1);
        IFluidHandlerItem container = FluidUtil.getFluidHandler(single); if (container == null) return;
        FluidTank tank = bank.fluids()[index];
        if (intoTank) {
            FluidStack offered = container.drain(tank.getCapacity(), false); if (offered == null) return;
            int accepted = tank.fill(offered, false); if (accepted <= 0) return;
            FluidStack drained = container.drain(accepted, true); if (drained == null || drained.amount <= 0) return;
            tank.fill(drained, true);
        } else {
            FluidStack offered = tank.drain(Integer.MAX_VALUE, false); if (offered == null) return;
            int accepted = container.fill(offered, true); if (accepted <= 0) return;
            tank.drain(accepted, true);
        }
        ItemStack remainder = container.getContainer();
        if (held.getCount() == 1) player.inventory.setItemStack(remainder);
        else { held.shrink(1); if (!remainder.isEmpty() && !player.inventory.addItemStackToInventory(remainder)) player.dropItem(remainder, false); }
        player.connection.sendPacket(new net.minecraft.network.play.server.SPacketSetSlot(-1, -1, player.inventory.getItemStack()));
        detectAndSendChanges();
    }
}
