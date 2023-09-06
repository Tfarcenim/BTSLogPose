package tfar.btslogpose.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/ping_blue.png"));
            renderTooltip3D(Minecraft.getMinecraft(),ping,e.getPartialTicks());
            Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.ICONS);
        }
    }

    public static void renderTooltip3D(Minecraft mc, BTSPing ping,double partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        BlockPos pos = ping.getPos();
        int alpha = 0xff;
        int outline1 = ping.getColor();
        double xpos = mc.getRenderManager().viewerPosX - pos.getX();
        double ypos = mc.getRenderManager().viewerPosY - pos.getY();
        double zpos = mc.getRenderManager().viewerPosZ - pos.getZ();
        double dist = Math.sqrt(xpos * xpos + ypos * ypos + zpos * zpos);
        double scale = 1/40f * (1 + dist/100);
        //scale /= sr.getScaleFactor() * 16;
     //   if (scale <= 0.01)
     //       scale = 0.01;
        //RenderHelper.start();
        GlStateManager.translate(-xpos, -ypos, -zpos);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY + 180, 0, 1, 0);
        GlStateManager.rotate(-mc.getRenderManager().playerViewX, 1, 0, 0);
        GlStateManager.scale(scale, -scale, scale);
        GlStateManager.disableDepth();

        Gui.drawModalRectWithCustomSizedTexture(0,0,0,0,128,128,128,128);

//        GlStateManager.enableDepth();
        GlStateManager.scale(1F / scale, 1F / -scale, 1F / scale);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1, 0, 0);
        GlStateManager.rotate(mc.getRenderManager().playerViewY - 180, 0, 1, 0);
        GlStateManager.translate(xpos, ypos, zpos);
        //RenderHelper.end();
    }

}
