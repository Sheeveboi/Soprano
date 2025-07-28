package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Networking.TypeGenerators;
import net.altosheeve.soprano.client.Networking.UDPClient;
import net.altosheeve.soprano.client.Networking.UDPObject;
import net.altosheeve.soprano.client.RenderMethods.Waypoint;
import net.minecraft.entity.Entity;

import java.io.IOException;
import java.util.*;

public class Relaying {

    public static String host = "170.187.207.133";
    public static int port = 443;

    public static void relayInfo() throws IOException {
        if (Rendering.client.world == null) return;
        for (Waypoint waypoint : Waypoint.waypoints) waypoint.importance -= waypoint.decayRate;

        for (Entity entity : Rendering.client.world.getEntities()) {
            if (entity.isPlayer()) {

                UDPObject send = new UDPObject((byte) 0x1,
                        TypeGenerators.encodePlayer(
                                (float) entity.getX() - .5f,
                                (float) entity.getY() + 1.5f,
                                (float) entity.getZ() - .5f,
                                entity.getUuid()));

                UDPClient.queueObject(send);

            }
        }

        UDPClient.pushQueue();
    }

    public static void gatherTelemetry(UDPObject udpObject) {
        Iterator<Byte> buffer = udpObject.data.iterator();

        String UUID = TypeGenerators.decodeUUID(buffer);

        float x = 0;
        float y = 0;
        float z = 0;

        if (udpObject.identifier == 1) {
            x = TypeGenerators.decodeFloat(buffer);
            y = TypeGenerators.decodeFloat(buffer);
            z = TypeGenerators.decodeFloat(buffer);
        }

        if (udpObject.identifier == 2) {
            x = TypeGenerators.decodeInt(buffer);
            y = TypeGenerators.decodeInt(buffer);
            z = TypeGenerators.decodeInt(buffer);
        }

        System.out.println(UUID);

        Waypoint.updateWaypoint(x, y, z, Waypoint.Type.values()[buffer.next()], UUID);

    }

    public static void startStream() throws IOException {

        UDPClient.createConnection(host, port);
        UDPClient.listen(Relaying::gatherTelemetry);

    }

    public static class TestEntity {
        public String UUID;

        public float x;
        public float y;
        public float z;

        public TestEntity(String UUID, float x, float y, float z) {
            this.UUID = UUID;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    
    public static void main(String[] args) throws InterruptedException, IOException {

        startStream();

        UDPObject send1 = new UDPObject((byte) 0x1,
                TypeGenerators.encodePlayer(
                        (float) 500,
                        (float) 123,
                        (float) 5,
                        UUID.randomUUID()));

        UDPObject send2 = new UDPObject((byte) 0x1,
                TypeGenerators.encodePlayer(
                        (float) 1,
                        (float) 2,
                        (float) 800,
                        UUID.randomUUID()));

        while (true) {

            UDPClient.queueObject(send1);
            UDPClient.queueObject(send2);

            UDPClient.pushQueue();
            
            Thread.sleep(1000);
        }
        
    }
}
