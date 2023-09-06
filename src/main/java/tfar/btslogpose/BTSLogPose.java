package tfar.btslogpose;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import tfar.btslogpose.command.BTSLogPoseCommand;
import tfar.btslogpose.net.PacketHandler;
import tfar.btslogpose.net.S2CBTSPingPacket;
import tfar.btslogpose.world.BTSPingSavedData;

import java.util.ArrayList;

@Mod(modid = BTSLogPose.MOD_ID)
@Mod.EventBusSubscriber
public class BTSLogPose {
    public static final String MOD_ID = "btslogpose";

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        PacketHandler.registerMessages();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new BTSLogPoseCommand());
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent e) {
        BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(e.player.world);
        PacketHandler.sendPacketToClient(new S2CBTSPingPacket(btsPingSavedData.getPings()),(EntityPlayerMP) e.player);
    }

    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent e) {
        PacketHandler.sendPacketToClient(new S2CBTSPingPacket(new ArrayList<>()),(EntityPlayerMP) e.player);
    }
}
