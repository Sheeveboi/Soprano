package net.altosheeve.soprano.client.Tuba;

import java.util.ArrayList;

public class Execution {
    private static final TBM sopranoTBM = new TBM();
    private static Movement instructions;

    public static void setProgram(ArrayList<Byte> program) { instructions = new Movement(program); }
    public static void toggle() { sopranoTBM.enableExec = !sopranoTBM.enableExec; }
    public static void execute() {
        if (instructions != null) sopranoTBM.itterateInstructionSet(instructions);
    }
}
