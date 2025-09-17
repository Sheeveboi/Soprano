package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Tuba.Encoding;

import java.util.ArrayList;

public class TestProgram {
    public static ArrayList<Byte> getProgram() {
        ArrayList<Byte> testProgram = new ArrayList<>();

        testProgram.add((byte) 0x19); //_PUT_ALL
        testProgram.addAll(Encoding._ENCODE_STATIC_INTEGER(0)); //registery zero

        testProgram.addAll(Encoding._ENCODE_STATIC_INTEGER(Encoding._ENCODE_STRING("test 1").size()));
        testProgram.addAll(Encoding._ENCODE_STRING("test 1"));

        testProgram.add((byte) 0x0);
        testProgram.add((byte) 1); //dynamic value
        testProgram.addAll(Encoding._ENCODE_STATIC_INTEGER(0));

        //tolerance
        testProgram.addAll(Encoding._ENCODE_STATIC_INTEGER(2));
        testProgram.addAll(Encoding._ENCODE_STATIC_INTEGER(100));

        testProgram.add((byte) 0x17); //wait five seconds
        testProgram.addAll(Encoding._ENCODE_STATIC_INTEGER(20 * 5));


        int pt = Encoding._ENCODE_STRING("test 1").size() + 1;
        testProgram.add((byte) 0x19); //_PUT_ALL
        testProgram.addAll(Encoding._ENCODE_STATIC_INTEGER(pt));
        testProgram.addAll(Encoding._ENCODE_STATIC_INTEGER(Encoding._ENCODE_STRING("this is a test variable").size()));
        testProgram.addAll(Encoding._ENCODE_STRING("this is a test variable"));

        testProgram.add((byte) 0x20);
        testProgram.add((byte) 1);
        testProgram.addAll(Encoding._ENCODE_STATIC_INTEGER(pt));

        return testProgram;
    }
}
