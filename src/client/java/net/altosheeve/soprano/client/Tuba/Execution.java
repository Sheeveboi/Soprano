package net.altosheeve.soprano.client.Tuba;

import java.util.ArrayList;

public class Execution {
    private static final TBM sopranoTBM = new TBM();
    private static CivKernel instructions;

    public static void setProgram(ArrayList<Byte> program) { instructions = new CivKernel(program); }
    public static void toggle() { sopranoTBM.enableExec = !sopranoTBM.enableExec; }
    public static void execute() {
        if (instructions != null) sopranoTBM.itterateInstructionSet(instructions);
    }
}
