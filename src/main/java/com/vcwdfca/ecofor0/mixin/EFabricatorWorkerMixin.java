package com.vcwdfca.ecofor0.mixin;

import com.vcwdfca.ecofor0.block.FabricatorController;
import github.kasuminova.novaeng.common.block.ecotech.efabricator.prop.Levels;
import github.kasuminova.novaeng.common.tile.ecotech.efabricator.EFabricatorController;
import github.kasuminova.novaeng.common.tile.ecotech.efabricator.EFabricatorWorker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EFabricatorWorker.class, remap = false)
public abstract class EFabricatorWorkerMixin {

    @Redirect(
            method = "getQueueDepth",
            at = @At(
                    value = "INVOKE",
                    target = "Lgithub/kasuminova/novaeng/common/tile/ecotech/efabricator/EFabricatorController;getLevel()Lgithub/kasuminova/novaeng/common/block/ecotech/efabricator/prop/Levels;"
            ),
            require = 1,
            remap = false
    )
    private Levels ecofor0$omitL13QueueDepthLevel(final EFabricatorController controller) {
        if (ecofor0$isL13(controller)) {
            return null;
        }
        return controller.getLevel();
    }

    @Redirect(
            method = "getQueueDepth",
            at = @At(
                    value = "INVOKE",
                    target = "Lgithub/kasuminova/novaeng/common/block/ecotech/efabricator/prop/Levels;applyOverclockQueueDepth(I)I"
            ),
            require = 1,
            remap = false
    )
    private int ecofor0$applyL13QueueDepth(final Levels level, final int value) {
        if (ecofor0$isL13()) {
            return value * FabricatorController.L13_QUEUE_DEPTH_MULTIPLIER;
        }
        return level.applyOverclockQueueDepth(value);
    }

    @Redirect(
            method = "doWork",
            at = @At(
                    value = "INVOKE",
                    target = "Lgithub/kasuminova/novaeng/common/tile/ecotech/efabricator/EFabricatorController;getLevel()Lgithub/kasuminova/novaeng/common/block/ecotech/efabricator/prop/Levels;"
            ),
            require = 1,
            remap = false
    )
    private Levels ecofor0$omitL13EnergyUsageLevel(final EFabricatorController controller) {
        if (ecofor0$isL13(controller)) {
            return null;
        }
        return controller.getLevel();
    }

    @Redirect(
            method = "doWork",
            at = @At(
                    value = "INVOKE",
                    target = "Lgithub/kasuminova/novaeng/common/block/ecotech/efabricator/prop/Levels;applyOverclockEnergyUsage(I)I"
            ),
            require = 1,
            remap = false
    )
    private int ecofor0$applyL13EnergyUsage(final Levels level, final int value) {
        if (ecofor0$isL13()) {
            return value * FabricatorController.L13_ENERGY_USAGE_MULTIPLIER;
        }
        return level.applyOverclockEnergyUsage(value);
    }

    @Unique
    private boolean ecofor0$isL13() {
        final EFabricatorWorker worker = (EFabricatorWorker) (Object) this;
        final EFabricatorController controller = worker.getController();
        return controller != null && ecofor0$isL13(controller);
    }

    @Unique
    private static boolean ecofor0$isL13(final EFabricatorController controller) {
        return controller.getParentController() == FabricatorController.L13;
    }
}
