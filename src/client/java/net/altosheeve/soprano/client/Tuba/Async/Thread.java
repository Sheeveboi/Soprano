package net.altosheeve.soprano.client.Tuba.Async;

import net.altosheeve.soprano.client.Tuba.BasicFunctions;

public class Thread {

    public boolean block = false;
    private final BasicFunctions instructionSet;

    public Thread(BasicFunctions instructionSet) {
        this.instructionSet = instructionSet;
    }

    public boolean iterateInstructionSet() {
        if (!block) instructionSet.run();
        return instructionSet.finished();
    }

}
