// L13 delta fragment. Requires the matching instance script; do not use standalone.
# L13 ECO 子系统
RegistryHyperNet.addResearchCognitionData(ResearchCognitionData.create("extendable_digital_storage_subsystem_l13", "§cECO - L13 可扩展子系统",
    <novaeng_core:extendable_digital_storage_subsystem_l13>,    // 预览物品
    12.0,                 // 科技等级（难度）
    baseComputationPoint * (baseTFloPsPerTick + (6.0D * 10000)) * 2.0D,          // 需要科研点
    baseTFloPsPerTick + (6.0D * 10000),                   // 最低每 Tick 算力要求
    [
        "L9 级 ECO 子系统已达到结构与吞吐极限,需要以更高阶的 HyperNet 核心重新构建。",
        "L13 方案把存储、计算和合成主机统一到同一套高能粒子接口。",
    ],
    [
        "§2解锁集成式处理车间配方：§cECO - L13 可扩展存储子系统主机。",
        "§2解锁集成式处理车间配方：§cECO - L13 可扩展计算子系统主机。",
        "§2解锁集成式处理车间配方：§cECO - L13 可扩展合成子系统主机。",
    ],
    [
        "extendable_digital_storage_subsystem_l9",
        "assembly_line_plus",
    ]
));
