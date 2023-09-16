package tfar.btslogpose.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
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
    public String discovered_icon;
    public String command_on_track;
    public String command_on_untrack;
    public String command_on_discovery;
    public AABB discovery;


    private static final Logger LOGGER = LogManager.getLogger();

    private void makeDefaultCommands(String region,String island) {
        command_on_track = "/btsping track %player "+island;
        command_on_untrack = "/btsping untrack %player "+island;
        command_on_discovery = "/btsisland discover "+region+" "+island+" %player";
    }

    private static final String[] default_east_blue_islands = new String[]{"foosha","shelltown","orangetown","alvida_hideout","arlong_park","baratie","loguetown","syrup"};
    private static final String[] default_grand_line_islands = new String[]{"alabasta","amazon_lily","drum_island","drum_island_2","enies_lobby",
            "impeldown","laboon","little_garden","long_ring_long_land","marineford","saboady","thriller_bark","water_7","whiskey_peak"};
    private static final String[] default_new_world_islands = new String[]{"dressrosa","fishman_island","mary_geoise","punk_hazard","whole_cake","zou"};


    private static BTSIslandConfig makeDefaultIsland(String region,String island) {
        BTSIslandConfig btsIslandConfig = new BTSIslandConfig();
        btsIslandConfig.discovered_icon = new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/island/"+region+"/"+island+".png").toString();
        btsIslandConfig.discovery = new AABB(0,0,0,64,64,64).offset(0,192,0);
        btsIslandConfig.makeDefaultCommands(region,island);
        return btsIslandConfig;
    }


    private static final List<File> FILES;
    static final Map<String,String[]> defs;


    static  {

        FILES = new ArrayList<>();
        defs = new HashMap<>();
        new File("config/btsregions/").mkdir();
        for (String s : BTSLogPose.REGIONS) {
            File file = new File("config/btsregions/" + s + ".json");
            FILES.add(file);
        }
        defs.put("east_blue",default_east_blue_islands);
        defs.put("grand_line",default_grand_line_islands);
        defs.put("new_world",default_new_world_islands);

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

    public static Map<String, Map<String, BTSIslandConfig>> read() {
        Map<String, Map<String, BTSIslandConfig>> mapMap = new HashMap<>();
        for (File file : FILES) {
            String filename = file.getName();
            if (!file.exists()) {
                Map<String, BTSIslandConfig> list = new HashMap<>();
                String r = filename.replace(".json", "");

                    String[] strings = defs.get(r);
                    for (String s : strings) {
                        list.put(s, makeDefaultIsland(r, s));
                    }

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
                mapMap.put(filename,config);
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
