package tfar.btslogpose.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BTSIslandConfig {
    public String undiscovered_icon = new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/island/undiscovered.png").toString();
    public String discovered_icon = "discovered.png";
    transient public String translation_key;
    public AxisAlignedBB discovery = new AxisAlignedBB(0,0,0,64,64,64);


    private static final Logger LOGGER = LogManager.getLogger();

    private static final List<File> FILES;

    static  {

        FILES = new ArrayList<>();
        new File("config/btsregions/").mkdir();
        for (String s : BTSLogPose.REGIONS) {
            File file = new File("config/btsregions/" + s + ".json");
            FILES.add(file);
        }
    }

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

    public static Map<String, Map<String, BTSIslandConfig>> read() {
        Map<String, Map<String, BTSIslandConfig>> mapMap = new HashMap<>();
        for (File file : FILES) {
            if (!file.exists()) {
                Map<String, BTSIslandConfig> list = new HashMap<>();
                list.put("btsisland1", new BTSIslandConfig());
                list.put("btsisland2", new BTSIslandConfig());
                write(list,file);
                LOGGER.info("Loading default config for "+file.getName());
                mapMap.put(file.getName(),list);
                continue;
            }

            Reader reader = null;
            try {

                reader = new FileReader(file);

                JsonReader jsonReader = new JsonReader(reader);

                Gson gson = new Gson();

                // Type listType = new TypeToken<ArrayList<BTSIslandConfig>>(){}.getType();

                LOGGER.info("Loading existing config");

                JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

                Map<String, BTSIslandConfig> config = new HashMap<>();

                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    JsonElement jsonElement = entry.getValue();
                    BTSIslandConfig btsIslandConfig = gson.fromJson(jsonElement, BTSIslandConfig.class);
                    config.put(entry.getKey(), btsIslandConfig);
                }
                mapMap.put(file.getName(),config);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
        return mapMap;
    }

    public static void write(Map<String,BTSIslandConfig> configs,File file) {
        Gson gson = new Gson();
        JsonWriter writer = null;
        try {
            writer = gson.newJsonWriter(new FileWriter(file));
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
