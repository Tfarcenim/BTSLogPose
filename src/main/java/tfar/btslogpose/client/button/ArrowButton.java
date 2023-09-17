package tfar.btslogpose.client.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import tfar.btslogpose.client.BTSLogPoseClient;
import tfar.btslogpose.net.C2SToggleTrackingPacket;
import tfar.btslogpose.net.PacketHandler;

public class ArrowButton extends ImageButton {

    private final int buttonu;
    private final int buttonv;
    public ArrowButton(int buttonId, int xIn, int yIn, int widthIn, int heightIn, int u, int v, int uWidth, int vHeight,
                       ResourceLocation resource, int textureSizeX, int textureSizeY, String tooltipComponent) {
        super(buttonId, xIn, yIn, widthIn, heightIn, u, v, uWidth, vHeight, resource, textureSizeX, textureSizeY, tooltipComponent);
        this.buttonu = u;
        this.buttonv = v;
        updateUVs(false);
    }

    public void updateUVs(boolean hovered) {
        if (hovered) {
            setUV(buttonu,buttonv);
        }else {
            setUV(TrackingButton.UVs.TRACK);
        }
    }

    public void setUV(TrackingButton.UVs uvs) {
        setUV(uvs.u, uvs.v);
    }
    public void setUV(int u,int v) {
        this.u = u;
        this.v = v;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;



            mc.getTextureManager().bindTexture(this.resourceLocation);
            GlStateManager.disableDepth();

            GlStateManager.enableBlend();

            boolean pressed = mousePressed(mc, mouseX, mouseY) && Mouse.isButtonDown(0);

            updateUVs(pressed);

            drawScaledCustomSizeModalRect(x, y, u, v, uWidth,vHeight, width, height, textureSizeX, textureSizeY);



            GlStateManager.enableDepth();
        }
    }
}
