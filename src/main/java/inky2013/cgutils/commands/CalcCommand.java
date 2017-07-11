package inky2013.cgutils.commands;

import inky2013.cgutils.maths.MathsCommandExecutor;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ethan on 03/01/2017.
 */
public class CalcCommand extends CommandBase
{
    List<String> aliases = new ArrayList(Arrays.asList("calc"));

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
        return "/"+this.aliases.get(0).toString()+" <calculation>";
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        String cstr = String.join(" ", args);
        MathsCommandExecutor.execute(sender, cstr);

    }

}