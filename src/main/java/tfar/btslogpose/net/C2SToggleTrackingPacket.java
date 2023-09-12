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
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;

import java.util.Map;

// not threadsafe!
public class C2SToggleTrackingPacket implements IMessage {

  private String regionName;
  private String islandName;
  private boolean currentlyTracked;

  public C2SToggleTrackingPacket() {
  }

  public C2SToggleTrackingPacket(String regionName, String islandName, boolean currentlyTracked) {
    this.regionName = regionName;
    this.islandName = islandName;
    this.currentlyTracked = currentlyTracked;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    regionName = ByteBufUtils.readUTF8String(buf);
    islandName = ByteBufUtils.readUTF8String(buf);
    currentlyTracked = buf.readBoolean();
  }

  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeUTF8String(buf,regionName);
    ByteBufUtils.writeUTF8String(buf,islandName);
    buf.writeBoolean(currentlyTracked);
  }

  public static class Handler implements IMessageHandler<C2SToggleTrackingPacket, IMessage> {
    @Override
    public IMessage onMessage(C2SToggleTrackingPacket message, MessageContext ctx) {
      // Always use a construct like this to actually handle your message. This ensures that
      // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
      // is called on the networking thread so it is not safe to do a lot of things
      // here.
      FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
      return null;
    }

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    //need to make sure the server doesn't crash if the client sends bad packets
    private void handle(C2SToggleTrackingPacket message, MessageContext ctx) {
      // This code is run on the server side. So you can do server-side calculations here
      Map<String, BTSIslandConfig> stringBTSIslandConfigMap = BTSLogPose.configs.get(message.regionName);
      if (stringBTSIslandConfigMap != null) {
        BTSIslandConfig btsIslandConfig = stringBTSIslandConfigMap.get(message.islandName);
        if (btsIslandConfig != null) {
          EntityPlayerMP player = ctx.getServerHandler().player;
          String rawCommand = message.currentlyTracked ? btsIslandConfig.command_on_untrack : btsIslandConfig.command_on_track;
          rawCommand = rawCommand.replace("%player",player.getName());
          MinecraftServer server = player.server;
          server.getCommandManager().executeCommand(server,rawCommand);
        } else {
          LOGGER.warn("Island" + message.islandName +" not found in configs, ignoring");
        }
      } else {
        LOGGER.warn("Region " + message.regionName +" not found in configs, ignoring");
      }
    }
  }
}