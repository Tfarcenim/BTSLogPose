package tfar.btslogpose.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;
import tfar.btslogpose.world.BTSPing;
import tfar.btslogpose.world.BTSPingSavedData;

import java.util.Map;

// not threadsafe!
public class C2SUntrackPingPacket implements IMessage {

  private String islandName;

  public C2SUntrackPingPacket() {
  }

  public C2SUntrackPingPacket(String islandName) {
    this.islandName = islandName;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    islandName = ByteBufUtils.readUTF8String(buf);
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf,islandName);
  }

  public static class Handler implements IMessageHandler<C2SUntrackPingPacket, IMessage> {
    @Override
    public IMessage onMessage(C2SUntrackPingPacket message, MessageContext ctx) {
      // Always use a construct like this to actually handle your message. This ensures that
      // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
      // is called on the networking thread so it is not safe to do a lot of things
      // here.
      FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
      return null;
    }

    private static final Logger LOGGER = LogManager.getLogger();

    //need to make sure the server doesn't crash if the client sends bad packets
    private void handle(C2SUntrackPingPacket message, MessageContext ctx) {
      // This code is run on the server side. So you can do server-side calculations here
      EntityPlayerMP player = ctx.getServerHandler().player;
      BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(player.getServerWorld());
      BTSPing btsPing = btsPingSavedData.lookupByName(message.islandName);
      if (btsPing != null) {
        btsPingSavedData.untrack(btsPing,player);
      } else {
        LOGGER.warn("Could not find ping " + message.islandName +", ignoring");
      }
    }
  }
}