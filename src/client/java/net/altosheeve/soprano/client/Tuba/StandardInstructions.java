package net.altosheeve.soprano.client.Tuba;

public class StandardInstructions extends BasicInstructions{
    public StandardInstructions() {
        super();
        this.registerInstruction((byte) 0b001, this::_ASSIGN_TO_CACHE);
    }
    @Override
    public void _INIT() { //reserved for program startup (byte value 0)
    }
    protected void _ASSIGN_TO_CACHE() {
        this.programPointer++;                          //move program forward once
        this.readByte = this.translateProgramPointer(); //set read byte to current program byte
    }
    public void _POINT() { //moves the program to new location (byte value 1)
        this._ASSIGN_TO_CACHE();              //assign read byte as next program byte
        this.programPointer = this.readByte; //assign program pointer as read byte
    }
    public void _WRITE() { //write program code to memory (byte value 3)
        this._READ();                                   //assign next program byte to memory pointer (Map Key). assign memory pointer to read byte
        this.writeByte = this.readByte;                 //assign read byte to write byte
        this._ASSIGN_TO_CACHE();                         //assign next program byte to read byte
        this.memory.put(this.writeByte, this.readByte); //assign read byte to memory value (Map Value)
    }
    public void _READ() { //assign memory location to cache point (byte value 2)
        this._ASSIGN_TO_CACHE();                          // assign next program byte to read byte
        this.readByte = this.memory.get(readByte);  // treat read byte as pointer in memory map.
    }
}
