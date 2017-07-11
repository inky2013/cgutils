package inky2013.cgutils.utils;

import com.google.gson.Gson;
import net.minecraft.util.text.TextComponentTranslation;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Created by ethan on 02/01/2017.
 */
public class CommandStackReader {

    public static CommandStackData[] getStackedCommandsFromFile() {

        Gson gson = new Gson();

        File stackdirFile = new File("config/cgutils/stacks/");
        CGLogger.info(stackdirFile.getAbsolutePath());

        //TODO Convert text to TextComponentTranslation

        if (!(stackdirFile.exists())) {
            CGLogger.info(String.format("Creating stacks directory at '%s'", stackdirFile.getAbsolutePath()));
            boolean result = false;
            try {
                stackdirFile.mkdirs();
                result = true;
            } catch (SecurityException se) {
                CGLogger.warn(String.format("Permission was denied accessing '%s'", stackdirFile.getAbsolutePath()));
            }
            if (result) {
                CGLogger.info(String.format("Created '%s'", stackdirFile.getAbsolutePath()));
            }
        }

        File[] files = stackdirFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });

        CommandStackData[] commandlist = new CommandStackData[files.length];

        for (int i = 0; i < files.length; i++) {
            try {
                CommandStackData stk = gson.fromJson(new FileReader(files[i]), CommandStackData.class);
                commandlist[i] = stk;
            } catch (IOException e) {
                CGLogger.warn(new TextComponentTranslation("cgutils.fileioexception"));
            }
        }

        return commandlist;
    }

}
