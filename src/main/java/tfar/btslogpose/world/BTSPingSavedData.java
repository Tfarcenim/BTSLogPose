package tfar.btslogpose.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.net.PacketHandler;
import tfar.btslogpose.net.S2CBTSPingPacket;

import java.util.ArrayList;
import java.util.List;

public class BTSPingSavedData extends WorldSavedData {

    private final List<BTSPing> btsPings = new ArrayList<>();

    public static final String ID = BTSLogPose.MOD_ID+"_btspings";

    public BTSPingSavedData(String name) {
        super(name);
    }

    public BTSPingSavedData() {
        super(ID);
    }

    public List<BTSPing> getPings() {
        return btsPings;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        btsPings.clear();
        NBTTagList nbtTagList = nbt.getTagList("pings", Constants.NBT.TAG_COMPOUND);
        for (NBTBase nbtBase : nbtTagList) {
            NBTTagCompound nbtTagCompound = (NBTTagCompound) nbtBase;
            int[] ints = nbtTagCompound.getIntArray("pos");
            BlockPos pos = new BlockPos(ints[0], ints[1], ints[2]);
            int height = nbtTagCompound.getInteger("color");
            btsPings.add(BTSPing.of(pos, height));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList nbtTagList = new NBTTagList();
        for (BTSPing entry : btsPings) {
            BlockPos pos = entry.getPos();
            int height = entry.getColor();
            if (height > 0) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                nbtTagCompound.setInteger("color", height);
                nbtTagList.appendTag(nbtTagCompound);
            }
        }
        compound.setTag("pings", nbtTagList);
        return compound;
    }

    public void addPing(EntityPlayerMP player,BTSPing ping) {
        btsPings.add(ping);
        PacketHandler.sendPacketToClient(new S2CBTSPingPacket(btsPings), player);
        markDirty();
    }

    public void removePing(BTSPing ping) {
        btsPings.remove(ping);
        markDirty();
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
}
