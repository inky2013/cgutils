package inky2013.cgutils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by ethan on 21/01/2017.
 */
public class GetName extends CommandBase {

    @Override
    public String getCommandName() {
        return "getname";
    }

    @Override
    public String getCommandUsage(ICommandSender v) {
        return "/getname";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        boolean broadcast = false;
        if (args.length > 0 && (args[0].equals("true") || args[0].equals("broadcast"))) {
            broadcast = true;
        }
        if (broadcast) {
            List<EntityPlayerMP> players = server.getPlayerList().getPlayerList();
            for (int i=0;i<players.size();i++) {
                players.get(i).addChatMessage(new TextComponentTranslation("cgutils.showentitydata", sender.getName(), sender.getClass().getName()));
            }
        } else {
            sender.addChatMessage(new TextComponentTranslation("cgutils.showentitydata", sender.getName(), sender.getClass().getName()));
        }
    }
}
