package com.vcwdfca.ecofor0.network.packet;

import com.vcwdfca.ecofor0.ecofor0;
import com.vcwdfca.ecofor0.common.container.CatalystContainer;
import com.vcwdfca.ecofor0.common.container.FrequencyContainer;
import com.vcwdfca.ecofor0.common.container.PagedPatternContainer;
import com.vcwdfca.ecofor0.common.tile.TileSuperPatternAssembly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PatternGuiPacket implements IMessage {

    private int window;
    private int action;
    private int index;

    public PatternGuiPacket() {}

    public PatternGuiPacket(int window, int action, int index) { this.window = window; this.action = action; this.index = index; }

    public void fromBytes(ByteBuf buf) {
        window = buf.readInt();
        action = buf.readInt();
        index = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(window);
        buf.writeInt(action);
        buf.writeInt(index);
    }

    public static final class Handler implements IMessageHandler<PatternGuiPacket, IMessage> {

        public IMessage onMessage(PatternGuiPacket msg, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> handle(player, msg)); return null;
        }

        static void handle(EntityPlayerMP player, PatternGuiPacket msg) {
            if (player.openContainer.windowId != msg.window) {
                return;
            }

            if (player.openContainer instanceof PagedPatternContainer) {
                PagedPatternContainer container = (PagedPatternContainer) player.openContainer;
                TileSuperPatternAssembly assembly = container.getAssembly();
                if (!FrequencyContainer.allowed(assembly, player)) {
                    return;
                }
                if (msg.action == 0) {
                    container.page(msg.index);
                } else if (msg.action == 1 && msg.index >= 0 && msg.index < TileSuperPatternAssembly.PATTERN_COUNT && msg.index / 36 == container.page()) {
                    open(player, assembly, 100 + msg.index);
                }
            } else if (player.openContainer instanceof CatalystContainer) {
                CatalystContainer container = (CatalystContainer) player.openContainer;
                TileSuperPatternAssembly assembly = container.getAssembly();
                if (!container.canInteractWith(player)) {
                    return;
                }
                if (msg.action == 2) {
                    open(player, assembly, 2);
                    if (player.openContainer instanceof PagedPatternContainer) {
                        ((PagedPatternContainer) player.openContainer).page(container.getPattern() / 36);
                    }
                } else if (msg.action == 3 || msg.action == 4) {
                    container.transferFluid(player, msg.index, msg.action == 3);
                }
            }
        }

        static void open(EntityPlayerMP player, TileSuperPatternAssembly tile, int id) {
            player.openGui(ecofor0.INSTANCE, id, player.world, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
        }
    }
}
