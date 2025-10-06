package net.altosheeve.soprano.client.Tuba;

import net.altosheeve.soprano.client.Tuba.Async.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.minecraft.util.math.MathHelper.floor;

public abstract class BasicFunctions {
    private ArrayList<Byte> TBMinstructionPointers = new ArrayList<>(); //This is the program memory. the TBM will read from this list and call the translateInstruction() function to interpret the instruction
    protected interface Cb {
        void cb();
    } // generic callback method

    protected Map<Byte, Cb> instructionMap = new HashMap<>();// this map dictates the Java functions the TBM will execute when the translateInstruction() function is called
    protected Map<Cb, Byte> instructionRegister = new HashMap<>(); //this map allows implementations of this class to reference how its instructions are mapped
    private ArrayList<Request> requests = new ArrayList<>();// list of concurrent requests being executed alongside the main program
    protected Map<Byte, Byte> memory = new HashMap<>(); //general memory for programs to use
    protected ArrayList<Byte> entryValues;
    protected ArrayList<Byte> exitValues;

    protected int programPointer = 0; // tells where the TBM is in the program memory

    // tells the TBM if the program has finished executing or not
    public boolean finished() {
        return (TBMinstructionPointers.isEmpty() || this.programPointer > TBMinstructionPointers.size() - 1) &&
                this.requests.isEmpty();
    }

    // tells the TBM if there are concurrent requests to be run after the main thread is finished with its current program iteration
    public boolean hasRequests() {
        return !this.requests.isEmpty();
    }

    // runs all concurrent requests
    protected void fulfillRequests() {
        while (!this.requests.isEmpty() && this.requests.getFirst().exec()) { this.requests.removeFirst(); }
    }

    //stops TBM for instructions that need to halt the main program
    protected void addRequest(Request request) { this.requests.add(request); }

    // allows implementations of this class to register custom bytecodes or override current ones within a programs memory to a callback function
    protected void registerInstruction(byte code, Cb cb) {
        instructionMap.put(code, cb);
        instructionRegister.put(cb, code);
    }

    //gets function registries from the instructionRegister
    protected byte translateInstruction(Cb cb) {
        return this.instructionRegister.get(cb);
    }

    // returns the current program bytecode
    public byte translateProgramPointer() {
        System.out.println(this.TBMinstructionPointers.get(this.programPointer) + ": " + (char) this.TBMinstructionPointers.get(this.programPointer).byteValue());
        return this.TBMinstructionPointers.get(this.programPointer);
    }

    // if the current program bytecode is mapped to a callback function, run that function
    protected void translateInstruction(byte code) {
        if (instructionMap.containsKey(code)) {
            instructionMap.get(code).cb();
        }
    }

    //moves the program forward by + 1
    public void itter() { this.programPointer++; }

    //skips ahead by a set amount and returns the skipped bytes
    public ArrayList<Byte> skip(int amount) {
        this.programPointer += amount;
        return (ArrayList<Byte>) this.TBMinstructionPointers.subList(this.programPointer - amount, this.programPointer);
    }

    //inserts an instruction at the end of the program
    public void insertInstruction(byte b) { this.TBMinstructionPointers.add(b); }

    //inserts an instruction at the specified index in the program
    public void insertInstruction(byte b, int i) { this.TBMinstructionPointers.add(i, b); }

    //adds multiple instructions to the end of the program
    public void addInstructions(ArrayList<Byte> bytes) { this.TBMinstructionPointers.addAll(bytes); }

    //inserts multiple instructions at the specified index in the program
    public void insertInstructions(ArrayList<Byte> bytes, int i) { this.TBMinstructionPointers.addAll(i, bytes); }

    //adds an instruction at a floating point based priority index within the program
    public void scheduleInstruction(byte b, float priority) {

        int index = floor((this.TBMinstructionPointers.size() - 1) * (priority - 1));
        this.TBMinstructionPointers.add(index, b);

    }

    //adds multiple instructions at a floating point based priority index within the program
    public void scheduleInstructions(ArrayList<Byte> bytes, float priority) {

        int index = floor((this.TBMinstructionPointers.size() - 1) * (priority - 1));
        this.TBMinstructionPointers.addAll(index, bytes);

    }

    public void pushStack(BasicFunctions instructions) {
        this.childStackObject = instructions;
        System.out.println("pushing onto stack");
        stackCount++;
    }

    //runs a stack-based execution abstraction
    public void run() {

        if (!this.finished()) {
            if (childStackObject != null) { //if there are stack objects ahead of this the stack
                if (childStackObject.hasRequests()) childStackObject.fulfillRequests();
                else childStackObject.run();
            } else { //if there are no stack objects ahead of this in the stack
                if (this.hasRequests()) this.fulfillRequests();
                else {
                    this.translateInstruction(this.translateProgramPointer()); //execute current instruction
                    this.itter(); //move forward once in the program
                }
            }
        }

    }

    //constructor
    public BasicFunctions(ArrayList<Byte> program, ArrayList<Byte> valuesIn) {
        this.entryValues = valuesIn;
        this.TBMinstructionPointers = program;
    }
}
