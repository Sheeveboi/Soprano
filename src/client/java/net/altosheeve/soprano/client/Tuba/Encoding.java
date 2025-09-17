package net.altosheeve.soprano.client.Tuba;

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
            instructions.itter();
            int registery = instructions.translateProgramPointer();
            return instructions.memory.get((byte) registery);
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

    public static ArrayList<Byte> _ENCODE_STRING(String string) {
        ArrayList<Byte> out = new ArrayList<>();
        out.add((byte) string.length());
        for (char c : string.toCharArray()) out.add((byte) c);
        return out;
    }

    public static ArrayList<Byte> _ENCODE_STATIC_INTEGER(int value) {
        ArrayList<Byte> out = new ArrayList<>();
        out.add((byte) 0);
        out.add((byte) value);
        return out;
    }
}
