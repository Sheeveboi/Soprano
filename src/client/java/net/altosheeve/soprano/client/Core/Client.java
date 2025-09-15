package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.BetterGUI.Hotkeys;
import net.altosheeve.soprano.client.Networking.Verification;
import net.altosheeve.soprano.client.Nodes.Navigation;
import net.altosheeve.soprano.client.Nodes.NodeCreation;
import net.altosheeve.soprano.client.RenderMethods.Waypoint;
import net.altosheeve.soprano.client.Tuba.Encoding;
import net.altosheeve.soprano.client.Tuba.Execution;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client implements ClientModInitializer {

    private static int tick = 0;
    private static boolean init = true;

    @Override
    public void onInitializeClient() {
        Keys.registerKeys();

        try { NodeCreation.loadNodes(); }
        catch (IOException e) { throw new RuntimeException(e); }

        Execution.setProgram(TestProgram.getProgram());

        try {
            Relaying.startStream();
        } catch (IOException e) {
            System.out.println("whatT???");
            throw new RuntimeException(e);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (Hotkeys.keys.isEmpty() && client.options != null) Hotkeys.gatherHotkeys();
            try {
                Keys.handleKeys();
                tick ++;
                tick = tick % 100;
                Navigation.tick = tick;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Execution.execute();
            try {
                Relaying.relayInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        WorldRenderEvents.LAST.register(Rendering::render3d);
        //HudRenderCallback.EVENT.register(Rendering::render2d);
    }
}
