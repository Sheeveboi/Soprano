package net.altosheeve.soprano.client.Networking;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;

/**
 * Abstraction layer over the default Java TCP client. a specified
 * callback function is called when a response is sent front the server to the client.
 * all connections are asyncronous and non-blocking due to this architecture. Messages
 * are passed as plaintext using the 'sendText' method. For people who give a hoot, the
 * raw socket is also available for lower level functionality.
 * @since 1.0.1
 * @author Alto, Feather
 */
public class TCPClient {
    public Socket sock;
    public SSLSocket secureSock;
    private BufferedWriter out;
    private BufferedReader in;
    private int port;
    public String uri;
    public boolean reconnect = false;
    public boolean alive = true;
    public boolean used = false;
    public boolean useSSL;
    public boolean stream;

    RecvHandler recvHandler;
    replyCB rcb;

    public interface replyCB {
        public void cb(String c);
    }

    public TCPClient(String u, int port, replyCB cb, boolean s, boolean ssl) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        createConnection(u, port);
        uri = u;
        rcb = cb;
        useSSL = ssl;
        stream = s;
    }

    public void createConnection(String ip, int port) throws IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, KeyManagementException {
        if (useSSL) {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("keystoreFile"), "keystorePassword".toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(ks, "keystorePassword".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ks);

            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = tmf.getTrustManagers();
            sc.init(kmf.getKeyManagers(), trustManagers, null);


            SSLSocketFactory ssf = sc.getSocketFactory();
            SSLSocket s = (SSLSocket) ssf.createSocket(ip, port);
            s.startHandshake();

            secureSock = s;
            in = new BufferedReader(new InputStreamReader(secureSock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(secureSock.getOutputStream()));
        } else {
            sock = new Socket(ip, port);
            sock.setKeepAlive(stream);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        }
    }
    public void start() {
        if (reconnect) {
            recvHandler.stop();
            recvHandler = new RecvHandler();
            recvHandler.start();

            alive = true;
        }
        else {
            recvHandler = new RecvHandler();
            alive = true;
            recvHandler.start();
        }
    }

    public void sendText(String msg) throws IOException {
        out.write(msg);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        sock.close();
        alive = false;
    }
    public class RecvHandler extends Thread {
        public void run() {
            StringBuilder out = new StringBuilder();
            ArrayList<Character> buffer = new ArrayList<>();
            boolean char1 = false;
            while (alive) {
                try {
                    int i = in.read();
                    if (i == -1) {
                        stopConnection();
                        break;
                    }

                    buffer.add((char)i);

                    if (i == 13 && !char1) char1 = true;
                    else if (i == 10 && char1) {
                        StringBuilder builder = new StringBuilder(buffer.size());
                        for (Character c : buffer) {
                            builder.append(c);
                        }
                        out.append(builder);
                        if (stream) rcb.cb(builder.toString());
                        buffer.clear();
                    } else char1 = false;

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!stream) rcb.cb(out.toString());
        }
    }
}