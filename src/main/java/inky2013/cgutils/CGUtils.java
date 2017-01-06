package inky2013.cgutils;

import inky2013.cgutils.commands.PrintTextTranslation;
import inky2013.cgutils.commands.Update;
import inky2013.cgutils.proxy.CommonProxy;
import inky2013.cgutils.utils.CGCreativeTab;
import inky2013.cgutils.utils.CGLogger;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;


/**
 * Created by ethan on 02/01/2017.
 */
@Mod(modid = Constants.modid, name = Constants.modname, version = Constants.version)
public class CGUtils {

    @SidedProxy(clientSide = Constants.clientproxy,serverSide = Constants.serverproxy)
    public static CommonProxy proxy;

    public static Configuration config;
    public static final SimpleNetworkWrapper packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.modid);

    public static boolean registerCommandStacker = true;
    public static boolean registerUpdateCommand = true;
    public static int[] oreSpawnAllow = {
            10 //Ender Ore
    };
    public static String[] nocopy;

    @Mod.Instance(Constants.modid)
    public static CGUtils instance;

    public static CGCreativeTab creativeTab = new CGCreativeTab();

    public Update updatecmd = new Update();


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        proxy.preInit(e);
        config = new Configuration(e.getSuggestedConfigurationFile());
        getCfg();

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);

    }
    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new PrintTextTranslation());
        if (registerCommandStacker) {
            for (int i = 0; i < CommonProxy.stackedcommands.length; i++) {
                event.registerServerCommand(CommonProxy.stackedcommands[i]);
            }
        }
        if (registerUpdateCommand) {
            event.registerServerCommand(updatecmd);
        }
    }


    public static void getCfg() {
        try {
            config.load();

            //oreSpawnAllow[0] = config.get("WorldGen", "enderOre",10,"Rarity of ender ore. Set to 0 to disable the ore. Default is 10").getInt();
            nocopy = config.get("Item Duplication", "blacklist",new String[]{}, "Stuff you shouldn't be able to copy.").getStringList();
            registerCommandStacker = config.get("Commands", "registerCommandStacker",true,"Register the command stacker (runs preset commands set in the commandstacks directory)").getBoolean();
            registerUpdateCommand = config.get("Commands", "registerUpdateCommand",true,"Register the command stacker (downloads a file set in the updates directory)").getBoolean();

        } catch (Exception e) {
            CGLogger.warn("Failed to read config.");
            CGLogger.warn(e.toString());
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

}
