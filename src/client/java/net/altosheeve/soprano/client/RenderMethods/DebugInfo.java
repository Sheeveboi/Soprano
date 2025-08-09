package net.altosheeve.soprano.client.RenderMethods;

import net.altosheeve.soprano.client.BetterGUI.Hotkeys;
import net.altosheeve.soprano.client.Core.Rendering;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class DebugInfo {

    public static float avgLatency = 0;
    public static float immediateLatency = 0;
    public static float sinceLast = 0;

    public static void draw(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        //context.drawText(Rendering.client.textRenderer, "Average Latency  : " + avgLatency + "ms", 5,20, 0xffffff, true);
        //context.drawText(Rendering.client.textRenderer, "Immediate Latency : " + immediateLatency + "ms", 5,35, 0xffffff, true);
        context.drawText(Rendering.client.textRenderer, "Last Packet : " + sinceLast + "ms", 2,10, 0xffffff, true);
    }
}
