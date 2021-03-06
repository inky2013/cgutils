package inky2013.cgutils.utils;

import com.google.gson.Gson;
import inky2013.cgutils.commands.StackedCommand;
import inky2013.cgutils.commands.UpdateInformation;
import net.minecraft.util.text.TextComponentTranslation;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpdateReader {

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static UpdateInformation[] getUpdatesFromFile() {

        Gson gson = new Gson();

        File stackdirFile = new File("config/cgutils/updates/");

        //TODO Convert text to TextComponentTranslation

        if (!(stackdirFile.exists())) {
            CGLogger.info(String.format("Creating updates directory at '%s'", stackdirFile.getAbsolutePath()));
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

        UpdateInformation[] commandlist = new UpdateInformation[files.length];

        for (int i = 0; i < files.length; i++) {
            try {
                commandlist[i] = gson.fromJson(new FileReader(files[i]), UpdateInformation.class);;
            } catch (IOException e) {
                CGLogger.warn(new TextComponentTranslation("cgutils.fileioexception"));
            }
        }

        return commandlist;
    }

}
