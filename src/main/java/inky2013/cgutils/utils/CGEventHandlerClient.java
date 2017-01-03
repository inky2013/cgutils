package inky2013.cgutils.utils;

import inky2013.cgutils.CGUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by ethan on 03/01/2017.
 */
public class CGEventHandlerClient {

    @SubscribeEvent
    public void onEntityJoiningWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() != null && event.getEntity() instanceof EntityPlayer) {
            event.getEntity().addChatMessage(new TextComponentTranslation("cgutils.update"));
        }
    }

}
