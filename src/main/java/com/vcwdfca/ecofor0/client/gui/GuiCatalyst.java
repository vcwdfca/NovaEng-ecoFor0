package com.vcwdfca.ecofor0.client.gui;

import com.vcwdfca.ecofor0.network.AssemblyNetwork;
import com.vcwdfca.ecofor0.network.packet.PatternGuiPacket;
import com.vcwdfca.ecofor0.component.AssemblyResource;
import com.vcwdfca.ecofor0.common.container.CatalystContainer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class GuiCatalyst extends GuiContainer {

    private final CatalystContainer container;

    public GuiCatalyst(CatalystContainer container) {
        super(container);
        this.container = container;
        xSize = 194;
        ySize = 196;
    }

    private int tankAt(int x, int y) {
        x -= guiLeft + 98; y -= guiTop + 30;
        return x >= 0 && y >= 0 && x < 54 && y < 54 && x % 18 < 16 && y % 18 < 16 ? y / 18 * 3 + x / 18 : -1;
    }

    @Override
    public void drawScreen(int x, int y, float partial) {
        drawDefaultBackground();
        super.drawScreen(x, y, partial);
        renderHoveredToolTip(x, y);
        int tank = tankAt(x, y);
        if (tank >= 0) {
            FluidStack fluid = container.getBank().fluids()[tank].getFluid();
            List<String> lines = new ArrayList<>();
            if (fluid != null) {
                lines.add(fluid.getLocalizedName());
                lines.add(String.format(Locale.ROOT, "%,d / 16,000 mB", fluid.amount));
            }
            lines.add(I18n.format("superpatternassembly.catalyst.fluid_hint"));
            drawHoveringText(lines, x, y);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partial, int x, int y) {
        drawRect(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, 0xff555555);
        drawRect(guiLeft + 2, guiTop + 2, guiLeft + xSize - 2, guiTop + ySize - 2, 0xffc6c6c6);
        for (Slot slot : inventorySlots.inventorySlots) slot(guiLeft + slot.xPos, guiTop + slot.yPos);
        for (int i = 0; i < 9; i++) {
            int sx = guiLeft + 98 + i % 3 * 18, sy = guiTop + 30 + i / 3 * 18; slot(sx, sy);
            FluidStack fluid = container.getBank().fluids()[i].getFluid();
            if (fluid == null) {
                continue;
            }
            RenderHelper.enableGUIStandardItemLighting(); itemRender.renderItemAndEffectIntoGUI(AssemblyResource.of(fluid).icon(), sx, sy); RenderHelper.disableStandardItemLighting();
        }
    }

    private void slot(int x, int y) {
        drawRect(x - 1, y - 1, x + 17, y + 17, 0xff373737);
        drawRect(x, y, x + 17, y + 17, 0xffffffff);
        drawRect(x, y, x + 16, y + 16, 0xff8b8b8b);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        fontRenderer.drawString(I18n.format("superpatternassembly.catalyst.title", container.getPattern() + 1), 8, 7, 0x404040);
        fontRenderer.drawString(I18n.format("superpatternassembly.catalyst.items"), 8, 19, 0x404040);
        fontRenderer.drawString(I18n.format("superpatternassembly.catalyst.fluids"), 98, 19, 0x404040);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, 101, 0x404040);
    }

    @Override
    protected void actionPerformed(@NotNull GuiButton button) {
        AssemblyNetwork.CHANNEL.sendToServer(new PatternGuiPacket(container.windowId, 2, 0));
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        int tank = tankAt(x, y);
        if (tank >= 0 && (button == 0 || button == 1)) {
            AssemblyNetwork.CHANNEL.sendToServer(new PatternGuiPacket(container.windowId, button == 0 ? 3 : 4, tank));
            return;
        }
        super.mouseClicked(x, y, button);
    }
}
