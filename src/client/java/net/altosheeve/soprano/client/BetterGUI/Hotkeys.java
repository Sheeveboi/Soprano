package net.altosheeve.soprano.client.BetterGUI;

import net.altosheeve.soprano.client.Core.Rendering;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Hotkeys {

    public static ArrayList<String> keys = new ArrayList<>();
    public static void gatherHotkeys() {
        assert MinecraftClient.getInstance().options != null;
        for (KeyBinding hotbarKey : MinecraftClient.getInstance().options.hotbarKeys) {
            String[] path = hotbarKey.getBoundKeyTranslationKey().split("\\.");
            String out = path[2];
            if (Objects.equals(path[1], "mouse")) out = "M" + path[2];
            keys.add(out);
        }
    }

}
