package tfar.btslogpose.client;

import net.minecraft.client.gui.GuiButton;

public class TrackingButton extends GuiButton {
    protected final Runnable press;
    public TrackingButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText,Runnable press) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.press = press;
    }
}
