package net.altosheeve.soprano.client.Tuba;

import java.util.ArrayList;

public class Encoding {

    public static int _PARSE_INT(BasicFunctions instructions) {
        instructions.itter();

        int staticOrDynamic = instructions.translateProgramPointer();

        if (staticOrDynamic == 0) {
            instructions.itter();
            return instructions.translateProgramPointer();
        }

        else {
            instructions.itter();
            int registery = instructions.translateProgramPointer();
            return instructions.memory.get((byte) registery);
        }
    }

    public static String _PARSE_STRING(BasicFunctions instructions) {


        instructions.itter();

        int staticOrDynamic = instructions.translateProgramPointer();

        if (staticOrDynamic == 0) {
            instructions.itter();

            int length = instructions.translateProgramPointer();
            StringBuilder out = new StringBuilder();
            for (int i = 0 ; i < length; i++) {
                instructions.itter();
                out.append((char) instructions.translateProgramPointer());
            }
            return out.toString();
        }
        else {
            int registry = Encoding._PARSE_INT(instructions);

            int length = instructions.memory.get((byte) registry);
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
