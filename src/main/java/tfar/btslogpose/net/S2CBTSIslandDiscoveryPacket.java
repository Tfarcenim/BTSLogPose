package tfar.btslogpose.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tfar.btslogpose.client.BTSLogPoseClient;

import java.util.ArrayList;
import java.util.List;

// not threadsafe!
public class S2CBTSIslandDiscoveryPacket implements IMessage {

  private String region;
  private List<String> discoveries = new ArrayList<>();
  public S2CBTSIslandDiscoveryPacket() {
  }

  public S2CBTSIslandDiscoveryPacket(String region,List<String> discoveries) {
    this.region = region;
    this.discoveries = discoveries;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    region = ByteBufUtils.readUTF8String(buf);
    int size = buf.readInt();
    for( int i = 0; i< size;i++) {
      discoveries.add(ByteBufUtils.readUTF8String(buf));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf,region);
    buf.writeInt(discoveries.size());
    for (String s : discoveries) {
      ByteBufUtils.writeUTF8String(buf,s);
    }
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
      Minecraft.getMinecraft().addScheduledTask(() -> BTSLogPoseClient.setDiscoveries(message.region,message.discoveries));
    }
  }
}