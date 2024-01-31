package tfar.btslogpose.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;
import tfar.btslogpose.net.PacketHandler;
import tfar.btslogpose.net.S2CBTSIslandDiscoveryPacket;

import java.util.*;

public class BTSDiscoveryData extends WorldSavedData {

    private final Map<String, Map<String, List<UUID>>> discoveries = new HashMap<>();

    public static final String ID = BTSLogPose.MOD_ID + "_btsislanddiscoveries";

    public BTSDiscoveryData(String name) {
        super(name);
    }

    public BTSDiscoveryData() {
        this(ID);
    }


    public void discover(String region, String island, EntityPlayerMP player) {


        if (!discoveries.containsKey(region)) {
            discoveries.put(region,new HashMap<>());
        }

        Map<String,List<UUID>> map = discoveries.get(region);

        if (!map.containsKey(island)) {
            map.put(island,new ArrayList<>());
        }

        List<UUID> uuids = map.get(island);

        uuids.add(player.getPersistentID());
        syncDiscoveries(player);
        markDirty();
    }

    public boolean unDiscover(String region, String island, EntityPlayerMP player) {
        Map<String,List<UUID>> map = discoveries.get(region);
        List<UUID> uuids = map.get(island);
        boolean removed = false;
        if (uuids != null) {
            removed = uuids.remove(player.getPersistentID());
        }
        if (removed) {
            syncDiscoveries(player);
            markDirty();
        }
        return removed;
    }

    public void discoverAll(String region, EntityPlayerMP player) {


        if (!discoveries.containsKey(region)) {
            discoveries.put(region,new HashMap<>());
        }

        Map<String,List<UUID>> map = discoveries.get(region);

        Map<String, BTSIslandConfig> keys = BTSLogPose.configs.get(region);

        if (keys != null) {
            for (Map.Entry<String, BTSIslandConfig> entry : keys.entrySet()) {
                String island = entry.getKey();
                if (!map.containsKey(island)) {
                    map.put(island, new ArrayList<>());
                }
                List<UUID> uuids = map.get(island);
                uuids.add(player.getPersistentID());
            }
        }
        syncDiscoveries(player);
        markDirty();
    }

    public void unDiscoverAll(String region, EntityPlayerMP player) {

        Map<String,List<UUID>> map = discoveries.get(region);

        if (map != null) {
            for (Map.Entry<String, List<UUID>> entry : map.entrySet()) {
                List<UUID> uuids = entry.getValue();
                uuids.remove(player.getPersistentID());
            }
        }

        syncDiscoveries(player);
        markDirty();
    }

    public boolean hasDiscovered(String region, String island, EntityPlayerMP playerMP) {
        return getDiscoveriesForRegion(region,playerMP).contains(island);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        discoveries.clear();
        for (String region : nbt.getKeySet()) {
            NBTTagCompound nbtTagCompound = nbt.getCompoundTag(region);
            Map<String,List<UUID>> islands = new HashMap<>();
            for (String island : nbtTagCompound.getKeySet()) {
                List<UUID> uuids = deserializeuuids(nbtTagCompound.getTagList(island,Constants.NBT.TAG_STRING));
                islands.put(island,uuids);
            }
            discoveries.put(region,islands);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
        for (Map.Entry<String, Map<String, List<UUID>>> entry : discoveries.entrySet()) {
            String region = entry.getKey();
            Map<String, List<UUID>> discoveredIslands = entry.getValue();
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            for (Map.Entry<String, List<UUID>> mapEntry : discoveredIslands.entrySet()) {
                String islandName = entry.getKey();
                nbtTagCompound.setTag(islandName, BTSPingSavedData.serializeuuids(mapEntry.getValue()));
            }
            nbtTagCompound1.setTag(region,nbtTagCompound);
        }
        return nbtTagCompound1;
    }

    public static List<UUID> deserializeuuids(NBTTagList list) {
        List<UUID> uuids = new ArrayList<>();
        for (NBTBase nbtBase : list) {
            NBTTagString nbtTagString = (NBTTagString) nbtBase;
            UUID uuid = UUID.fromString(nbtTagString.getString());
            uuids.add(uuid);
        }
        return uuids;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    public static BTSDiscoveryData getOrCreate(World world) {
        BTSDiscoveryData btsDiscoveryData = (BTSDiscoveryData) world.getMapStorage().getOrLoadData(BTSDiscoveryData.class, BTSDiscoveryData.ID);
        if (btsDiscoveryData == null) {
            btsDiscoveryData = new BTSDiscoveryData();
            world.getMapStorage().setData(BTSDiscoveryData.ID, btsDiscoveryData);
        }
        return btsDiscoveryData;
    }

    public List<String> getDiscoveriesForRegion(String region,EntityPlayerMP playerMP) {
        List<String> discs = new ArrayList<>();
        Map<String,List<UUID>> map = discoveries.get(region);
        if (map != null) {
            for (Map.Entry<String, List<UUID>> entry : map.entrySet()) {
                if (entry.getValue().contains(playerMP.getPersistentID())) {
                    discs.add(entry.getKey());
                }
            }
        }
        return discs;
    }

    public void syncDiscoveries(EntityPlayerMP player) {
        for (Map.Entry<String, Map<String, List<UUID>>> map : discoveries.entrySet()) {
            String region = map.getKey();
            PacketHandler.sendPacketToClient(new S2CBTSIslandDiscoveryPacket(region,getDiscoveriesForRegion(region,player)), player);
        }
    }
}
