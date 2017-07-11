package inky2013.cgutils.commands;

import inky2013.cgutils.utils.PlayerHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ethan Brews on 07/07/2017.
 */
public class TpxCommand extends CommandBase
{
    List<String> aliases = new ArrayList(Arrays.asList("tpx"));

    @Override
    public String getCommandName()
    {
        return this.aliases.get(0);
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return this.aliases.get(0)+" [player] <dim> <x> <y> <z> OR "+this.aliases.get(0)+" [target player] <destination player>";
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length >= 1 && args.length <= 2) {
            EntityPlayer target;
            if (args.length == 1) {
                if (!(sender instanceof EntityPlayer)) {
                    sender.addChatMessage(new TextComponentString("Invalid arguments for non-player"));
                    return;
                }
                target = (EntityPlayer) sender;
            } else {
                target = PlayerHelper.getPlayerFromUsername(args[0]);
            }
            EntityPlayer destPlayer = PlayerHelper.getPlayerFromUsername(args[1]);
            if (target == null || destPlayer == null) {
                sender.addChatMessage(new TextComponentString("That player could not be found"));
            }

        }
    }

}
