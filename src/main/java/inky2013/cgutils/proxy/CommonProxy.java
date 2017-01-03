package inky2013.cgutils.proxy;

import inky2013.cgutils.CGUtils;
import inky2013.cgutils.Constants;
import inky2013.cgutils.commands.StackedCommand;
import inky2013.cgutils.utils.CGLogger;
import inky2013.cgutils.utils.CommandStackReader;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by ethan on 02/01/2017.
 */
public class CommonProxy {

    public static StackedCommand[] stackedcommands;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        CGLogger.info(String.format("Starting %s", Constants.modid));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        if (CGUtils.registerCommandStacker) {
            stackedcommands = CommandStackReader.getStackedCommandsFromFile();
            for (int i = 0; i < stackedcommands.length; i++) {
                CGLogger.info("Found command '" + stackedcommands[i].getCommandName() + "'");
            }
        }
    }
    public void registerItemRenderer(Item item, int meta, String id) {
    }

}
