package main.data_structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.PipelineProcessor;
import main.instruction.Instruction;
import main.resources.PhysicalRegister;

public class LSQEntry {
Instruction instr;	
int cycle;


public int getCycle() {
	return cycle;
}
public void setCycle(int cycle) {
	this.cycle = cycle;
}

public LSQEntry(Instruction instr, int cycle) {
	super();
	this.instr = instr;
	this.cycle = cycle;
}
public LSQEntry(Instruction instr) {
	super();
	this.instr = instr;
}
public Instruction getInstr() {
	return instr;
}

public void setInstr(Instruction instr) {
	this.instr = instr;
}

public static Instruction fetchNextLS() {
	Instruction instr = null;
	if(PipelineProcessor.LSQ.size() > 0) {
		
		for(int i=0; i< PipelineProcessor.LSQ.size(); i++) {
			if(PipelineProcessor.LSQ.get(i).getInstr().getResult() != null) {
				instr = PipelineProcessor.LSQ.get(i).getInstr();
				System.out.println("LSQ rem: " + instr.toString());
				PipelineProcessor.LSQ.remove(i);
				break;
			}
		}
	}else System.out.println("Empty LSQ");
	
	return instr;
}

public static void updateTargetAddr(int iAddr, int mAddr) {
	
	if(PipelineProcessor.LSQ.size() > 0) {
		
		for(int i=0; i< PipelineProcessor.LSQ.size(); i++) {
			if(PipelineProcessor.LSQ.get(i).getInstr().getInstrAddress() == iAddr) {
				PipelineProcessor.LSQ.get(i).getInstr().setResult(mAddr);
				System.out.println("Acquired target address.." + PipelineProcessor.LSQ.get(i).getInstr().toString() + " " + mAddr);
				break;
			}
		}
	}
	
}


public static Boolean loadBypass() {
	
	return false;
}

public static void displayLSQ() {
	
}
public static void displayLSQ(List<LSQEntry> lsq) {
	System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tIndex   			Instruction");
	if(lsq != null) {
		int i=0;
		for(LSQEntry le : lsq) {
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + i++ + " 				 " + le.getInstr().toString());
		}
		System.out.println("\n\n");
	}
}

public static Integer selectInstr() {
	Integer ind = null;
	HashMap<Integer,Instruction> readyInstrs = new HashMap<Integer,Instruction>();
	Instruction readyStore = null;
	Instruction readyLoad = null;
	Integer lIndex = null, sIndex = null;
	boolean lFound = false, sFound = false;
	if(PipelineProcessor.LSQ.size() > 0) {
		
		for(int i=0; i< PipelineProcessor.LSQ.size(); i++) {
			if(PipelineProcessor.LSQ.get(i).getInstr().getResult() != null) {
				
				if(PipelineProcessor.LSQ.get(i).getInstr().getInstr().equals("STORE") && !sFound) {
					readyInstrs.put(i,PipelineProcessor.LSQ.get(i).getInstr());
					sFound = true;
					sIndex = i;
				}
				
				if(PipelineProcessor.LSQ.get(i).getInstr().getInstr().equals("LOAD")) {
					readyInstrs.put(i,PipelineProcessor.LSQ.get(i).getInstr());
				}
			}
		}
		
		//Selection
		if(!readyInstrs.isEmpty()) {
			if(readyInstrs.get(0).getInstr().equals("LOAD")) {
				ind = 0;
			
			}else {
				int addr = readyInstrs.get(0).getResult();
				readyStore = readyInstrs.get(0);
				for(int i =1; i< readyInstrs.size(); i++) {
					if(readyInstrs.get(i) != null && addr != readyInstrs.get(i).getInstrAddress()) {
						readyLoad = readyInstrs.get(i);
						lIndex = i;
						break;
					}
				}
			}
			
			if(ind != null) {
				System.err.println("First load" + PipelineProcessor.LSQ.get(ind).getInstr().toString());
				PipelineProcessor.LSQ.remove(ind);
				return ind;
			}else if(readyLoad != null && lIndex != null) {
				for(int i = 0; i<lIndex; i++) {
					if(PipelineProcessor.LSQ.get(i).getInstr().getInstr().equals("STORE") && PipelineProcessor.LSQ.get(i).getInstr().getResult() == null) {
						lIndex = null;
					}
				}
			}
			if(lIndex != null) {
				System.err.println("Bypassed load : " + PipelineProcessor.LSQ.get(lIndex).getInstr().toString());
				PipelineProcessor.LSQ.remove(lIndex);
				return lIndex;
			}else if(sIndex != null) {
				System.err.println("First store" + PipelineProcessor.LSQ.get(sIndex).getInstr().toString());
				PipelineProcessor.LSQ.remove(sIndex);
				return sIndex;
			}
		}else System.out.println("Empty LSQ");
	}
	
	
	return ind;
}

public static void LSQForwarding() {
	Instruction hInstr = PipelineProcessor.LSQ.get(0).getInstr();
	
	if(hInstr.getInstr().equals("STORE")) {
		
	}
}

public static void earlyLoadFetch() {
	int i=0;
	for(LSQEntry l : PipelineProcessor.LSQ) {
		for(RobEntry r : PipelineProcessor.ROB) {
			if(r.getInstr().getInstr().equals("STORE")) {
				if(r.getInstr().getStage().equals(main.instruction.Instruction.Stage.EXECUTION_COMPLETE)) {
					if(l.getInstr().getInstr().equals("LOAD") 
						&& l.getInstr().getResult() != null){
							if(l.getInstr().getResult() == r.getInstr().getResult()) {
								PipelineProcessor
									.pRegisterFile.get(l.getInstr()
									.getdRegAddr())
									.setValue(r.getInstr().getRes());
								System.out.println(r.getInstr().toString() + " Res forwarded to load " + l.getInstr().toString());
								earlyLoadComplete(i);
							}
					}
				}
			}
		}
		i++;
	}
}


public static void earlyLoadComplete(int index) {
	PhysicalRegister.makePregValid(PipelineProcessor.LSQ.get(index).getInstr().prIndex);
	RobEntry.updateRobEntry(PipelineProcessor.LSQ.get(index).getInstr().getInstrAddress(), main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
	System.out.println("Early load complete : " + PipelineProcessor.LSQ.get(index).getInstr());
}
}


