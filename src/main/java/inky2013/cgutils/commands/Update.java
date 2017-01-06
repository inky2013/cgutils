package inky2013.cgutils.commands;

import inky2013.cgutils.utils.CGLogger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ethan on 03/01/2017.
 */
public class Update extends CommandBase{

    @Override
    public String getCommandName()
    {
        return "update";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayer;
    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "update <name>";
    }

    private HashMap<String, UpdateInformation> urls = new HashMap<String, UpdateInformation>();

    public void registerDownload(UpdateInformation ui) {
        if (Arrays.asList(new String[]{"list", "reload"}).contains(ui.name)) {
            CGLogger.warn("Registering an update command as \""+ui.name+"\"");
            return;
        }
        if (urls.containsKey(ui.name)) {
            CGLogger.error(String.format("CGUtils updater key \"%s\" could not be added as it is already in use."));
        } else {
            urls.put(ui.name, ui);
            CGLogger.info(String.format("Assigned '%s' to '%s'", ui.name, ui.url));
        }
    }
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 1) {
            if (!(urls.containsKey(args[0]))) {
                sender.addChatMessage(new TextComponentTranslation("cgutils.noupdatenamed", args[0]));
            }
            args = new String[]{urls.get(args[0]).url, urls.get(args[0]).saveLocation};
        } else if (args.length != 2) { return; }
        try {

            URL website = new URL(args[0]);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(args[1]);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        } catch (MalformedURLException e) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.javaerror.malformedurl"));
        } catch (IOException e) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.javaerror.io"));
        } catch (SecurityException e) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.javaerror.security"));
        }
    }

}
