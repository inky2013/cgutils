package inky2013.cgutils;

import inky2013.cgutils.commands.*;
import inky2013.cgutils.utils.CGLogger;
import inky2013.cgutils.utils.CommandStackReader;
import inky2013.cgutils.utils.CommandStackRegistry;
import inky2013.cgutils.utils.UpdateReader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


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
    public static String[] sudoBlacklist;
    public static String[] sudoWhitelist;
    public static String workingDirectory = System.getProperty("user.dir");
    public static UpdateInformation[] updates;

    @Mod.Instance(Constants.modid)
    public static CGUtils instance;

    public Update updatecmd = new Update();
    public CommandStackRegistry stackRegistry = new CommandStackRegistry();
    public MinecraftServer server;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        CGLogger.info(String.format("Starting %s", Constants.modid));
        config = new Configuration(e.getSuggestedConfigurationFile());
        getCfg();

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
            for (int i = 0; i < updates.length; i++) {
                CGLogger.info("Found update '" + updates[i].name + "'");
            }
        }
        return updates;
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        server = event.getServer();
        event.registerServerCommand(new PrintTextTranslation());
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
            registerSudoCommand = config.get("Command", "registerSudoCommand",true,"Register sudo command to run any command as op. This allows fake players to run admin command. Actual players can't use the command unless they are admin or whitelisted.").getBoolean();
            sudoWhitelist = config.get("Command", "sudoWhitelist", new String[]{}, "Whitelisted entity names for /sudo command. If left empty the blacklist will be used instead.").getStringList();
            sudoBlacklist = config.get("Command", "sudoBlacklist", new String[]{}, "Blacklisted entity names for /sudo command. If this and sudoWhitelist are left blank everything except non-opped players will be allowed to use the command.").getStringList();

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


}
