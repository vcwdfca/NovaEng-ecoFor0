package com.vcwdfca.ecofor0.client;

import com.vcwdfca.ecofor0.common.container.CatalystContainer;
import com.vcwdfca.ecofor0.common.container.FrequencyContainer;
import com.vcwdfca.ecofor0.common.container.PagedPatternContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class ClientProxy {

    public void receive(int window, NBTTagCompound data) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.addScheduledTask(() -> {
            if (mc.player != null && mc.player.openContainer instanceof PagedPatternContainer && mc.player.openContainer.windowId == window && data != null && data.hasKey("spaPatternPage"))
                ((PagedPatternContainer) mc.player.openContainer).page(data.getInteger("spaPatternPage"));
            if (mc.player != null && mc.player.openContainer instanceof CatalystContainer && mc.player.openContainer.windowId == window && data != null)
                ((CatalystContainer) mc.player.openContainer).getBank().load(data);
            if (mc.player != null && mc.player.openContainer instanceof FrequencyContainer && mc.player.openContainer.windowId == window && data != null)
                ((FrequencyContainer) mc.player.openContainer).snapshot = data;
        });
    }
}
