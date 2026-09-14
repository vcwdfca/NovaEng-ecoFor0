package com.vcwdfca.ecofor0.common.container;

import appeng.container.slot.SlotRestrictedInput;
import com.vcwdfca.ecofor0.network.AssemblyNetwork;
import com.vcwdfca.ecofor0.common.tile.TileSuperPatternAssembly;
import github.kasuminova.mmce.common.container.ContainerMEPatternProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

/** Constant server slot IDs across pages prevent a delayed click from targeting another pattern. */
public final class PagedPatternContainer extends ContainerMEPatternProvider {

    private final TileSuperPatternAssembly assembly;
    private int page;

    public PagedPatternContainer(TileSuperPatternAssembly owner, EntityPlayer player) {
        super(owner, player); assembly = owner;
        for (int i = 0; i < TileSuperPatternAssembly.PATTERN_COUNT; i++) {
            PatternSlot slot = new PatternSlot(i, player);
            if (i < 36) { slot.setContainer(this); slot.slotNumber = 36 + i; inventorySlots.set(36 + i, slot); }
            else addSlotToContainer(slot);
        }
    }

    public int page() {
        return page;
    }

    private boolean hidden(int id) {
        return id >= 0 && id < inventorySlots.size() && inventorySlots.get(id) instanceof PatternSlot && !((PatternSlot) inventorySlots.get(id)).active();
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return FrequencyContainer.allowed(assembly, player) && super.canInteractWith(player);
    }

    @Override
    @NotNull
    public ItemStack transferStackInSlot(EntityPlayer player, int id) {
        if (!canInteractWith(player) || id < 0 || id >= inventorySlots.size() || hidden(id)) return ItemStack.EMPTY;
        return super.transferStackInSlot(player, id);
    }

    @Override
    public void doAction(net.minecraft.entity.player.EntityPlayerMP player, appeng.helpers.InventoryAction action, int id, long value) {
        if (!canInteractWith(player)) return;
        if (action == appeng.helpers.InventoryAction.FILL_ITEM || action == appeng.helpers.InventoryAction.EMPTY_ITEM) {
            if (id != 0) return; // The inherited shared fluid tank is the only AE custom slot.
        } else if (hidden(id)) return;
        super.doAction(player, action, id, value);
    }

    public void page(int value) {
        if (value < 0 || value >= TileSuperPatternAssembly.PATTERN_PAGES) return;
        resetDrag();
        page = value;
        for (Slot slot : inventorySlots) if (slot instanceof PatternSlot) ((PatternSlot) slot).position();
        for (IContainerListener listener : listeners) syncPage(listener);
    }

    private void syncPage(IContainerListener listener) {
        if (!(listener instanceof net.minecraft.entity.player.EntityPlayerMP)) return;
        net.minecraft.nbt.NBTTagCompound tag = new net.minecraft.nbt.NBTTagCompound(); tag.setInteger("spaPatternPage", page);
        AssemblyNetwork.CHANNEL.sendTo(new AssemblyNetwork.Snapshot(windowId, tag), (net.minecraft.entity.player.EntityPlayerMP) listener);
    }

    @Override
    public void addListener(@NotNull IContainerListener listener) {
        super.addListener(listener); syncPage(listener);
    }

    @Override
    @NotNull
    public ItemStack slotClick(int id, int dragType, ClickType type, @NotNull EntityPlayer player) {
        if (!FrequencyContainer.allowed(assembly, player)) return ItemStack.EMPTY;
        if (hidden(id)) return ItemStack.EMPTY;
        return super.slotClick(id, dragType, type, player);
    }

    public TileSuperPatternAssembly getAssembly() {
        return this.assembly;
    }

    public final class PatternSlot extends SlotRestrictedInput {

        private final int index;

        PatternSlot(int index, EntityPlayer player) {
            super(PlacableItemType.ENCODED_PATTERN, assembly.getPatterns(), index, 0, 0, player.inventory);
            this.index = index; position();
        }

        boolean active() {
            return index / 36 == page;
        }

        void position() {
            xPos = active() ? 8 + index % 9 * 18 : -10000;
            yPos = 28 + index % 36 / 9 * 18;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public boolean isEnabled() {
            return active();
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return active() && super.isItemValid(stack);
        }

        @Override
        public boolean canTakeStack(EntityPlayer player) {
            return active() && super.canTakeStack(player);
        }

        public int getIndex() {
            return this.index;
        }
    }
}
