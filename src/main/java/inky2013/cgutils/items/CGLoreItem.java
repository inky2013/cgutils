package inky2013.cgutils.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

/**
 * Created by ethan on 03/01/2017.
 */
public class CGLoreItem extends CGItem {

    public String[] par3listtxt = new String[3];

    public CGLoreItem(String name, String[] pr3lst) {
        this.name = name;
        setUnlocalizedName(name);
        //setRegistryName(name);
        this.par3listtxt = pr3lst;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4){
        NBTTagCompound nbt = par1ItemStack.getTagCompound();
        for (int i=0;i<par3listtxt.length;i++) {
            if (par3listtxt[i] != "") {
                par3List.add(par3listtxt[i]);
            }
        }

    }

}
