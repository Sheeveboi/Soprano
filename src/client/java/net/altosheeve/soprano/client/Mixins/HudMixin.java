package net.altosheeve.soprano.client.Mixins;

import net.altosheeve.soprano.client.BetterGUI.Hotkeys;
import net.altosheeve.soprano.client.Core.Rendering;
import net.altosheeve.soprano.client.RenderMethods.DebugInfo;
import net.altosheeve.soprano.client.RenderMethods.HotkeyVisualizer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudMixin {

    @Inject(at = @At("TAIL"), method = "renderMainHud")
    private void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        HotkeyVisualizer.draw(context, tickCounter, ci);
        DebugInfo.draw(context, tickCounter, ci);
    }
}

