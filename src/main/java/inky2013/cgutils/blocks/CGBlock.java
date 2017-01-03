package inky2013.cgutils.blocks;

import inky2013.cgutils.CGUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

/**
 * Created by ethan on 03/01/2017.
 */
public class CGBlock extends Block {

    protected String name;

    public CGBlock(Material material, String name) {
        super(material);
        super.setCreativeTab(CGUtils.creativeTab);
        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void registerItemModel(ItemBlock itemBlock) {
        CGUtils.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    @Override
    public CGBlock setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}