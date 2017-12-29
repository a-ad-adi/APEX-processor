package main.data_structures;

import java.util.List;

import main.PipelineProcessor;

public class RenameTableEntry {
Integer index;
Integer loc;

public Integer getIndex() {
	return index;
}
public void setIndex(Integer index) {
	this.index = index;
}
public Integer getLoc() {
	return loc;
}
public void setLoc(Integer loc) {
	this.loc = loc;
}
public RenameTableEntry(Integer index, Integer loc) {
	super();
	this.index = index;
	this.loc = loc;
}

public static void updateRenameTable() {
	Integer addr1=null,addr2=null,addr3=null; 
	if(PipelineProcessor.execI.getInstr() != null) addr1 = PipelineProcessor.execI.getInstr().getdRegAddr();
	if(PipelineProcessor.execM2.getInstr() != null) addr2 = PipelineProcessor.execM2.getInstr().getdRegAddr();
	if(PipelineProcessor.execD4.getInstr() != null) addr3 = PipelineProcessor.execD4.getInstr().getdRegAddr();
	
	for(int i=0; i<16; i++) {
		if(addr1 != null && PipelineProcessor.renameTable.get(i).getIndex() == addr1 && PipelineProcessor.renameTable.get(i).getLoc() == 0) {
			//System.err.println("valid 1");
			PipelineProcessor.pRegisterFile.get(i).setrStatus(main.resources.PhysicalRegister.status.VALID);
		}
		
		if(addr2 != null && PipelineProcessor.renameTable.get(i).getIndex() == addr2 && PipelineProcessor.renameTable.get(i).getLoc() == 0) {
			PipelineProcessor.pRegisterFile.get(i).setrStatus(main.resources.PhysicalRegister.status.VALID);
			//System.err.println("valid 2");
		}
		
		if(addr3 != null && PipelineProcessor.renameTable.get(i).getIndex() == addr3 && PipelineProcessor.renameTable.get(i).getLoc() == 0) {
			PipelineProcessor.pRegisterFile.get(i).setrStatus(main.resources.PhysicalRegister.status.VALID);
			//System.err.println("valid 3");
		}
	}
}

public static void dispalyRenameTable(List<RenameTableEntry> rmt) {
	System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tIndex   			Instruction");
	int i=0;
	for(RenameTableEntry r : rmt) {
		String index,loc;
		if(r.getLoc() == null)
			loc = "-";
		else 
			loc = r.getLoc().toString();
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + i+ " " + r.getIndex() + " \t|" + loc);
		i++;
	}
}

public static void dispalyRenameTable() {
	System.out.println("\t\t\t\t\t\t\t\t\t\t\t\tIndex   			Instruction");
	int i=0;
	for(RenameTableEntry r : PipelineProcessor.renameTable) {
		String index,loc;
		if(r.getLoc() == null)
			loc = "-";
		else 
			loc = r.getLoc().toString();
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + i+ " " + r.getIndex() + " \t|" + loc);
		i++;
	}
}
}
