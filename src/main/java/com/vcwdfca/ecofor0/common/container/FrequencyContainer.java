package com.vcwdfca.ecofor0.common.container;

import appeng.api.config.SecurityPermissions;
import appeng.me.GridAccessException;
import com.vcwdfca.ecofor0.network.AssemblyNetwork;
import com.vcwdfca.ecofor0.component.FrequencyData;
import github.kasuminova.mmce.common.tile.base.MEMachineComponent;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class FrequencyContainer extends Container {
    public final MEMachineComponent tile;
    public NBTTagCompound snapshot = new NBTTagCompound();
    private final EntityPlayer viewer;
    private int ticks;
    public FrequencyContainer(MEMachineComponent tile, EntityPlayer player) {
        this.tile = tile;
        viewer = player;
    }

    public static boolean allowed(MEMachineComponent tile, EntityPlayer player) {
        if (tile == null || tile.isInvalid() || tile.getWorld() != player.world || player.world.getTileEntity(tile.getPos()) != tile
                || player.getDistanceSqToCenter(tile.getPos()) > 64 || !player.world.canMineBlockBody(player, tile.getPos())) return false;

        try {
            return tile.getProxy().getSecurity().hasPermission(player, SecurityPermissions.BUILD);
        } catch (GridAccessException offline) {
            return true;
        }
    }

    @Override
    public boolean canInteractWith(@NotNull EntityPlayer player) {
        return allowed(tile, player);
    }

    @Override
    public @NotNull ItemStack transferStackInSlot(@NotNull EntityPlayer player, int slot) {
        return ItemStack.EMPTY;
    }

    private NBTTagCompound state() {
        FrequencyData data = FrequencyData.get(tile.getWorld());
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList entries = new NBTTagList();
        for (FrequencyData.Entry entry : data.list()) {
            NBTTagCompound row = new NBTTagCompound(); row.setUniqueId("id", entry.id); row.setString("name", entry.name);
            row.setBoolean("editable", entry.creator.equals(viewer.getUniqueID()) || viewer.canUseCommand(2, ""));
            row.setBoolean("occupied", entry.manager != null);
            if (entry.manager != null) {
                row.setInteger("dimension", entry.manager.dimension); row.setLong("pos", entry.manager.pos.toLong());
            }
            entries.appendTag(row);
        }
        tag.setTag("entries", entries); return tag;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges(); if (tile.getWorld().isRemote || ticks++ % 10 != 0) return;
        NBTTagCompound next = state(); if (snapshot.equals(next)) return; snapshot = next;
        for (IContainerListener listener : listeners) if (listener instanceof EntityPlayerMP)
            AssemblyNetwork.CHANNEL.sendTo(new AssemblyNetwork.Snapshot(windowId, snapshot), (EntityPlayerMP) listener);
    }

    @Override
    public void addListener(@NotNull IContainerListener listener) {
        super.addListener(listener);
        if (listener instanceof EntityPlayerMP) AssemblyNetwork.CHANNEL.sendTo(new AssemblyNetwork.Snapshot(windowId, state()), (EntityPlayerMP) listener);
    }

    public void action(EntityPlayerMP player, UUID id) {
        if (!canInteractWith(player)) return;
        FrequencyData data = FrequencyData.get(tile.getWorld()); FrequencyData.Entry entry = data.find(id);
        ticks = 0;
        detectAndSendChanges();
    }
}
