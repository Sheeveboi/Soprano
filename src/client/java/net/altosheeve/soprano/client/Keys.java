package net.altosheeve.soprano.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keys {
    public static KeyBinding testKey;

    public static void registerKeys() {
        testKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Test Key",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_LEFT_BRACKET,
                        "Test Key"
                )
        );
    }

    public static void handleKeys() {
        while(testKey.wasPressed()) System.out.println("Test key works!");
    }
}
