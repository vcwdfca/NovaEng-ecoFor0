package com.vcwdfca.ecofor0.mixin;

import com.vcwdfca.ecofor0.block.storage.StorageController;
import github.kasuminova.novaeng.common.block.ecotech.estorage.BlockEStorageController;
import github.kasuminova.novaeng.common.block.ecotech.estorage.prop.DriveStorageLevel;
import github.kasuminova.novaeng.common.tile.ecotech.estorage.EStorageCellDrive;
import github.kasuminova.novaeng.common.tile.ecotech.estorage.EStorageController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EStorageCellDrive.class, remap = false)
public abstract class EStorageCellDriveMixin {

    @Inject(method = "isCellSupported", at = @At("HEAD"), cancellable = true, remap = false)
    private void ecofor0$allowL13Cells(final DriveStorageLevel level,
                                       final CallbackInfoReturnable<Boolean> cir) {
        final EStorageController controller = ((EStorageCellDrive) (Object) this).getController();
        if (controller == null) {
            return;
        }

        final BlockEStorageController parent = controller.getParentController();
        if (parent == StorageController.L13) {
            cir.setReturnValue(level == DriveStorageLevel.A
                    || level == DriveStorageLevel.B
                    || level == DriveStorageLevel.C);
        }
    }
}
