package tfar.btslogpose.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import tfar.btslogpose.net.PacketHandler;
import tfar.btslogpose.net.S2COpenRegionScreenPacket;
import tfar.btslogpose.world.BTSPingSavedData;

import javax.annotation.Nullable;
import java.util.List;

public class OpenRegionScreenCommand extends CommandBase {

    @Override
    public String getName() {
        return "btsregionmenu";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.bteregionmenu.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        PacketHandler.sendPacketToClient(new S2COpenRegionScreenPacket(),(EntityPlayerMP) sender.getCommandSenderEntity());
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
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
