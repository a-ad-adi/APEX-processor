package main.resources;

import java.util.Arrays;

import main.instruction.Instruction.Stage;

public class Code {

	String[] instruction;
	int address;
	Stage stage;
	
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public String[] getInstruction() {
		return instruction;
	}
	public void setInstruction(String[] instruction) {
		this.instruction = instruction;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public Code(String[] instruction, int address) {
		super();
		this.instruction = instruction;
		this.address = address;
		this.stage = main.instruction.Instruction.Stage.NA;
	}
	
	public String getInstructionString() {
		return Arrays.toString(instruction);
	}
}
