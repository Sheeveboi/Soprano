package net.altosheeve.soprano.client.Networking;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
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

    public static byte[] encodePlayer(float x, float y, float z, UUID UUID) {

        byte[] UUIDBytes = UUID.toString().getBytes();

        byte[] xBytes = encodeFloat(x);
        byte[] yBytes = encodeFloat(y);
        byte[] zBytes = encodeFloat(z);

        return combineBuffers(UUIDBytes, xBytes, yBytes, zBytes);

    }

    public static String decodeUUID(Iterator<Byte> buffer) {

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 36 && buffer.hasNext(); i++) out.append((char) buffer.next().byteValue());

        return out.toString();
    }

    public static float decodeFloat(Iterator<Byte> buffer) {
        byte first  = buffer.next();
        byte second = buffer.next();
        byte third  = buffer.next();
        byte fourth = buffer.next();

        return ByteBuffer.wrap(new byte[] { first, second, third, fourth }).getFloat();
    }

    public static float decodeInt(Iterator<Byte> buffer) {
        byte first  = buffer.next();
        byte second = buffer.next();
        byte third  = buffer.next();
        byte fourth = buffer.next();

        return ByteBuffer.wrap(new byte[] { first, second, third, fourth }).getInt();
    }

}
