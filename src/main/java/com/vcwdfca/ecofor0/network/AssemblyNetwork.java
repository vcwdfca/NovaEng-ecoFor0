package com.vcwdfca.ecofor0.network;

import com.vcwdfca.ecofor0.ecofor0;
import com.vcwdfca.ecofor0.network.packet.PatternGuiPacket;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class AssemblyNetwork {

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel("spa_assemblies");

    public static void init() {
        CHANNEL.registerMessage(SnapshotHandler.class, Snapshot.class, 0, Side.CLIENT);
        CHANNEL.registerMessage(PatternGuiPacket.Handler.class, PatternGuiPacket.class, 4, Side.SERVER);
    }

    public static class Snapshot implements IMessage {

        int window;
        NBTTagCompound data;

        public Snapshot() {}

        public Snapshot(int window, NBTTagCompound data) {
            this.window = window; this.data = data;
        }

        public void fromBytes(ByteBuf buf) {
            window = buf.readInt();
            data = ByteBufUtils.readTag(buf);
        }

        public void toBytes(ByteBuf buf) {
            buf.writeInt(window);
            ByteBufUtils.writeTag(buf, data);
        }
    }

    public static class SnapshotHandler implements IMessageHandler<Snapshot, IMessage> {

        @Override
        public IMessage onMessage(Snapshot message, MessageContext ctx) {
            ecofor0.UI.receive(message.window, message.data);
            return null;
        }
    }

}
