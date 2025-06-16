package net.altosheeve.soprano.client.BetterGUI.Generics;

import net.altosheeve.soprano.client.BetterGUI.Container;
import net.altosheeve.soprano.client.BetterGUI.Interactable;
import net.altosheeve.soprano.client.BetterGUI.ScreenContainer;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class GenericTextField extends Interactable {
    public GenericTextField(float rx, float ry, float rWidth, float rHeight, int padding, ScreenContainer screen) {
        super(rx, ry, rWidth, rHeight, padding);
        this.screen = screen;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        System.out.println(chr);
        return true;
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void init() {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.fillGradient(this.x, this.y, this.width + this.x, this.height + this.y, 0xFF444444, 0xFF444444);
        final MultilineText visheader = MultilineText.create(this.screen.textRenderer, Text.of("Testing 2"));
        visheader.drawWithShadow(context, getX(0), getY(0), 10, 0xffffffff);
    }
}
