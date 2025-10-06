package net.altosheeve.soprano.client.Tuba.Async;

import net.altosheeve.soprano.client.Tuba.BasicFunctions;

public class Thread {
    public boolean enableExec = false; //for external program control only
    public boolean iterateInstructionSet(BasicFunctions instructionSet) {
        if (enableExec) instructionSet.run();
        return instructionSet.finished();
    }
}
