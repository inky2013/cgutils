package inky2013.cgutils.maths;

import inky2013.cgutils.utils.PlayerHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.objecthunter.exp4j.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by Ethan Brews on 06/07/2017.
 */
public class MathsCommandExecutor {

    private static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public static void execute(ICommandSender sender, String toEvaluate) {

        VariableSpace variableSpace = VariableSpaceStore.getPlayerVarspace(PlayerHelper.getUUIDFromPlayer((EntityPlayer) sender));

        String[] vars = {};
        List<String> notset = new ArrayList<String>();
        // sender.addChatMessage(new TextComponentString("Variable \""+String.valueOf(c)+"\" is not set"));

        if (StringUtils.contains(toEvaluate, "=")) {
            String[] temp = toEvaluate.split("=");
            toEvaluate = temp[1];
            vars = temp[0].split(",");
            sender.addChatMessage(new TextComponentString(vars.toString()));
        }

        Set<String> inEquation = new HashSet<String>();
        Map<String, Double> variables = new HashMap<String, Double>();

        for (char c : alphabet) {
            if (StringUtils.contains(toEvaluate, c)) {
                inEquation.add(String.valueOf(c));
                Double varval = variableSpace.getVar(c);
                if (varval == null) {
                    notset.add(String.valueOf(c));
                } else {
                    variables.put(String.valueOf(c), varval);
                }

            }
        }

        Expression e = new ExpressionBuilder(toEvaluate).variables(inEquation).build().setVariables(variables);

        Double value;
        try {
            value = e.evaluate();
        } catch (IllegalArgumentException err) {
            sender.addChatMessage(new TextComponentString(err.getMessage()));
            return;
        }


        if (vars.length > 0) {
            for (String s : vars) {
                variableSpace.setVar(s.charAt(0), value);
            }
            sender.addChatMessage(new TextComponentString(String.join(", ", vars)+" = "+toEvaluate+" = "+String.valueOf(value)));
        } else {
            sender.addChatMessage(new TextComponentString(toEvaluate+" = "+String.valueOf(value)));
        }

    }

}
