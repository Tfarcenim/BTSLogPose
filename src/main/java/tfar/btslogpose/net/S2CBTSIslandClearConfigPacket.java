package tfar.btslogpose.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.client.BTSLogPoseClient;
import tfar.btslogpose.config.BTSIslandConfig;

import java.util.HashMap;
import java.util.Map;

// not threadsafe!
public class S2CBTSIslandClearConfigPacket implements IMessage {

  public S2CBTSIslandClearConfigPacket() {
  }


  @Override
  public void fromBytes(ByteBuf buf) {
  }

  @Override
  public void toBytes(ByteBuf buf) {
  }

  public static class Handler implements IMessageHandler<S2CBTSIslandClearConfigPacket, IMessage> {
    @Override
    public IMessage onMessage(S2CBTSIslandClearConfigPacket message, MessageContext ctx) {
      // Always use a construct like this to actually handle your message. This ensures that
      // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
      // is called on the networking thread so it is not safe to do a lot of things
      // here.
      FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
      return null;
    }

    private void handle(S2CBTSIslandClearConfigPacket message, MessageContext ctx) {
      // This code is run on the server side. So you can do server-side calculations here
      Minecraft.getMinecraft().addScheduledTask(() -> BTSLogPoseClient.client_configs.clear());
    }
  }
}