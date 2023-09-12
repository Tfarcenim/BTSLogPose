package tfar.btslogpose.command.islands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;

import javax.annotation.Nullable;
import java.util.List;

public class BTSDiscoverIslandCommand extends CommandBase {

    @Override
    public String getName() {
        return "discover";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.btsislands.reload.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        notifyCommandListener(sender, this, "commands.btsislands.reload.success");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
