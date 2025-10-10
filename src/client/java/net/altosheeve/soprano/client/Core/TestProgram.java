package net.altosheeve.soprano.client.Core;

import net.altosheeve.soprano.client.Tuba.Typing;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TestProgram {

    public static ArrayList<Byte> testFunction() {

        ArrayList<Byte> testProgram = new ArrayList<>();

        //create test 1 value in memory

        testProgram.add((byte) 0x19);

        testProgram.add((byte) Typing.STATIC_EXPRESSION); //static value
        testProgram.addAll(Typing._ENCODE_INTEGER(0)); //registery zero

        testProgram.add((byte) Typing.STATIC_EXPRESSION); //static value
        testProgram.addAll(Typing._ENCODE_INTEGER(Typing._ENCODE_STRING("test 1").size()));

        //add body
        testProgram.addAll(Typing._ENCODE_STRING("test 1"));

        //calibrate
        testProgram.add((byte) 0x0);

        //dynamic target value
        testProgram.add((byte) 1);

        //static registry value
        testProgram.add((byte) Typing.STATIC_EXPRESSION);
        testProgram.addAll(Typing._ENCODE_INTEGER(0));

        //tolerance
        testProgram.add((byte) Typing.STATIC_EXPRESSION);
        testProgram.addAll(Typing._ENCODE_FLOAT(.002f));

        //wait five seconds
        testProgram.add((byte) 0x17);

        //static time value
        testProgram.add((byte) Typing.STATIC_EXPRESSION);
        testProgram.addAll(Typing._ENCODE_INTEGER(20 * 5));

        //print 'test 1'
        testProgram.add((byte) 0x20);

        //dynamic print value
        testProgram.add((byte) Typing.DYNAMIC_EXPRESSION);

        //static registry value
        testProgram.add((byte) Typing.STATIC_EXPRESSION);
        testProgram.addAll(Typing._ENCODE_INTEGER(0));

        //print "hello world!"
        testProgram.add((byte) 0x20);
        testProgram.add((byte) Typing.STATIC_EXPRESSION);
        testProgram.addAll(Typing._ENCODE_STRING("hello world!"));

        //create a function in memory
        testProgram.add((byte) 0x19); //put all

        //static registry value
        testProgram.add((byte) Typing.STATIC_EXPRESSION); //static value
        testProgram.addAll(Typing._ENCODE_INTEGER(0)); //registery zero

        //create body
        ArrayList<Byte> testConditional = new ArrayList<>();

        testConditional.add((byte) 0x23); // return
        testConditional.add((byte) Typing.STATIC_EXPRESSION); // static value
        testConditional.addAll(Typing._ENCODE_INTEGER(Typing._ENCODE_STRING("this condition has passed!").size())); // encode string object length
        testConditional.addAll(Typing._ENCODE_STRING("this condition has passed!")); // encode string

        //encode length
        testProgram.add((byte) 0); //static value
        testProgram.addAll(Typing._ENCODE_INTEGER(Typing._ENCODE_FUNCTION_BODY(testConditional).size())); //encode conditional size

        //encode body
        testProgram.addAll(Typing._ENCODE_FUNCTION_BODY(testConditional));

        //conditional
        testProgram.add((byte) 0x24);

        //get conditional value
        testProgram.add((byte) Typing.STATIC_EXPRESSION);
        testProgram.addAll(Typing._ENCODE_INTEGER(1));

        //dynamic condition body
        testProgram.add((byte) Typing.DYNAMIC_EXPRESSION);

        //static registry value
        testProgram.add((byte) Typing.STATIC_EXPRESSION); //static registry value
        testProgram.addAll(Typing._ENCODE_INTEGER(0)); //registry 0

        //print result
        testProgram.add((byte) 0x20); //print utf-8
        testProgram.add((byte) Typing.FUNCTIONAL_EXPRESSION); //functional expression

        testProgram.add((byte) 0x23);
        testProgram.add((byte) Typing.STATIC_EXPRESSION);
        testProgram.addAll(Typing._ENCODE_INTEGER(Typing._ENCODE_STRING("RETURN VALUE!!").size()));
        testProgram.addAll(Typing._ENCODE_STRING("RETURN VALUE!!"));

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

        testProgram.add((byte) 0x20); //print return value
        testProgram.add((byte) Typing.FUNCTIONAL_EXPRESSION);

        testProgram.add((byte) 0x2); //path to

        //encode node to path to
        testProgram.add((byte) Typing.STATIC_EXPRESSION);
        testProgram.addAll(Typing._ENCODE_STRING("Conditional Test 1"));

        //encode tolerance
        testProgram.add((byte) Typing.STATIC_EXPRESSION);
        testProgram.addAll(Typing._ENCODE_FLOAT(.8f));

        return testProgram;
    }
}
