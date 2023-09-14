package tfar.btslogpose.client.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tfar.btslogpose.client.BTSLogPoseClient;
import tfar.btslogpose.net.C2SToggleTrackingPacket;
import tfar.btslogpose.net.PacketHandler;

public class TrackingButton extends ImageButton {
    public final Runnable press;
    private final String region;
    private final String island;

    public TrackingButton(int buttonId, int xIn, int yIn, int widthIn, int heightIn, int u, int v, int uWidth, int vHeight,
                          ResourceLocation resource, int textureSizeX, int textureSizeY, String tooltipComponent, String region,String island) {
        super(buttonId, xIn, yIn, widthIn, heightIn, u, v, uWidth, vHeight, resource, textureSizeX, textureSizeY, tooltipComponent);
        this.press = createRunnable();
        this.region = region;
        this.island = island;
        updateUVs(false);
    }

    public Runnable createRunnable() {
        return () -> {
            boolean currentlyTracked = BTSLogPoseClient.isIslandTracked(region, island);
            PacketHandler.sendPacketToServer(new C2SToggleTrackingPacket(region, island, currentlyTracked));
            if (currentlyTracked) {
                BTSLogPoseClient.unTrackIsland(region, island);
            } else {
                BTSLogPoseClient.trackIsland(region, island);
            }
            updateUVs(false);
        };
    }

    public void updateUVs(boolean pressed) {
        if (currentlyTracked()) {
            if (pressed) {
                setUV(UVs.UNTRACK_PRESSED);
            } else {
                setUV(UVs.UNTRACK);
            }
        } else {
            if (pressed) {
                setUV(UVs.TRACK_PRESSED);
            } else {
                setUV(UVs.TRACK);
            }
        }
    }

    public boolean currentlyTracked() {
        return BTSLogPoseClient.isIslandTracked(region, island);
    }

    public void setUV(UVs uvs) {
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

            boolean pressed = mousePressed(mc, mouseX, mouseY);

            updateUVs(pressed);

            drawScaledCustomSizeModalRect(x, y, u, v, uWidth,vHeight, width, height, textureSizeX, textureSizeY);



            GlStateManager.enableDepth();
        }
    }

    public enum UVs {
        TRACK(736,39),
        TRACK_PRESSED(584,42),
        UNTRACK(580,377),
        UNTRACK_PRESSED(736,377);

        final int u;
        final int v;

        UVs(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }
}
