package tfar.btslogpose.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.client.button.ImageButton;

import java.io.IOException;

public class SelectRegionScreen extends ScaledGuiScreen {

    private static final ResourceLocation BACK = new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/menu/gui_1.png");

    public SelectRegionScreen() {
        super(BACK, 823,456);
    }

    @Override
    public void initGui() {
        super.initGui();

        int backGroundSizeX = (int) (getW() *backGroundScale);
        int backGroundSizeY = (int) (backgroundTextureSizeY * backGroundScale);

        int guiLeft = (this.width - backGroundSizeX) / 2;
        int guiTop = (this.height - backGroundSizeY) / 2;
        int y = guiTop + 141;


        int space = 78;

        int uWidth = 145;int vHeight = 60;
        int bWidth = uWidth/2;
        int bHeight = vHeight/2;

        int xStart = guiLeft + 30;

        addButton(new ImageButton(2,xStart,y,bWidth,bHeight,592,4,uWidth,vHeight,
                background,backgroundTextureSizeX,backgroundTextureSizeY,null));
        addButton(new ImageButton(1,xStart + space,y,bWidth,bHeight,592,68,uWidth,vHeight,
                background,backgroundTextureSizeX,backgroundTextureSizeY,null));
        addButton(new ImageButton(0,xStart + space * 2,y,bWidth,bHeight,592,133,uWidth,vHeight,
                background,backgroundTextureSizeX,backgroundTextureSizeY,null));
        addButton(new ImageButton(EXIT,guiLeft + 270,guiTop,47/2,45/2,740,1,47,45,
                background,backgroundTextureSizeX,backgroundTextureSizeY,null));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: Minecraft.getMinecraft().displayGuiScreen(new RegionScreen("east_blue.json"));break;
            case 1: Minecraft.getMinecraft().displayGuiScreen(new RegionScreen("grand_line.json"));break;
            case 2: Minecraft.getMinecraft().displayGuiScreen(new RegionScreen("new_world.json"));break;
            case EXIT:Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    @Override
    public int getW() {
        return 585;
    }

}
