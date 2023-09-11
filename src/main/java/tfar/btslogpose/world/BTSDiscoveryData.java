package tfar.btslogpose.world;

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
import tfar.btslogpose.net.S2CBTSIslandDiscoveryPacket;

import java.util.*;

public class BTSDiscoveryData extends WorldSavedData {

    private final Map<String, List<UUID>> discoveries = new HashMap<>();

    public static final String ID = BTSLogPose.MOD_ID+"_btsislanddiscoveries";

    public BTSDiscoveryData(String name) {
        super(name);
    }

    public BTSDiscoveryData() {
        super(ID);
    }


    public void discover(String island,EntityPlayerMP player) {
        discoveries.get(island).add(player.getPersistentID());
        syncDiscoveries(player);
        markDirty();
    }

    public void unDiscover(String island,EntityPlayerMP player) {
        discoveries.get(island).remove(player.getPersistentID());
        syncDiscoveries(player);
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        discoveries.clear();
        NBTTagList nbtTagList = nbt.getTagList("islands", Constants.NBT.TAG_COMPOUND);
        for (NBTBase nbtBase : nbtTagList) {
            NBTTagCompound nbtTagCompound = (NBTTagCompound) nbtBase;
            String name = nbtTagCompound.getString("island");
            List<UUID> uuids = deserializeuuids(nbtTagCompound.getTagList("tracking",Constants.NBT.TAG_STRING));
            discoveries.put(null,uuids);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList nbtTagList = new NBTTagList();
        for (Map.Entry<String,List<UUID>> entry : discoveries.entrySet()) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setString("island",entry.getKey());
            nbtTagCompound.setTag("tracking",BTSPingSavedData.serializeuuids(entry.getValue()));
            nbtTagList.appendTag(nbtTagCompound);
            }
        compound.setTag("islands", nbtTagList);
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

    @Override
    public void markDirty() {
        super.markDirty();
    }

    public static BTSDiscoveryData getOrCreate(World world) {
        BTSDiscoveryData ladderSavedData = (BTSDiscoveryData) world.getMapStorage().getOrLoadData(BTSDiscoveryData.class, BTSDiscoveryData.ID);
        if (ladderSavedData == null) {
            ladderSavedData = new BTSDiscoveryData();
            world.getMapStorage().setData(BTSDiscoveryData.ID, ladderSavedData);
        }
        return ladderSavedData;
    }

    public List<String> getDiscoveries(EntityPlayerMP playerMP) {
        List<String> discs = new ArrayList<>();
        for (Map.Entry<String,List<UUID>> entry : discoveries.entrySet()) {
            if (entry.getValue().contains(playerMP.getPersistentID())) {
                discs.add(entry.getKey());
            }
        }
        return discs;
    }

    public void syncDiscoveries(EntityPlayerMP player) {
        PacketHandler.sendPacketToClient(new S2CBTSIslandDiscoveryPacket(getDiscoveries(player)),player);
    }
}
