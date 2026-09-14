package com.vcwdfca.ecofor0.component;

import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;

/** Marker for outputs expanded at the recipe-context boundary, outside MMCE's one-entry-per-tile map. */
public final class AssemblyOutputComponent<T> extends MachineComponent<T> {
    private final ComponentType type;
    private final T handler;

    public AssemblyOutputComponent(ComponentType type, T handler) {
        super(IOType.OUTPUT);
        this.type = type;
        this.handler = handler;
    }

    @Override public ComponentType getComponentType() { return type; }
    @Override public T getContainerProvider() { return handler; }
}
