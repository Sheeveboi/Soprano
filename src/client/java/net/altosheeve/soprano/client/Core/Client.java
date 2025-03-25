package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Nodes.NodeCreation;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

import java.io.IOException;

public class Client implements ClientModInitializer {

    private static int tick = 0;
    @Override
    public void onInitializeClient() {
        Keys.registerKeys();

        try { NodeCreation.loadNodes(); }
        catch (IOException e) { throw new RuntimeException(e); }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                Keys.handleKeys();
                tick++;
                tick = tick % 100;
                Navigation.tick = tick;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Execution.execute();
        });

        WorldRenderEvents.AFTER_TRANSLUCENT.register(Rendering::render);
    }
}
