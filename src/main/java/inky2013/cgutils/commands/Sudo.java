package inky2013.cgutils.commands;

import inky2013.cgutils.CGUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by ethan on 21/01/2017.
 */
public class Sudo extends CommandBase {

    @Override
    public String getCommandName() {
        return "sudo";
    }

    @Override
    public String getCommandUsage(ICommandSender v) {
        return "/sudo <command>";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (CGUtils.sudoWhitelist.length > 0) {
            for (int i=0; i<CGUtils.sudoWhitelist.length;i++) {
                if (CGUtils.sudoWhitelist[i].equals(sender.getName())) {
                    return true;
                }
            }
            return false;
        }
        if (CGUtils.sudoBlacklist.length > 0) {
            for (int i=0; i<CGUtils.sudoBlacklist.length;i++) {
                if (CGUtils.sudoBlacklist[i].equals(sender.getName())) {
                    return false;
                }
            }
            return true;
        }
        if (sender instanceof EntityPlayer) {
            return super.checkPermission(server, sender);
        }
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 0) { return; }
        server.getCommandManager().executeCommand(server, StringUtils.join(args, " "));
    }

}
