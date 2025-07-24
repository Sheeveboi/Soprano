package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Networking.Request;
import net.altosheeve.soprano.client.Networking.TypeGenerators;
import net.altosheeve.soprano.client.Networking.UDPClient;
import net.altosheeve.soprano.client.RenderMethods.Waypoint;
import net.minecraft.entity.Entity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Relaying {

    private static File identityFile = new File("identity.id");
    public static String sessionToken = "";
    public static String token = "";
    public static String host = "localhost";
    public static int port = 443;
    public static ArrayList<byte[]> leftovers = new ArrayList<>();

    public static void relayInfo() throws IOException {
        if (Rendering.client.world == null) return;
        for (Waypoint waypoint : Waypoint.waypoints) waypoint.importance -= waypoint.decayRate;

        byte[] fullPayload = new byte[]{};

        ArrayList<byte[]> remove = new ArrayList<>();

        for (byte[] leftover : leftovers) {

            byte[] original = fullPayload;
            byte[] out = TypeGenerators.combineBuffers(original, leftover);

            if (out.length > UDPClient.packetLength) {
                leftovers.add(leftover);
            } else {
                fullPayload = TypeGenerators.combineBuffers(original, leftover);
                remove.add(leftover);
            }

        }

        for (Entity entity : Rendering.client.world.getEntities()) {
            if (entity.isPlayer()) {

                byte[] playerData = TypeGenerators.encodePlayer((float) entity.getX(), (float) Rendering.client.player.getY() + 2, (float) entity.getZ(), entity.getUuid());
                byte[] original = fullPayload;
                byte[] out = TypeGenerators.combineBuffers(original, playerData);

                if (out.length > UDPClient.packetLength) {
                    leftovers.add(playerData);
                } else {
                    fullPayload = TypeGenerators.combineBuffers(original, playerData);
                }

            }
        }

        leftovers.removeAll(remove);

        UDPClient.sendData(fullPayload);
    }

    public static void gatherTelemetry(byte[] message) {
        try {
            int i = 0;
            while (i < message.length) {
                boolean intOrFloat = false;
                if (message[i] == 0x1) {
                    i += 1;
                    intOrFloat = true;
                }
                else if (message[i] == 0x2) {
                    i += 1;
                    intOrFloat = false;
                }
                else if (message[i] == 0x0) break;
                i += Waypoint.updateWaypoint(message, i, intOrFloat) + 1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void startStream() throws IOException {

        UDPClient.createConnection(host, port);
        UDPClient.listen(Relaying::gatherTelemetry);

    }
}
