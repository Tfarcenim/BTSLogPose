package tfar.btslogpose.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import tfar.btslogpose.world.BTSPingSavedData;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BTSListPingCommand extends CommandBase {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.btsping.list.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(sender.getEntityWorld());
            Set<String> names = btsPingSavedData.getNames();
            String list = String.join(",", names);
            notifyCommandListener(sender, this, list);
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1) {
            BTSPingSavedData btsPingSavedData = BTSPingSavedData.getOrCreate(sender.getEntityWorld());
            return getListOfStringsMatchingLastWord(args,btsPingSavedData.getNames());
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
