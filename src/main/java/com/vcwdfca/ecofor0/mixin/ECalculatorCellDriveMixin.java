package com.vcwdfca.ecofor0.mixin;

import com.vcwdfca.ecofor0.calculator.CalculatorController;
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.prop.DriveStorageLevel;
import github.kasuminova.novaeng.common.block.ecotech.ecalculator.prop.Levels;
import github.kasuminova.novaeng.common.item.ecalculator.ECalculatorCell;
import github.kasuminova.novaeng.common.tile.ecotech.ecalculator.ECalculatorCellDrive;
import github.kasuminova.novaeng.common.tile.ecotech.ecalculator.ECalculatorController;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ECalculatorCellDrive.class, remap = false)
public abstract class ECalculatorCellDriveMixin {

    @Shadow
    protected EnumFacing connectedSide;

    @Inject(method = "getSuppliedBytes", at = @At("HEAD"), cancellable = true, remap = false)
    private void ecofor0$getL13SuppliedBytes(final CallbackInfoReturnable<Long> cir) {
        final ECalculatorCellDrive drive = (ECalculatorCellDrive) (Object) this;
        if (!ecofor0$isL13(drive)) {
            return;
        }

        final ItemStack stack = drive.getDriveInv().getStackInSlot(0);
        if (stack.isEmpty() || !(stack.getItem() instanceof ECalculatorCell)) {
            cir.setReturnValue(0L);
            return;
        }

        final ECalculatorCell cell = (ECalculatorCell) stack.getItem();
        cir.setReturnValue(cell.getTotalBytes());
    }

    @Inject(method = "connectTransmitter", at = @At("HEAD"), cancellable = true, remap = false)
    private void ecofor0$connectL13Transmitter(final EnumFacing side,
                                                 final Levels level,
                                                 final CallbackInfoReturnable<Boolean> cir) {
        final ECalculatorCellDrive drive = (ECalculatorCellDrive) (Object) this;
        if (!ecofor0$isL13(drive)) {
            return;
        }

        final ItemStack stack = drive.getDriveInv().getStackInSlot(0);
        if (stack.isEmpty() || !(stack.getItem() instanceof ECalculatorCell)) {
            cir.setReturnValue(false);
            return;
        }

        final ECalculatorCell cell = (ECalculatorCell) stack.getItem();
        if (!ecofor0$isSupportedCell(cell.getLevel())) {
            cir.setReturnValue(false);
            return;
        }

        if (this.connectedSide != side) {
            this.connectedSide = side;
            drive.markForUpdateSync();
        }
        cir.setReturnValue(true);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Unique
    private static boolean ecofor0$isL13(final ECalculatorCellDrive drive) {
        final ECalculatorController controller = drive.getController();
        return controller != null && controller.getParentController() == CalculatorController.L13;
    }

    @Unique
    private static boolean ecofor0$isSupportedCell(final DriveStorageLevel level) {
        return level == DriveStorageLevel.A
                || level == DriveStorageLevel.B
                || level == DriveStorageLevel.C;
    }
}
