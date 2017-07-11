package inky2013.cgutils.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import inky2013.cgutils.CGUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by Ethan Brews on 01/07/2017.
 */
public class URLDataLoader {

    public static URLData getUrl(JsonObject o) {
        Gson gson = new Gson();
        return gson.fromJson(o, URLData.class);
    }

}
