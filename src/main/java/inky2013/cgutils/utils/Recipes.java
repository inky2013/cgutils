package inky2013.cgutils.utils;

import com.google.common.collect.ImmutableList;
import inky2013.cgutils.CGUtils;
import inky2013.cgutils.items.CGItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ethan on 03/01/2017.
 */
public class Recipes {

    public int registerCopyRecipes() {

        int loaded = 0;

        List<Item> list = ImmutableList.copyOf(Item.REGISTRY);
        for (int i=0;i<list.size();i++) {
                if (!( Arrays.asList(CGUtils.nocopy).contains(list.get(i)) )) {
                    GameRegistry.addShapelessRecipe(new ItemStack(list.get(i), 2), new ItemStack(list.get(i)), CGItems.copydust);
                    loaded++;
                }
        }

        return loaded;
    }

}
