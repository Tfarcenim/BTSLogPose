package tfar.btslogpose.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tfar.btslogpose.client.BTSLogPoseClient;
import tfar.btslogpose.world.BTSPing;

import java.util.ArrayList;
import java.util.List;

// not threadsafe!
public class S2CUntrackIslandPacket implements IMessage {

  private String island;
  public S2CUntrackIslandPacket() {
  }

  public S2CUntrackIslandPacket(String island) {
    this.island = island;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
      island = ByteBufUtils.readUTF8String(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf,island);
    }

  public static class Handler implements IMessageHandler<S2CUntrackIslandPacket, IMessage> {
    @Override
    public IMessage onMessage(S2CUntrackIslandPacket message, MessageContext ctx) {
      // Always use a construct like this to actually handle your message. This ensures that
      // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
      // is called on the networking thread so it is not safe to do a lot of things
      // here.
      FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
      return null;
    }

    private void handle(S2CUntrackIslandPacket message, MessageContext ctx) {
      // This code is run on the server side. So you can do server-side calculations here
      Minecraft.getMinecraft().addScheduledTask(() -> BTSLogPoseClient.untrackIsland(message.island));
    }
  }
}