package tfar.btslogpose.command.pings;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.world.BTSPing;
import tfar.btslogpose.world.BTSPingSavedData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BTSCreatePingCommand extends CommandBase {

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.btsping.create.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length > 1) {
            int j = 0;
            String name = args[j++];//0
            Vec3d vec3d = sender.getPositionVector();
            String color = args[j++];//1

            CoordinateArg coordinateArgX;
            CoordinateArg coordinateArgY;
            CoordinateArg coordinateArgZ;

            if (args.length > 4) {
                coordinateArgX = parseCoordinate(vec3d.x, args[j++], true);//2
                coordinateArgY = parseCoordinate(vec3d.y, args[j++], false);//3
                coordinateArgZ = parseCoordinate(vec3d.z, args[j++], true);//4
            } else {
                BlockPos pos = sender.getPosition();
                coordinateArgX = parseCoordinate(vec3d.x, pos.getX()+"", true);//2
                coordinateArgY = parseCoordinate(vec3d.y, pos.getY()+"", false);//3
                coordinateArgZ = parseCoordinate(vec3d.z, pos.getZ()+"", true);//4
            }



            notifyCommandListener(sender, this, "commands.btsping.create.success.coordinates",
                    name, color,coordinateArgX.getResult(), coordinateArgY.getResult(), coordinateArgZ.getResult());

            BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(sender.getEntityWorld());

            btsPingSavedData.addPing(
                    BTSPing.of(new BlockPos(coordinateArgX.getResult(),coordinateArgY.getResult(),coordinateArgZ.getResult()),color,name));
        } else {
            throw new WrongUsageException(getUsage(sender));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
        } else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, colors);
        } else if (args.length > 2 && args.length < 6) {
            return getTabCompletionCoordinate(args, 2, targetPos);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }


    public static final List<String> colors = new ArrayList<>();
    static {
        colors.add("blue");
        colors.add("green");
        colors.add("red");
        colors.add("yellow");
    }
}
