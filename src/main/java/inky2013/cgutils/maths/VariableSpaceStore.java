package inky2013.cgutils.maths;

import li.cil.repack.org.luaj.vm2.ast.Str;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan Brews on 06/07/2017.
 */
public class VariableSpaceStore {

    private static Map<String,VariableSpace> varspaces = new HashMap<String, VariableSpace>();
    private static Map<UUID,String> playerVarspaces = new HashMap<UUID, String>();

    public static VariableSpace getSpace(String key) {
        return varspaces.get(key);
    }

    public static VariableSpace addSpace(String key) {
        VariableSpace vs = new VariableSpace();
        varspaces.put(key, vs);
        return vs;
    }

    public static void setPlayerVarspace(UUID uuid, String space) {
        playerVarspaces.put(uuid, space);
    }

    public static VariableSpace getPlayerVarspace(UUID uuid) {
        VariableSpace vs = varspaces.get(playerVarspaces.get(uuid));
        if (vs != null) {
            return vs;
        }
        vs = addSpace(uuid.toString());
        setPlayerVarspace(uuid, uuid.toString());
        return vs;
    }

}
