package inky2013.cgutils.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by ethan on 21/01/2017.
 */
public class Sound extends CommandBase {

    @Override
    public String getCommandName() {
        return "sound";
    }

    @Override
    public String getCommandUsage(ICommandSender v) {
        return "/sound <player> <sound>";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return server.isSinglePlayer() || super.checkPermission(server, sender);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length != 2) { return; }
        server.getCommandManager().executeCommand(server, String.format("playsound %s player %s ~ ~ ~ 10 1 1", args[1], args[0]));
    }

}
