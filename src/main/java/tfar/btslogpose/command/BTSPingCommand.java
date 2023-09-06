package tfar.btslogpose.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.world.BTSPing;
import tfar.btslogpose.world.BTSPingSavedData;

import javax.annotation.Nullable;
import java.util.List;

public class BTSPingCommand extends CommandBase {
    @Override
    public String getName() {
        return "btsping";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands."+ BTSLogPose.MOD_ID+".btsping.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            String arg1 = args[0];
            if (arg1.equals("add")) {
                int j = 1;
                Entity entity = getEntity(server, sender, args[j++]);
                BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(sender.getEntityWorld());

                int i = 4096;
                Vec3d vec3d = sender.getPositionVector();
                CommandBase.CoordinateArg coordinateArgX = parseCoordinate(vec3d.x, args[j++], true);
                CommandBase.CoordinateArg coordinateArgY = parseCoordinate(vec3d.y, args[j++], -4096, 4096, false);
                CommandBase.CoordinateArg coordinateArgZ = parseCoordinate(vec3d.z, args[j++], true);
             //   CommandBase.CoordinateArg commandbase$coordinatearg3 = parseCoordinate(args.length > j ? (double)entity1.rotationYaw : (double)entity.rotationYaw, args.length > j ? args[j] : "~", false);
                ++j;
              //  CommandBase.CoordinateArg commandbase$coordinatearg4 = parseCoordinate(args.length > j ? (double)entity1.rotationPitch : (double)entity.rotationPitch, args.length > j ? args[j] : "~", false);
                notifyCommandListener(sender, this, "commands.teleport.success.coordinates", entity.getName(), coordinateArgX.getResult(), coordinateArgY.getResult(), coordinateArgZ.getResult());

                btsPingSavedData.addPing((EntityPlayerMP) entity,
                        BTSPing.of(new BlockPos(coordinateArgX.getResult(),coordinateArgY.getResult(),coordinateArgZ.getResult()),0));
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {

        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "add", "remove", "remove_all");
        } else if (args.length == 2) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if (args.length > 2 && args.length < 6) {
            return getTabCompletionCoordinate(args, 2, targetPos);
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
