package com.vcwdfca.ecofor0.fabricator;

import github.kasuminova.novaeng.common.block.ecotech.efabricator.BlockEFabricatorParallelProc;
import github.kasuminova.novaeng.common.tile.ecotech.efabricator.EFabricatorParallelProc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FabricatorParallelProc extends BlockEFabricatorParallelProc {
    /**
     * L13 parallel-processor modifier configuration, extrapolated from Core's
     * approximately threefold L4-to-L6-to-L9 progression:
     * <ul>
     * <li>Normal modifier: ADD 768 (L9's 256 * 3).</li>
     * <li>Overclock modifier: ADD 1152 (L9's 384 * 3) and 0.96 energy
     * multiplier (L9's 0.97 - 0.01).</li>
     * </ul>
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
