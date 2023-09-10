package tfar.btslogpose.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.math.AxisAlignedBB;
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

public class BTSIslandConfig {
    public String registry_name = "btsisland";
    public String undiscovered_icon = "undiscovered.png";
    public String discovered_icon = "discovered.png";
    public String translation_key = "btslogpose.island.undefined.name";

    public String run_command_if_undiscovered = "/btslogpose unlock_island btsisland";

    public AxisAlignedBB discovery = new AxisAlignedBB(0,0,0,64,64,64);


    private static final Logger LOGGER = LogManager.getLogger();

    private static final File FILE = new File("config/"+ BTSLogPose.MOD_ID +".json");

    public static List<BTSIslandConfig> read() {
        if (!FILE.exists()) {
            List<BTSIslandConfig> list = new ArrayList<>();
            list.add(new BTSIslandConfig());
            list.add(new BTSIslandConfig());
            write(list);
            LOGGER.info("Loading default config");
            return list;
        }

        Reader reader = null;
        try {

            reader = new FileReader(FILE);

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<BTSIslandConfig>>(){}.getType();

            LOGGER.info("Loading existing config");
            return gson.<ArrayList<BTSIslandConfig>>fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static void write(List<BTSIslandConfig> configs) {
        Gson gson = new Gson();
        JsonWriter writer = null;
        try {
            writer = gson.newJsonWriter(new FileWriter(FILE));
            writer.setIndent("    ");

            gson.toJson(gson.toJsonTree(configs.toArray(configs.toArray(new BTSIslandConfig[0])), BTSIslandConfig[].class), writer);
        } catch (Exception e) {
            LOGGER.error("Couldn't save config");
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}
