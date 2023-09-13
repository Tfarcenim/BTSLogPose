package tfar.btslogpose.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public abstract class ScaledGuiScreen extends GuiScreen {

    protected final static int EXIT = 999;
    protected double backGroundScale = .5;
    protected final ResourceLocation background;
    protected final int backgroundTextureSizeX;
    protected final int backgroundTextureSizeY;

    public ScaledGuiScreen(ResourceLocation background, int backgroundTextureSizeX, int backgroundTextureSizeY) {
        this.background = background;
        this.backgroundTextureSizeX = backgroundTextureSizeX;
        this.backgroundTextureSizeY = backgroundTextureSizeY;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawBackgroundLayer(partialTicks, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public int getW() {
        return 580;
    }


    protected void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int w = getW();

        int backGroundSizeX = (int) (w *backGroundScale);
        int backGroundSizeY = (int) (backgroundTextureSizeY * backGroundScale);

        int i = (this.width - backGroundSizeX) / 2;
        int j = (this.height - backGroundSizeY) / 2;
        drawScaledCustomSizeModalRect(i, j, 0, 0, w,backgroundTextureSizeY, backGroundSizeX, backGroundSizeY,backgroundTextureSizeX,backgroundTextureSizeY);
    }
}
