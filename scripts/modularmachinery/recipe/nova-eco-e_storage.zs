// L13 delta fragment. Requires the matching instance script; do not use standalone.
RecipeBuilder.newBuilder("extendable_digital_storage_subsystem_l13_controller", "workshop", 19200)
    .addEnergyPerTickInput(131072000)
    .addInputs([
        <liquid:crystalloid> * 12000,
        <contenttweaker:hypernet_cpu_t4> * 4,
        <contenttweaker:hypernet_ram_t4> * 8,
        <contenttweaker:industrial_circuit_v4> * 32,
        <mets:advanced_heat_vent> * 24,
        <mets:advanced_oc_heat_vent> * 48,
        <appliedenergistics2:paint_ball:30> * 16,
        <ore:plateDenseSteel> * 32,
        <appliedenergistics2:controller> * 1,
        <ore:ingotInfinity> * 16,
    ])
    .addOutputs(<novaeng_core:extendable_digital_storage_subsystem_l13> * 1)
    .requireResearch("extendable_digital_storage_subsystem_l13")
    .build();

recipes.addShaped(<novaeng_core:estorage_energy_cell_l13>, [
    [<novaeng_core:estorage_energy_cell_l9>, <contenttweaker:field_generator_v5>, <novaeng_core:estorage_energy_cell_l9>],
    [<novaeng_core:estorage_energy_cell_l9>, <contenttweaker:industrial_circuit_v5>, <novaeng_core:estorage_energy_cell_l9>],
    [<novaeng_core:estorage_energy_cell_l9>, <contenttweaker:field_generator_v5>, <novaeng_core:estorage_energy_cell_l9>]
]);
