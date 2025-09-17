package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Tuba.Encoding;

import java.util.ArrayList;

public class TestProgram {
    public static ArrayList<Byte> getProgram() {
        ArrayList<Byte> testProgram = new ArrayList<>();

        //create test 1 value in memory
        testProgram.add((byte) 0x19);

        testProgram.add((byte) 0); //static value
        testProgram.addAll(Encoding._ENCODE_INTEGER(0)); //registery zero

        testProgram.add((byte) 0); //static value
        testProgram.addAll(Encoding._ENCODE_INTEGER(Encoding._ENCODE_STRING("test 1").size()));

        //add body
        testProgram.addAll(Encoding._ENCODE_STRING("test 1"));

        //calibrate
        testProgram.add((byte) 0x0);

        //dynamic value
        testProgram.add((byte) 1);

        //static registry value
        testProgram.add((byte) 0);
        testProgram.addAll(Encoding._ENCODE_INTEGER(0));

        //tolerance
        testProgram.add((byte) 0); //static value
        testProgram.addAll(Encoding._ENCODE_INTEGER(2));

        testProgram.add((byte) 0); //static value
        testProgram.addAll(Encoding._ENCODE_INTEGER(100));

        //wait five seconds
        testProgram.add((byte) 0x17);

        testProgram.add((byte) 0); //static value
        testProgram.addAll(Encoding._ENCODE_INTEGER(20 * 5));

        //create 'this is a test variable' value in memory
        testProgram.add((byte) 0x19);

        int pt = Encoding._ENCODE_STRING("test 1").size() + 1;

        testProgram.add((byte) 0); //static value
        testProgram.addAll(Encoding._ENCODE_INTEGER(pt));

        testProgram.add((byte) 0); //static value
        testProgram.addAll(Encoding._ENCODE_INTEGER(Encoding._ENCODE_STRING("this is a test variable").size()));

        //encode body
        testProgram.addAll(Encoding._ENCODE_STRING("this is a test variable"));

        //print 'this is a test variable'
        testProgram.add((byte) 0x20);

        //dynamic value
        testProgram.add((byte) 1);

        //static registry value
        testProgram.add((byte) 0);
        testProgram.addAll(Encoding._ENCODE_INTEGER(pt));

        return testProgram;
    }
}
