// L13 delta fragment. Requires the matching instance script; do not use standalone.
RecipeBuilder.newBuilder("extendable_fabricator_subsystem_l13_controller", "workshop", 19200)
    .addEnergyPerTickInput(131072000)
    .addInputs([
        <liquid:crystalloid> * 12000,
        <contenttweaker:hypernet_cpu_t4> * 6,
        <contenttweaker:hypernet_ram_t4> * 12,
        <contenttweaker:industrial_circuit_v4> * 40,
        <mets:advanced_heat_vent> * 36,
        <mets:advanced_oc_heat_vent> * 72,
        <appliedenergistics2:paint_ball:30> * 16,
        <ore:plateCarbon> * 32,
        <threng:big_assembler:2> * 1,
        <ore:ingotInfinity> * 16,
    ])
    .addOutputs(<novaeng_core:extendable_fabricator_subsystem_l13> * 1)
    .requireResearch("extendable_fabricator_subsystem")
    .requireResearch("extendable_digital_storage_subsystem_l13")
    .build();

recipes.addShaped(<novaeng_core:efabricator_parallel_proc_l13> * 2, [
    [<contenttweaker:infinity_processor>, <contenttweaker:field_generator_v5>, <contenttweaker:infinity_processor>],
    [<novaeng_core:efabricator_parallel_proc_l9>, <contenttweaker:industrial_circuit_v5>, <novaeng_core:efabricator_parallel_proc_l9>],
    [<contenttweaker:infinity_processor>, <contenttweaker:field_generator_v5>, <contenttweaker:infinity_processor>]
]);

recipes.addShaped(<novaeng_core:efabricator_tail_l13>, [
    [<gravisuite:crafting:2>, <contenttweaker:industrial_circuit_v5>, <gravisuite:crafting:2>],
    [<contenttweaker:electric_motor_v5>, <novaeng_core:efabricator_casing>, <contenttweaker:electric_motor_v5>],
    [<gravisuite:crafting:2>, <contenttweaker:industrial_circuit_v5>, <gravisuite:crafting:2>]
]);
