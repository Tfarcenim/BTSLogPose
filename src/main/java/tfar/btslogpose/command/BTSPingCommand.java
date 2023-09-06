package tfar.btslogpose.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nullable;
import java.util.List;

public class BTSPingCommand extends CommandTreeBase {

    public BTSPingCommand() {
        this.addSubcommand(new BTSCreatePingCommand());
        this.addSubcommand(new BTSDeletePingCommand());
        this.addSubcommand(new BTSTrackPingCommand());
        this.addSubcommand(new BTSUnTrackPingCommand());
        this.addSubcommand(new BTSListPingCommand());
    }
    @Override
    public String getName() {
        return "btsping";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.btsping.usage";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
