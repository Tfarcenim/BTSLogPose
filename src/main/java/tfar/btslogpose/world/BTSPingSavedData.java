package tfar.btslogpose.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.net.PacketHandler;
import tfar.btslogpose.net.S2CBTSPingPacket;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class BTSPingSavedData extends WorldSavedData {

    private final Map<BTSPing, List<UUID>> btsPings = new HashMap<>();

    public static final String ID = BTSLogPose.MOD_ID+"_btspings";

    public BTSPingSavedData(String name) {
        super(name);
    }

    public BTSPingSavedData() {
        super(ID);
    }

    public Set<BTSPing> getPings() {
        return btsPings.keySet();
    }

    public Set<String> getNames() {
        return getPings().stream().map(BTSPing::getName).collect(Collectors.toSet());
    }

    public List<BTSPing> getTrackedPings(EntityPlayerMP player) {
        List<BTSPing> pings = btsPings.keySet().stream().filter(ping -> isTracking(ping, player)).collect(Collectors.toList());
        return pings;
    }

    public void track(BTSPing ping,EntityPlayerMP player) {
        if (ping != null) {
            btsPings.get(ping).add(player.getPersistentID());
            sendPings(player);
            markDirty();
        }
    }

    public void untrack(BTSPing ping,EntityPlayerMP player) {
        if (ping != null) {
            btsPings.get(ping).remove(player.getPersistentID());
            sendPings(player);
            markDirty();
        }
    }

    public boolean isTracking(BTSPing ping,EntityPlayerMP player) {
        return btsPings.get(ping).contains(player.getPersistentID());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        btsPings.clear();
        NBTTagList nbtTagList = nbt.getTagList("pings", Constants.NBT.TAG_COMPOUND);
        for (NBTBase nbtBase : nbtTagList) {
            NBTTagCompound nbtTagCompound = (NBTTagCompound) nbtBase;
            BTSPing btsPing = BTSPing.fromNBT(nbtTagCompound.getCompoundTag("btsping"));
            List<UUID> uuids = deserializeuuids(nbtTagCompound.getTagList("tracking",Constants.NBT.TAG_STRING));
            btsPings.put(btsPing,uuids);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList nbtTagList = new NBTTagList();
        for (Map.Entry<BTSPing,List<UUID>> entry : btsPings.entrySet()) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setTag("btsping",entry.getKey().toNBT());
            nbtTagCompound.setTag("tracking",serializeuuids(entry.getValue()));
                nbtTagList.appendTag(nbtTagCompound);
            }
        compound.setTag("pings", nbtTagList);
        return compound;
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

    public static NBTTagList serializeuuids(List<UUID> uuids) {
        NBTTagList nbtTagList = new NBTTagList();
        for (UUID uuid : uuids) {
            nbtTagList.appendTag(new NBTTagString(uuid.toString()));
        }
        return nbtTagList;
    }

    public void addPing(BTSPing ping) {
        btsPings.put(ping,new ArrayList<>());
        markDirty();
    }

    public boolean removePingByName(World world,String name) {
        BTSPing remove = null;
        for (Map.Entry<BTSPing,List<UUID>> entry : btsPings.entrySet()) {
            BTSPing btsPing = entry.getKey();
            if (btsPing.getName().equals(name)) {
                remove = btsPing;
                break;
            }
        }
        btsPings.remove(remove);
        updateAllPings(world);
        markDirty();
        return remove != null;
    }

    @Nullable
    public BTSPing lookupByName(String name) {
        for (Map.Entry<BTSPing,List<UUID>> entry : btsPings.entrySet()) {
            BTSPing btsPing = entry.getKey();
            if (btsPing.getName().equals(name)) {
                return btsPing;
            }
        }
        return null;
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    public static BTSPingSavedData getOrCreate(World world) {
        BTSPingSavedData ladderSavedData = (BTSPingSavedData) world.getMapStorage().getOrLoadData(BTSPingSavedData.class, BTSPingSavedData.ID);
        if (ladderSavedData == null) {
            ladderSavedData = new BTSPingSavedData();
            world.getMapStorage().setData(BTSPingSavedData.ID, ladderSavedData);
        }
        return ladderSavedData;
    }

    public void sendPings(EntityPlayerMP player) {
        PacketHandler.sendPacketToClient(new S2CBTSPingPacket(getTrackedPings(player)),player);
    }

    public void updateAllPings(World world) {
        List<EntityPlayer> players = world.playerEntities;
        for (EntityPlayer player : players) {
            sendPings((EntityPlayerMP) player);
        }
    }

    public void clearPings(EntityPlayerMP player) {
        PacketHandler.sendPacketToClient(new S2CBTSPingPacket(new ArrayList<>()),player);
    }
}
