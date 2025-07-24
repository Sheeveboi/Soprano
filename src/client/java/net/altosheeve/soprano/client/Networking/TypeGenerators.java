package net.altosheeve.soprano.client.Networking;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class TypeGenerators {

    public static byte[] combineBuffers(byte[]... buffers) {

        int totalLength = 0;
        for (byte[] buffer : buffers) totalLength += buffer.length;
        byte[] out = new byte[totalLength];

        int incrementalLength = 0;
        for (byte[] buffer : buffers) {
            System.arraycopy(buffer, 0, out, incrementalLength, buffer.length);
            incrementalLength += buffer.length;
        }

        return out;
    }

    public static byte[] encodeFloat(float value) {
        int intBits = Float.floatToIntBits(value);
        return new byte[] { (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits) };
    }

    public static String decodeUUID(byte[] message, int index) {
        return new String(message).substring(index, index + 36);
    }

    public static float decodeFloat(byte[] message, int index) {
        return ByteBuffer.wrap(new byte[] {message[index], message[index + 1], message[index + 2], message[index + 3]}).getFloat();
    }

    public static int decodeInt(byte[] message, int index) {
        return ByteBuffer.wrap(new byte[] {message[index], message[index + 1], message[index + 2], message[index + 3]}).getInt();
    }

    public static byte[] encodePlayer(float x, float y, float z, UUID UUID) {

        byte[] UUIDBytes = UUID.toString().getBytes();

        byte[] xBytes = encodeFloat(x);
        byte[] yBytes = encodeFloat(y);
        byte[] zBytes = encodeFloat(z);

        byte[] playerIdentifier = new byte[]{0x1};

        return combineBuffers(playerIdentifier, UUIDBytes, xBytes, yBytes, zBytes);

    }

}
