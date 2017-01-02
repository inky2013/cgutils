package inky2013.cgutils.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class StackedCommand implements ICommand
{
    private final List aliases;
    private final List commands;

    protected String fullEntityName;
    protected Entity conjuredEntity;

    public StackedCommand(String name, String[] cmds)
    {
        aliases = new ArrayList();
        commands = new ArrayList();
        for (int i=0; i<cmds.length;i++) {
            commands.add(cmds[i]);
        }
    }


    public int compareTo(Object o) {
        return 0;
    }


    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] strs, BlockPos pos) {
        return new ArrayList();
    }

    @Override
    public String getCommandName()
    {
        return this.aliases.get(0).toString();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

    }

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return this.aliases.get(0).toString();
    }

    @Override
    public List getCommandAliases()
    {
        return this.aliases;
    }

    //@Override
    public void processCommand(ICommandSender sender, String[] argString)
    {
        World world = sender.getEntityWorld();

        if (world.isRemote)
        {
            System.out.println("Not processing on Client side");
        }
        else
        {
            System.out.println("Processing on Server side");
            if(argString.length == 0)
            {
                sender.addChatMessage(new ChatComponentText("Invalid argument"));
                return;
            }

            sender.addChatMessage(new ChatComponentText("Conjuring: [" + argString[0]
                    + "]"));

            fullEntityName = WildAnimals.MODID+"."+argString[0];
            if (EntityList.stringToClassMapping.containsKey(fullEntityName))
            {
                conjuredEntity = EntityList.createEntityByName(fullEntityName, world);
                conjuredEntity.setPosition(sender.getPlayerCoordinates().posX,
                        sender.getPlayerCoordinates().posY,
                        sender.getPlayerCoordinates().posZ);
                world.spawnEntityInWorld(conjuredEntity);
            }
            else
            {
                sender.addChatMessage(new ChatComponentText("Entity not found"));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender var1)
    {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender var1, String[] var2)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] var1, int var2)
    {
        // TODO Auto-generated method stub
        return false;
    }
}