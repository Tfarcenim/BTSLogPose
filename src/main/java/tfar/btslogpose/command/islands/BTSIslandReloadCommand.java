package tfar.btslogpose.command.islands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;
import tfar.btslogpose.net.PacketHandler;
import tfar.btslogpose.world.BTSIslandManager;

public class BTSIslandReloadCommand extends CommandBase {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.btsisland.reload.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BTSLogPose.configs = BTSIslandConfig.read();
        for (EntityPlayerMP playerMP : server.getPlayerList().getPlayers()) {
            BTSIslandManager.sendIslandConfigsToClient(playerMP);
        }
        notifyCommandListener(sender, this, "commands.btsisland.reload.success");
    }
}
