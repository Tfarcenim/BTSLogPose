package tfar.btslogpose.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;

import java.util.HashMap;
import java.util.Map;

// not threadsafe!
public class S2CBTSIslandConfigPacket implements IMessage {

  private Map<String, BTSIslandConfig> pings = new HashMap<>();
  public S2CBTSIslandConfigPacket() {
  }

  public S2CBTSIslandConfigPacket(Map<String, BTSIslandConfig> pings) {
    this.pings = pings;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    int size = buf.readInt();
    for( int i = 0; i< size;i++) {
      String name = ByteBufUtils.readUTF8String(buf);
      pings.put(name,BTSIslandConfig.fromNetwork(buf));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(pings.size());
    for (Map.Entry<String, BTSIslandConfig> entry : pings.entrySet()) {
      String name = entry.getKey();
      ByteBufUtils.writeUTF8String(buf,name);
      entry.getValue().toNetwork(buf);
    }
  }

  public static class Handler implements IMessageHandler<S2CBTSIslandConfigPacket, IMessage> {
    @Override
    public IMessage onMessage(S2CBTSIslandConfigPacket message, MessageContext ctx) {
      // Always use a construct like this to actually handle your message. This ensures that
      // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
      // is called on the networking thread so it is not safe to do a lot of things
      // here.
      FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
      return null;
    }

    private void handle(S2CBTSIslandConfigPacket message, MessageContext ctx) {
      // This code is run on the server side. So you can do server-side calculations here
      Minecraft.getMinecraft().addScheduledTask(() -> BTSLogPose.configs = message.pings);
    }
  }
}