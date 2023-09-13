package tfar.btslogpose.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;
import tfar.btslogpose.net.C2SToggleTrackingPacket;
import tfar.btslogpose.net.PacketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegionScreen extends ScaledGuiScreen {

    private final String region;
    private static final ResourceLocation BACK = new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/menu/gui_2.png");


    private final List<Pair<String, BTSIslandConfig>> islandConfigMap;

    public RegionScreen(String region) {
        super(BACK, 893,456);
        this.region = region;
        islandConfigMap = new ArrayList<>();
        BTSLogPose.configs.get(region).entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).forEach(islandConfigMap::add);
    }

    int index = 0;

    private GuiButton[] trackingButtons = new GuiButton[3];
    private ImageButton[] imageButtons = new ImageButton[3];

    private static final int LEFT_ARROW = 0x10000;
    private static final int RIGHT_ARROW = 0x10001;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        
    }

    @Override
    public void initGui() {
        super.initGui();

        int backGroundSizeX = (int) (getW() *backGroundScale);
        int backGroundSizeY = (int) (backgroundTextureSizeY * backGroundScale);

        int guiLeft = (this.width - backGroundSizeX) / 2;
        int guiTop = (this.height - backGroundSizeY) / 2;

        mapButtons(guiLeft,guiTop);

        addButton(new GuiButton(LEFT_ARROW,guiLeft+80,guiTop+170,40,20,"<"));
        addButton(new GuiButton(RIGHT_ARROW,guiLeft+180,guiTop+170,40,20,">"));
    }

    private void mapButtons(int guiLeft,int guiTop) {
        int islandCount = islandConfigMap.size();
        if (islandCount < 4) {
            for (int i = 0; i < islandConfigMap.size();i++) {
                Pair<String,BTSIslandConfig> pair = islandConfigMap.get(i);
                String islandName = pair.getLeft();
                BTSIslandConfig config = pair.getRight();
                trackingButtons[i] = new TrackingButton(i,guiLeft+44 + 76 * i,guiTop+135,60,20,"",() -> {
                    boolean currentlyTracked = BTSLogPoseClient.isIslandTracked(region,islandName);
                    PacketHandler.sendPacketToServer(new C2SToggleTrackingPacket(region,islandName,currentlyTracked));
                    if (currentlyTracked) {
                        BTSLogPoseClient.unTrackIsland(region, islandName);
                    } else {
                        BTSLogPoseClient.trackIsland(region, islandName);
                    }
                });

                List<String> discs = BTSLogPoseClient.discovered.get(region);
                ResourceLocation image = discs != null && discs.contains(islandName) ? new ResourceLocation(config.discovered_icon) : new ResourceLocation(config.undiscovered_icon);

                imageButtons[i] = new ImageButton(i,32+ guiLeft + i  * 79, guiTop+61,64,64,0,0,image,512,512,
                        "btslogpose.island."+islandName+".name");

                addButton(imageButtons[i]);
                addButton(trackingButtons[i]);
            }
        } else {
            for (int i = 0; i < 3;i++) {
                int ind = index + i;

                if (ind < islandConfigMap.size()) {
                    Pair<String, BTSIslandConfig> pair = islandConfigMap.get(ind);
                    String islandName = pair.getLeft();
                    BTSIslandConfig config = pair.getRight();
                    trackingButtons[i] = new TrackingButton(i, guiLeft + 44 + 76 * i, guiTop + 135, 60, 20, "", () -> {
                        boolean currentlyTracked = BTSLogPoseClient.isIslandTracked(region, islandName);
                        PacketHandler.sendPacketToServer(new C2SToggleTrackingPacket(region, islandName, currentlyTracked));
                        if (currentlyTracked) {
                            BTSLogPoseClient.unTrackIsland(region, islandName);
                        } else {
                            BTSLogPoseClient.trackIsland(region, islandName);
                        }
                    });

                    List<String> discs = BTSLogPoseClient.discovered.get(region);
                    ResourceLocation image = discs != null && discs.contains(islandName) ? new ResourceLocation(config.discovered_icon) : new ResourceLocation(config.undiscovered_icon);

                    imageButtons[i] = new ImageButton(i, 32 + guiLeft + i * 79, guiTop + 61, 64, 64, 0, 0, image, 512, 512,
                            "btslogpose.island." + islandName + ".name");

                    addButton(imageButtons[i]);
                    addButton(trackingButtons[i]);
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button instanceof TrackingButton) {
            TrackingButton trackingButton = (TrackingButton) button;
            trackingButton.press.run();
        } else {
            switch (button.id) {
                case LEFT_ARROW:
                    if (index >0) {
                        index--;break;
                    }
                case RIGHT_ARROW:if (index + 3 < islandConfigMap.size()) {
                    index++;break;
                }
            }
            for (int i = 0; i < 3;i++) {
                buttonList.remove(imageButtons[i]);
                buttonList.remove(trackingButtons[i]);
            }

            int backGroundSizeX = (int) (getW() *backGroundScale);
            int backGroundSizeY = (int) (backgroundTextureSizeY * backGroundScale);

            int guiLeft = (this.width - backGroundSizeX) / 2;
            int guiTop = (this.height - backGroundSizeY) / 2;

            mapButtons(guiLeft,guiTop);

        }
    }
}
