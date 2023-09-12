package tfar.btslogpose.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.config.BTSIslandConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegionScreen extends ScaledGuiScreen {

    private final String region;
    private static final ResourceLocation BACK = new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/menu/gui_2.png");


    private final List<Pair<String, BTSIslandConfig>> islandConfigMap;

    public RegionScreen(String region) {
        super(BACK, 823,456);
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

        addButton(new GuiButton(LEFT_ARROW,guiLeft+80,guiTop+170,40,20,"<"));
        addButton(new GuiButton(RIGHT_ARROW,guiLeft+180,guiTop+170,40,20,">"));

    }

    private void mapButtons(int guiLeft,int guiTop) {
        int islandCount = islandConfigMap.size();
        if (islandCount < 4) {
            for (int i = 0; i < islandConfigMap.size();i++) {
                Pair<String,BTSIslandConfig> pair = islandConfigMap.get(i);
                String name = pair.getLeft();
                BTSIslandConfig config = pair.getRight();
                trackingButtons[i] = new GuiButton(i,guiLeft+50 + 66 * i,guiTop+125,20,20,"");
                images[i] = BTSLogPoseClient.discovered.contains(name) ? new ResourceLocation(config.discovered_icon) : new ResourceLocation(config.undiscovered_icon);
                addButton(trackingButtons[i]);
            }
        } else {

        }
    }



    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case RIGHT_ARROW:
        }
    }
    protected void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundLayer(partialTicks, mouseX, mouseY);

        int w = getW();

        int backGroundSizeX = (int) (w *backGroundScale);
        int backGroundSizeY = (int) (backgroundTextureSizeY * backGroundScale);

        int i = (this.width - backGroundSizeX) / 2;
        int j = (this.height - backGroundSizeY) / 2;

        for (int i1 = 0 ; i1 < 3;i1++) {
            ResourceLocation resourceLocation = images[i1];
            if (resourceLocation != null) {
                this.mc.getTextureManager().bindTexture(resourceLocation);
                int iconSize = 512;
                int screenSize = 64;
                drawScaledCustomSizeModalRect(27+ i + i1  * 74, j+52, 0, 0, iconSize,iconSize, screenSize, screenSize,iconSize,iconSize);
            }
        }
    }
}
