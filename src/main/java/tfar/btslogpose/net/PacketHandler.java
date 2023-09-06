package tfar.btslogpose.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import tfar.btslogpose.BTSLogPose;

public class PacketHandler {

  public static SimpleNetworkWrapper INSTANCE = null;

  public PacketHandler() {
  }
  public static void registerMessages() {
    INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BTSLogPose.MOD_ID);
    // Register messages which are sent from the client to the server here:
    INSTANCE.registerMessage(S2CBTSPingPacket.Handler.class, S2CBTSPingPacket.class, 0, Side.CLIENT);
  }

  public static void sendPacketToAllClientsInDimension(IMessage pkt, int dim) {
    INSTANCE.sendToDimension(pkt,dim);
  }

  public static void sendPacketToClient(IMessage pkt, EntityPlayerMP player) {
    INSTANCE.sendTo(pkt,player);
  }
}