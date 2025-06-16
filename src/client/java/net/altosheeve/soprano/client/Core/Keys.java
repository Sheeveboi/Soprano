package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Nodes.NodeCreation;
import net.altosheeve.soprano.client.Nodes.Screens.EditNodeScreen;
import net.altosheeve.soprano.client.Tuba.Execution;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.Objects;

public class Keys {
    public static KeyBinding nodeScreen;
    public static KeyBinding connectNode;
    public static KeyBinding selectNode;
    public static KeyBinding enableExec;
    public static KeyBinding testKey;

    public static void registerKeys() {
        /*nodeScreen = KeyBindingHelper.registerKeyBinding(
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
        testKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Test Key",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_SEMICOLON,
                        "Soprano"
                )
        );*/
        //public release isnt ready for true tuba yet
    }

    public static void handleKeys() throws IOException {
        /*while(nodeScreen.wasPressed()) MinecraftClient.getInstance().setScreen(new EditNodeScreen(Text.of("test")));
        while(connectNode.wasPressed()) NodeCreation.connectNode();
        while(selectNode.wasPressed()) NodeCreation.selectNode();
        while(enableExec.wasPressed()) Execution.toggle();
        while(testKey.wasPressed()) {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            assert player != null;

            for (int slot = 0; slot < 36; slot++) {
                if (Objects.equals(player.getInventory().getStack(slot).getItemName().getString(), "Baked Potato")) {
                    player.getInventory().swapSlotWithHotbar(slot);
                }
            }
        }*/
    }
}
