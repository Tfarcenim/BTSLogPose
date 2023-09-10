package tfar.btslogpose.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tfar.btslogpose.BTSLogPose;

public class RegionScreen extends GuiScreen {

    private final String region;

    public RegionScreen(String region) {
        this.region = region;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawBackgroundLayer(partialTicks, mouseX, mouseY);

    }

    @Override
    public void drawBackground(int tint) {
        super.drawBackground(tint);
    }


    protected void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/menu/gui_2.png"));
        int texWidth = 823;
        int texHeight = 456;
        int w = 528;

        int i = (this.width - w/3) / 2;
        int j = (this.height - texHeight/3) / 2;
        drawScaledCustomSizeModalRect(i, j, 0, 0, w,texHeight, w/3, texHeight/3,texWidth,texHeight);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
