package net.altosheeve.soprano.client.Tuba;

public class TBM {
    public boolean enableExec = false; //for external program control only
    private boolean busy = false; //for internal program control only
    public void itterateInstructionSet(BasicFunctions instructionSet) {
        if (!instructionSet.finished()) {
            if (enableExec) { //allows for external control if TBM is running as a higher level construct, for instance if it's running in MINECRAFT
                if (!busy) { //allows for requests to block the main thread, forcing the main thread to wait for all requests that block the main thread to complete
                    instructionSet.translateInstruction(instructionSet.translateProgramPointer()); //execute current instruction
                    instructionSet.itter(); //move forward once in the program
                }
                busy = instructionSet.hasRequests();
                if (instructionSet.hasRequests()) instructionSet.fulfillRequests(); //if there are requests to be done after the main threads iteration, run them all
            }
        }
    }
}
