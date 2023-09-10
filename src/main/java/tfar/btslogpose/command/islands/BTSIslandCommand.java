package tfar.btslogpose.command.islands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;
import tfar.btslogpose.command.pings.*;

import javax.annotation.Nullable;
import java.util.List;

public class BTSIslandCommand extends CommandTreeBase {

    public BTSIslandCommand() {
        this.addSubcommand(new BTSIslandReloadCommand());
    }
    @Override
    public String getName() {
        return "btsisland";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.island.usage";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
