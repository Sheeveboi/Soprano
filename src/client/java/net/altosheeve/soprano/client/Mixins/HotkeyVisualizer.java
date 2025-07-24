package net.altosheeve.soprano.client.Mixins;

import net.altosheeve.soprano.client.BetterGUI.Hotkeys;
import net.altosheeve.soprano.client.Core.Rendering;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HotkeyVisualizer {

    @Inject(at = @At("TAIL"), method = "renderMainHud")
    private void renderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        int w = context.getScaledWindowWidth() / 2;

        context.getMatrices().push();
        context.getMatrices().translate(0.0F, 0.0F, -90.0F);

        for (int i = 0; i < 9; i++) context.drawText(Rendering.client.textRenderer, Hotkeys.keys.get(i), w - 85 + i * 20,context.getScaledWindowHeight() - 26, 0xffffff, true);

        context.getMatrices().pop();
    }
}

