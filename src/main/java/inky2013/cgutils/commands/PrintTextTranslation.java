package inky2013.cgutils.commands;

import li.cil.repack.org.luaj.vm2.ast.Str;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ethan on 03/01/2017.
 */
public class PrintTextTranslation extends CommandBase
{
    List<String> aliases = new ArrayList(Arrays.asList("printCGUtilsTextTranslation"));

    @Override
    public String getCommandName()
    {
        return this.aliases.get(0).toString();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return this.aliases.get(0).toString();
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        sender.addChatMessage(new TextComponentTranslation(args[0]).setStyle(new Style().setColor(TextFormatting.BLUE)));
    }

}