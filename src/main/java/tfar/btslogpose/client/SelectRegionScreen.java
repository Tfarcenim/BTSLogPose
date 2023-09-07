package tfar.btslogpose.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tfar.btslogpose.BTSLogPose;

import java.io.IOException;

public class SelectRegionScreen extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawBackgroundLayer(partialTicks, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
        int guiLeft = (this.width - 823/3) / 2;
        int guiTop = (this.height - 585/3) / 2;
        addButton(new GuiButton(0,guiLeft+56,guiTop+115,50,20,"test"));
        addButton(new GuiButton(0,guiLeft+56 + 53,guiTop+115,50,20,"test"));
        addButton(new GuiButton(0,guiLeft+56 + 53 * 2,guiTop+115,50,20,"test"));

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void drawBackground(int tint) {
        super.drawBackground(tint);
    }


    protected void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/menu/gui_1.png"));
        int texWidth = 823;
        int texHeight = 456;
        int w = 585;

        int i = (this.width - w/3) / 2;
        int j = (this.height - texHeight/3) / 2;
        drawScaledCustomSizeModalRect(i, j, 0, 0, w,texHeight, w/3, texHeight/3,texWidth,texHeight);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
