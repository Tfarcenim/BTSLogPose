package tfar.btslogpose.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tfar.btslogpose.client.BTSLogPoseClient;

import java.util.ArrayList;
import java.util.List;

// not threadsafe!
public class S2CBTSIslandDiscoveryPacket implements IMessage {

  private List<String> discoveries = new ArrayList<>();
  public S2CBTSIslandDiscoveryPacket() {
  }

  public S2CBTSIslandDiscoveryPacket(List<String> discoveries) {
    this.discoveries = discoveries;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    int size = buf.readInt();
    for( int i = 0; i< size;i++) {
    //  pings.add(BTSPing.fromNetwork(buf));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(discoveries.size());
   // for (BTSPing p : pings) {
    //  p.toNetwork(buf);
   // }
  }

  public static class Handler implements IMessageHandler<S2CBTSIslandDiscoveryPacket, IMessage> {
    @Override
    public IMessage onMessage(S2CBTSIslandDiscoveryPacket message, MessageContext ctx) {
      // Always use a construct like this to actually handle your message. This ensures that
      // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
      // is called on the networking thread so it is not safe to do a lot of things
      // here.
      FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
      return null;
    }

    private void handle(S2CBTSIslandDiscoveryPacket message, MessageContext ctx) {
      // This code is run on the server side. So you can do server-side calculations here
      Minecraft.getMinecraft().addScheduledTask(() -> BTSLogPoseClient.setDiscoveries(message.discoveries));
    }
  }
}