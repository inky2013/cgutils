package inky2013.cgutils.utils;

import inky2013.cgutils.commands.StackedCommand;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ethan on 22/01/2017.
 */
public class CommandStackRegistry {

    private List<StackedCommand> stackedCommands = new ArrayList<StackedCommand>();

    public List<StackedCommand> getStacks() {
        return this.stackedCommands;
    }

    public List<String> reloadStack() {
        CommandStackData[] stacks = CommandStackReader.getStackedCommandsFromFile();
        List<String> newStacks = new ArrayList<String>();
        for (int i=0;i<stacks.length;i++) {
            boolean found = false;
            for (int j=0;j<this.stackedCommands.size();j++) {
                if (this.stackedCommands.get(j).getCommandName().equals(stacks[i].name)) {
                    CGLogger.info("Stack Exists");
                    this.stackedCommands.get(j).updateData(stacks[i].commands, stacks[i].requiresop, stacks[i].requiresExactArgs, stacks[i].argsAmt, stacks[i].executeAsServer);
                    found = true;
                }
            }
            if (found) {
                continue;
            }
            newStacks.add(stacks[i].name);
        }
        return newStacks;
    }
    public void registerCommands(FMLServerStartingEvent event) {
        CommandStackData[] stacks = CommandStackReader.getStackedCommandsFromFile();
        for (int i=0;i<stacks.length;i++) {
            StackedCommand x = new StackedCommand(stacks[i].name, stacks[i].commands, stacks[i].requiresop, stacks[i].requiresExactArgs, stacks[i].argsAmt, stacks[i].executeAsServer);
            event.registerServerCommand(x);
            stackedCommands.add(x);
        }
    }

}
