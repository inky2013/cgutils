package inky2013.cgutils.utils;

import inky2013.cgutils.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Created by ethan on 03/01/2017.
 */
public class CGCreativeTab extends CreativeTabs {

    public CGCreativeTab() {
        super(Constants.modid);
        setBackgroundImageName("item_search.png");
    }

    @Override
    public Item getTabIconItem() {
        return Items.APPLE;
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }

}