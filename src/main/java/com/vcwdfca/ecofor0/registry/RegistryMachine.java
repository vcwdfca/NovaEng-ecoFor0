package com.vcwdfca.ecofor0.registry;

import com.vcwdfca.ecofor0.calculator.CalculatorController;
import com.vcwdfca.ecofor0.fabricator.FabricatorController;
import com.vcwdfca.ecofor0.storage.StorageController;
import com.vcwdfca.ecofor0.util.IControllerLevelDisplay;
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
            "§b可扩展计算子系统主机",
            CalculatorController.L13
        );
        register(
            "extendable_fabricator_subsystem_l13",
            "§e可扩展合成子系统主机",
            FabricatorController.L13
        );
        register(
            "extendable_digital_storage_subsystem_l13",
            "§9可扩展存储子系统主机",
            StorageController.L13
        );
    }

    private static <C extends BlockController & IPatternAddition & IControllerLevelDisplay> void register(
            String registryName, String description, C controller) {
        DynamicMachine machine = new DynamicMachine(registryName);
        machine.setLocalizedName("§9ECO - §c" + controller.getDisplayLevel() + " " + description);
        machine.setHasFactory(false);
        MachineRegistry.registerMachines(Collections.singleton(machine));

        controller.setMainPattern();
        controller.setDynamicPattern();

        BlockController.MACHINE_CONTROLLERS.put(machine, controller);
    }
}
