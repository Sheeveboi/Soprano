package net.altosheeve.soprano.client.BetterGUI;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;

import java.util.ArrayList;

import static java.lang.Math.round;

public abstract class Container<T extends Element & Selectable & Drawable>
        implements Drawable,
        Element,
        Selectable {
    public int x;
    public int y;
    private final float rx;
    private final float ry;
    public int width;
    public int height;
    private final float rWidth;
    private final float rHeight;
    public int padding;
    public boolean visible = true;
    public Mouse mouse = MinecraftClient.getInstance().mouse;
    public ScreenContainer screen;
    public Container parent;
    ArrayList<Container> childrenContainers = new ArrayList<>();
    private ArrayList<Container> childrenWidgets = new ArrayList<>();

    public Container(int width, int height, int padding, ScreenContainer screen) {
        this.x = 0;
        this.y = 0;
        this.rx = 0;
        this.ry = 0;
        this.width = width;
        this.height = height;
        this.rWidth = 0;
        this.rHeight = 0;
        this.padding = padding;
        this.screen = screen;
    }
    public Container(float rx, float ry, float rWidth, float rHeight, int padding) {
        this.x = 0;
        this.y = 0;
        this.rx = rx;
        this.ry = ry;
        this.width = 0;
        this.height = 0;
        this.rWidth = rWidth;
        this.rHeight = rHeight;
        this.padding = padding;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {}

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    public int getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    protected String reduceFloat(double i) {
        try {
            return Double.toString(i).substring(0, 4);
        } catch (Exception e) {
            if (i == 1) {
                return "1.00";
            } else if (i == 0) {
                return "0.00";
            }
        }
        return "";
    }

    public int getX(float x1) {
        return (int) (((this.width - this.padding * 2) * x1) + this.x + this.padding);
    }
    public int getY(float y1) {
        return (int) (((this.height - this.padding * 2) * y1) + this.y + this.padding);
    }
    public int getW(float w1) {
        return (int) ((this.width - this.padding * 2) * w1);
    }
    public int getH(float h1) {
        return (int) ((this.height - this.padding * 2) * h1);
    }
    public void generateRelativity() {
        this.x = round((parent.width - parent.padding * 2) * this.rx) + parent.x + parent.padding;
        this.y = round((parent.height - parent.padding * 2) * this.ry) + parent.y + parent.padding;

        this.width = round((parent.width - parent.padding * 2) * this.rWidth);
        this.height = round((parent.height - parent.padding * 2) * this.rHeight);
    }
    public int xAgainstRelativeTo(Container currentContainer, Container relativeContainer, boolean leftOrRight, int padding) {
        if (leftOrRight) return (relativeContainer.x + relativeContainer.width) + padding;
        else return (relativeContainer.x - (currentContainer.width + padding));
    }
    public int yAgainstRelativeTo(Container currentContainer, Container relativeContainer, boolean topOrBottom, int padding) {
        if (topOrBottom) return (relativeContainer.y + relativeContainer.height + padding);
        else return (relativeContainer.y - (currentContainer.height + padding));
    }

    public void addWidget(Container widget) {
        childrenWidgets.add(widget);
        this.screen.addSelectable(widget);
    }

    public void addContainer(Container container) {
        container.parent = this;
        container.screen = this.screen;
        container.generateRelativity();
        container.init();

        if (container instanceof Interactable) ((Interactable) container).registerClickEvent();

        this.screen.addNonSelectable(container);
        this.childrenContainers.add(container);
    }

    public abstract void init();
    public void setDynamicHeight() {
        int highest = 0;
        for (Container child : this.childrenWidgets) {
            if (child.getHeight() + child.y >= highest) {
                highest = (int) (child.getHeight() + child.y);
            }
        }
        for (Container child : this.childrenContainers) {
            if (child.getHeight() + child.y >= highest) {
                highest = (int) child.getHeight() + child.y;
            }
        }
        this.height = highest + this.padding;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        setDynamicHeight();
    }
}
