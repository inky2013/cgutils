package inky2013.cgutils.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by ethan on 03/01/2017.
 */
public class CGItems {

    public static CGItem enderPowder;
    public static CGLoreItem copydust;
    public static CGLoreItem hammer;

    public static void init() {
        enderPowder = register(new CGItem("enderPowder"));
        copydust = register(new CGLoreItem("copydust", new String[]{"It's magical"}));
        hammer = register(new CGLoreItem("hammer", new String[]{"Useful for smashing stuff"}));
    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof CGItem) {
            ((CGItem)item).registerItemModel();
        }

        return item;
    }

}
