package inky2013.cgutils.commands;

import inky2013.cgutils.utils.CGLogger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class StackedCommand extends CommandBase
{
    private List<String> aliases;
    private List<String> commands;
    private boolean op;
    private boolean requiresArgs;
    private boolean execAsServer;
    private int argslen;

    public StackedCommand(String name, String[] cmds, boolean needsop, boolean requiresArgs, int argslen, boolean execAsServer)
    {
        this.aliases = new ArrayList<String>();
        this.aliases.add(name);
        this.commands = new ArrayList<String>();
        updateData(cmds, needsop, requiresArgs, argslen, execAsServer);
    }

    public void updateData(String[] cmds, boolean needsop, boolean requiresArgs, int argslen, boolean execAsServer) {
        this.requiresArgs = requiresArgs;
        this.argslen = argslen;
        this.execAsServer = execAsServer;
        for (int i=0; i<cmds.length;i++) {
            commands.add(cmds[i]);
        }
        op = needsop;

    }

    @Override
    public String getCommandName()
    {
        return this.aliases.get(0).toString();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (!(op)) { return true; }
        return server.isSinglePlayer() || super.checkPermission(server, sender);
    }
    public List getCommands() {
        return commands;
    }
    public boolean getOpRequirement() { return op; }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return this.aliases.get(0).toString();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (requiresArgs) {
            if (args.length != argslen) {
                sender.addChatMessage(new TextComponentTranslation("cgutils.cmdstack.invalidargs", argslen));
                return;
            }
        }
        if (commands.size() == 0) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.cmdstack.emptystack"));
            return;
        }
        for (int i=0; i<commands.size(); i++) {
            try {
                String cmd = commands.get(i).toString();
                if (cmd.contains("{{sender}}")) {
                    cmd = cmd.replace("{{sender}}", sender.getName());
                }
                for (int z=0; z<args.length; z++) {
                    if (cmd.contains("{{"+z+"}}")) {
                        cmd = cmd.replace("{{"+z+"}}", args[z]);
                    }
                }
                int output = server.getCommandManager().executeCommand(server, cmd);
                if (server.getServer().worldServerForDimension(0).getGameRules().getBoolean("commandStackOutput")) {
                    sender.addChatMessage(new TextComponentTranslation("cgutils.cmdstack.returned", cmd, output));
                }
            } catch (Exception e){
                sender.addChatMessage(new TextComponentTranslation("cgutils.badcmdstack"));
                CGLogger.error(e);
                break;
            }
        }

    }

}