package tfar.btslogpose.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import tfar.btslogpose.BTSLogPose;
import tfar.btslogpose.world.BTSPing;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(Side.CLIENT)
public class BTSLogPoseClient {

    private static List<BTSPing> pings = new ArrayList<>();
    public static void setPings(List<BTSPing> pings) {
        BTSLogPoseClient.pings = pings;
    }

    @SubscribeEvent
    public static void renderPings(RenderWorldLastEvent e) {
        for (BTSPing ping : pings) {
            renderTooltip3D(Minecraft.getMinecraft(),ping,e.getPartialTicks());
            Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
        }
    }

    public static void renderTooltip3D(Minecraft mc, BTSPing ping,double partialTicks) {
        BlockPos pos = ping.getPos();
        int outline1 = ping.getColor();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/ping_blue.png"));
        double xpos = mc.getRenderManager().viewerPosX - pos.getX();
        double ypos = mc.getRenderManager().viewerPosY - pos.getY();
        double zpos = mc.getRenderManager().viewerPosZ - pos.getZ();

        final double actualDistance = Minecraft.getMinecraft().player.getDistance(pos.getX(),pos.getY(),pos.getZ());

        double viewDistance = actualDistance;
        final double maxRenderDistance = mc.gameSettings.renderDistanceChunks * 16;
        if (viewDistance > maxRenderDistance) {
            final Vec3d delta =new Vec3d(xpos,ypos,zpos).normalize();
            xpos = delta.x * maxRenderDistance;
            ypos = delta.y * maxRenderDistance;
            zpos = delta.z * maxRenderDistance;
            viewDistance = maxRenderDistance;
        }
        final double maxScale = 1/20f;
        double scaleMultiplier = Math.max(1,actualDistance / maxRenderDistance);
        double scale = maxScale / scaleMultiplier;

        double effectiveScale = maxScale / (Math.min(4,Math.sqrt(scaleMultiplier)));

        GlStateManager.pushMatrix();
        GlStateManager.translate(-xpos, -ypos, -zpos);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY + 180, 0, 1, 0);
        GlStateManager.rotate(-mc.getRenderManager().playerViewX, 1, 0, 0);
        GlStateManager.scale(effectiveScale, -effectiveScale, effectiveScale);
        GlStateManager.disableDepth();
        Gui.drawModalRectWithCustomSizedTexture(0,0,0,0,128,128,128,128);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}
