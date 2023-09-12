package tfar.btslogpose.command.islands;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class BTSIslandCommand extends CommandTreeBase {

    public BTSIslandCommand() {
        this.addSubcommand(new BTSIslandReloadCommand());
        this.addSubcommand(new BTSDiscoverIslandCommand());
        this.addSubcommand(new BTSUnDiscoverIslandCommand());
    }
    @Override
    public String getName() {
        return "btsisland";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.island.usage";
    }

}
