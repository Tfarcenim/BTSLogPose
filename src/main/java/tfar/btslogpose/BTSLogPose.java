package tfar.btslogpose;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import tfar.btslogpose.command.OpenRegionScreenCommand;
import tfar.btslogpose.command.islands.BTSIslandCommand;
import tfar.btslogpose.command.pings.BTSPingCommand;
import tfar.btslogpose.config.BTSIslandConfig;
import tfar.btslogpose.net.PacketHandler;
import tfar.btslogpose.world.BTSIslandManager;
import tfar.btslogpose.world.BTSPingSavedData;

import java.util.List;
import java.util.Map;

@Mod(modid = BTSLogPose.MOD_ID)
@Mod.EventBusSubscriber
public class BTSLogPose {
    public static final String MOD_ID = "btslogpose";

    public static Map<String,Map<String,BTSIslandConfig>> configs;

    public static final List<String> REGIONS = Lists.newArrayList("east_blue","grand_line","new_world");


    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        PacketHandler.registerMessages();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent evt) {
        evt.registerServerCommand(new BTSPingCommand());
        evt.registerServerCommand(new BTSIslandCommand());
        evt.registerServerCommand(new OpenRegionScreenCommand());
        configs = BTSIslandConfig.read();
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent e) {
        BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(e.player.world);
        btsPingSavedData.sendPings((EntityPlayerMP) e.player);
    }

    @Mod.EventHandler
    public void shutdown(FMLServerStoppingEvent e) {
        BTSIslandManager.btsDiscoveryData = null;
    }

    @SubscribeEvent
    public static void logout(PlayerEvent.PlayerLoggedOutEvent e) {
        BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(e.player.world);
        btsPingSavedData.clearPings((EntityPlayerMP) e.player);
    }
}
