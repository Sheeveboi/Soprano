package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Tuba.Typing;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TestProgram {

    public static ArrayList<Byte> testFunction() {

        ArrayList<Byte> testProgram = new ArrayList<>();

        //create test 1 value in memory

        testProgram.add((byte) 0x19);

        testProgram.add((byte) 0); //static value
        testProgram.addAll(Typing._ENCODE_INTEGER(0)); //registery zero

        testProgram.add((byte) 0); //static value
        testProgram.addAll(Typing._ENCODE_INTEGER(Typing._ENCODE_STRING("test 1").size()));

        //add body
        testProgram.addAll(Typing._ENCODE_STRING("test 1"));

        //calibrate
        testProgram.add((byte) 0x0);

        //dynamic target value
        testProgram.add((byte) 1);

        //static registry value
        testProgram.add((byte) 0);
        testProgram.addAll(Typing._ENCODE_INTEGER(0));

        //tolerance
        testProgram.add((byte) 0);
        testProgram.addAll(Typing._ENCODE_FLOAT((float) 2 / 100));

        //wait five seconds
        /*testProgram.add((byte) 0x17);

        //static time value
        testProgram.add((byte) 0);
        testProgram.addAll(Typing._ENCODE_INTEGER(20 * 5));*/

        //print 'test 1'
        testProgram.add((byte) 0x20);

        //dynamic print value
        testProgram.add((byte) 1);

        //static registry value
        testProgram.add((byte) 0);
        testProgram.addAll(Typing._ENCODE_INTEGER(0));

        testProgram.add((byte) 0x20);
        testProgram.add((byte) 0x0);
        testProgram.addAll(Typing._ENCODE_STRING("hello world!"));

        return testProgram;

    }

    public static ArrayList<Byte> getProgram() {

        ArrayList<Byte> testFunction = testFunction();

        ArrayList<Byte> testParameters = new ArrayList<>(Typing._ENCODE_FLOAT(.75f));

        ArrayList<Byte> testProgram = new ArrayList<>();

        testProgram.add((byte) 0x22); //execute a function

        testProgram.add((byte) 0); //static argument assignment
        testProgram.addAll(Typing._ENCODE_FUNCTION_ARGS(testParameters)); //encode function arguments

        testProgram.add((byte) 0); //static body assignment
        testProgram.addAll(Typing._ENCODE_FUNCTION_BODY(testFunction)); //encode function body

        return testProgram;
    }
}
