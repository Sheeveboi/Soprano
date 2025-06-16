package net.altosheeve.soprano.client.BetterGUI;

import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;

public abstract class ScreenContainer extends Screen {

    protected InitEvent initCb;
    public TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    public ArrayList<ClickEvent> clickEvents = new ArrayList<>();
    protected ScreenContainer(Text title) {
        super(title);
    }
    public void addNonSelectable(Drawable d) {
        addDrawable(d);
    }

    public interface ClickEvent {
        void cb(int x, int y);
    }
    public interface InitEvent {
        void cb();
    }
    public void registerClickEvent(ClickEvent cb) {
        clickEvents.add(cb);
    }
    public void init() {
        ScreenMouseEvents.afterMouseRelease(this).register((a, b, c, d) -> { for (ClickEvent cb : clickEvents) cb.cb((int)b, (int)c); } );
        this.initCb.cb();
    }

    public <T extends Element & Selectable & Drawable> void addSelectableThing(T d) { addDrawableChild(d); }
    public <T extends Element & Selectable & Drawable> void addSelectable(T s) {
        addDrawable(s);
        addSelectableChild(s);
    }
}
