package com.vcwdfca.ecofor0.common.tile;

import appeng.api.AEApi;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.channels.IFluidStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.me.GridAccessException;
import com.mekeng.github.common.me.data.IAEGasStack;
import com.mekeng.github.common.me.data.impl.AEGasStack;
import com.mekeng.github.common.me.storage.IGasStorageChannel;
import com.vcwdfca.ecofor0.block.BlockSuperPatternAssembly;
import com.vcwdfca.ecofor0.component.AssemblyOutputComponent;
import com.vcwdfca.ecofor0.component.CatalystBank;
import com.vcwdfca.ecofor0.component.CatalystFluids;
import com.vcwdfca.ecofor0.component.GasOutputBuffer;
import com.vcwdfca.ecofor0.component.InputCapacity;
import com.vcwdfca.ecofor0.component.OutputTransfer;
import com.vcwdfca.ecofor0.util.PatternExpansion;
import github.kasuminova.mmce.common.tile.MEPatternProvider;
import github.kasuminova.mmce.common.util.AEFluidInventoryUpgradeable;
import github.kasuminova.mmce.common.util.InfItemFluidHandler;
import hellfirepvp.modularmachinery.common.lib.ComponentTypesMM;
import github.kasuminova.novaeng.common.tile.MEPatternProviderNova;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.util.IOInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;

/** A distinct MMCE input/output component. CPU batching is supplied by installed NovaCore 1.23.1. */
public final class TileSuperPatternAssembly extends MEPatternProvider implements MEPatternProviderNova, IGridTickable {
    // MMCE supplies this when a controller forms; it is not a saved item name.
    private String attachedMachineName;
    public static final int PATTERN_PAGES = 4, PATTERNS_PER_PAGE = 36, PATTERN_COUNT = PATTERN_PAGES * PATTERNS_PER_PAGE;
    private final CatalystBank[] catalysts = new CatalystBank[PATTERN_COUNT];
    public static final int OUTPUT_ITEM_SLOTS = 36;
    public static final int OUTPUT_FLUID_SLOTS = 9;
    private static final String OUTPUT_ITEMS = "spaOutputItems";
    private static final String OUTPUT_FLUIDS = "spaOutputFluids";
    private static final String OUTPUT_GASES = "spaOutputGases";
    private final IOInventory outputItems = new IOInventory(this, new int[0],
            IntStream.range(0, OUTPUT_ITEM_SLOTS).toArray());
    private final AEFluidInventoryUpgradeable outputFluids =
            new AEFluidInventoryUpgradeable(this, OUTPUT_FLUID_SLOTS, Integer.MAX_VALUE);
    private final GasOutputBuffer outputGases = new GasOutputBuffer(() -> {
        if (world != null && !world.isRemote) markNoUpdateSync();
    });
    private final List<MachineComponent<?>> outputComponents = new ArrayList<>();

    public TileSuperPatternAssembly() {
        PatternExpansion.install(this);
        for (int i = 0; i < PATTERN_COUNT; i++) {
            catalysts[i] = new CatalystBank(this::catalystChanged);
            InfItemFluidHandler input;
            if (i < combinationComponents.size()) input = (InfItemFluidHandler) combinationComponents.get(i).getContainerProvider();
            else {
                input = new InfItemFluidHandler();
                final InfItemFluidHandler provider = input;
                final long group = getUniqueGroupID();
                combinationComponents.add(new MachineComponent<InfItemFluidHandler>(hellfirepvp.modularmachinery.common.machine.IOType.INPUT) {
                    @Override public hellfirepvp.modularmachinery.common.crafting.ComponentType getComponentType() { return ComponentTypesMM.COMPONENT_ITEM_FLUID_GAS; }
                    @Override public InfItemFluidHandler getContainerProvider() { return provider; }
                    @Override public long getGroupID() { return group; }
                });
                input.setOnItemChanged(slot -> catalystChanged()); input.setOnFluidChanged(slot -> catalystChanged()); input.setOnGasChanged(slot -> catalystChanged());
            }
            input.setSubItemHandler(new net.minecraftforge.items.wrapper.CombinedInvWrapper(catalysts[i].items(), subItemHandler));
            input.setSubFluidHandler(new CatalystFluids(catalysts[i], subFluidHandler));
        }
        workMode = WorkModeSetting.ISOLATION_INPUT;
        outputItems.setStackLimit(Integer.MAX_VALUE, IntStream.range(0, OUTPUT_ITEM_SLOTS).toArray());
        outputComponents.add(new AssemblyOutputComponent<>(ComponentTypesMM.COMPONENT_ITEM, outputItems));
        outputComponents.add(new AssemblyOutputComponent<>(ComponentTypesMM.COMPONENT_FLUID, outputFluids));
        outputComponents.add(new AssemblyOutputComponent<>(ComponentTypesMM.COMPONENT_GAS, outputGases));
    }

    public CatalystBank catalysts(int index) { return catalysts[index]; }
    private void catalystChanged() {
        handlerDirty = true;
        if (world != null && !world.isRemote) markNoUpdateSync();
    }
    @Override protected void refreshPatterns() {
        for (int i = 0; i < details.length; i++) refreshPattern(i);
        if (currentPatternIdx >= 0 && currentPatternIdx < details.length) currentPattern = details[currentPatternIdx];
        if (world != null && !world.isRemote) {
            try { proxy.getGrid().postEvent(new appeng.api.networking.events.MENetworkCraftingPatternChange(this, proxy.getNode())); }
            catch (GridAccessException ignored) {}
        }
    }

    // NovaCore's executeCrafting reads this interface for each medium. No global CPU overwrite here.
    @Override
    public boolean r$isIgnoreParallel() {
        return true;
    }

    @Override
    public void r$IgnoreParallel() {
        /* Always enabled for this block. */
    }

    @Override
    @NotNull
    public Collection<MachineComponent<?>> provideComponents() {
        List<MachineComponent<?>> components = new ArrayList<>(super.provideComponents());
        components.addAll(outputComponents);
        return components;
    }

    @Override
    public ItemStack getVisualItemStack() {
        return new ItemStack(BlockSuperPatternAssembly.ITEM_INSTANCE);
    }

    @Override
    public String getMachineName() {
        return attachedMachineName == null || attachedMachineName.isEmpty()
                ? BlockSuperPatternAssembly.INSTANCE.getTranslationKey() : attachedMachineName;
    }

    @Override
    public void setMachineName(String name) {
        super.setMachineName(name);
        attachedMachineName = name;
    }

    /** Auto Pattern Upload queries the tile's display name, not MMCE's interface terminal name. */
    @Override
    public net.minecraft.util.text.ITextComponent getDisplayName() {
        if (hasCustomInventoryName())
            return new net.minecraft.util.text.TextComponentString(super.getCustomInventoryName());
        return new net.minecraft.util.text.TextComponentTranslation(
                attachedMachineName == null || attachedMachineName.isEmpty()
                        ? BlockSuperPatternAssembly.INSTANCE.getTranslationKey() + ".name" : attachedMachineName);
    }

    @Override
    public String getCustomInventoryName() {
        return hasCustomInventoryName() ? super.getCustomInventoryName() : getMachineName();
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails pattern, InventoryCrafting table) {
        InfItemFluidHandler target = handler;
        if (workMode == WorkModeSetting.ISOLATION_INPUT) {
            target = null;
            for (int i = 0; i < details.length; i++) {
                if (pattern.equals(details[i])) {
                    target = (InfItemFluidHandler) combinationComponents.get(i).getContainerProvider();
                    break;
                }
            }
        }
        if (target == null) return false;
        // MMCE appends into int stacks. Refuse the whole batch before touching it if capacity is exceeded.
        if (!InputCapacity.fits(target, table)) {
            return false;
        }
        return super.pushPattern(pattern, table);
    }

    @Override
    @NotNull
    public TickingRequest getTickingRequest(@NotNull IGridNode node) {
        // Do not sleep: outputs can be produced on MMCE worker threads, or loaded while ME is offline.
        return new TickingRequest(5, 40, false, false);
    }

    @Override
    @NotNull
    public TickRateModulation tickingRequest(@NotNull IGridNode node, int ticksSinceLastCall) {
        if (!proxy.isActive()) return TickRateModulation.SLOWER;
        boolean changed = false;
        try {
            IItemStorageChannel items = AEApi.instance().storage().getStorageChannel(IItemStorageChannel.class);
            IMEMonitor<IAEItemStack> itemNetwork = proxy.getStorage().getInventory(items);
            // Resolve all grid services before touching the buffer. Keep leftovers on failed/partial insertions.
            changed = OutputTransfer.items(outputItems, stored -> {
                IAEItemStack request = items.createStack(stored);
                if (request == null) return stored;
                IAEItemStack left = itemNetwork.injectItems(request, appeng.api.config.Actionable.MODULATE, source);
                return left == null ? ItemStack.EMPTY : left.createItemStack();
            });
            IFluidStorageChannel fluids = AEApi.instance().storage().getStorageChannel(IFluidStorageChannel.class);
            IMEMonitor<IAEFluidStack> fluidNetwork = proxy.getStorage().getInventory(fluids);
            changed |= OutputTransfer.fluids(outputFluids,
                    stored -> fluidNetwork.injectItems(stored, appeng.api.config.Actionable.MODULATE, source));
            IGasStorageChannel gases = AEApi.instance().storage().getStorageChannel(IGasStorageChannel.class);
            IMEMonitor<IAEGasStack> gasNetwork = proxy.getStorage().getInventory(gases);
            changed |= outputGases.flush(stored -> {
                IAEGasStack request = AEGasStack.of(stored);
                if (request == null) return stored;
                IAEGasStack left = gasNetwork.injectItems(request, appeng.api.config.Actionable.MODULATE, source);
                return left == null ? null : left.getGasStack();
            });
        } catch (GridAccessException ignored) {
            // Channel loss, full cells and unpowered networks leave queued output in persistent buffers.
        }
        if (changed) markChunkDirty();
        return changed ? TickRateModulation.FASTER : TickRateModulation.SLOWER;
    }

    @Override
    public NBTTagCompound writeProviderNBT(NBTTagCompound tag) {
        patterns.writeToNBT(tag, "patterns");
        subItemHandler.writeToNBT(tag, "subItemHandler");
        subFluidHandler.writeToNBT(tag, "subFluidHandler");
        tag.setByte("workMode", (byte) workMode.ordinal());
        if (currentPatternIdx >= 0) tag.setInteger("currentPatternIdx", currentPatternIdx);
        NBTTagCompound banks = new NBTTagCompound();
        for (int i = 0; i < catalysts.length; i++) if (catalysts[i].isNotEmpty()) banks.setTag(Integer.toString(i), catalysts[i].save());
        tag.setTag("spaCatalysts", banks);
        // Preserve both sets of input buffers even while modes are being switched with ME offline.
        synchronized (handler) { handler.writeToNBT(tag, "handler"); }
        NBTTagCompound components = new NBTTagCompound();
        for (int i = 0; i < combinationComponents.size(); i++) {
            InfItemFluidHandler buffer = (InfItemFluidHandler) combinationComponents.get(i).getContainerProvider();
            buffer.writeToNBT(components, "handler#" + i);
        }
        tag.setTag("components", components);
        Lock lock = outputItems.getRWLock().readLock();
        lock.lock();
        try { tag.setTag(OUTPUT_ITEMS, outputItems.writeNBT()); }
        finally { lock.unlock(); }
        lock = outputFluids.getRWLock().readLock();
        lock.lock();
        try { outputFluids.writeToNBT(tag, OUTPUT_FLUIDS); }
        finally { lock.unlock(); }
        tag.setTag(OUTPUT_GASES, outputGases.save());
        tag.setBoolean("machineCompleted", machineCompleted);
        if (hasCustomInventoryName()) tag.setString("spaCustomName", super.getCustomInventoryName());
        return tag;
    }

    @Override
    public void readProviderNBT(NBTTagCompound original) {
        NBTTagCompound tag = original.copy();
        NBTTagCompound savedPatterns = tag.getCompoundTag("patterns");
        savedPatterns.setInteger("Size", PATTERN_COUNT);
        tag.setTag("patterns", savedPatterns);
        int mode = tag.getByte("workMode");
        if (mode < 0 || mode >= WorkModeSetting.values().length) tag.setByte("workMode", (byte) 0);
        super.readProviderNBT(tag);
        // Older versions wrote this as a byte; getInteger accepts both numeric NBT forms.
        currentPatternIdx = tag.hasKey("currentPatternIdx") && workMode == WorkModeSetting.ENHANCED_BLOCKING_MODE
                ? tag.getInteger("currentPatternIdx") : -1;
        if (currentPatternIdx < -1 || currentPatternIdx >= PATTERN_COUNT) currentPatternIdx = -1;
        NBTTagCompound banks = tag.getCompoundTag("spaCatalysts");
        for (int i = 0; i < catalysts.length; i++) catalysts[i].load(banks.getCompoundTag(Integer.toString(i)));
        handler.readFromNBT(tag, "handler");
        NBTTagCompound components = tag.getCompoundTag("components");
        for (int i = 0; i < combinationComponents.size(); i++) {
            ((InfItemFluidHandler) combinationComponents.get(i).getContainerProvider())
                    .readFromNBT(components, "handler#" + i);
        }
        if (tag.hasKey(OUTPUT_ITEMS, 10)) {
            outputItems.readNBT(tag.getCompoundTag(OUTPUT_ITEMS));
            outputItems.setStackLimit(Integer.MAX_VALUE, IntStream.range(0, outputItems.getSlots()).toArray());
        }
        outputFluids.readFromNBT(tag, OUTPUT_FLUIDS);
        outputGases.load(tag.getCompoundTag(OUTPUT_GASES));
        if (tag.hasKey("machineCompleted")) machineCompleted = tag.getBoolean("machineCompleted");
        if (tag.hasKey("spaCustomName")) setCustomName(tag.getString("spaCustomName"));
    }

    @Override
    public boolean isAllDefault() {
        for (int i = 0; i < PATTERN_COUNT; i++) if (!patterns.getStackInSlot(i).isEmpty() || catalysts[i].isNotEmpty()) return false;
        return super.isAllDefault() && pendingItems() == 0 && pendingFluid() == 0 && pendingGas() == 0;
    }

    public long pendingGas() { return outputGases.pending(); }

    public long pendingItems() {
        Lock lock = outputItems.getRWLock().readLock();
        lock.lock();
        try {
            long amount = 0;
            for (int i = 0; i < outputItems.getSlots(); i++) amount += outputItems.getStackInSlot(i).getCount();
            return amount;
        } finally { lock.unlock(); }
    }

    public long pendingFluid() {
        Lock lock = outputFluids.getRWLock().readLock();
        lock.lock();
        try {
            long amount = 0;
            for (int i = 0; i < outputFluids.getSlots(); i++) {
                IAEFluidStack stack = outputFluids.getFluidInSlot(i);
                if (stack != null) amount += stack.getStackSize();
            }
            return amount;
        } finally { lock.unlock(); }
    }
}
