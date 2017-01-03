package inky2013.cgutils.items;

import inky2013.cgutils.CGUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;
import java.util.UUID;

/**
 * Created by ethan on 03/01/2017.
 */
public class CGItem extends Item {

    protected String name;

    public CGItem() {
        this.name = UUID.randomUUID().toString();
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CGUtils.creativeTab);
    }

    public CGItem(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CGUtils.creativeTab);
    }

    public void registerItemModel() {
        CGUtils.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public CGItem setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}