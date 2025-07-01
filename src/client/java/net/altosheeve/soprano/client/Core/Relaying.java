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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Relaying {

    private static File identityFile = new File("identity.id");
    public static String sessionToken = "";
    public static String token = "";
    public static String host = "localhost";
    public static int port = 443;

    public static void relayInfo() throws IOException {
        if (Rendering.client.world == null) return;
        for (Waypoint waypoint : Waypoint.waypoints) waypoint.importance -= waypoint.decayRate;

        byte[] fullPayload = new byte[]{};

        for (Entity entity : Rendering.client.world.getEntities()) {
            if (entity.isPlayer()) {
                byte[] playerData = TypeGenerators.encodePlayer((float) entity.getX(), (float) Rendering.client.player.getY() + 2, (float) entity.getZ(), entity.getUuid());
                byte[] original = fullPayload;

                fullPayload = TypeGenerators.combineBuffers(original, playerData);
            }
        }

        UDPClient.sendData(fullPayload);
    }

    public static void gatherTelemetry(String message) {

        for (int i = 0; i < message.length(); i++) {
            if (message.getBytes(StandardCharsets.UTF_8)[i] == 0x1) {
                i += 1;
                String UUID = TypeGenerators.decodeUUID(message, i);

                i += 36;
                float[] x = TypeGenerators.decodeFloat(message, i);

                i += (int) (x[1] + 1);
                float[] y = TypeGenerators.decodeFloat(message, i);

                i += (int) (y[1] + 1);
                float[] z = TypeGenerators.decodeFloat(message, i);

                i += (int) (z[1] + 1);
                int threat = message.getBytes(StandardCharsets.UTF_8)[i];

                if (!UUID.equals(Rendering.client.player.getUuidAsString())) Waypoint.updateWaypoint(x[0] - .5f, (float) Rendering.client.player.getY(), z[0] - .5f, Waypoint.Type.values()[threat - 2], UUID, true);
            }
        }

    }

    public static void startStream() throws IOException {

        UDPClient.createConnection(host, port);
        UDPClient.listen(Relaying::gatherTelemetry);

    }

    public static void verify(Map<String, List<String>> headers, String body) throws IOException {

        String refreshR;
        refreshR = String.valueOf(headers.get("refresh"));

        FileWriter writer = new FileWriter("identity.id");
        writer.write(refreshR);
        writer.close();

        Request.get("http://170.187.207.133/verify?token=%s".formatted(token), (h, b) -> startStream());

    }

    public static void createSession() throws IOException {

        if (!identityFile.exists()) {
            identityFile.createNewFile();
            return;
        }

        Scanner scanner = new Scanner(identityFile);
        StringBuilder refresh = new StringBuilder();
        while (scanner.hasNextLine()) refresh.append(scanner.nextLine());

        if (refresh.isEmpty()) {
            identityFile.delete();
            identityFile.createNewFile();



        }

        Request.get("http://170.187.207.133/verify/refresh", Relaying::verify);

    }
}
