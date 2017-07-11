package inky2013.cgutils.maths;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ethan Brews on 06/07/2017.
 */
public class VariableSpace {
    private Map<Character,Double> variables = new HashMap<Character, Double>();

    public void setVar(char c, Double var) {
        variables.put(c, var);
    }

    public Double getVar(char c) {
        return variables.get(c);
    }
}
