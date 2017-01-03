package inky2013.cgutils.proxy;

import inky2013.cgutils.utils.CGEventHandlerClient;
import inky2013.cgutils.utils.CGEventHandlerServer;
import inky2013.cgutils.utils.CGLogger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by ethan on 02/01/2017.
 */
public class ServerProxy  extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);

    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        MinecraftForge.EVENT_BUS.register(new CGEventHandlerServer());
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
}
