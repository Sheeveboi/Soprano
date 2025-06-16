package net.altosheeve.soprano.client.BetterGUI.Generics;

import net.altosheeve.soprano.client.BetterGUI.Interactable;
import net.altosheeve.soprano.client.BetterGUI.ScreenContainer;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class GenericButton extends Interactable {
    public String buttonText;
    public int color;
    public GenericButton(float rx, float ry, float rWidth, float rHeight, int padding, ScreenContainer screen) {
        super(rx, ry, rWidth, rHeight, padding);
        this.buttonText = "";
        this.color = 0xFF444444;
        this.screen = screen;
    }
    public GenericButton(float rx, float ry, float rWidth, float rHeight, int padding, String buttonText, ScreenContainer screen) {
        super(rx, ry, rWidth, rHeight, padding);
        this.buttonText = buttonText;
        this.color = 0xFF444444;
        this.screen = screen;
    }

    public GenericButton(float rx, float ry, float rWidth, float rHeight, int padding, String buttonText, int color, ScreenContainer screen) {
        super(rx, ry, rWidth, rHeight, padding);
        this.buttonText = buttonText;
        this.color = color;
        this.screen = screen;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.fillGradient(this.x, this.y, this.width + this.x, this.height + this.y, 0xFF444444, 0xFF444444);
        final MultilineText visheader = MultilineText.create(this.screen.textRenderer, Text.of(buttonText));
        visheader.drawWithShadow(context, this.x,this.y, 10, 0xffffffff);
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}