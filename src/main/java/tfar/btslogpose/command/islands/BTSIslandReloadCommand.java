package tfar.btslogpose.command.islands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;

public class BTSIslandReloadCommand extends CommandBase {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.btsislands.reload.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BTSLogPose.configs = BTSIslandConfig.read();
        notifyCommandListener(sender, this, "commands.btsislands.reload.success");
    }
}
