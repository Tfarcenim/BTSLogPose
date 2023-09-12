package tfar.btslogpose.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegionScreen extends GuiScreen {

    private final String region;

    private final List<Pair<String, BTSIslandConfig>> islandConfigMap;

    public RegionScreen(String region) {
        this.region = region;
        islandConfigMap = new ArrayList<>();
        BTSLogPose.configs.get(region).entrySet().stream().map(entry -> Pair.of(entry.getKey(), entry.getValue())).forEach(islandConfigMap::add);
    }

    int index = 0;

    private GuiButton[] trackingButtons = new GuiButton[3];
    private ResourceLocation[] images = new ResourceLocation[3];

    private static final int LEFT_ARROW = 0x10000;
    private static final int RIGHT_ARROW = 0x10001;
    @Override
    public void initGui() {
        super.initGui();

        int guiLeft = (this.width - 823/3) / 2;
        int guiTop = (this.height - 585/3) / 2;

        mapButtons(guiLeft,guiTop);

        addButton(new GuiButton(LEFT_ARROW,guiLeft+90,guiTop+140,20,20,""));
        addButton(new GuiButton(RIGHT_ARROW,guiLeft+160,guiTop+140,20,20,""));

    }

    private void mapButtons(int guiLeft,int guiTop) {
        int islandCount = islandConfigMap.size();
        if (islandCount < 4) {
            for (int i = 0; i < islandConfigMap.size();i++) {
                Pair<String,BTSIslandConfig> pair = islandConfigMap.get(i);
                String name = pair.getLeft();
                BTSIslandConfig config = pair.getRight();
                trackingButtons[i] = new GuiButton(i,guiLeft+90,guiTop+140,20,20,"");
                images[i] = BTSLogPoseClient.discovered.contains(name) ? new ResourceLocation(config.discovered_icon) : new ResourceLocation(config.undiscovered_icon);
                addButton(trackingButtons[i]);
            }
        } else {

        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawBackgroundLayer(partialTicks, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case RIGHT_ARROW:
        }
    }
    protected void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/menu/gui_2.png"));
        int texWidth = 823;
        int texHeight = 456;
        int w = 531;

        double backScale = .50;
        int backGroundSizeX = (int) (w *backScale);
        int backGroundSizeY = (int) (texHeight * backScale);

        int i = (this.width - backGroundSizeX) / 2;
        int j = (this.height - backGroundSizeY) / 2;
        drawScaledCustomSizeModalRect(i, j, 0, 0, w,texHeight, backGroundSizeX, backGroundSizeY,texWidth,texHeight);

        for (int i1 = 0 ; i1 < 3;i1++) {
            ResourceLocation resourceLocation = images[i1];
            if (resourceLocation != null) {
                this.mc.getTextureManager().bindTexture(resourceLocation);
                int iconSize = 512;
                int screenSize = 64;
                drawScaledCustomSizeModalRect(18+ i + i1  * 64, j+35, 0, 0, iconSize,iconSize, screenSize, screenSize,iconSize,iconSize);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
