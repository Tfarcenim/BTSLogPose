package tfar.btslogpose.command.pings;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;
import tfar.btslogpose.BTSLogPose;

public class BTSLogPoseCommand extends CommandTreeBase {

    public BTSLogPoseCommand() {
        addSubcommand(new BTSPingCommand());
    }

    @Override
    public String getName() {
        return BTSLogPose.MOD_ID;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands."+BTSLogPose.MOD_ID+".usage";
    }
}
