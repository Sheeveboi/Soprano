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

        int i = Float.floatToIntBits(value);
        byte[] hexBytes = Integer.toHexString(i).getBytes(StandardCharsets.UTF_8);
        byte[] length = new byte[]{(byte) hexBytes.length};

        return combineBuffers(length, hexBytes);
    }

    public static String decodeUUID(String message, int index) {
        return message.substring(index, index + 36);
    }

    public static float[] decodeFloat(String message, int index) {
        int length = message.getBytes(StandardCharsets.UTF_8)[index];
        long i = Long.parseLong(message.substring(index + 1, index + length + 1), 16);
        return new float[] {Float.intBitsToFloat((int) i), length};
    }

    public static int[] decodeInt(String message, int index) {
        int length = message.getBytes(StandardCharsets.UTF_8)[index];
        return new int[] {Integer.parseInt(message.substring(index + 1, index + length + 1)), length};
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
