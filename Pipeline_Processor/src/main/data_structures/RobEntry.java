package main.data_structures;

import java.util.ArrayList;
import java.util.List;

import main.PipelineProcessor;
import main.instruction.Instruction;

public class RobEntry {
public enum status {EXEC,WIP,COMPLTED,UNALLOC,INVALID}
private Instruction instr;
private Boolean interrupted;
private Boolean committed;
private Integer exCode;
private status status;
private int pc;
int cycle;
public RobEntry(Instruction instr) {
	super();
	this.instr = instr;
	this.interrupted = false;
	this.committed = false;
	this.exCode = null;
}
public RobEntry(Instruction instr, int cycle) {
	super();
	this.instr = instr;
	this.cycle = cycle;
}
public Instruction getInstr() {
	return instr;
}
public void setInstr(Instruction instr) {
	this.instr = instr;
}
public Boolean getInterrupted() {
	return interrupted;
}
public void setInterrupted(Boolean interrupted) {
	this.interrupted = interrupted;
}
public Boolean getCommitted() {
	return committed;
}
public void setCommitted(Boolean committed) {
	this.committed = committed;
}

public int getCycle() {
	return cycle;
}
public void setCycle(int cycle) {
	this.cycle = cycle;
}
public status getStatus() {
	return status;
}
public void setStatus(status status) {
	this.status = status;
}
public Integer getExCode() {
	return exCode;
}
public void setExCode(Integer exCode) {
	this.exCode = exCode;
}


public int getPc() {
	return pc;
}
public void setPc(int pc) {
	this.pc = pc;
}
public static void commitInstr() {
	Instruction instr;
	Integer dIndex;
	boolean cmt = false;
	//System.out.println("Committing..");
	for(int i=0; i<1; i++) {
		
		if(PipelineProcessor.ROB.size() >= 2) {
			instr = PipelineProcessor.ROB.get(0).getInstr();
			//System.out.println("Entry at head :" + instr.toString() + " " + instr.getStage());
			if(instr.getStage() == main.instruction.Instruction.Stage.EXECUTION_COMPLETE) {
				
				switch(instr.getType()) {
				case "ATM": case "MV": case "LS": case "JMP": case "HLT":
				if(instr.prIndex != null) {	
					dIndex = instr.prIndex;
					
					System.out.println("Committed : " + instr.toString());
					System.out.println("reg free : " + instr.prIndex);
					//System.out.println("Res : " + instr.getResult() + " " + PipelineProcessor.pRegisterFile.get(instr.prIndex).getValue());
					PipelineProcessor.registerFile.get(instr.arIndex).setValue(PipelineProcessor.pRegisterFile.get(instr.prIndex).getValue());	//copy value
					PipelineProcessor.registerFile.get(instr.arIndex).setzFlag((PipelineProcessor.pRegisterFile.get(instr.prIndex).getzFlag())); //copy flag
					System.out.println("New loc : " + instr.arIndex + " val : " + PipelineProcessor.registerFile.get(instr.arIndex).getValue());
					//change
/*					PipelineProcessor.pRegisterFile.get(instr.prIndex).setAllocated(false);
					PipelineProcessor.pRegisterFile.get(.prIndex).setRenamed(false);*/
					cmt =true;
					PipelineProcessor.pRegisterFile.get(instr.prIndex).setAllocated(false);
					PipelineProcessor.pRegisterFile.get(instr.prIndex).setRenamed(false);
					if(PipelineProcessor.renameTable.get(instr.arIndex).getIndex() == instr.prIndex) {
						boolean clear = true;
/*						for(IQEntry ie: PipelineProcessor.issueQueue) {
							if(ie.getInstr().getsReg1Addr() != null && ie.getInstr().getsReg1Addr() == instr.prIndex)
								clear = false;
							if(ie.getInstr().getsReg2Addr() != null && ie.getInstr().getsReg2Addr() == instr.prIndex)
								clear = false;
							
							if(!clear) {
								System.out.println("Cannot clear P"+ instr.prIndex + " of " + instr.toString());
								break;
							}
						}*/
						if(clear) {							
							System.out.println("clear mapping : " + instr.arIndex + " " + instr.prIndex);
							PipelineProcessor.renameTable.get(instr.arIndex).setIndex(instr.arIndex);
							PipelineProcessor.renameTable.get(instr.arIndex).setLoc(1);
							//RenameTableEntry.dispalyRenameTable();
						}
					}else {


					}
						

				}else cmt = true;
					break;
				case "BR":
					BIS.removeEntry(instr.getInstrAddress());
					cmt = true;
					break;
/*				case "JMP":
					break;
				case "HLT":
					break;*/
				default:	
					break;
				}
				if(cmt)	{
					//System.out.println("Removed : " + PipelineProcessor.ROB.get(0).getInstr().toString());
					PipelineProcessor.commitInstr.add(instr);
					PipelineProcessor.ROB.remove(0);
					//System.out.println("Remove I : " + i);
					cmt = false;	
				}else {
					System.out.println("no::" + PipelineProcessor.ROB.get(0));
				}
			}else {
				System.out.println("not Removed : " + PipelineProcessor.ROB.get(0).getInstr().toString());
				break;
			}

		}else {
			
		}
	}
}

public static boolean deAllocReg(int regIndex) {
	boolean deAlloc = true;
	int dReg = 0;
	//System.out.println("checking");
	for(int i=0; i<PipelineProcessor.issueQueue.size(); i++) {
		if(PipelineProcessor.issueQueue.get(i).getInstr().getsReg1Addr() != null && 
				regIndex == PipelineProcessor.issueQueue.get(i).getInstr().getsReg1Addr()	
				) {
			if(PipelineProcessor.issueQueue.get(i).getInstr().getOpr1() == null) {				
				deAlloc = false; System.out.println("Cannot deAlloc : P" + regIndex); break;
			}else {
				
				System.out.println("Dealloc P" + regIndex + " reg value is forwarded"); 
			}
					
		}	

		if(PipelineProcessor.issueQueue.get(i).getInstr().getsReg2Addr() != null && 
				regIndex == PipelineProcessor.issueQueue.get(i).getInstr().getsReg2Addr()	
				) {
			if(PipelineProcessor.issueQueue.get(i).getInstr().getOpr2() != null) {
				
				deAlloc = false; System.out.println("Cannot deAlloc : P" + regIndex); break; 
			}else {
				System.out.println("Dealloc P" + regIndex + " reg value is forwarded");
			}
		}	

	}
	return deAlloc;
}

public static void updateRobEntry(int addr, main.instruction.Instruction.Stage stage) {
	int i=0;
	
	for(RobEntry rbe : PipelineProcessor.ROB) {
		
		if(rbe.getInstr().getInstrAddress() == addr) {
			PipelineProcessor.ROB.get(i).getInstr().setStage(stage);
			System.out.println("Ready to commit : " + i + "  " + PipelineProcessor.ROB.get(i).getInstr().toString());
			break;
		}
		i++;
	}
}

public static void updateLSInstr(int address, int res) {
	for(RobEntry r : PipelineProcessor.ROB) {
		if(r.getInstr().getInstrAddress() == address) {
			r.getInstr().setRes(res);
			System.out.println("Result updated in ROB");
			System.out.println(r.getInstr().toString() + " " + res);
			break;
		}
	}
}
public static void displayROB() {
	int i=0;
	//System.out.println("Reorder buffer : ");
	System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tIndex   			Instruction");
	for(RobEntry robE : PipelineProcessor.ROB) {
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + i++ + " 				 " + robE.getInstr().toString());
	}
	System.out.println("\n\n");
}

public static void displayROB(List<RobEntry> rob) {
	int i=0;
	//System.out.println("Reorder buffer : ");
	System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tIndex   			Instruction");
	if(rob != null) {
		
		for(RobEntry robE : rob) {
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + i++ + " 				 " + robE.getInstr().toString());
		}
		System.out.println("\n\n");
	}
}

}
