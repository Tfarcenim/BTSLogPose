package tfar.btslogpose.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ImageButton extends GuiButton {
    protected final int uWidth;
    protected final int vHeight;
    protected final ResourceLocation resourceLocation;
    protected final int textureSizeX;
    protected final int textureSizeY;

    @Nullable
    private final String tooltipComponent;
    protected int u;
    protected int v;

    public ImageButton(int buttonId, int xIn, int yIn, int widthIn, int heightIn, int u, int v, ResourceLocation resource, int textureSizeX, int textureSizeY, String tooltipComponent) {
        this(buttonId, xIn,yIn,widthIn,heightIn,u,v,textureSizeX,textureSizeY,resource,textureSizeX,textureSizeY,tooltipComponent);
    }

    public ImageButton(int buttonId, int xIn, int yIn, int widthIn, int heightIn, int u, int v, int uWidth,int vHeight,ResourceLocation resource, int textureSizeX, int textureSizeY, String tooltipComponent) {
        super(buttonId, xIn, yIn, widthIn, heightIn, "");
        this.u = u;
        this.v = v;
        this.uWidth = uWidth;
        this.vHeight = vHeight;
        this.resourceLocation = resource;
        this.textureSizeX = textureSizeX;
        this.textureSizeY = textureSizeY;
        this.tooltipComponent = tooltipComponent;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            mc.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.disableDepth();

            GlStateManager.enableBlend();

            drawScaledCustomSizeModalRect(x, y, u, v, uWidth,vHeight, width, height, textureSizeX, textureSizeY);



            GlStateManager.enableDepth();
        }
    }

    @Override
    public void drawButtonForegroundLayer(int mouseX, int mouseY) {
        super.drawButtonForegroundLayer(mouseX, mouseY);
        if (tooltipComponent != null) {
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (screen != null) {
                screen.drawHoveringText(I18n.format(tooltipComponent), mouseX, mouseY);
            }
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        //super.playPressSound(soundHandlerIn);
    }
}