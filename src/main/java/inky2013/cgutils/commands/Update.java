package inky2013.cgutils.commands;

import com.google.common.io.Files;
import inky2013.cgutils.CGUtils;
import inky2013.cgutils.proxy.CommonProxy;
import inky2013.cgutils.utils.CGLogger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

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

    public void clear() {
        urls.clear();
    }

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
        if (args.length == 1) { //reload, list, help
            if (args[0].equalsIgnoreCase("help")) {
                for (int i=1; i<8; i++) {
                    sender.addChatMessage(new TextComponentTranslation("cgutils.update.help.line"+ String.valueOf(i)));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("list")) {
                for (Map.Entry<String, UpdateInformation> entry : urls.entrySet()) {
                    String key = entry.getKey();
                    UpdateInformation value = entry.getValue();
                    if (value.group == null) {
                        sender.addChatMessage(new TextComponentTranslation("cgutils.update.list", key));
                    } else {
                        sender.addChatMessage(new TextComponentTranslation("cgutils.update.list.grouped", key, value.group));
                    }

                }
                return;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                CGUtils.instance.proxy.reloadUpdates();
                sender.addChatMessage(new TextComponentTranslation("cgutils.update.reload"));
                return;
            }

        } else if (args.length == 2) { //get, group, list
            if (args[0].equalsIgnoreCase("get")) {
                if (urls.containsKey(args[1])) {
                    downloadUpdate(urls.get(args[1]), sender);
                    return;
                }
                sender.addChatMessage(new TextComponentTranslation("cgutils.update.nokey"));
            }
            if (args[0].equalsIgnoreCase("group")) {
                int updated = 0;
                for (Map.Entry<String, UpdateInformation> entry : urls.entrySet()) {
                    String key = entry.getKey();
                    UpdateInformation value = entry.getValue();
                    if (value.group == null) { continue; }
                    if (value.group.equalsIgnoreCase(args[1])) {
                        updated++;
                        downloadUpdate(value, sender);
                    }

                }
                if (updated == 0) {
                    sender.addChatMessage(new TextComponentTranslation("cgutils.update.group.none", args[1]));
                    return;
                }
                sender.addChatMessage(new TextComponentTranslation("cgutils.update.group.updatedint", updated));
                return;
            }
            if (args[0].equalsIgnoreCase("list")) {
                for (Map.Entry<String, UpdateInformation> entry : urls.entrySet()) {
                    String key = entry.getKey();
                    UpdateInformation value = entry.getValue();
                    if (value.group.equalsIgnoreCase(args[1])) {
                        sender.addChatMessage(new TextComponentTranslation("cgutils.update.list", key));
                    }
                }
                return;
            }


        } else if (args.length == 3) { //get url
            if (args[0].equalsIgnoreCase("get")) {
                downloadUpdate(new UpdateInformation(null, args[2], args[1]), sender);
                return;
            }
        }
        sender.addChatMessage(new TextComponentTranslation("cgutils.update.invalidargs"));
    }



    public void downloadUpdate(UpdateInformation currentRequest, ICommandSender sender) {
        String filename = FilenameUtils.getName(currentRequest.saveLocation);
        String pathname = FilenameUtils.getFullPath(currentRequest.saveLocation);
        if (pathname.equals("")) { pathname = CGUtils.workingDirectory; }
        CGUtils.ensureDirectory(new File(pathname));

        try {
            URL url = new URL(currentRequest.url);
            URLConnection con = url.openConnection();
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int code = connection.getResponseCode();
            CGLogger.info("Server responded with HTTP status code "+String.valueOf(code));
            if (!(String.valueOf(code).startsWith("2"))) {
                sender.addChatMessage(new TextComponentTranslation("cgutils.update.httpcode", code));
                if (currentRequest.name == null) {
                    sender.addChatMessage(new TextComponentTranslation("cgutils.update.didnotcomplete.unnamed"));
                } else {
                    sender.addChatMessage(new TextComponentTranslation("cgutils.update.didnotcomplete.named", currentRequest.name));
                }
                return;
            }

            //Try and get a filename from somewhere (savepath -> HTTP headers -> url)
            if (filename.equals("")) {
                String fieldValue = con.getHeaderField("Content-Disposition");
                if (!(fieldValue == null || !fieldValue.contains("filename=\""))) {
                    filename = fieldValue.substring(fieldValue.indexOf("filename=\"") + 10, fieldValue.length() - 1);
                }
                if (filename.equals("")) {
                    filename = FilenameUtils.getName(currentRequest.url);
                }
                if (filename.equals("")) {
                    sender.addChatMessage(new TextComponentTranslation("cgutils.update.response.nofilename"));
                    return;
                }
            }
            File download = new File(System.getProperty("java.io.tmpdir"), filename);
            ReadableByteChannel rbc = Channels.newChannel(con.getInputStream());
            FileOutputStream fos = new FileOutputStream(download);
            boolean didDownload;
            try {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                didDownload = true;
            } finally {
                fos.close();
            }
            if (!(didDownload)) {
                sender.addChatMessage(new TextComponentTranslation("cgutils.update.unknownfail"));
                return;
            }
            Files.copy(download, new File(pathname, filename));
        } catch (FileNotFoundException e) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.javaerror.filenotfound"));
            CGLogger.error(e);

        } catch (MalformedURLException e) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.javaerror.malformedurl"));
            CGLogger.error(e);
            return;

        } catch (IOException e) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.javaerror.ioexception"));
            CGLogger.error(e);
            return;

        } catch (SecurityException e) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.javaerror.security"));
            CGLogger.error(e);
            return;
        } catch (IllegalStateException e) {
            sender.addChatMessage(new TextComponentTranslation("cgutils.javaerror.illegalstate"));
            CGLogger.error(e);
            return;
        }
        if (currentRequest.name == null) { currentRequest.name = FilenameUtils.getBaseName(currentRequest.saveLocation); }
        sender.addChatMessage(new TextComponentTranslation("cgutils.update.complete", currentRequest.name));
    }

}
