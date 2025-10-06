package net.altosheeve.soprano.client.Tuba;

import net.altosheeve.soprano.client.Tuba.Async.Thread;

import java.util.ArrayList;

public class Execution {
    private static final net.altosheeve.soprano.client.Tuba.Async.Thread sopranoTBM = new Thread();
    private static CivKernel instructions;

    public static void setProgram(ArrayList<Byte> program) { instructions = new CivKernel(program, new ArrayList<>(), null); }
    public static void toggle() { sopranoTBM.enableExec = !sopranoTBM.enableExec; }
    public static void execute() {
        if (instructions != null) sopranoTBM.iterateInstructionSet(instructions);
    }
}
