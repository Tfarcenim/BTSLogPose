package tfar.btslogpose.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;

public class ImageButton extends GuiButton {
    private final ResourceLocation resourceLocation;
    private final int textureSizeX;
    private final int textureSizeY;
    private final String tooltipComponent;
    private final int u;
    private final int v;

    public ImageButton(int buttonId, int xIn, int yIn, int widthIn, int heightIn, int u, int v, ResourceLocation resource, int textureSizeX, int textureSizeY, String tooltipComponent) {
        super(buttonId, xIn, yIn, widthIn, heightIn, "");
        this.u = u;
        this.v = v;
        this.resourceLocation = resource;
        this.textureSizeX = textureSizeX;
        this.textureSizeY = textureSizeY;
        this.tooltipComponent = tooltipComponent;
    }

    public void setPosition(int p_191746_1_, int p_191746_2_) {
        this.x = p_191746_1_;
        this.y = p_191746_2_;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            mc.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.disableDepth();



            drawScaledCustomSizeModalRect(x, y, u, v, textureSizeX, textureSizeY, width, height, textureSizeX, textureSizeY);

            RenderHelper.disableStandardItemLighting();

            if (hovered) {
                GuiScreen screen = Minecraft.getMinecraft().currentScreen;
                if (screen != null) {
                    screen.drawHoveringText(I18n.format(tooltipComponent), mouseX, mouseY);
                }
            }

            RenderHelper.enableGUIStandardItemLighting();

            // this.drawTexturedModalRect(this.x, this.y, this.u, this.v, this.width, this.height);
            GlStateManager.enableDepth();
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        //super.playPressSound(soundHandlerIn);
    }
}