package net.altosheeve.soprano.client.BetterGUI;

import net.minecraft.client.gui.DrawContext;

public abstract class Interactable extends Container {
    public boolean entered = false;
    public Interactable(float rx, float ry, float rWidth, float rHeight, int padding) {
        super(rx, ry, rWidth, rHeight, padding);
    }
    @Override
    public void init() {
    }
    public void onClick() {
    };

    public void registerClickEvent() {
        this.screen.registerClickEvent((mouseX, mouseY) -> {
            if (mouseX >= this.x
                    && mouseY >= this.y
                    && mouseX <= this.width + this.x
                    && mouseY <= this.height + this.y
                    && visible) {
                this.onClick();
            }
        });
    }
    public void onHover() {};

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (mouseX >= this.x
                && mouseY >= this.y
                && mouseX <= this.width + this.x
                && mouseY <= this.height + this.y
                && visible) {
            if (!entered) {
                this.onHover();
                entered = true;
            }
        } else {
            if (entered) {
                entered = false;
            }
        }
    }

}
