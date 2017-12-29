package main.instruction;

import main.PipelineProcessor;

public class Instruction {
public enum Stage{FETCH_START,FETCH_COMPLETE,
				  DECODE_START,DECODE_COMPLETE,
				  EXECUTE_START,EXECUTE_COMPLETE,
				  I1_START, I1_END, 
				  M1_START, M1_END, M2_START, M2_END, 
				  D1_START, D1_END, D2_START, D2_END, D3_START, D3_END, D4_START, D4_END,
				  MEMORY_START,MEMORY_COMPLETE,
				  WRITEBACK_START,WRITEBACK_COMPLETE,
				  EXECUTION_COMPLETE,
				  NA};
public enum ALUStage{I1_START, I1_END, 
					M1_START, M1_END, M2_START, M2_END, 
					D1_START, D1_END, D2_START, D2_END, D3_START, D3_END, D4_START, D4_END
			}				  
private String instrId;
private String instr;
private Integer sReg1Addr;
private Integer sReg2Addr;
private Integer dRegAddr;
private String label;
private Integer literal;
private Integer instrAddress;
private Stage stage;
private ALUStage aluStage;
private Integer stallCount;
private Integer result;
private Integer opr1;
private Integer opr2;
private Boolean signFlag, zeroFlag, carryFlag;
private String type;
private String fuType;
public Boolean isRenamed;
public static Boolean branchTaken = false;
public static int branchCount = 0;
public Boolean branchReady = false;
public Integer arIndex;
public Integer prIndex;
public Integer tReg;
public Integer res;
public Instruction(String instrId,Integer instrAddr) {
	super();
	this.instrId = instrId;
	this.instrAddress = instrAddr;
	this.stallCount = 0;
}

public int getArIndex() {
	return arIndex;
}

public void setArIndex(int arIndex) {
	this.arIndex = arIndex;
}

public int getPrIndex() {
	return prIndex;
}

public void setPrIndex(int prIndex) {
	this.prIndex = prIndex;
}

public Instruction(String instrId) {
	super();
	this.instrId = instrId;
}


public Instruction(String instrId, String instr, Integer sReg1Addr, Integer sReg2Addr, Integer dRegAddr, String label,
		Integer literal, Integer instrAddress, Stage stage, int stallCount, String type, String fuType) {
	super();
	this.instrId = instrId;
	this.instr = instr;
	this.sReg1Addr = sReg1Addr;
	this.sReg2Addr = sReg2Addr;
	this.dRegAddr = dRegAddr;
	this.label = label;
	this.literal = literal;
	this.instrAddress = instrAddress;
	this.stage = stage;
	this.stallCount = stallCount;
	this.result = null;
	this.type = type;
	this.fuType = fuType;
	this.isRenamed = false;
	this.branchReady = false;
	tReg = null;
	res = null;
}


public Integer getRes() {
	return res;
}

public void setRes(Integer res) {
	this.res = res;
}

public String getInstrId() {
	return instrId;
}

public void setInstrId(String instrId) {
	this.instrId = instrId;
}

public Stage getStage() {
	return stage;
}

public void setStage(Stage stage) {
	this.stage = stage;
}

public ALUStage getAluStage() {
	return aluStage;
}

public void setAluStage(ALUStage aluStage) {
	this.aluStage = aluStage;
}

public int getStallCount() {
	return stallCount;
}

public void setStallCount(int stallCount) {
	this.stallCount = stallCount;
}

public String getInstr() {
	return instr;
}

public void setInstr(String instr) {
	this.instr = instr;
}

public Integer getsReg1Addr() {
	return sReg1Addr;
}

public void setsReg1Addr(Integer sReg1Addr) {
	this.sReg1Addr = sReg1Addr;
}

public Integer getsReg2Addr() {
	return sReg2Addr;
}

public void setsReg2Addr(Integer sReg2Addr) {
	this.sReg2Addr = sReg2Addr;
}

public Integer getdRegAddr() {
	return dRegAddr;
}

public void setdRegAddr(Integer dRegAddr) {
	this.dRegAddr = dRegAddr;
}

public String getLabel() {
	return label;
}

public void setLabel(String label) {
	this.label = label;
}

public Integer getLiteral() {
	return literal;
}

public void setLiteral(Integer literal) {
	this.literal = literal;
}

public Integer getInstrAddress() {
	return instrAddress;
}

public void setInstrAddress(Integer instrAddress) {
	this.instrAddress = instrAddress;
}

public Integer getResult() {
	return result;
}

public void setResult(Integer result) {
	this.result = result;
}

public Integer getOpr1() {
	return opr1;
}

public void setOpr1(Integer opr1) {
	this.opr1 = opr1;
}

public Integer getOpr2() {
	return opr2;
}

public void setOpr2(Integer opr2) {
	this.opr2 = opr2;
}

public Boolean getSignFlag() {
	return signFlag;
}

public void setSignFlag(Boolean signFlag) {
	this.signFlag = signFlag;
}

public Boolean getZeroFlag() {
	return zeroFlag;
}

public void setZeroFlag(Boolean zeroFlag) {
	this.zeroFlag = zeroFlag;
}

public Boolean getCarryFlag() {
	return carryFlag;
}

public void setCarryFlag(Boolean carryFlag) {
	this.carryFlag = carryFlag;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getFuType() {
	return fuType;
}

public void setFuType(String fuType) {
	this.fuType = fuType;
}

public static Boolean getBranchTaken() {
	return branchTaken;
}

public static void setBranchTaken(Boolean branchTaken) {
	Instruction.branchTaken = branchTaken;
}

public static int getBranchCount() {
	return branchCount;
}

public static void setBranchCount(int branchCount) {
	Instruction.branchCount = branchCount;
}

public void setStallCount(Integer stallCount) {
	this.stallCount = stallCount;
}

@Override
public String toString() {
	String str="( " + this.getInstrId() + "-" + this.getInstrAddress() + ") ";
	switch(this.type) {
	case "ATM": case "LG":
		if(!this.isRenamed)
			str = str + this.getInstr() + " R" + this.getdRegAddr() + " R" + this.getsReg1Addr() + " R" + this.getsReg2Addr();
		else
			str = str + this.getInstr() + " P" + this.getdRegAddr() + " P" + this.getsReg1Addr() + " P" + this.getsReg2Addr();
		break;
	case "MV":
		if(!this.isRenamed)
			str = str + this.getInstr() + " R" + this.getdRegAddr() + " #" + this.getLiteral();
		else
			str = str + this.getInstr() + " P" + this.getdRegAddr() + " #" + this.getLiteral();
		break;
	case "LS":
		if(this.getInstr().equals("LOAD")) {			
			if(!this.isRenamed)
				str = str + this.getInstr() + " R" + this.getdRegAddr()+ " R" + this.getsReg1Addr() + " #" + this.getLiteral();
			else	
				str = str + this.getInstr() + " P" + this.getdRegAddr()+ " P" + this.getsReg1Addr() + " #" + this.getLiteral();
		}else {
			if(!this.isRenamed)
				str = str + this.getInstr() + " R" + this.getsReg2Addr()+ " R" + this.getsReg1Addr() + " #" + this.getLiteral();
			else	
				str = str + this.getInstr() + " P" + this.getsReg2Addr()+ " P" + this.getsReg1Addr() + " #" + this.getLiteral();			
		}
		break;
	case "BR":
		str = str + this.getInstr() + " #" + this.getLiteral();
		break;
	case "JMP":
		if(!this.isRenamed)
			str = str + this.getInstr() + " R" + this.getsReg1Addr() + " #" + this.getLiteral();
		else
			str = str + this.getInstr() + " P" + this.getsReg1Addr() + " #" + this.getLiteral();
		break;
	case "HLT":
		str = str + "HALT";
		break;
	default:	
		break;
	}
	
	return str;
}

/*public String toString() {
	return "Instruction [instrId=" + instrId + ", instr=" + instr + ", sReg1Addr=" + sReg1Addr + ", sReg2Addr="
			+ sReg2Addr + ", dRegAddr=" + dRegAddr + ", label=" + label + ", literal=" + literal + ", instrAddress="
			+ instrAddress + ", stage=" + stage + ", stallCount=" + stallCount + ", result=" + result + "]";
}
*/

public boolean isFlowDependent(Instruction instruction) {
	boolean bDependent = false;
	if(instruction != null) {
		//this refers to the later instruction in the program order
		if(this.getsReg1Addr()!= null && instruction.getdRegAddr() != null && this.getsReg1Addr().equals(instruction.getdRegAddr())) {
			//flow dependency over source register1
			bDependent = true;
		}else if(this.getsReg2Addr() != null &&  instruction.getdRegAddr() != null && this.getsReg2Addr().equals(instruction.getdRegAddr())) {
			//flow dependency over source register2
			bDependent = true;
		}
	}
	return bDependent;
}

public Boolean isAwakened() {
	if(this.getInstr().equals("JUMP")) {
		if(this.getOpr1() != null || (/*this.getsReg1Addr() != null && */PipelineProcessor.registerFile.get(this.getsReg1Addr()).isStatus().equals(main.resources.Register.status.VALID))) {
			if(PipelineProcessor.registerFile.get(this.getsReg1Addr()).isStatus().equals(main.resources.Register.status.VALID))
			System.out.println("allowed : " + this.getOpr1() + " " +this.getsReg1Addr());
			return true;			
		}else {
			System.out.println("not allowed");
		}
	}
	if((this.getOpr1() != null || (this.getsReg1Addr() != null && PipelineProcessor.registerFile.get(this.getsReg1Addr()).isStatus() == main.resources.Register.status.VALID)) &&
			(this.getOpr2() != null || (this.getsReg2Addr() != null && PipelineProcessor.registerFile.get(this.getsReg2Addr()).isStatus() == main.resources.Register.status.VALID))
			)
		{
			System.out.println(this.getInstrId() + " Instruction awakened");
			return true;			

		}
		
		System.out.println("Instruction is not ready");
		return false;
}

public boolean isOutputDependent(Instruction instruction) {
	boolean bDependent = false;
	if(instruction != null) {
		//this refers to the later instruction in the program order
		if(this.getdRegAddr()!= null && instruction.getdRegAddr() != null && this.getdRegAddr().equals(instruction.getdRegAddr())) {
			//flow dependency over source register1
			bDependent = true;
		}
	}
	return bDependent;
	
}

}
