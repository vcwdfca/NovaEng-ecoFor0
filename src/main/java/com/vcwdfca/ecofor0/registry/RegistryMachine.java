package com.vcwdfca.ecofor0.registry;

import com.vcwdfca.ecofor0.block.CalculatorController;
import com.vcwdfca.ecofor0.block.FabricatorController;
import com.vcwdfca.ecofor0.block.StorageController;
import com.vcwdfca.ecofor0.util.IPatternAddition;
import hellfirepvp.modularmachinery.common.block.BlockController;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.machine.MachineRegistry;

import java.util.Collections;

public final class RegistryMachine {

    private RegistryMachine() {
    }

    public static void register() {
        register(
            "extendable_calculator_subsystem_l13",
            "§9ECO - §5C13 §9可扩展计算子系统",
            CalculatorController.L13
        );
        register(
            "extendable_fabricator_subsystem_l13",
            "§9ECO - §bF13 §e可扩展合成子系统",
            FabricatorController.L13
        );
        register(
            "extendable_digital_storage_subsystem_l13",
            "§9ECO - §5L13 §9可扩展存储子系统",
            StorageController.L13
        );
    }

    private static <C extends BlockController & IPatternAddition> void register(String registryName, String localizedName, C controller) {
        DynamicMachine machine = new DynamicMachine(registryName);
        machine.setLocalizedName(localizedName);
        MachineRegistry.registerMachines(Collections.singleton(machine));

        controller.setMainPattern();
        controller.setDynamicPattern();

        BlockController.MACHINE_CONTROLLERS.put(machine, controller);
    }
}
