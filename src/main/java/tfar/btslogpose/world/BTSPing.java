package tfar.btslogpose.world;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import tfar.btslogpose.BTSLogPose;

import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BTSPing {
    private final BlockPos pos;
    private final String color;
    private transient final ResourceLocation tex;
    private final String name;

    public BTSPing(BlockPos pos, String color,String name) {
        this.pos = pos;
        this.color = color;
        this.name = name;
        tex = new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/ping_"+color+".png");
    }

    public static BTSPing of(BlockPos pos, String color,String name) {
        return new BTSPing(pos,color,name);
    }

    public BlockPos getPos() {
        return pos;
    }

    public String getColor() {
        return color;
    }

    public ResourceLocation getTex() {
        return tex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BTSPing BTSPing = (BTSPing) o;
        return Objects.equals(color, BTSPing.color) && Objects.equals(pos, BTSPing.pos) && Objects.equals(name, BTSPing.name);
    }

    public void toNetwork(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        ByteBufUtils.writeUTF8String(buf,color);
        ByteBufUtils.writeUTF8String(buf,name);
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        nbtTagCompound.setString("color", color);
        nbtTagCompound.setString("name",name);
        return nbtTagCompound;
    }

    public static BTSPing fromNetwork(ByteBuf buf) {
        return BTSPing.of(new BlockPos(buf.readInt(), buf.readInt(),buf.readInt()),
                ByteBufUtils.readUTF8String(buf),
                ByteBufUtils.readUTF8String(buf));
    }

    public static BTSPing fromNBT(NBTTagCompound compound) {
        int[] ints = compound.getIntArray("pos");
        BlockPos pos = new BlockPos(ints[0], ints[1], ints[2]);
        String color = compound.getString("color");
        String name = compound.getString("name");
        return BTSPing.of(pos, color,name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(pos, color,name);
    }

    public String getName() {
        return name;
    }
}
