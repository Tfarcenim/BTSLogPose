package tfar.btslogpose;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import tfar.btslogpose.command.BTSLogPoseCommand;
import tfar.btslogpose.net.PacketHandler;

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
}
