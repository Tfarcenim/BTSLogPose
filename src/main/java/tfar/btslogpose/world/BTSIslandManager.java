package tfar.btslogpose.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;
import tfar.btslogpose.net.PacketHandler;
import tfar.btslogpose.net.S2CBTSIslandConfigPacket;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class BTSIslandManager {

    public static BTSDiscoveryData btsDiscoveryData;

    @SubscribeEvent
    public static void playerTick(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            WorldServer serverWorld = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);

            for( Map.Entry<String,Map<String,BTSIslandConfig>> regionEntry : BTSLogPose.configs.entrySet()) {
                String region = regionEntry.getKey();
                for (Map.Entry<String, BTSIslandConfig> islandEntry : regionEntry.getValue().entrySet()) {
                    String islandName = islandEntry.getKey();
                    List<EntityPlayerMP> players = serverWorld.getEntitiesWithinAABB(EntityPlayerMP.class, islandEntry.getValue().discovery,
                            (entityPlayer) -> !BTSIslandManager.hasDiscovered(region, islandName, entityPlayer, serverWorld));
                    for (EntityPlayerMP playerMP : players) {
                        System.out.println(playerMP + " is within " + islandName);
                        discover(region, islandName, playerMP, serverWorld);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void login(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayerMP player = (EntityPlayerMP) e.player;
        sendIslandConfigsToClient(player);
    }

    public static BTSDiscoveryData getOrCreateDiscoveryData(WorldServer level) {
        if (btsDiscoveryData == null) {
            btsDiscoveryData = BTSDiscoveryData.getOrCreate(level);
        }
        return btsDiscoveryData;
    }

    public static void discover(String region,String island, EntityPlayerMP player, WorldServer overworld) {
        BTSDiscoveryData data = getOrCreateDiscoveryData(overworld);
        data.discover(region,island,player);
    }

    public static void undiscover(String region,String island, EntityPlayerMP player, WorldServer overworld) {
        BTSDiscoveryData data = getOrCreateDiscoveryData(overworld);
        data.unDiscover(region,island,player);
    }

    public static void sendIslandConfigsToClient(EntityPlayerMP player) {
        for (Map.Entry<String, Map<String, BTSIslandConfig>> entry : BTSLogPose.configs.entrySet()) {
            String regionName = entry.getKey();
            Map<String,BTSIslandConfig> regionConfig = entry.getValue();
            PacketHandler.sendPacketToClient(new S2CBTSIslandConfigPacket(regionName,regionConfig),player);
        }
    }

    public static boolean hasDiscovered(String region,String island, EntityPlayerMP player, WorldServer overworld) {
        BTSDiscoveryData data = getOrCreateDiscoveryData(overworld);
        return data.hasDiscovered(region,island,player);
    }
}
