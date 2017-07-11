package inky2013.cgutils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inky2013.cgutils.commands.*;
import inky2013.cgutils.utils.*;
import jdk.nashorn.internal.parser.JSONParser;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IFMLSidedHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Created by ethan on 02/01/2017.
 */
@Mod(modid = Constants.modid, name = Constants.modname, version = Constants.version, updateJSON = "https://inky2013.github.io/cgutils/update.json", acceptedMinecraftVersions = "[1.10.2,1.11.2]")
public class CGUtils {

    public static Configuration config;

    public static boolean registerCommandStacker = true;
    public static boolean registerUpdateCommand = true;
    public static boolean registerSoundCommand = true;
    public static boolean registerSudoCommand = true;
    public static boolean registerNameCommand = true;
    public static boolean registerCalcCommand = true;
    public static String globalIPAddressCheckUrl = "https://api.ipify.org/";
    public static boolean modifyIPAddressAtServer = false;
    public static boolean modifyIPAddressAtClient = false;

    public static String uploadInstructions = "";
    public static boolean doesUploadFileContainUrl = false;
    public static String downloadInstructions = "";

    public static String[] sudoBlacklist;
    public static String[] sudoWhitelist;
    public static String workingDirectory = System.getProperty("user.dir");
    public static UpdateInformation[] updates;

    public static File serverUploadConfigFile = new File("config/cgutils/serverUpload.json");
    public static File serverDownloadConfigFile = new File("config/cgutils/serverDownload.json");

    @Mod.Instance(Constants.modid)
    public static CGUtils instance;

    public Update updatecmd = new Update();
    public CommandStackRegistry stackRegistry = new CommandStackRegistry();
    public MinecraftServer server;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        CGLogger.info(String.format("Starting %s", Constants.modid));
        config = new Configuration(new File("config/cgutils/cgutils.cfg"));
        getCfg();
        try {
            new File("cgutils/").mkdirs();
            if (!serverUploadConfigFile.exists()) {
                serverUploadConfigFile.createNewFile();
            }
            if (!serverDownloadConfigFile.exists()) {
                serverDownloadConfigFile.createNewFile();
            }
            uploadInstructions = readFile(serverUploadConfigFile.getPath(), StandardCharsets.UTF_8);
            if (uploadInstructions.startsWith("http://") || uploadInstructions.startsWith("https://")) {
                doesUploadFileContainUrl = true;
            }
            downloadInstructions = readFile(serverDownloadConfigFile.getPath(), StandardCharsets.UTF_8);


            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                if (modifyIPAddressAtClient) {
                    String jsonString = readFile(serverDownloadConfigFile.getPath(), StandardCharsets.UTF_8);
                    JsonParser parser = new JsonParser();
                    JsonObject o = parser.parse(jsonString).getAsJsonObject();
                    String address = IPManager.getServerAddress(URLDataLoader.getUrl(o));
                    NBTTagCompound servernbt = CompressedStreamTools.read(new DataInputStream(new FileInputStream("servers.dat")));

                    if (!servernbt.hasKey("servers")) {
                        servernbt.setTag("servers", new NBTTagList());
                    }

                    if (address != null) {
                        CGLogger.info("Retrieved server IP as "+address);
                        NBTTagCompound servercompound = null;
                        for (int i=0;i<servernbt.getTagList("servers", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND).tagCount();i++) {
                            if (servernbt.getTagList("servers", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i).hasKey("cgutils_tracking")) {
                                servercompound = servernbt.getTagList("servers", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND).getCompoundTagAt(i);
                                CGLogger.info(servercompound);
                                servernbt.getTagList("servers", net.minecraftforge.common.util.Constants.NBT.TAG_COMPOUND).removeTag(i);
                                break;
                            }
                        }
                        if (servercompound == null) {
                            servercompound = new NBTTagCompound();
                            servercompound.setString("name", "CGUtils Auto Updating Server");
                            servercompound.setBoolean("cgutils_tracking", true);
                        }
                        servercompound.setString("ip", address);
                        servernbt.getTagList("servers", net.minecraftforge.common.util.Constants.NBT.TAG_LIST).appendTag(servercompound);
                    }

                    File serverdataFile = new File("servers.dat");
                    serverdataFile.delete();
                    CompressedStreamTools.write(servernbt, serverdataFile);
                }

            } else if (modifyIPAddressAtServer) { // We are server-side
                String jsonString = readFile(serverUploadConfigFile.getPath(), StandardCharsets.UTF_8);
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(jsonString).getAsJsonObject();
                if (o.has("url")) {
                    CGLogger.info("Attempting to update server IP address");
                    IPManager.uploadServerAddress(URLDataLoader.getUrl(o));
                } else {
                    IPManager.uploadServerAddress(o.get("command").getAsString());
                }
            }


        } catch (IOException err) {
            CGLogger.error("IOException while creating server info files.");
        }

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        reloadUpdates();

    }

    public void reloadUpdates() {
        CGUtils.instance.updatecmd.clear();
        UpdateInformation[] u = getUpdateCommands();
        for (UpdateInformation i : u) {
            CGUtils.instance.updatecmd.registerDownload(i);
        }
    }

    public UpdateInformation[] getUpdateCommands() {
        if (CGUtils.registerUpdateCommand) {
            updates = UpdateReader.getUpdatesFromFile();
            for (UpdateInformation update: updates) {
                CGLogger.info("Found update '" + update.name + "'");
            }
        }
        return updates;
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        server = event.getServer();
        //event.registerServerCommand(new PrintTextTranslation());
        if (registerCommandStacker) {
            event.getServer().worldServerForDimension(0).getGameRules().setOrCreateGameRule("commandStackOutput", "false");
            event.registerServerCommand(new ReloadStacks());
            stackRegistry.registerCommands(event);
        }
        if (registerUpdateCommand) {
            event.registerServerCommand(updatecmd);
        }
        if (registerSoundCommand) {
            event.registerServerCommand(new Sound());
        }
        if (registerCalcCommand) {
            event.registerServerCommand(new CalcCommand());
        }
        if (registerSudoCommand) {
            event.registerServerCommand(new Sudo());
        }
        if (registerNameCommand) {
            event.registerServerCommand(new GetName());
        }
    }


    public static void getCfg() {
        try {
            config.load();
            registerCommandStacker = config.get("Commands", "registerCommandStacker",true,"Register the command stacker (runs preset commands set in the commandstacks directory)").getBoolean();
            registerUpdateCommand = config.get("Commands", "registerUpdateCommand",true,"Register the command stacker (downloads a file set in the updates directory)").getBoolean();
            registerSoundCommand = config.get("Commands", "registerSoundCommand",true,"Adds a /sound command as a simpler alternative to /playsound").getBoolean();
            registerNameCommand = config.get("Commands", "registerNameCommand",true,"Adds a /getname command to get the name of the sender.").getBoolean();
            registerSudoCommand = config.get("Commands", "registerSudoCommand",true,"Register sudo command to run any command as op. This allows fake players to run admin command. Actual players can't use the command unless they are admin or whitelisted.").getBoolean();
            sudoWhitelist = config.get("Commands", "sudoWhitelist", new String[]{}, "Whitelisted entity names for /sudo command. If left empty the blacklist will be used instead.").getStringList();
            sudoBlacklist = config.get("Commands", "sudoBlacklist", new String[]{}, "Blacklisted entity names for /sudo command. If this and sudoWhitelist are left blank everything except non-opped players will be allowed to use the command.").getStringList();
            modifyIPAddressAtServer = config.get("Server IP", "uploadServerIP", modifyIPAddressAtServer, "[Server Only] Upload the server IP address to a known server so that clients can access it (Good to use if your IP is dynamic)").getBoolean();
            modifyIPAddressAtClient = config.get("Server IP", "downloadServerIP", modifyIPAddressAtClient, "[Client Only] Upload the server IP address to a known server so that clients can access it (Good to use if your IP is dynamic)").getBoolean();
            globalIPAddressCheckUrl = config.get("Server IP", "globalIPAddressCheckUrl", globalIPAddressCheckUrl, "The URL checked to find the server's global IP address").getString();

        } catch (Exception e) {
            CGLogger.warn("Failed to read config.");
            CGLogger.warn(e.toString());
        } finally {
            if (config.hasChanged()) config.save();
        }

    }

    public static void ensureDirectory(File ind) {
        if (!(ind.exists())) {
            CGLogger.info(String.format("Creating directory at '%s'", ind.getAbsolutePath()));
            boolean result = false;
            try {
                ind.mkdirs();
                result = true;
            } catch (SecurityException se) {
                CGLogger.warn(String.format("Permission was denied accessing '%s'", ind.getAbsolutePath()));
            }
            if (result) {
                CGLogger.info(String.format("Created '%s'", ind.getAbsolutePath()));
            }
        }
    }

    public static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


}
