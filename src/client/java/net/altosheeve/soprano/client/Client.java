package net.altosheeve.soprano.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class Client implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Keys.registerKeys();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Keys.handleKeys();
        });

        WorldRenderEvents.AFTER_TRANSLUCENT.register(Rendering::render);
    }
}
