package main.Display;

import java.util.ArrayList;
import java.util.List;

import main.PipelineProcessor;
import main.data_structures.IQEntry;
import main.data_structures.LSQEntry;
import main.data_structures.RenameTableEntry;
import main.data_structures.RobEntry;
import main.instruction.Instruction;

public class CycleInfo {
int cycleNo;
String fetch;
String decodeRF;
String execute;
String memory;
String writeBack;
String intFU;
String mul1;
String mul2;
String div1;
String div2;
String div3;
String div4;
String mem1;
String mem2;
String mem3;
List<RobEntry> rob;
List<IQEntry> iq;
List<LSQEntry> lsq;
List<Instruction> cInstr;
List<RenameTableEntry> rmt;
public List<RenameTableEntry> getRmt() {
	return rmt;
}


public void setRmt(List<RenameTableEntry> rmt) {
	this.rmt = rmt;
}


public List<RobEntry> getRob() {
	return rob;
}


public void setRob(List<RobEntry> rob) {
	this.rob = rob;
}


public List<IQEntry> getIq() {
	return iq;
}


public void setIq(List<IQEntry> iq) {
	this.iq = iq;
}


public List<LSQEntry> getLsq() {
	return lsq;
}


public void setLsq(List<LSQEntry> lsq) {
	this.lsq = lsq;
}


public List<Instruction> getcInstr() {
	return cInstr;
}


public void setcInstr(List<Instruction> cInstr) {
	this.cInstr = cInstr;
}


public void setExecute(String execute) {
	this.execute = execute;
}

public CycleInfo() {
	
}

public CycleInfo(int cycleNo, String fetch, String decodeRF, String execute, String memory, String writeBack) {
	this.cycleNo = cycleNo;
	this.fetch = fetch;
	this.decodeRF = decodeRF;
	this.execute = execute;
	this.memory = memory;
	this.writeBack = writeBack;
}


public CycleInfo(int cycleNo, String fetch, String decodeRF,
		String intFU, String mul1, String mul2, String div1, String div2, String div3, String div4, List<RobEntry> rob,
		List<IQEntry> iq, List<LSQEntry> lsq, List<Instruction> cInstr, List<RenameTableEntry> tRmt) {
	super();
	this.cycleNo = cycleNo;
	this.fetch = fetch;
	this.decodeRF = decodeRF;
	this.intFU = intFU;
	this.mul1 = mul1;
	this.mul2 = mul2;
	this.div1 = div1;
	this.div2 = div2;
	this.div3 = div3;
	this.div4 = div4;
	this.rob = rob;
	this.iq = iq;
	this.lsq = lsq;
	this.cInstr = cInstr;
	this.rmt = tRmt;
}

public CycleInfo(int cycleNo, String fetch, String decodeRF,
		String intFU, String mul1, String mul2, String div1, String div2, String div3, String div4, List<RobEntry> rob,
		List<IQEntry> iq, List<LSQEntry> lsq, List<Instruction> cInstr, List<RenameTableEntry> tRmt, String mem1, String mem2, String mem3) {
	super();
	this.cycleNo = cycleNo;
	this.fetch = fetch;
	this.decodeRF = decodeRF;
	this.intFU = intFU;
	this.mul1 = mul1;
	this.mul2 = mul2;
	this.div1 = div1;
	this.div2 = div2;
	this.div3 = div3;
	this.div4 = div4;
	this.rob = rob;
	this.iq = iq;
	this.lsq = lsq;
	this.cInstr = cInstr;
	this.rmt = tRmt;
	this.mem1 = mem1;
	this.mem2 = mem2;
	this.mem3 = mem3;
}


public CycleInfo(int cycleNo, String fetch, String decodeRF, String intFU, String mul1,
		String mul2, String div1, String div2, String div3, String div4, String memory, String writeBack) {
	this.cycleNo = cycleNo;
	this.fetch = fetch;
	this.decodeRF = decodeRF;
	this.intFU = intFU;
	this.mul1 = mul1;
	this.mul2 = mul2;
	this.div1 = div1;
	this.div2 = div2;
	this.div3 = div3;
	this.div4 = div4;
	this.memory = memory;
	this.writeBack = writeBack;	
}


public int getCycleNo() {
	return cycleNo;
}

public void setCycleNo(int cycleNo) {
	this.cycleNo = cycleNo;
}

public String getFetch() {
	return fetch;
}

public void setFetch(String fetch) {
	this.fetch = fetch;
}

public String getDecodeRF() {
	return decodeRF;
}

public void setDecodeRF(String decodeRF) {
	this.decodeRF = decodeRF;
}

public String getExecute() {
	return execute;
}

public void setExecute1(String execute) {
	this.execute = execute;
}

public String getMemory() {
	return memory;
}

public void setMemory(String memory) {
	this.memory = memory;
}

public String getWriteBack() {
	return writeBack;
}

public void setWriteBack(String writeBack) {
	this.writeBack = writeBack;
}


public String getIntFU() {
	return intFU;
}


public void setIntFU(String intFU) {
	this.intFU = intFU;
}


public String getMul1() {
	return mul1;
}


public void setMul1(String mul1) {
	this.mul1 = mul1;
}


public String getMul2() {
	return mul2;
}


public void setMul2(String mul2) {
	this.mul2 = mul2;
}


public String getDiv1() {
	return div1;
}


public void setDiv1(String div1) {
	this.div1 = div1;
}


public String getDiv2() {
	return div2;
}


public void setDiv2(String div2) {
	this.div2 = div2;
}


public String getDiv3() {
	return div3;
}


public void setDiv3(String div3) {
	this.div3 = div3;
}


public String getDiv4() {
	return div4;
}


public void setDiv4(String div4) {
	this.div4 = div4;
}


public String getMem1() {
	return mem1;
}


public void setMem1(String mem1) {
	this.mem1 = mem1;
}


public String getMem2() {
	return mem2;
}


public void setMem2(String mem2) {
	this.mem2 = mem2;
}


public String getMem3() {
	return mem3;
}


public void setMem3(String mem3) {
	this.mem3 = mem3;
}


public static void updateGanttChart() {
	String fetch, decodeRF, execute, execute2, memory, writeBack; 

	if(PipelineProcessor.fetch.getInstr() != null)			 				
		fetch = PipelineProcessor.fetch.getInstr().getInstrId();
	else fetch = "-";
	if(PipelineProcessor.decodeRF.getInstr() != null)
		decodeRF = PipelineProcessor.decodeRF.getInstr().getInstrId();
	else decodeRF = "-";
	if(PipelineProcessor.execute.getInstr() != null)
		execute = PipelineProcessor.execute.getInstr().getInstrId();
	else execute = "-";
	if(PipelineProcessor.memory.getInstr() != null)
		memory = PipelineProcessor.memory.getInstr().getInstrId();
	else memory = "-";
	if(PipelineProcessor.writeBack.getInstr() != null)
		writeBack = PipelineProcessor.writeBack.getInstr().getInstrId();
	else writeBack = "-";
	
	PipelineProcessor.ganttChart[PipelineProcessor.cycle] = new CycleInfo(PipelineProcessor.cycle, fetch, decodeRF, execute, memory, writeBack);
}

/*
public static void updateCycleInfo(String fetch, String decodeRF, String intFU, String mul1, String mul2, String div1, String div2, String div3,
		String div4 ,String memory,String writeBack) {
	PipelineProcessor.cycleInfo[PipelineProcessor.cycle] = new CycleInfo(PipelineProcessor.cycle, fetch, decodeRF, intFU, mul1, mul2, div1, div2, div3, div4, memory, writeBack);
}
*/
public static void updateCycleInfo(String fetch, String decodeRF,
		String intFU, String mul1, String mul2, String div1, String div2, String div3, String div4, List<RobEntry> rob,
		List<IQEntry> iq, List<LSQEntry> lsq, List<Instruction> cInstr, List<RenameTableEntry> tRmt) {
	PipelineProcessor.cycleInfo[PipelineProcessor.cycle] = new CycleInfo(PipelineProcessor.cycle, fetch, decodeRF, intFU, mul1, mul2, div1, div2, div3, div4,rob,iq,lsq,cInstr,tRmt);
}

public static void updateCycleInfo(String fetch, String decodeRF,
		String intFU, String mul1, String mul2, String div1, String div2, String div3, String div4, List<RobEntry> rob,
		List<IQEntry> iq, List<LSQEntry> lsq, List<Instruction> cInstr, List<RenameTableEntry> tRmt, String mem1, String mem2, String mem3
		) {
	PipelineProcessor.cycleInfo[PipelineProcessor.cycle] = new CycleInfo(PipelineProcessor.cycle, fetch, decodeRF, 
			intFU, mul1, mul2, div1, div2, div3, div4,rob,iq,lsq,cInstr,tRmt,mem1,mem2,mem3);
}

public static void updateCycleInfo() {
	String fetch, decodeRF, intFU, mul1, mul2, div1, div2, div3, div4 , memory, writeBack;

	if(PipelineProcessor.fetch.getInstr() != null)
		fetch = PipelineProcessor.fetch.getInstr().getInstrId() 
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.fetch.getInstr().getInstrId().replace("I", ""))).getInstructionString();
	else fetch = "Empty";
	
	if(PipelineProcessor.decodeRF.getInstr() != null)
		decodeRF = PipelineProcessor.decodeRF.getInstr().getInstrId()  
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.decodeRF.getInstr().getInstrId().replace("I", ""))).getInstructionString();
	else decodeRF = "Empty";

	if(PipelineProcessor.intFU.getStages()[0] != null)						
		intFU = PipelineProcessor.intFU.getStages()[0].getInstrId()
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.intFU.getStages()[0].getInstrId().replace("I", ""))).getInstructionString();
	else intFU = "Empty";

	if(PipelineProcessor.mulFU.getStages()[0] != null)						
		mul1 = PipelineProcessor.mulFU.getStages()[0].getInstrId()
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.mulFU.getStages()[0].getInstrId().replace("I", ""))).getInstructionString();
	else mul1 = "Empty";

	if(PipelineProcessor.mulFU.getStages()[1] != null)						
		mul2 = PipelineProcessor.mulFU.getStages()[1].getInstrId()
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.mulFU.getStages()[1].getInstrId().replace("I", ""))).getInstructionString();
	else mul2 = "Empty";
	
	if(PipelineProcessor.divFU.getStages()[0] != null)						
		div1 = PipelineProcessor.divFU.getStages()[0].getInstrId()
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.divFU.getStages()[0].getInstrId().replace("I", ""))).getInstructionString();
	else div1 = "Empty";

	if(PipelineProcessor.divFU.getStages()[1] != null)						
		div2 = PipelineProcessor.divFU.getStages()[1].getInstrId()
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.divFU.getStages()[1].getInstrId().replace("I", ""))).getInstructionString();
	else div2 = "Empty";

	if(PipelineProcessor.divFU.getStages()[2] != null)						
		div3 = PipelineProcessor.divFU.getStages()[2].getInstrId()
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.divFU.getStages()[2].getInstrId().replace("I", ""))).getInstructionString();
	else div3 = "Empty";

	if(PipelineProcessor.divFU.getStages()[3] != null)						
		div4 = PipelineProcessor.divFU.getStages()[3].getInstrId()
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.divFU.getStages()[3].getInstrId().replace("I", ""))).getInstructionString();
	else div4 = "Empty";

	if(PipelineProcessor.memory.getInstr() != null)
		memory = PipelineProcessor.memory.getInstr().getInstrId()
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.memory.getInstr().getInstrId().replace("I", ""))).getInstructionString();
	else memory = "Empty";
	if(PipelineProcessor.writeBack.getInstr() != null)	
		writeBack = PipelineProcessor.writeBack.getInstr().getInstrId()
		+ " - " + PipelineProcessor.codeMemory.get(Integer.parseInt(PipelineProcessor.writeBack.getInstr().getInstrId().replace("I", ""))).getInstructionString();
	else writeBack = "Empty";

	PipelineProcessor.cycleInfo[PipelineProcessor.cycle] = new CycleInfo(PipelineProcessor.cycle, fetch, decodeRF, intFU, mul1, mul2, div1, div2, div3, div4, memory, writeBack);
}

public static void displayGanttChart() {
	System.out.println("\n\n...................Gantt chart.....................\n\n");
	System.out.println("Cycle    Fetch    DecodeRF   Execute    Memory   WriteBack \n\n");
	for(int z=0 ;z<PipelineProcessor.n ; z++) {
		System.out.println(	PipelineProcessor.ganttChart[z].getCycleNo() + 1 + "\t" +
							PipelineProcessor.ganttChart[z].getFetch() + " \t  " +
							PipelineProcessor.ganttChart[z].getDecodeRF() + "\t\t  " +
							PipelineProcessor.ganttChart[z].getExecute() + " \t  " +
							PipelineProcessor.ganttChart[z].getMemory() + "   \t  " +
							PipelineProcessor.ganttChart[z].getWriteBack() + "   \t  " 
						  );
		System.out.println("--------------------------------------------------------------------------------");
	}
   System.out.println("\n\n");		
}

public static void displayCycleInfo() {
	 
	for(int z=0 ;z<PipelineProcessor.n; z++) {
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------\n");
		System.out.println("\n\nCycle     : " + (PipelineProcessor.cycleInfo[z].getCycleNo()+1));
		System.out.println("--------------\n");
		System.out.println("Fetch     : " + PipelineProcessor.cycleInfo[z].getFetch().replace("[", "").replace("]", ""));
		System.out.println("Decode    : " + PipelineProcessor.cycleInfo[z].getDecodeRF().replace("[", "").replace("]", ""));
		
		System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\tROB :");
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t-------");
		RobEntry.displayROB(PipelineProcessor.cycleInfo[z].getRob());
		
		System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\tIQ :");
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t-------");
		IQEntry.displayIQ(PipelineProcessor.cycleInfo[z].getIq());
		
		System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\tCommit :");
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t-------");
		PipelineProcessor.displayCommittedInstr(PipelineProcessor.cycleInfo[z].getcInstr());
		
		System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\tLSQ :");
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t-------");
		LSQEntry.displayLSQ(PipelineProcessor.cycleInfo[z].getLsq());
		
		System.out.println("\n\t\t\t\t\t\t\t\t\t\t\t\tRename table :");
		System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t-------");
		RenameTableEntry.dispalyRenameTable(PipelineProcessor.cycleInfo[z].getRmt());
		
		
		System.out.println("IntFU     : " + PipelineProcessor.cycleInfo[z].getIntFU().replace("[", "").replace("]", ""));
		System.out.println("Mul1      : " + PipelineProcessor.cycleInfo[z].getMul1().replace("[", "").replace("]", ""));
		System.out.println("Mul2      : " + PipelineProcessor.cycleInfo[z].getMul2().replace("[", "").replace("]", ""));
		System.out.println("Div1      : " + PipelineProcessor.cycleInfo[z].getDiv1().replace("[", "").replace("]", ""));
		System.out.println("Div2      : " + PipelineProcessor.cycleInfo[z].getDiv2().replace("[", "").replace("]", ""));
		System.out.println("Div3      : " + PipelineProcessor.cycleInfo[z].getDiv3().replace("[", "").replace("]", ""));
		System.out.println("Div4      : " + PipelineProcessor.cycleInfo[z].getDiv4().replace("[", "").replace("]", ""));
		System.out.println("Mem1      : " + PipelineProcessor.cycleInfo[z].getMem1().replace("[", "").replace("]", ""));
		System.out.println("Mem2      : " + PipelineProcessor.cycleInfo[z].getMem2().replace("[", "").replace("]", ""));
		System.out.println("Mem3      : " + PipelineProcessor.cycleInfo[z].getMem3().replace("[", "").replace("]", ""));
	}
}
}
