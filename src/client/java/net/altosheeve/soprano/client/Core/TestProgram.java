package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Tuba.Encoding;

import java.util.ArrayList;

public class TestProgram {
    public static ArrayList<Byte> getProgram() {
        ArrayList<Byte> testProgram = new ArrayList<>();
        testProgram.add((byte) 0x0); //calibrate
        testProgram.addAll(Encoding._ENCODE_STRING("test 1"));
        testProgram.add((byte) 2); //set tolerance
        testProgram.add((byte) 10);

        /*testProgram.add((byte) 0x2); //path to
        testProgram.addAll(Encoding._ENCODE_STRING("chest 1"));
        testProgram.add((byte) 7); //set tolerance
        testProgram.add((byte) 10);

        testProgram.add((byte) 0x11); //take last item in chest
        testProgram.add((byte) 53);

        testProgram.add((byte) 0x12); //take inclusive oak logs
        testProgram.addAll(Encoding._ENCODE_STRING("Oak Log"));
        testProgram.add((byte) 0);

        testProgram.add((byte) 0x12); //put inclusive oak logs
        testProgram.addAll(Encoding._ENCODE_STRING("Oak Log"));
        testProgram.add((byte) 1);

        testProgram.add((byte) 0x9); //leave chest*/

        testProgram.add((byte) 0x2); //path to
        testProgram.addAll(Encoding._ENCODE_STRING("barrel 1"));
        testProgram.add((byte) 7); //set tolerance
        testProgram.add((byte) 10);

        testProgram.add((byte) 0x14); //swap around stuff test
        testProgram.add((byte) 0); //swap around stuff test
        testProgram.add((byte) 28); //swap around stuff test

        testProgram.add((byte) 0x14); //swap around stuff test
        testProgram.add((byte) 1); //swap around stuff test
        testProgram.add((byte) 29); //swap around stuff test

        testProgram.add((byte) 0x14); //swap around stuff test
        testProgram.add((byte) 2); //swap around stuff test
        testProgram.add((byte) 30); //swap around stuff test

        /*testProgram.add((byte) 0x15); //swap around stuff test
        testProgram.addAll(Encoding._ENCODE_STRING("Baked Potato")); //swap around stuff test
        testProgram.add((byte) 0); //to inventory
        testProgram.add((byte) 31); //swap around stuff test

        testProgram.add((byte) 0x15); //swap around stuff test
        testProgram.addAll(Encoding._ENCODE_STRING("Baked Potato")); //swap around stuff test
        testProgram.add((byte) 1); //from inventory
        testProgram.add((byte) 0); //swap around stuff test*/


        return testProgram;
    }
}
