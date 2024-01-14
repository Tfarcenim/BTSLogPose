package tfar.btslogpose.command.pings;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.world.BTSPing;
import tfar.btslogpose.world.BTSPingSavedData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BTSTrackPingCommand extends CommandBase {

    @Override
    public String getName() {
        return "track";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.btsping.track.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 1) {
            int j = 0;
            Entity entity = getEntity(server, sender, args[j++]);
            String name = args[j++];
            BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(sender.getEntityWorld());
            BTSPing ping = btsPingSavedData.lookupByName(name);
            if (ping != null) {
                notifyCommandListener(sender, this, "commands.btsping.track.success", entity.getName(),name);
                btsPingSavedData.track(ping,(EntityPlayerMP) entity);
            }
        } else {
            throw new WrongUsageException(getUsage(sender));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else if (args.length == 2) {
            BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(sender.getEntityWorld());
            return getListOfStringsMatchingLastWord(args,btsPingSavedData.getNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
