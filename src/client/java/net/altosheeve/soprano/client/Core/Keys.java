package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Nodes.NodeCreation;
import net.altosheeve.soprano.client.Nodes.Screens.EditNodeScreen;
import net.altosheeve.soprano.client.Tuba.Execution;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

public class Keys {
    public static KeyBinding nodeScreen;
    public static KeyBinding connectNode;
    public static KeyBinding selectNode;
    public static KeyBinding enableExec;

    public static void registerKeys() {
        nodeScreen = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Add / Edit node",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_LEFT_BRACKET,
                        "Soprano"
                )
        );

        connectNode = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Connect Nodes",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_RIGHT_BRACKET,
                        "Soprano"
                )
        );

        selectNode = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Select Node",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_BACKSLASH,
                        "Soprano"
                )
        );
        enableExec = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Enable Execution",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_APOSTROPHE,
                        "Soprano"
                )
        );
    }

    public static void handleKeys() throws IOException {
        while(nodeScreen.wasPressed()) MinecraftClient.getInstance().setScreen(new EditNodeScreen(Text.of("test")));
        while(connectNode.wasPressed()) NodeCreation.connectNode();
        while(selectNode.wasPressed()) NodeCreation.selectNode();
        while(enableExec.wasPressed()) Execution.toggle();
    }
}
