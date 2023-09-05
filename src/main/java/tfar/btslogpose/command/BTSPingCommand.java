package tfar.btslogpose.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import tfar.btslogpose.BTSLogPose;

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

            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {

        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, "add", "remove", "remove_all");
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
