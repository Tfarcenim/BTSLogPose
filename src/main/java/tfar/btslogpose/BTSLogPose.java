package tfar.btslogpose;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import tfar.btslogpose.command.BTSPingCommand;
import tfar.btslogpose.net.PacketHandler;
import tfar.btslogpose.world.BTSPingSavedData;

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
        evt.registerServerCommand(new BTSPingCommand());
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent e) {
        BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(e.player.world);
        btsPingSavedData.sendPings((EntityPlayerMP) e.player);
    }

    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent e) {
        BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(e.player.world);
        btsPingSavedData.clearPings((EntityPlayerMP) e.player);
    }
}
