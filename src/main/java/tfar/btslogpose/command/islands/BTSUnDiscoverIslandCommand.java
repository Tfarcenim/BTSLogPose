package tfar.btslogpose.command.islands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;
import tfar.btslogpose.world.BTSIslandManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class BTSUnDiscoverIslandCommand extends CommandBase {

    @Override
    public String getName() {
        return "undiscover";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.btsislands.undiscover_island.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 2) {
            int j = 0;
            String region = args[j++];
            String island = args[j++];
            Entity entity = getEntity(server, sender, args[j++]);
            if (BTSLogPose.REGIONS.contains(region)) {
                BTSIslandManager.undiscover(region+".json",island,(EntityPlayerMP) entity,server.getWorld(0));
                notifyCommandListener(sender, this, "commands.btsislands.undiscover_island.success");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args,BTSLogPose.REGIONS);
        } else if (args.length == 2) {
            String region = args[0] +".json";
            Map<String,BTSIslandConfig> keys = BTSLogPose.configs.get(region);
            if (keys != null) {
                return getListOfStringsMatchingLastWord(args,keys.keySet());
            }
        } else if (args.length == 3) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
