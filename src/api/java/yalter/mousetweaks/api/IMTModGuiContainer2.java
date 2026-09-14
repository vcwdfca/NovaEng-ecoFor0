package yalter.mousetweaks.api;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

interface IMTModGuiContainer2 {
    boolean MT_isMouseTweaksDisabled();

    boolean MT_isWheelTweakDisabled();

    Container MT_getContainer();

    Slot MT_getSlotUnderMouse();

    boolean MT_isCraftingOutput(Slot var1);

    boolean MT_isIgnored(Slot var1);

    boolean MT_disableRMBDraggingFunctionality();
}
