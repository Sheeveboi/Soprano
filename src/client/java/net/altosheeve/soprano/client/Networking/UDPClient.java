package net.altosheeve.soprano.client.Networking;

import net.altosheeve.soprano.client.Core.Relaying;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class UDPClient {

    public static DatagramSocket socket;
    public static InetAddress ip;
    public static int port;

    public static final int packetLength = 1024;

    public interface MessageCallback {
        void cb(byte[] message);
    }

    public static class ListeningThread extends Thread {
        private final MessageCallback cb;
        private final DatagramSocket socket;
        public ListeningThread(MessageCallback cb, DatagramSocket socket) {
            this.cb = cb;
            this.socket = socket;
        }
        public void run() {

            while (true) {
                byte[] receive = new byte[packetLength];
                DatagramPacket packet = new DatagramPacket(receive, packetLength);
                try {
                    this.socket.receive(packet);
                    //System.out.println(Arrays.toString(receive));
                    this.cb.cb(receive);
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

    public static void createConnection(String uri, int p) throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        ip = InetAddress.getByName(uri);
        port = p;
    }

    public static void sendData(byte[] message) throws IOException {
        byte[] out = new byte[packetLength];

        System.arraycopy(message, 0, out, 0, message.length);

        DatagramPacket packet = new DatagramPacket(out, packetLength, ip, port);
        socket.send(packet);
    }

    public static void listen(MessageCallback cb) {
        System.out.println("Soprano: UDP client now listening");
        ListeningThread listener = new ListeningThread(cb, socket);
        listener.start();
    }

    public static void main(String[] args) throws IOException {
        createConnection(Relaying.host, Relaying.port);
    }

}
