package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Nodes.Navigation;
import net.altosheeve.soprano.client.Nodes.NodeCreation;
import net.altosheeve.soprano.client.Tuba.Execution;
import net.altosheeve.soprano.client.Tuba.Values;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

import java.io.IOException;
import java.util.ArrayList;

public class Client implements ClientModInitializer {

    private static int tick = 0;
    @Override
    public void onInitializeClient() {
        Keys.registerKeys();

        /*try { NodeCreation.loadNodes(); }
        catch (IOException e) { throw new RuntimeException(e); }

        ArrayList<Byte> testProgram = new ArrayList<>();
        testProgram.add((byte) 0x0); //calibrate
        testProgram.addAll(Values._ENCODE_STRING("test 1"));
        testProgram.add((byte) 2); //set tolerance
        testProgram.add((byte) 10);

        testProgram.add((byte) 0x2); //path to
        testProgram.addAll(Values._ENCODE_STRING("test iroad 3"));
        testProgram.add((byte) 7); //set tolerance
        testProgram.add((byte) 10);

        testProgram.add((byte) 0x2); //path to
        testProgram.addAll(Values._ENCODE_STRING("test iroad 4"));
        testProgram.add((byte) 8); //set tolerance
        testProgram.add((byte) 10);

        testProgram.add((byte) 0x2); //path to
        testProgram.addAll(Values._ENCODE_STRING("test iroad 5"));
        testProgram.add((byte) 8); //set tolerance
        testProgram.add((byte) 10);

        testProgram.add((byte) 0x2); //path to
        testProgram.addAll(Values._ENCODE_STRING("test iroad 6"));
        testProgram.add((byte) 8); //set tolerance
        testProgram.add((byte) 10);

        testProgram.add((byte) 0x2); //path to
        testProgram.addAll(Values._ENCODE_STRING("test iroad 3"));
        testProgram.add((byte) 8); //set tolerance
        testProgram.add((byte) 10);

        testProgram.add((byte) 0x2); //path to
        testProgram.addAll(Values._ENCODE_STRING("test 1"));
        testProgram.add((byte) 8); //set tolerance
        testProgram.add((byte) 10);

        Execution.setProgram(testProgram);*/

        try {
            Relaying.startStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                Keys.handleKeys();
                tick ++;
                tick = tick % 100;
                Navigation.tick = tick;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Execution.execute();
            try {
                Relaying.relayInfo();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        WorldRenderEvents.LAST.register((Rendering::render3d));
    }
}
