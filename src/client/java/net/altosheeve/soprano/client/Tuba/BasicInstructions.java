package net.altosheeve.soprano.client.Tuba;

import net.altosheeve.soprano.client.Tuba.Async.Request;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BasicInstructions {
    private ArrayList<Byte> TBMinstructionPointers = new ArrayList<>(); //This is the program memory. the TBM will read from this list and call the translateInstruction() function to interpret the instruction
    protected interface Cb {
        void cb();
    } // generic callback method
    protected Map<Byte, Cb> instructionMap = new HashMap<>();// this map dictates the Java functions the TBM will execute when the translateInstruction() function is called
    protected Map<Cb, Byte> instructionRegister = new HashMap<>(); //this map allows implementations of this class to reference how its instructions are mapped
    protected Map<Byte, Byte> memory = new HashMap<>(); //general memory for programs to use
    private ArrayList<Request> requests = new ArrayList<>();// list of concurrent requests being executed alongside the main program
    protected int programPointer = 0;// tells where the TBM is in the program memory
    protected byte readByte = 0b0;
    protected byte writeByte = 0b0;
    public boolean finished() { return (TBMinstructionPointers.isEmpty() || this.programPointer > TBMinstructionPointers.size() - 1) && this.requests.isEmpty(); }// tells the TBM if the program has finished executing or not
    public boolean hasRequests() { return !this.requests.isEmpty(); }// tells the TBM if there are concurrent requests to be run after the main thread is finished with its current program iteration
    protected void fulfillRequests() {
        while (!this.requests.isEmpty() && this.requests.get(0).exec()) { this.requests.remove(0); }
    }// runs all concurrent requests
    protected void addRequest(Request request) { this.requests.add(request); }
    protected void registerInstruction(byte code, Cb cb) {
        instructionMap.put(code, cb);
        instructionRegister.put(cb, code);
    }// allows implementations of this class to register custom bytecodes or override current ones within a programs memory to a callback function
    protected byte translateInstruction(Cb cb) { return this.instructionRegister.get(cb); } //gets function registries from the instructionRegister
    public byte translateProgramPointer() { return this.TBMinstructionPointers.get(this.programPointer); }// returns the current program bytecode
    protected void translateInstruction(byte code) {
        if (instructionMap.containsKey(code)) {
            instructionMap.get(code).cb();
        }
    }// if the current program bytecode is mapped to a callback function, run that function
    public void itter() { this.programPointer++; }
    public BasicInstructions(ArrayList<Byte> program) {
        this.TBMinstructionPointers = program;
    }
}
