package tfar.btslogpose.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class BTSIslandManager {

    public static Map<BTSIslandConfig,List<UUID>> discovered = new HashMap<>();

    @SubscribeEvent
    public static void playerTick(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            WorldServer serverWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
            for (BTSIslandConfig config : BTSLogPose.configs.values()) {
                List<EntityPlayerMP> players = serverWorld.getEntitiesWithinAABB(EntityPlayerMP.class,config.discovery);
                for (EntityPlayerMP playerMP : players) {
                    System.out.println(playerMP +" is within "+config.registry_name);
                }
            }
        }
    }

    public static void sendIslandConfigsToClient(EntityPlayerMP player) {

    }

    public static boolean hasDiscovered(BTSIslandConfig config,EntityPlayerMP player) {
        return false;
    }

    public static void discover(BTSIslandConfig config, EntityPlayerMP player) {

    }
}
