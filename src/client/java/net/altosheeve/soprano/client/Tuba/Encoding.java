package net.altosheeve.soprano.client.Tuba;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Encoding {

    public static int _PARSE_INTEGER(BasicFunctions instructions) {

        //get static or dynamic
        instructions.itter();
        int staticOrDynamic = instructions.translateProgramPointer();

        //if value is static
        if (staticOrDynamic == 0) {

            //skip integer mark
            instructions.itter();

            //decode value
            instructions.itter();
            return instructions.translateProgramPointer();

        }

        //if value is dynamic
        else {

            //get location of value in memory. we can add + 1 to skip the integer mark
            int registry = Encoding._PARSE_INTEGER(instructions) + 1;

            //decode value from memory
            return instructions.memory.get((byte) registry);

        }
    }

    public static String _PARSE_STRING(BasicFunctions instructions) {

        //get static or dynamic
        instructions.itter();
        int staticOrDynamic = instructions.translateProgramPointer();

        //if value is static
        if (staticOrDynamic == 0) {

            //skip string mark
            instructions.itter();

            //get length
            instructions.itter();
            int length = instructions.translateProgramPointer();

            //decode body
            StringBuilder out = new StringBuilder();
            for (int i = 0 ; i < length; i++) {
                instructions.itter();
                out.append((char) instructions.translateProgramPointer());
            }
            return out.toString();
        }

        //if value is dynamic
        else {

            //get location of dynamic value in memory. we can add + 1 to the pointer to skip the string mark
            int registry = Encoding._PARSE_INTEGER(instructions) + 1;

            //get length from memory
            int length = instructions.memory.get((byte) registry);

            //decode body from memory
            StringBuilder out = new StringBuilder();
            for (int i = 0; i < length; i++) out.append((char) instructions.memory.get((byte) (registry + 1 + i)).byteValue());
            return out.toString();

        }
    }

    public static float _PARSE_FLOAT(BasicFunctions instructions) {

        //get static or dynamic
        instructions.itter();
        int staticOrDynamic = instructions.translateProgramPointer();

        //if value is static
        if (staticOrDynamic == 0) {

            //get float bytes from program
            instructions.itter();
            byte first  = instructions.translateProgramPointer();
            instructions.itter();
            byte second  = instructions.translateProgramPointer();
            instructions.itter();
            byte third  = instructions.translateProgramPointer();
            instructions.itter();
            byte fourth  = instructions.translateProgramPointer();

            //decode value
            return ByteBuffer.wrap(new byte[] { first, second, third, fourth }).getFloat();

        }

        //if value is dynamic
        else {

            //get location of value in memory. we can add + 1 to skip the integer mark
            int registry = Encoding._PARSE_INTEGER(instructions) + 1;

            //get float bytes from memory
            byte first  = instructions.memory.get((byte) (registry));
            byte second = instructions.memory.get((byte) (registry + 1));
            byte third  = instructions.memory.get((byte) (registry + 2));
            byte fourth = instructions.memory.get((byte) (registry + 3));

            //decode value
            return ByteBuffer.wrap(new byte[] { first, second, third, fourth }).getFloat();

        }
    }

    public static ArrayList<Byte> _ENCODE_FLOAT(float value) {
        ArrayList<Byte> out = new ArrayList<>();

        out.add((byte) 2); //mark as float

        //encode value
        int intBits = Float.floatToIntBits(value);

        out.add((byte) (intBits >> 24));
        out.add((byte) (intBits >> 16));
        out.add((byte) (intBits >> 8));
        out.add((byte) (intBits));

        return out;
    }

    public static ArrayList<Byte> _ENCODE_STRING(String string) {

        ArrayList<Byte> out = new ArrayList<>(); //instantiate out

        out.add((byte) 1); //mark as string
        out.add((byte) string.length()); //encode length of string

        for (char c : string.toCharArray()) out.add((byte) c); //encode body of string

        return out;

    }

    public static ArrayList<Byte> _ENCODE_INTEGER(int value) {

        ArrayList<Byte> out = new ArrayList<>(); //instantiate out

        out.add((byte) 0); //mark as integer
        out.add((byte) value); //encode value

        return out;

    }
}
