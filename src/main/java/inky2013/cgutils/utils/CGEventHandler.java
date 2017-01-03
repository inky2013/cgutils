package inky2013.cgutils.utils;

import inky2013.cgutils.items.CGItem;
import inky2013.cgutils.items.CGItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by ethan on 03/01/2017.
 */
public class CGEventHandler {

    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent e) {

        for(int i=0; i < e.craftMatrix.getSizeInventory(); i++)
        {
            if(e.craftMatrix.getStackInSlot(i) != null)
            {
                ItemStack j = e.craftMatrix.getStackInSlot(i);
                if(j.getItem() != null && j.getItem() == CGItems.hammer)
                {
                    ItemStack k = new ItemStack(CGItems.hammer, 2, (j.getItemDamage() + 1));
                    e.craftMatrix.setInventorySlotContents(i, k);
                }
            }
        }
    }

}
