package tfar.btslogpose.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.btslogpose.BTSLogPose;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class BTSIslandConfig {
    public String undiscovered_icon = "undiscovered.png";
    public String discovered_icon = "discovered.png";
    transient public String translation_key;

    public String run_command_if_undiscovered = "/btslogpose unlock_island btsisland";

    public AxisAlignedBB discovery = new AxisAlignedBB(0,0,0,64,64,64);


    private static final Logger LOGGER = LogManager.getLogger();

    private static final File FILE = new File("config/"+ BTSLogPose.MOD_ID +".json");

    public void toNetwork(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf,undiscovered_icon);
        ByteBufUtils.writeUTF8String(buf,discovered_icon);
    }

    public static BTSIslandConfig fromNetwork(ByteBuf buf) {
        BTSIslandConfig btsIslandConfig = new BTSIslandConfig();
        btsIslandConfig.undiscovered_icon = ByteBufUtils.readUTF8String(buf);
        btsIslandConfig.discovered_icon = ByteBufUtils.readUTF8String(buf);
        return btsIslandConfig;
    }

    public String createTranslationKey(String registry_name) {
        if (translation_key == null) {
            translation_key = "btslogpose.island."+registry_name+".name";
        }
        return translation_key;
    }

    public static Map<String,BTSIslandConfig> read() {
        if (!FILE.exists()) {
            Map<String,BTSIslandConfig> list = new HashMap<>();
            list.put("btsisland1",new BTSIslandConfig());
            list.put("btsisland2",new BTSIslandConfig());
            write(list);
            LOGGER.info("Loading default config");
            return list;
        }

        Reader reader = null;
        try {

            reader = new FileReader(FILE);

            JsonReader jsonReader = new JsonReader(reader);

            Gson gson = new Gson();

           // Type listType = new TypeToken<ArrayList<BTSIslandConfig>>(){}.getType();

            LOGGER.info("Loading existing config");

            JsonObject jsonObject = gson.fromJson(jsonReader,JsonObject.class);

            Map<String,BTSIslandConfig> config = new HashMap<>();

            for (Map.Entry<String,JsonElement> entry : jsonObject.entrySet()) {
                JsonElement jsonElement = entry.getValue();
                BTSIslandConfig btsIslandConfig = gson.fromJson(jsonElement, BTSIslandConfig.class);
                config.put(entry.getKey(),btsIslandConfig);
            }
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    public static void write(Map<String,BTSIslandConfig> configs) {
        Gson gson = new Gson();
        JsonWriter writer = null;
        try {
            writer = gson.newJsonWriter(new FileWriter(FILE));
            writer.setIndent("    ");

            JsonObject object = new JsonObject();

            for (Map.Entry<String,BTSIslandConfig> entry : configs.entrySet()) {
                String name = entry.getKey();
                BTSIslandConfig config = entry.getValue();
                JsonElement jsonElement = gson.toJsonTree(config);
                object.add(name,jsonElement);
            }

            gson.toJson(object, writer);
        } catch (Exception e) {
            LOGGER.error("Couldn't save config");
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }
}
