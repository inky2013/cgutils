package inky2013.cgutils.commands;

import inky2013.cgutils.CGUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
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
public class ReloadStacks extends CommandBase
{
    List<String> aliases = new ArrayList(Arrays.asList("reloadStacks"));

    @Override
    public String getCommandName()
    {
        return this.aliases.get(0).toString();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return server.isSinglePlayer() || super.checkPermission(server, sender);
    }
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return this.aliases.get(0).toString();
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        List<String> newStacks = CGUtils.instance.stackRegistry.reloadStack();
        if (newStacks.size() == 0) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.stacks.reload"));
        } else {
            sender.addChatMessage(new TextComponentTranslation("cgutils.stacks.reload.newfound", newStacks.size(), String.join(",", newStacks)));
        }

    }

}