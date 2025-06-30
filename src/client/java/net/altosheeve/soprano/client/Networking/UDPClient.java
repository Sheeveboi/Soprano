package net.altosheeve.soprano.client.Networking;

import com.google.common.base.Utf8;
import net.altosheeve.soprano.client.Core.Relaying;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UDPClient {

    public static DatagramSocket socket;
    public static InetAddress ip;
    public static int port;

    public static final int packetLength = 1024;
    public static byte[] unbuiltPacket = new byte[]{};

    public interface MessageCallback {
        void cb(String message);
    }

    public static class ListeningThread extends Thread {
        private final MessageCallback cb;
        private final DatagramSocket socket;
        public ListeningThread(MessageCallback cb, DatagramSocket socket) {
            this.cb = cb;
            this.socket = socket;
        }
        public void run() {

            byte[] recieve = new byte[packetLength];
            while (true) {
                DatagramPacket packet = new DatagramPacket(recieve, packetLength);
                try {
                    this.socket.receive(packet);
                    if (handleMessage(recieve)) this.cb.cb(new String(unbuiltPacket, StandardCharsets.UTF_8));
                } catch (IOException e) {
                    System.out.println("error in receive, attempting reconnect");
                    break;
                }
            }

            while (true) {
                int tries = 0;
                try {
                    createConnection(ip.getHostName(), port);
                    System.out.println("reconnect successful");
                    break;
                } catch (SocketException | UnknownHostException e) {
                    tries++;
                    System.out.println("reconnect failed with " + tries + " tries");
                }
            }

        }
    }

    private static byte[] cutLeftSide(int length, byte[] bytes) {
        byte[] out = new byte[bytes.length - length];
        if (bytes.length - length >= 0) System.arraycopy(bytes, length, out, 0, bytes.length - length);
        return out;
    }

    private static byte[] cutRightSide(int length, byte[] bytes) {
        byte[] out = new byte[bytes.length - length];
        if (bytes.length - length >= 0) System.arraycopy(bytes, 0, out, 0, bytes.length - length);
        return out;
    }

    public static void createConnection(String uri, int p) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        ip = InetAddress.getByName(uri);
        port = p;
    }

    public static void sendData(String message) throws IOException {

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] buffer = new byte[messageBytes.length + 2];
        byte[] out;

        int relativeIndex = 0;

        buffer[0] = 0x0;
        for (int i = 0; i < messageBytes.length; i++) buffer[i + 1] = messageBytes[i];

        if (packetLength < buffer.length) out = new byte[packetLength];
        else                              out = new byte[buffer.length];

        for (int i = 0; i < buffer.length; i++) {

            if (relativeIndex > packetLength - 1) {

                DatagramPacket packet = new DatagramPacket(out, out.length, ip, port);
                socket.send(packet);

                relativeIndex = 0;

                if (i + packetLength > buffer.length) out = new byte[buffer.length - i];
                else                                  out = new byte[packetLength];

            } else {

                out[relativeIndex] = buffer[i];
                relativeIndex ++;

            }
        }

        DatagramPacket packet = new DatagramPacket(out, out.length, ip, port);
        socket.send(packet);
    }

    public static void sendData(byte[] message) throws IOException {

        byte[] buffer = new byte[message.length + 2];
        byte[] out;

        int relativeIndex = 0;

        buffer[0] = 0x0;
        for (int i = 0; i < message.length; i++) buffer[i + 1] = message[i];

        if (packetLength < buffer.length) out = new byte[packetLength];
        else                              out = new byte[buffer.length];

        for (int i = 0; i < buffer.length; i++) {

            if (relativeIndex > packetLength - 1) {

                DatagramPacket packet = new DatagramPacket(out, out.length, ip, port);
                socket.send(packet);

                relativeIndex = 0;

                if (i + packetLength > buffer.length) out = new byte[buffer.length - i];
                else                                  out = new byte[packetLength];

            } else {

                out[relativeIndex] = buffer[i];
                relativeIndex ++;

            }
        }

        DatagramPacket packet = new DatagramPacket(out, out.length, ip, port);
        socket.send(packet);
    }

    public static boolean handleMessage(byte[] message) {
        if (message[0] == 0x0) unbuiltPacket = cutLeftSide(1, message);
        else {
            byte[] original = unbuiltPacket;
            unbuiltPacket = new byte[unbuiltPacket.length + message.length];

            System.arraycopy(original, 0, unbuiltPacket, 0, original.length);
            System.arraycopy(message, 0, unbuiltPacket, 0, message.length);
        }

        for (int i = 1; i < message.length; i++) {
            if (message[i] == 0x0) {
                unbuiltPacket = cutRightSide(unbuiltPacket.length - i + 1, unbuiltPacket);
                return true;
            }
        }

        return false;
    }

    public static void listen(MessageCallback cb) {
        ListeningThread listener = new ListeningThread(cb, socket);
        listener.start();
    }

    public static void main(String[] args) throws IOException {
        createConnection(Relaying.host, Relaying.port);
        sendData("this is a test message from the client");
        listen(System.out::println);
    }

}
