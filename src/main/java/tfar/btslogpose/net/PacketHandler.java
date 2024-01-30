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
    int i = 0;
    // Register messages which are sent from the client to the server here:
    INSTANCE.registerMessage(S2CUntrackIslandPacket.Handler.class, S2CUntrackIslandPacket.class, i++, Side.CLIENT);
    INSTANCE.registerMessage(S2CBTSPingPacket.Handler.class, S2CBTSPingPacket.class, i++, Side.CLIENT);
    INSTANCE.registerMessage(S2COpenRegionScreenPacket.Handler.class, S2COpenRegionScreenPacket.class, i++, Side.CLIENT);
    INSTANCE.registerMessage(S2CBTSIslandDiscoveryPacket.Handler.class, S2CBTSIslandDiscoveryPacket.class, i++, Side.CLIENT);
    INSTANCE.registerMessage(S2CBTSIslandConfigPacket.Handler.class, S2CBTSIslandConfigPacket.class, i++, Side.CLIENT);
    INSTANCE.registerMessage(S2CBTSIslandClearConfigPacket.Handler.class, S2CBTSIslandClearConfigPacket.class, i++, Side.CLIENT);
    INSTANCE.registerMessage(C2SToggleTrackingPacket.Handler.class, C2SToggleTrackingPacket.class, i++, Side.SERVER);
    INSTANCE.registerMessage(C2SUntrackPingPacket.Handler.class, C2SUntrackPingPacket.class, i++, Side.SERVER);
  }

  public static void sendPacketToAllClientsInDimension(IMessage pkt, int dim) {
    INSTANCE.sendToDimension(pkt,dim);
  }

  public static void sendPacketToClient(IMessage pkt, EntityPlayerMP player) {
    INSTANCE.sendTo(pkt,player);
  }

  public static void sendPacketToServer(IMessage pkt) {
    INSTANCE.sendToServer(pkt);
  }
}