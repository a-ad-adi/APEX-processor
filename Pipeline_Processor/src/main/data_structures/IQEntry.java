package main.data_structures;

import java.util.ArrayList;
import java.util.List;

import main.PipelineProcessor;
import main.instruction.Instruction;

public class IQEntry {
private Instruction instr;
private int cycleNo;
private int lsqInstrAddr;

public IQEntry(Instruction instr, int cycleNo) {
	super();
	this.instr = instr;
	this.cycleNo = cycleNo;
}
public Instruction getInstr() {
	return instr;
}
public void setInstr(Instruction instr) {
	this.instr = instr;
}
public int getCycleNo() {
	return cycleNo;
}
public void setCycleNo(int cycleNo) {
	this.cycleNo = cycleNo;
}

public int getLsqInstrAddr() {
	return lsqInstrAddr;
}
public void setLsqInstrAddr(int lsqInstrAddr) {
	this.lsqInstrAddr = lsqInstrAddr;
}

public static void displayIQ() {
	int i=0;
	System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tIssue queue : ");
	System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tIndex   			Instruction");
	for(IQEntry iqE : PipelineProcessor.issueQueue) {
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + i + " 				 " + iqE.getInstr().toString());
	}
	System.out.println("\n\n");
}
public static void displayIQ(List<IQEntry> iq) {
	int i=0;
	System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tIssue queue : ");
	System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tIndex   			Instruction");
	if(iq != null && iq.size() >0) {
		
		for(IQEntry iqE : iq) {
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + i + " 				 " + iqE.getInstr().toString());
		}
		System.out.println("\n\n");
	}
}

public static void updateIQ(){

}
}
