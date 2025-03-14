package net.altosheeve.soprano.client.Tuba;

public class ValueParsing {
    public static int _PARSE_INT(BasicInstructions instructions) {
        instructions.itter();
        return instructions.translateProgramPointer();
    }
}
