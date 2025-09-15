package net.altosheeve.soprano.client.Tuba;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Encoding {
    public static int _PARSE_INT(BasicFunctions instructions) {
        instructions.itter();
        return instructions.translateProgramPointer();
    }

    public static String _PARSE_STRING(BasicFunctions instructions) {
        int size = _PARSE_INT(instructions);
        StringBuilder out = new StringBuilder();
        for (int i = 0 ; i < size; i++) {
            instructions.itter();
            out.append((char) instructions.translateProgramPointer());
        }
        return out.toString();
    }

    public static ArrayList<Byte> _ENCODE_STRING(String string) {
        ArrayList<Byte> out = new ArrayList<>();
        out.add((byte) string.length());
        for (char c : string.toCharArray()) out.add((byte) c);
        return out;
    }
}
