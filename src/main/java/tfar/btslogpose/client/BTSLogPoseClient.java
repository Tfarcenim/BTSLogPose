package tfar.btslogpose.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tfar.btslogpose.world.BTSPing;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;


@Mod.EventBusSubscriber(Side.CLIENT)
public class BTSLogPoseClient {

    private static List<BTSPing> pings = new ArrayList<>();
    public static Map<String,List<String>> discovered = new HashMap<>();
    public static Map<String, Set<String>> trackedIslands = new HashMap<>();

    public static void setPings(List<BTSPing> pings) {
        BTSLogPoseClient.pings = pings;
    }

    public static void setDiscoveries(String region,List<String> discovered) {
        BTSLogPoseClient.discovered.put(region,discovered);
    }



    public static void trackIsland(String region,String island) {
        if (!trackedIslands.containsKey(region)) {
            trackedIslands.put(region,new HashSet<>());
        }

        java.util.Set<String> islands = trackedIslands.get(region);
        islands.add(island);
        saveTrackedToFile();
    }

    public static void unTrackIsland(String region,String island) {
        if (!trackedIslands.containsKey(region)) {
            return;
        }

        Set<String> islands = trackedIslands.get(region);
        islands.remove(island);
        saveTrackedToFile();
    }

    private static final Logger LOGGER = LogManager.getLogger();

    public static void saveTrackedToFile() {
        Pair<String, Boolean> serverName = getServerName();
        String saveName = serverName.getLeft() + (serverName.getRight() ? "-integrated" : "-dedicated");
        new File("tracked-islands/").mkdir();
        File file = new File("tracked-islands/"+saveName+".json");

        Gson gson = new Gson();
        JsonWriter writer = null;
        try {
            writer = gson.newJsonWriter(new FileWriter(file));
            writer.setIndent("    ");

            JsonObject object = new JsonObject();

            for (Map.Entry<String, Set<String>> entry : trackedIslands.entrySet()) {
                String regionName = entry.getKey();
                Set<String> islands = entry.getValue();
                JsonElement jsonElement = gson.toJsonTree(islands);
                object.add(regionName,jsonElement);
            }

            gson.toJson(object, writer);
        } catch (Exception e) {
            LOGGER.error("Couldn't save tracked islands");
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static void loadTrackedFromFile(Pair<String, Boolean> serverName) {

        String saveName = serverName.getLeft() + (serverName.getRight() ? "-integrated" : "-dedicated");
        File file = new File("tracked-islands/"+saveName+".json");

        trackedIslands.clear();
        if (!file.exists()) {
            LOGGER.info("Didn't find tracked islands for this save");
            return;
        }

            Reader reader = null;
            try {

                reader = new FileReader(file);

                JsonReader jsonReader = new JsonReader(reader);

                Gson gson = new Gson();

                LOGGER.info("Loading tracked islands");

                JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    String region = entry.getKey();
                    JsonElement jsonElement = entry.getValue();
                    Type listOfMyClassObject = new TypeToken<HashSet<String>>() {}.getType();
                    Set<String> islands = gson.fromJson(jsonElement.getAsJsonArray(), listOfMyClassObject);
                    trackedIslands.put(region,islands);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(reader);
            }
    }

    public static Pair<String,Boolean> getServerName() {
        Minecraft minecraft = Minecraft.getMinecraft();
        ServerData serverData = minecraft.getCurrentServerData();
        if (serverData != null) {
            return Pair.of(serverData.serverName,false);
        } else {//singlePlayer
            IntegratedServer integratedServer = minecraft.getIntegratedServer();
            return Pair.of(integratedServer.getWorldName(),true);
        }
    }

    public static boolean isIslandTracked(String region,String island) {
        Set<String> islands = trackedIslands.get(region);
        return islands != null && islands.contains(island);
    }

    public static void openRegionScreen() {
        Minecraft.getMinecraft().displayGuiScreen(new SelectRegionScreen());
    }

    @SubscribeEvent
    public static void renderPings(RenderWorldLastEvent e) {
        for (BTSPing ping : pings) {
            renderTooltip3D(Minecraft.getMinecraft(),ping,e.getPartialTicks());
            Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
        }
    }

    @SubscribeEvent
    public static void joinWorld(EntityJoinWorldEvent e) {
        if (e.getEntity() == Minecraft.getMinecraft().player) {
            Pair<String,Boolean> serverName = getServerName();
            loadTrackedFromFile(serverName);
        }
    }

    public static void renderTooltip3D(Minecraft mc, BTSPing ping,double partialTicks) {
        BlockPos pos = ping.getPos();
        ResourceLocation color = ping.getTex();
        Minecraft.getMinecraft().getTextureManager().bindTexture(color);
        double xpos = mc.getRenderManager().viewerPosX - pos.getX();
        double ypos = mc.getRenderManager().viewerPosY - pos.getY();
        double zpos = mc.getRenderManager().viewerPosZ - pos.getZ();

        final double actualDistance = Minecraft.getMinecraft().player.getDistance(pos.getX(),pos.getY(),pos.getZ());

        double viewDistance = actualDistance;
        final double maxRenderDistance = mc.gameSettings.renderDistanceChunks * 16;
        if (viewDistance > maxRenderDistance) {
            final Vec3d delta =new Vec3d(xpos,ypos,zpos).normalize();
            xpos = delta.x * maxRenderDistance;
            ypos = delta.y * maxRenderDistance;
            zpos = delta.z * maxRenderDistance;
            viewDistance = maxRenderDistance;
        }
        final double maxScale = 1/20f;
        double scaleMultiplier = Math.max(1,actualDistance / maxRenderDistance);
        double scale = maxScale / scaleMultiplier;

        double effectiveScale = maxScale / (Math.min(4,Math.sqrt(scaleMultiplier)));

        GlStateManager.pushMatrix();
        GlStateManager.translate(-xpos, -ypos, -zpos);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY + 180, 0, 1, 0);
        GlStateManager.rotate(-mc.getRenderManager().playerViewX, 1, 0, 0);
        GlStateManager.scale(effectiveScale, -effectiveScale, effectiveScale);
        GlStateManager.disableDepth();
        Gui.drawModalRectWithCustomSizedTexture(0,0,0,0,128,128,128,128);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}
