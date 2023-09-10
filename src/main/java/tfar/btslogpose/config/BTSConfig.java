package tfar.btslogpose.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.btslogpose.BTSLogPose;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BTSConfig {
    public boolean enabled = true;
    public String undiscovered_icon = "undiscovered";
    public String discovered_icon = "discovered";

    private static final Logger LOGGER = LogManager.getLogger();

    private static final File FILE = new File("config/"+ BTSLogPose.MOD_ID +".json");

    public static List<BTSConfig> read() {
        if (!FILE.exists()) {
            List<BTSConfig> list = new ArrayList<>();
            list.add(new BTSConfig());
            list.add(new BTSConfig());
            write(list);
            return list;
        }

        Reader reader = null;
        try {

            reader = new FileReader(FILE);

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<BTSConfig>>(){}.getType();

            return gson.<ArrayList<BTSConfig>>fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static void write(List<BTSConfig> configs) {
        Gson gson = new Gson();
        JsonWriter writer = null;
        try {
            writer = gson.newJsonWriter(new FileWriter(FILE));
            writer.setIndent("    ");

            gson.toJson(gson.toJsonTree(configs.toArray(configs.toArray(new BTSConfig[0])), BTSConfig[].class), writer);
        } catch (Exception e) {
            LOGGER.error("Couldn't save config");
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}
