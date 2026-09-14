package com.vcwdfca.ecofor0.client.gui;

import com.vcwdfca.ecofor0.network.AssemblyNetwork;
import com.vcwdfca.ecofor0.network.packet.PatternGuiPacket;
import com.vcwdfca.ecofor0.common.container.PagedPatternContainer;
import com.vcwdfca.ecofor0.common.tile.TileSuperPatternAssembly;
import github.kasuminova.mmce.client.gui.GuiMEPatternProvider;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import java.io.IOException;

/** Preserve the MMCE provider UI, including its cache list, mode controls and shared auxiliary slots. */
public final class GuiSuperPattern extends GuiMEPatternProvider {

    private final PagedPatternContainer paged;

    public GuiSuperPattern(TileSuperPatternAssembly tile, EntityPlayer player) {
        super(tile, player);
        paged = new PagedPatternContainer(tile, player);
        inventorySlots = paged;
    }

    @Override public void initGui() {
        super.initGui();
        // GTLAdd's PaginationUIManager puts <<, page / total, >> below the pattern grid.
        // Keep the inventory label at x=7 and the original Nova controls outside the left edge.
        buttonList.add(new GuiButton(810, guiLeft + 64, guiTop + 100, 30, 12, "<<"));
        buttonList.add(new GuiButton(811, guiLeft + 140, guiTop + 100, 30, 12, ">>"));
    }

    @Override public void drawScreen(int x, int y, float partial) {
        for (GuiButton button : buttonList) {
            if (button.id == 810) button.enabled = paged.page() > 0;
            else if (button.id == 811) button.enabled = paged.page() < TileSuperPatternAssembly.PATTERN_PAGES - 1;
        }
        super.drawScreen(x, y, partial);
    }

    @Override public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {
        super.drawFG(offsetX, offsetY, mouseX, mouseY);
        String page = (paged.page() + 1) + " / " + TileSuperPatternAssembly.PATTERN_PAGES;
        fontRenderer.drawString(page, 117 - fontRenderer.getStringWidth(page) / 2, 102, 0x404040);
    }

    @Override protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 810 || button.id == 811) {
            int next = paged.page() + (button.id == 810 ? -1 : 1);
            if (next >= 0 && next < TileSuperPatternAssembly.PATTERN_PAGES)
                AssemblyNetwork.CHANNEL.sendToServer(new PatternGuiPacket(paged.windowId, 0, next));
        }
        else super.actionPerformed(button);
    }

    @Override protected void mouseClicked(int x, int y, int button) throws IOException {
        if (button == 2) {
            for (Slot slot : paged.inventorySlots) if (slot instanceof PagedPatternContainer.PatternSlot && slot.isEnabled()
                    && x >= guiLeft + slot.xPos && x < guiLeft + slot.xPos + 16 && y >= guiTop + slot.yPos && y < guiTop + slot.yPos + 16) {
                AssemblyNetwork.CHANNEL.sendToServer(new PatternGuiPacket(paged.windowId, 1, ((PagedPatternContainer.PatternSlot) slot).getIndex())); return;
            }
        }
        super.mouseClicked(x, y, button);
    }
}
