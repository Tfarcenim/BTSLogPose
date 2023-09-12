package tfar.btslogpose.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import tfar.btslogpose.BTSLogPose;

import java.io.IOException;

public class SelectRegionScreen extends ScaledGuiScreen {

    private static final ResourceLocation BACK = new ResourceLocation(BTSLogPose.MOD_ID,"textures/gui/menu/gui_1.png");

    public SelectRegionScreen() {
        super(BACK, 823,456);
    }

    @Override
    public void initGui() {
        super.initGui();
        int guiLeft = (this.width - 823/3) / 2;
        int guiTop = (this.height - 585/3) / 2;
        addButton(new GuiButton(0,guiLeft+56,guiTop+115,50,20,"test"));
        addButton(new GuiButton(1,guiLeft+56 + 53,guiTop+115,50,20,"test"));
        addButton(new GuiButton(2,guiLeft+56 + 53 * 2,guiTop+115,50,20,"test"));

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: Minecraft.getMinecraft().displayGuiScreen(new RegionScreen("east_blue.json"));break;
            case 1: Minecraft.getMinecraft().displayGuiScreen(new RegionScreen("grand_line.json"));break;
            case 2: Minecraft.getMinecraft().displayGuiScreen(new RegionScreen("new_world.json"));break;

        }
    }

    @Override
    public int getW() {
        return 585;
    }

}
