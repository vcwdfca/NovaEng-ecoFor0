package com.vcwdfca.ecofor0.util;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.helpers.DualityInterface;
import appeng.tile.inventory.AppEngInternalInventory;
import com.vcwdfca.ecofor0.common.tile.TileSuperPatternAssembly;
import github.kasuminova.mmce.common.tile.MEPatternProvider;
import github.kasuminova.mmce.common.util.PatternItemFilter;
import java.lang.reflect.Field;

/** MMCE has no resizing API for these final inventories. Replace only a new addon tile's instances. */
public final class PatternExpansion {

    private static final Field PATTERNS = field(MEPatternProvider.class, "patterns");
    private static final Field DETAILS = field(MEPatternProvider.class, "details");
    private static final Field INTERFACE_PATTERNS = field(DualityInterface.class, "patterns");

    private static Field field(Class<?> owner, String name) {
        try { Field field = owner.getDeclaredField(name); field.setAccessible(true); return field; }
        catch (ReflectiveOperationException error) { throw new ExceptionInInitializerError(error); }
    }
    
    public static void install(TileSuperPatternAssembly tile) {
        try {
            AppEngInternalInventory expanded = new AppEngInternalInventory(tile, TileSuperPatternAssembly.PATTERN_COUNT, 1, PatternItemFilter.INSTANCE);
            PATTERNS.set(tile, expanded);
            // MMCE's DualityInterface constructor already captured the original 36 slots.
            // Upload plugins and AE interface terminals must share the GUI's persistent inventory.
            INTERFACE_PATTERNS.set(tile.getInterfaceDuality(), expanded);
            DETAILS.set(tile, new ICraftingPatternDetails[TileSuperPatternAssembly.PATTERN_COUNT]);
        } catch (IllegalAccessException error) { throw new IllegalStateException("Cannot expand addon pattern inventory", error); }
    }
}
