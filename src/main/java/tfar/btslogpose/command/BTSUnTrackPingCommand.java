package tfar.btslogpose.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.world.BTSPing;
import tfar.btslogpose.world.BTSPingSavedData;

import javax.annotation.Nullable;
import java.util.List;

public class BTSUnTrackPingCommand extends CommandBase {

    @Override
    public String getName() {
        return "untrack";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.btsping.untrack.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0) {
            int j = 0;
            Entity entity = getEntity(server, sender, args[j++]);
            String name = args[j++];
            BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(sender.getEntityWorld());
            BTSPing ping = btsPingSavedData.lookupByName(name);
            if (ping != null) {
                notifyCommandListener(sender, this, "commands.btsping.untrack.success", entity.getName());
                btsPingSavedData.untrack(ping,(EntityPlayerMP) entity);
            }
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
