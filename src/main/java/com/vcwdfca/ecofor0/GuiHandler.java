package com.vcwdfca.ecofor0;

import com.vcwdfca.ecofor0.client.gui.GuiCatalyst;
import com.vcwdfca.ecofor0.client.gui.GuiSuperPattern;
import com.vcwdfca.ecofor0.common.container.CatalystContainer;
import com.vcwdfca.ecofor0.common.container.FrequencyContainer;
import com.vcwdfca.ecofor0.common.container.PagedPatternContainer;
import com.vcwdfca.ecofor0.common.tile.TileSuperPatternAssembly;
import github.kasuminova.mmce.common.tile.base.MEMachineComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class GuiHandler implements IGuiHandler {

    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof TileSuperPatternAssembly && FrequencyContainer.allowed((MEMachineComponent) tile, player)) {
            if (id == 2) return new PagedPatternContainer((TileSuperPatternAssembly) tile, player);
            if (id >= 100 && id < 100 + TileSuperPatternAssembly.PATTERN_COUNT)
                return new CatalystContainer((TileSuperPatternAssembly) tile, id - 100, player);
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
        if (tile instanceof TileSuperPatternAssembly) {
            if (id == 2) return new GuiSuperPattern((TileSuperPatternAssembly) tile, player);
            if (id >= 100 && id < 100 + TileSuperPatternAssembly.PATTERN_COUNT)
                return new GuiCatalyst(new CatalystContainer((TileSuperPatternAssembly) tile, id - 100, player));
        }

        return null;
    }
}
