package tfar.btslogpose.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.client.button.ArrowButton;
import tfar.btslogpose.client.button.ImageButton;
import tfar.btslogpose.client.button.TrackingButton;
import tfar.btslogpose.config.BTSIslandConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegionScreen extends ScaledGuiScreen {

    private final String region;
    private final SelectRegionScreen lastScreen;
    private static final ResourceLocation BACK = new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/menu/gui_2.png");


    private final List<Pair<String, BTSIslandConfig>> islandConfigMap;

    public RegionScreen(String region,SelectRegionScreen lastScreen) {
        super(BACK, 893,456);
        this.region = region;
        this.lastScreen = lastScreen;
        islandConfigMap = new ArrayList<>();
        BTSLogPoseClient.client_configs.get(region).entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).forEach(islandConfigMap::add);
    }

    int index = 0;

    private GuiButton[] trackingButtons = new GuiButton[3];
    private ImageButton[] imageButtons = new ImageButton[3];

    private static final int LEFT_ARROW = 0x10000;
    private static final int RIGHT_ARROW = 0x10001;


    @Override
    public void initGui() {
        super.initGui();

        int backGroundSizeX = (int) (getW() *backGroundScale);
        int backGroundSizeY = (int) (backgroundTextureSizeY * backGroundScale);

        int guiLeft = (this.width - backGroundSizeX) / 2;
        int guiTop = (this.height - backGroundSizeY) / 2;

        mapButtons(guiLeft,guiTop);

        addButton(new ArrowButton(LEFT_ARROW,guiLeft+59,guiTop+169,118/2,85/2,583,282,118,85,background,backgroundTextureSizeX,backgroundTextureSizeY,null));
        addButton(new ArrowButton(RIGHT_ARROW,guiLeft+173,guiTop+169,118/2,85/2,583,197,118,85,background,backgroundTextureSizeX,backgroundTextureSizeY,null));
        addButton(new ImageButton(EXIT,guiLeft + 270,guiTop,47/2,45/2,740,1,47,45,
                background,backgroundTextureSizeX,backgroundTextureSizeY,null));
    }

    private void mapButtons(int guiLeft,int guiTop) {
            for (int i = 0; i < 3;i++) {
                int ind = index + i;

                if (ind < islandConfigMap.size()) {
                    Pair<String, BTSIslandConfig> pair = islandConfigMap.get(ind);
                    String islandName = pair.getLeft();
                    BTSIslandConfig config = pair.getRight();
                    trackingButtons[i] = new TrackingButton(i, guiLeft + 28 + 77 * i, guiTop + 132, 150/2, 74/2,
                            736,39,150,74,background,backgroundTextureSizeX,backgroundTextureSizeY ,null,region,islandName);

                    List<String> discs = BTSLogPoseClient.discovered.get(region);
                    ResourceLocation image = discs != null && discs.contains(islandName) ? new ResourceLocation(config.discovered_icon) : new ResourceLocation(config.undiscovered_icon);

                    imageButtons[i] = new ImageButton(i, 32 + guiLeft + i * 79, guiTop + 61, 64, 64, 0, 0, image, 512, 512,
                            "btslogpose.island." + islandName + ".name").setSilent();

                    addButton(imageButtons[i]);
                    addButton(trackingButtons[i]);
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
                        index--;
                    }break;
                case RIGHT_ARROW:if (index + 3 < islandConfigMap.size()) {
                    index++;
                }break;
                case EXIT:mc.displayGuiScreen(lastScreen);return;
            }

            if (islandConfigMap.size() < 4)return;

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

    @Override
    protected void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundLayer(partialTicks, mouseX, mouseY);
        int u = 592;int v = 122;//right
        int u2 = 639; int v2 = 123;//left
        int w1= 36; int h = 55;//uv size
        int width = w1/2;
        int height = h/2;

        int w = getW();

        int backGroundSizeX = (int) (w *backGroundScale);
        int backGroundSizeY = (int) (backgroundTextureSizeY * backGroundScale);

        int i = (this.width - backGroundSizeX) / 2;
        int j = (this.height - backGroundSizeY) / 2;

        GlStateManager.enableBlend();

        if (index > 0) {//left
            drawScaledCustomSizeModalRect(i+7, j+80,  u2, v2,
                    w1, h,(int) (w1 * backGroundScale), (int) (h * backGroundScale), backgroundTextureSizeX, backgroundTextureSizeY);
        }

        if (index < islandConfigMap.size() - 3) {//right
            drawScaledCustomSizeModalRect(i+260, j+80,  u, v,
                    w1, h,(int) (w1 * backGroundScale), (int) (h * backGroundScale), backgroundTextureSizeX, backgroundTextureSizeY);
        }
    }

    @Override
    protected void drawForegroundLayer(int mouseX, int mouseY, float partialTicks) {
        super.drawForegroundLayer(mouseX, mouseY, partialTicks);

        RenderHelper.disableStandardItemLighting();

        for (GuiButton guibutton : this.buttonList)
        {
            if (guibutton.isMouseOver())
            {
                guibutton.drawButtonForegroundLayer(mouseX, mouseY);
                break;
            }
        }

        RenderHelper.enableGUIStandardItemLighting();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            mc.displayGuiScreen(lastScreen);
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }
}
