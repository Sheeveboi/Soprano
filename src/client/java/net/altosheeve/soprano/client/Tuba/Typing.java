package net.altosheeve.soprano.client.Tuba;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class Typing {

    public static final int INTEGER_IDENTIFIER = 0;
    public static final int STRING_IDENTIFIER = 1;
    public static final int FLOAT_IDENTIFIER = 2;
    public static final int FUNCTION_BODY_IDENTIFIER = 3;
    public static final int FUNCTION_ARGS_IDENTIFIER = 4;
    public static final int NULL_IDENTIFIER = 5;

    public static final int STATIC_EXPRESSION = 0;
    public static final int DYNAMIC_EXPRESSION = 1;
    public static final int ARGUMENTITIVE_EXPRESSION = 2;
    public static final int FUNCTIONAL_EXPRESSION = 3;

    public static final int INTEGER_SIZE = 1;
    public static final int FLOAT_SIZE = 4;

    //we won't use enums here so we can directly use type identifiers in the program
    public static int getTypeSize(int identifier) {

        switch (identifier) {
            case INTEGER_IDENTIFIER -> { return INTEGER_SIZE; }
            case FLOAT_IDENTIFIER -> { return FLOAT_SIZE; }
            default -> { return -1; }
        }
    }

    public static ArrayList<Byte> _GATHER_BODY(BasicFunctions instructions) {

        System.out.println("gathering type body");

        //get retrieval method
        instructions.itter();
        int retrievalMethod = instructions.translateProgramPointer();
        int idenfitier;
        int length;

        ArrayList<Byte> out = new ArrayList<>();

        switch (retrievalMethod) {

            //static
            case STATIC_EXPRESSION :

                System.out.println("static");

                instructions.itter();
                idenfitier = instructions.translateProgramPointer();

                if (idenfitier == NULL_IDENTIFIER) break;

                length = getTypeSize(idenfitier);

                if (length == -1) {

                    instructions.itter();
                    length = instructions.translateProgramPointer();

                }

                instructions.itter();
                out = instructions.skip(length);

                System.out.println(out);

                break;

            //dynamic
            case DYNAMIC_EXPRESSION :

                System.out.println("dynamic");

                int registry = _PARSE_INTEGER(instructions);

                idenfitier = instructions.memory.get((byte) registry);

                if (idenfitier == NULL_IDENTIFIER) break;

                length = getTypeSize(idenfitier);

                System.out.println("identifier: " + idenfitier);

                if (length == -1) length = instructions.memory.get((byte) (registry + 1));

                System.out.println("length: " + length);

                for (int i = 0; i < length; i++) out.add(instructions.memory.get((byte) (registry + 2 + i)));

                System.out.println(out);

                break;

            //argumentative
            case ARGUMENTITIVE_EXPRESSION :

                int targetArgument = _PARSE_INTEGER(instructions);
                int currentArgument = 0;

                Iterator<Byte> valuesIterator = instructions.entryValues.iterator();

                while (valuesIterator.hasNext()) {

                    idenfitier = valuesIterator.next();

                    if (idenfitier == NULL_IDENTIFIER) break;

                    length = getTypeSize(idenfitier);

                    if (length == -1) length = valuesIterator.next();

                    if (targetArgument == currentArgument) {

                        for (int i = 0; i < length && valuesIterator.hasNext(); i++) out.add(valuesIterator.next());

                        break;

                    }

                    else for (int i = 0; i < length && valuesIterator.hasNext(); i++) valuesIterator.next();

                }

                break;

            //functional
            case FUNCTIONAL_EXPRESSION :

                int identifier = instructions.exitValues.getFirst();

                if (identifier == NULL_IDENTIFIER) break;

                length = getTypeSize(identifier);

                System.out.println("identifier: " + identifier);

                if (length == -1) {

                    length = instructions.exitValues.get(1);

                    System.out.println("length: " + length);

                    for (int i = 2; i < length + 2; i++) out.add(instructions.exitValues.get(i));

                    break;
                }

                for (int i = 1; i < length + 1; i++) out.add(instructions.exitValues.get(i));

                break;

        }

        return out;
    }

    public static int _PARSE_INTEGER(BasicFunctions instructions) {
        return _GATHER_BODY(instructions).getFirst();
    }

    public static String _PARSE_STRING(BasicFunctions instructions) {

        StringBuilder out = new StringBuilder();
        ArrayList<Byte> stringData = _GATHER_BODY(instructions);

        for (byte b : stringData) out.append((char) b);

        return out.toString();

    }

    public static float _PARSE_FLOAT(BasicFunctions instructions) {

        //get type body
        ArrayList<Byte> body = _GATHER_BODY(instructions);

        byte first = body.get(0);
        byte second = body.get(1);
        byte third = body.get(2);
        byte fourth = body.get(3);

        return ByteBuffer.wrap(new byte[] { first, second, third, fourth }).getFloat();

    }

    public static CivKernel _PARSE_FUNCTION(BasicFunctions instructions) {

        ArrayList<Byte> arguments = _GATHER_BODY(instructions);
        ArrayList<Byte> body = _GATHER_BODY(instructions);

        return new CivKernel(body, arguments, instructions);

    }

    public static ArrayList<Byte> _ENCODE_FLOAT(float value) {
        ArrayList<Byte> out = new ArrayList<>();

        out.add((byte) FLOAT_IDENTIFIER); //mark as float

        //encode value
        int intBits = Float.floatToIntBits(value);

        out.add((byte) (intBits >> 24));
        out.add((byte) (intBits >> 16));
        out.add((byte) (intBits >> 8));
        out.add((byte) (intBits));

        System.out.println(out);

        return out;
    }

    public static ArrayList<Byte> _ENCODE_STRING(String string) {

        ArrayList<Byte> out = new ArrayList<>(); //instantiate out

        out.add((byte) STRING_IDENTIFIER); //mark as string
        out.add((byte) string.length()); //encode length of string

        for (char c : string.toCharArray()) out.add((byte) c); //encode body of string

        return out;

    }

    public static ArrayList<Byte> _ENCODE_INTEGER(int value) {

        ArrayList<Byte> out = new ArrayList<>(); //instantiate out

        out.add((byte) INTEGER_IDENTIFIER); //mark as integer
        out.add((byte) value); //encode value

        return out;

    }

    public static ArrayList<Byte> _ENCODE_FUNCTION_ARGS(ArrayList<Byte> arguments) {

        ArrayList<Byte> out = new ArrayList<>(); //instantiate out

        out.add((byte) FUNCTION_ARGS_IDENTIFIER); //mark as function arguments
        out.add((byte) (arguments.size())); //encode size of arguments
        out.addAll(arguments); //encode arguments

        return out;

    }

    public static ArrayList<Byte> _ENCODE_FUNCTION_BODY(ArrayList<Byte> body) {

        ArrayList<Byte> out = new ArrayList<>(); //instantiate out

        out.add((byte) FUNCTION_BODY_IDENTIFIER); //mark as function body
        out.add((byte) (body.size())); //encode length of function body
        out.addAll(body); //encode function body

        return out;

    }
}
