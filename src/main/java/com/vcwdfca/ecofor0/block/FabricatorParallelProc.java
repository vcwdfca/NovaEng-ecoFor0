package com.vcwdfca.ecofor0.block;

import github.kasuminova.novaeng.common.block.ecotech.efabricator.BlockEFabricatorParallelProc;
import github.kasuminova.novaeng.common.tile.ecotech.efabricator.EFabricatorParallelProc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FabricatorParallelProc extends BlockEFabricatorParallelProc {
    /**
     * L13 并行处理器的 Modifier 配置。
     * 沿 Core 梯度（L4→L6→L9 约 ×3）推算：
     * - 普通 Modifier：ADD 768（L9 的 256 × 3）
     * - 超频 Modifier：ADD 1152（L9 的 384 × 3），能耗系数 0.96（L9 的 0.97 - 0.01）
     */
    public static final BlockEFabricatorParallelProc L13 = new FabricatorParallelProc("l13",
            Collections.singletonList(new EFabricatorParallelProc.Modifier(EFabricatorParallelProc.Type.ADD, 768.0, false)),
            Arrays.asList(
                    new EFabricatorParallelProc.Modifier(EFabricatorParallelProc.Type.ADD, 1152.0, false),
                    new EFabricatorParallelProc.Modifier(EFabricatorParallelProc.Type.MULTIPLY, 0.96, true)
            )
    );

    protected FabricatorParallelProc(String level,
                                      List<EFabricatorParallelProc.Modifier> modifiers,
                                      List<EFabricatorParallelProc.Modifier> overclockModifiers) {
        super(level, modifiers, overclockModifiers);
    }
}
