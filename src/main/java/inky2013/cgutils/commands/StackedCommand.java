package inky2013.cgutils.commands;

import li.cil.oc.client.Sound;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class StackedCommand extends CommandBase
{
    private final List aliases;
    private final List commands;

    protected String fullEntityName;
    protected Entity conjuredEntity;

    public StackedCommand(String name, String[] cmds)
    {
        aliases = new ArrayList();
        commands = new ArrayList();
        for (int i=0; i<cmds.length;i++) {
            commands.add(cmds[i]);
        }
    }

    @Override
    public String getCommandName()
    {
        return this.aliases.get(0).toString();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return this.aliases.get(0).toString();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        for (int i=0; i<commands.size(); i++) {
            try {
                server.getCommandManager().executeCommand(server, commands.get(i).toString());
            } catch (Exception e){
                sender.addChatMessage(new TextComponentTranslation("cgutils.badcmdstack").setStyle(new Style().setColor(TextFormatting.RED)));
                break;
            }
        }

    }

}