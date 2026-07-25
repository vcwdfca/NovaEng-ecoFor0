// L13 delta fragment. Requires the matching instance script; do not use standalone.
RecipeBuilder.newBuilder("extendable_calculator_subsystem_l13_controller", "workshop", 19200)
    .addEnergyPerTickInput(131072000)
    .addInputs([
        <liquid:crystalloid> * 12000,
        <contenttweaker:hypernet_cpu_t4> * 8,
        <contenttweaker:hypernet_ram_t4> * 16,
        <contenttweaker:industrial_circuit_v4> * 16,
        <mets:advanced_heat_vent> * 48,
        <mets:advanced_oc_heat_vent> * 96,
        <appliedenergistics2:paint_ball:30> * 24,
        <ic2:crafting:4> * 16,
        <appliedenergistics2:crafting_monitor> * 1,
        <ore:ingotInfinity> * 16,
    ])
    .addOutputs(<novaeng_core:extendable_calculator_subsystem_l13> * 1)
    .requireResearch("extendable_calculator_subsystem")
    .requireResearch("extendable_digital_storage_subsystem_l13")
    .build();

recipes.addShaped(<novaeng_core:ecalculator_tail_l13>, [
    [<gravisuite:crafting:2>, <contenttweaker:industrial_circuit_v5>, <gravisuite:crafting:2>],
    [<contenttweaker:electric_motor_v5>, <novaeng_core:ecalculator_tail_l9>, <contenttweaker:electric_motor_v5>],
    [<gravisuite:crafting:2>, <contenttweaker:industrial_circuit_v5>, <gravisuite:crafting:2>]
]);

recipes.addShaped(<novaeng_core:ecalculator_thread_core_hyper_l13>, [
    [<gravisuite:crafting:1>, <contenttweaker:field_generator_v5>, <gravisuite:crafting:1>],
    [<novaeng_core:ecalculator_thread_core_hyper_l9>, <gravisuite:crafting:2>, <novaeng_core:ecalculator_thread_core_hyper_l9>],
    [<gravisuite:crafting:1>, <contenttweaker:industrial_circuit_v5>, <gravisuite:crafting:1>]
]);

recipes.addShaped(<novaeng_core:ecalculator_thread_core_l13>, [
    [<gravisuite:crafting:1>, <contenttweaker:field_generator_v5>, <gravisuite:crafting:1>],
    [<novaeng_core:ecalculator_thread_core_l9>, <gravisuite:crafting:2>, <novaeng_core:ecalculator_thread_core_l9>],
    [<gravisuite:crafting:1>, <contenttweaker:industrial_circuit_v5>, <gravisuite:crafting:1>]
]);

recipes.addShaped(<novaeng_core:ecalculator_parallel_proc_l13> * 2, [
    [<contenttweaker:infinity_processor>, <contenttweaker:field_generator_v5>, <contenttweaker:infinity_processor>],
    [<novaeng_core:ecalculator_parallel_proc_l9>, <contenttweaker:industrial_circuit_v5>, <novaeng_core:ecalculator_parallel_proc_l9>],
    [<contenttweaker:infinity_processor>, <contenttweaker:field_generator_v5>, <contenttweaker:infinity_processor>]
]);
