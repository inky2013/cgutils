package inky2013.cgutils;

import inky2013.cgutils.commands.PrintTextTranslation;
import inky2013.cgutils.commands.StackedCommand;
import inky2013.cgutils.proxy.CommonProxy;
import inky2013.cgutils.utils.CGCreativeTab;
import inky2013.cgutils.utils.CGLogger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.Arrays;


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
    public static int[] oreSpawnAllow = {
            10 //Ender Ore
    };

    @Mod.Instance(Constants.modid)
    public static CGUtils instance;

    public static CGCreativeTab creativeTab = new CGCreativeTab();


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
    }


    public static void getCfg() {
        try {
            config.load();

            oreSpawnAllow[0] = config.get("WorldGen", "enderOre",10,"Rarity of ender ore. Set to 0 to disable the ore. Default is 10").getInt();
            registerCommandStacker = config.get("Commands", "registerCommandStacker",true,"Register the command stacker (runs preset commands set in the commandstacks directory)").getBoolean();

        } catch (Exception e) {
            CGLogger.warn("Failed to read config.");
            CGLogger.warn(e.toString());
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

}
