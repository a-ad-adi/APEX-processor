package main.data_structures;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;

import main.PipelineProcessor;
import main.instruction.Instruction;

public class BIS {
Instruction instr;
List<RenameTableEntry> rt;
public Instruction getInstr() {
	return instr;
}
public void setInstr(Instruction instr) {
	this.instr = instr;
}
public List<RenameTableEntry> getRt() {
	return rt;
}
public void setRt(List<RenameTableEntry> rt) {
	this.rt = rt;
}

public static void saveStage(Instruction instr) {
	BIS b = new BIS();
	b.setInstr(instr);
	b.setRt(PipelineProcessor.renameTable);
	System.out.println("RT size : " + b.getRt().size());
	PipelineProcessor.bisList.put(instr.getInstrAddress(), b);
	System.out.println("BIS size : " + PipelineProcessor.bisList.size());
}

public static void revertStage(int addr) {
	System.out.println("Revert stage till : " + addr);
	boolean isFlushed = false;
	
/*	for(RobEntry r: PipelineProcessor.ROB) {
		System.out.println(r.getInstr().toString());
	}*/
	for(int i = PipelineProcessor.ROB.size() -1 ; i > 0; i--) {
		//System.out.println("flush a");
		if(addr < PipelineProcessor.ROB.get(i).getInstr().getInstrAddress()) {
			System.out.println("Removed-- : " + PipelineProcessor.ROB.get(i).getInstr().toString());
			PipelineProcessor.ROB.remove(i);
			isFlushed = true;
		}else {
			break;
		}
	}
	
	for(int i= PipelineProcessor.issueQueue.size() -1; i >0; i--) {
		//System.out.println("flush b");
		if(addr < PipelineProcessor.issueQueue.get(i).getInstr().getInstrAddress()) {
			PipelineProcessor.issueQueue.remove(i);
			isFlushed = true;
		}else {
			break;
		}
	}
	
	for(int i= PipelineProcessor.LSQ.size() -1; i >0; i--) {
		//System.out.println("flush c");
		if(addr < PipelineProcessor.LSQ.get(i).getInstr().getInstrAddress()) {
			PipelineProcessor.LSQ.remove(i);
			isFlushed = true;
		}else {
			break;
		}
	}
	if(isFlushed) {		
		PipelineProcessor.renameTable = new ArrayList<RenameTableEntry>();
		System.out.println("Rt size : " + PipelineProcessor.bisList.get(addr).getRt().size());
		List<RenameTableEntry> tRt = PipelineProcessor.bisList.get(addr).getRt();
		for(int i=0; i<16; i++) {
			PipelineProcessor.renameTable.add(tRt.get(i));
		}
	}
	
/*	System.out.println("ROB tail : " + PipelineProcessor.ROB.get(PipelineProcessor.ROB.size() -1).getInstr().toString());
	for(RobEntry r: PipelineProcessor.ROB) {
		System.out.println(r.getInstr().toString());
	}*/
}

public static void rollBackTo(int cycle, int addr) {
	boolean isFlushed = false;
	System.out.println("ROB flush : ");
	for(int i = PipelineProcessor.ROB.size() -1 ; i > 0; i--) {
		if(cycle <= PipelineProcessor.ROB.get(i).getCycle()) {
			System.out.println(PipelineProcessor.issueQueue.get(i));
			PipelineProcessor.issueQueue.remove(i);
			isFlushed = true;
		}
	}
	
	System.out.println("LSQ flush : ");
	for(int i= PipelineProcessor.LSQ.size() -1; i >0; i--) {
		if(cycle <= PipelineProcessor.LSQ.get(i).getCycle()) {
			System.out.println(PipelineProcessor.LSQ.get(i));
			PipelineProcessor.LSQ.remove(i);
			isFlushed = true;
			
		}
	}
	
	System.out.println("IQ flush : ");
	for(int i= PipelineProcessor.issueQueue.size() -1; i >0; i--) {
		if(cycle <= PipelineProcessor.issueQueue.get(i).getCycleNo()) {
			System.out.println(PipelineProcessor.issueQueue.get(i));
			PipelineProcessor.issueQueue.remove(i);
			isFlushed = true;
			
		}	
	}
	if(isFlushed) {		
		PipelineProcessor.renameTable = new ArrayList<RenameTableEntry>();
		System.out.println("Rt size : " + PipelineProcessor.bisList.get(addr).getRt().size());
		List<RenameTableEntry> tRt = PipelineProcessor.bisList.get(addr).getRt();
		for(int i=0; i<16; i++) {
			PipelineProcessor.renameTable.add(tRt.get(i));
		}
	}
	
}

public static void displayBis() {
	System.out.println("BIS contents :");
	for(Integer a : PipelineProcessor.bisList.keySet()) {
		System.out.println("Instruction : " + PipelineProcessor.bisList.get(a).getInstr().toString());
		List<RenameTableEntry> rt =  PipelineProcessor.bisList.get(a).getRt();
		System.out.println("Rename table : ");
		int i = 0;
		for(RenameTableEntry r : rt) {
			
			String loc;
			if(r.getLoc() != null) 
				loc = r.getLoc().toString();
			else 
				loc = "-";
			System.out.println(i + " " + r.getIndex() + " " + loc);
			i++;
		}
	}
}

public static void removeEntry(int addr) {
try {
	System.out.println("Size : " + PipelineProcessor.bisList.size());
	System.out.println("Removing : " + PipelineProcessor.bisList.get(addr).getInstr().toString());
	PipelineProcessor.bisList.remove(addr);

	}catch(NullPointerException npe) {
		System.err.println("Nothing in BIS");
	
	}catch(Exception e) {
		e.printStackTrace();
	}
}
}
