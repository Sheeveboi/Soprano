package net.altosheeve.soprano.client.BetterGUI.Generics;

import net.altosheeve.soprano.client.BetterGUI.Container;
import net.altosheeve.soprano.client.BetterGUI.Interactable;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;

public class GenericList extends Container {
    public int itemSeparation;
    public int visibleItemCount;
    public boolean arrangementMode;
    public float listStartX;
    public float listStartY;
    ArrayList<Container> items = new ArrayList<>();
    public GenericList(float rx, float ry, float listStartX, float listStartY, float rWidth, float rHeight, int padding, int itemSeparation, int visibleItemCount, boolean arrangementMode) {
        super(rx, ry, rWidth, rHeight, padding);
        this.itemSeparation = itemSeparation;
        this.visibleItemCount = visibleItemCount;
        this.arrangementMode = arrangementMode;
        this.listStartX = listStartX;
        this.listStartY = listStartY;
    }

    @Override
    public void init() {
    }
    public void scrollDownOne() {
        items.add(items.size(), items.get(0));
        items.remove(0);
    }
    public void scrollUpOne() {
        items.add(0, items.get(items.size() - 1));
        items.remove(items.size() - 1);
    }
    public void addItem(Container container) {
        container.parent = this;
        container.screen = this.screen;
        container.generateRelativity();
        container.init();

        if (container instanceof Interactable) ((Interactable) container).registerClickEvent();

        this.items.add(container);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.arrangementMode) {
            int prev = getY(this.listStartY);
            for (int i = 0; i < this.items.size(); i++) {
                Container item = items.get(i);
                if (i >= visibleItemCount - 1 || prev + item.height >= this.height) {
                    item.visible = false;
                } else {

                    item.visible = true;

                    if (prev + item.height >= this.height) break;

                    item.y = prev;
                    item.x = getX(this.listStartX);
                    prev = item.height + item.y + this.itemSeparation;

                    item.render(context, mouseX, mouseY, delta);

                }
            }
        } else {
            int prev = this.x;
            for (int i = 0; i < this.items.size(); i++) {
                Container item = items.get(i);
                if (i >= visibleItemCount - 1 || prev + item.height >= this.height) {
                    item.visible = false;
                } else {
                    item.visible = true;

                    if (prev + item.width >= this.width) break;

                    item.x = prev;
                    item.y = getY(this.listStartY);
                    prev = item.width + item.x + this.itemSeparation;

                    item.render(context, mouseX, mouseY, delta);

                    if (i >= visibleItemCount - 1) break;
                }
            }
        }
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
