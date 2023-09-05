package tfar.btslogpose.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tfar.btslogpose.world.BTSPing;

import java.util.ArrayList;
import java.util.List;

// not threadsafe!
public class S2CLadderPacket implements IMessage {

  private List<BTSPing> pillars = new ArrayList<>();
  public S2CLadderPacket() {
  }

  public S2CLadderPacket(List<BTSPing> pillars) {
    this.pillars = pillars;
  }

  @Override
  public void fromBytes(ByteBuf buf) {
    int size = buf.readInt();
    for( int i = 0; i< size;i++) {
      pillars.add(BTSPing.of(new BlockPos(buf.readInt(), buf.readInt(),buf.readInt()),buf.readInt()));
    }
  }

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeInt(pillars.size());
    for (BTSPing p : pillars) {
      BlockPos base = p.getPos();
      buf.writeInt(base.getX());
      buf.writeInt(base.getY());
      buf.writeInt(base.getZ());
      buf.writeInt(p.getColor());
    }
  }

  public static class Handler implements IMessageHandler<S2CLadderPacket, IMessage> {
    @Override
    public IMessage onMessage(S2CLadderPacket message, MessageContext ctx) {
      // Always use a construct like this to actually handle your message. This ensures that
      // youre 'handle' code is run on the main Minecraft thread. 'onMessage' itself
      // is called on the networking thread so it is not safe to do a lot of things
      // here.
      FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
      return null;
    }

    private void handle(S2CLadderPacket message, MessageContext ctx) {
      // This code is run on the server side. So you can do server-side calculations here
      //Minecraft.getMinecraft().addScheduledTask(() -> RendererCube.draw(message.pillars));
    }
  }
}