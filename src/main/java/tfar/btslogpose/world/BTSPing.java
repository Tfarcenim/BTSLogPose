package tfar.btslogpose.world;

import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class BTSPing {
    private BlockPos pos;
    private int color;

    public BTSPing(BlockPos pos, int color) {
        this.pos = pos;
        this.color = color;
    }

    public static BTSPing of(BlockPos pos, int color) {
        return new BTSPing(pos,color);
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BTSPing BTSPing = (BTSPing) o;
        return color == BTSPing.color && Objects.equals(pos, BTSPing.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, color);
    }
}
