package main.resources;

import java.nio.channels.Pipe;
import java.util.ArrayList;

import main.PipelineProcessor;
import main.data_structures.BIS;
import main.data_structures.RenameTableEntry;
import main.instruction.Instruction;
import main.pipeline.Stage;

public class ALU {
	public enum aluStatus {BUSY,FREE,IDLE};
	int currentStage;
	int stageCount;
	Instruction instr;
	Instruction[] stages;
	aluStatus status;
	Integer opr1, opr2, res, literal;
	
	public ALU(int currentStage, int stageCount, Instruction instr) {
		this.status = aluStatus.FREE;
		this.currentStage = currentStage;
		this.stageCount = stageCount;
		this.instr = instr;
		this.opr1 = null;
		this.opr2 = null;
		this.res = null;
		this.literal = null;
		this.stages = new Instruction[stageCount];
	}

	public aluStatus getStatus() {
		return status;
	}

	public void setStatus(aluStatus status) {
		this.status = status;
	}

	public Instruction[] getStages() {
		return stages;
	}

	public Instruction getStageInstruction(int stageIndex) {
		return stages[stageIndex];
	}

	public void setStage(int index, Instruction instr) {
		this.stages[index] = instr;
	}

	public int getCurrentStage() {
		return currentStage;
	}
	public void setCurrentStage(int currentStage) {
		this.currentStage = currentStage;
	}
	public int getStageCount() {
		return stageCount;
	}
	public void setStageCount(int stageCount) {
		this.stageCount = stageCount;
	}

	public void feedInstruction(Instruction instr) {
		this.setStage(0, instr);
		//System.out.println("Instruction found : " + this.stages[0].toString());
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

	public Integer getRes() {
		return res;
	}

	public void setRes(Integer res) {
		this.res = res;
	}

	//Arithmatic operations 
	
	public void add() {
		if(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr() != null && PipelineProcessor.intFU.getStageInstruction(0).getsReg2Addr() != null && 
				PipelineProcessor.intFU.getStageInstruction(0).getLiteral() == null) {
			
			loadOperands("INT");
			res = opr1 +
					opr2;
			PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
			if(res == 0) {
				PipelineProcessor.intFU.getStageInstruction(0).setZeroFlag(true);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).prIndex).setzFlag(true);
/*				System.out.println("dekh : " + PipelineProcessor.intFU.getStageInstruction(0).prIndex + " " + 
						PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).prIndex).getzFlag()
						);
*/			}else {
				PipelineProcessor.intFU.getStageInstruction(0).setZeroFlag(false);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).prIndex).setzFlag(false);
			}
		}else if(PipelineProcessor.intFU.getStageInstruction(0).getLiteral() != null){

			opr1 = PipelineProcessor.registerFile.get(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()).getValue();
			literal = PipelineProcessor.intFU.getStageInstruction(0).getLiteral();
			res = opr1 + literal;
			PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
			if(res == 0) {
				PipelineProcessor.intFU.getStageInstruction(0).setZeroFlag(true);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).prIndex).setzFlag(true);
			}else {
				PipelineProcessor.intFU.getStageInstruction(0).setZeroFlag(false);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).prIndex).setzFlag(true);
			}			
		}
		System.out.println("Add : " + opr1 + " + " + opr2 + " = " + res);

	}
	public void addc() {
		if(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr() != null && PipelineProcessor.intFU.getStageInstruction(0).getsReg2Addr() != null 
				&& PipelineProcessor.intFU.getStageInstruction(0).getLiteral() == null) {
			
			loadOperands("INT");
			res = opr1 + opr2;
			PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
		}else {

			opr1 = PipelineProcessor.registerFile.get(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()).getValue();
			literal = PipelineProcessor.intFU.getStageInstruction(0).getLiteral();
			res = opr1 + literal;
			PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
			
		}
		System.out.println("Add : " + opr1 + " + " + opr2 + " = " + res);
		
	}
	public void sub() {
		if(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr() != null && PipelineProcessor.intFU.getStageInstruction(0).getsReg2Addr() != null 
				&& PipelineProcessor.intFU.getStageInstruction(0).getLiteral() == null) {
			
			loadOperands("INT");
			System.out.println("Opr1 stat : " +PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()).getrStatus());
			res = opr1 - 
					opr2;
			PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
			if(res == 0) {
				PipelineProcessor.intFU.getStageInstruction(0).setZeroFlag(true);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).prIndex).setzFlag(true);
			}else {
				PipelineProcessor.intFU.getStageInstruction(0).setZeroFlag(false);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).prIndex).setzFlag(false);
			}
			
		}else {

			opr1 = PipelineProcessor.registerFile.get(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()).getValue();
			literal = PipelineProcessor.intFU.getStageInstruction(0).getLiteral();
			res = opr1 - literal;
			PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
			if(res == 0) {
				PipelineProcessor.intFU.getStageInstruction(0).setZeroFlag(true);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).prIndex).setzFlag(true);
			}else {
				PipelineProcessor.intFU.getStageInstruction(0).setZeroFlag(false);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).prIndex).setzFlag(false);
			}
		}
		System.out.println("Sub : " + opr1 + " - " + opr2 + " = " + res);

	}
	public static void subc() {
		
	}
	public void mul() {
		if(PipelineProcessor.mulFU.getStageInstruction(0).getsReg1Addr() != null && PipelineProcessor.mulFU.getStageInstruction(0).getsReg2Addr() != null) {
			
			this.loadOperands("MUL");
			//System.out.println(opr1 +" " + opr2);
			res = opr1 
					* opr2;
			PipelineProcessor.mulFU.getStageInstruction(0).setResult(res);
			if(res == 0) {
				System.out.println("set z");
				PipelineProcessor.mulFU.getStageInstruction(0).setZeroFlag(true);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.mulFU.getStageInstruction(0).prIndex).setzFlag(true);
			}else {
				PipelineProcessor.mulFU.getStageInstruction(0).setZeroFlag(false);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.mulFU.getStageInstruction(0).prIndex).setzFlag(false);
				System.out.println("no z");
			}
		}else if(PipelineProcessor.mulFU.getStageInstruction(0).getLiteral() == null){

			opr1 = PipelineProcessor.registerFile.get(PipelineProcessor.mulFU.getStageInstruction(0).getsReg1Addr()).getValue();
			literal = PipelineProcessor.mulFU.getStageInstruction(0).getLiteral();
			res = opr1 * literal;
			PipelineProcessor.mulFU.getStageInstruction(0).setResult(res);
			if(res == 0) {
				PipelineProcessor.mulFU.getStageInstruction(0).setZeroFlag(true);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.mulFU.getStageInstruction(0).prIndex).setzFlag(true);
			}else {
				PipelineProcessor.mulFU.getStageInstruction(0).setZeroFlag(false);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.mulFU.getStageInstruction(0).prIndex).setzFlag(false);
			}
			
		}
		System.out.println("MUL : " + opr1 + " * " + opr2 + " = " + res);

	}
	public void div() {
		
		if(PipelineProcessor.execD1.getInstr() != null && PipelineProcessor.execD1.getInstr().getsReg2Addr() != null 
				&& PipelineProcessor.execD1.getInstr().getLiteral() == null) {
			
			loadOperands("DIV");
			System.out.println("el : " + opr1 + opr2);
			res = opr1 / opr2;
			PipelineProcessor.execD1.getInstr().setResult(res);
			if(res == 0) {
				PipelineProcessor.divFU.getStageInstruction(0).setZeroFlag(true);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.divFU.getStageInstruction(0).prIndex).setzFlag(true);
			}else {
				PipelineProcessor.divFU.getStageInstruction(0).setZeroFlag(false);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.divFU.getStageInstruction(0).prIndex).setzFlag(false);
			}
		}/*else if(PipelineProcessor.execD1.getInstr().getLiteral() != null) {

			opr1 = PipelineProcessor.registerFile.get(PipelineProcessor.execD1.getInstr().getsReg1Addr()).getValue();
			literal = PipelineProcessor.execD1.getInstr().getLiteral();
			res = opr1 / literal;
			PipelineProcessor.execD1.getInstr().setResult(res);
			if(res == 0) {
				PipelineProcessor.divFU.getStageInstruction(0).setZeroFlag(true);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.divFU.getStageInstruction(0).prIndex).setzFlag(true);
			}else {
				PipelineProcessor.divFU.getStageInstruction(0).setZeroFlag(false);
				PipelineProcessor.pRegisterFile.get(PipelineProcessor.divFU.getStageInstruction(0).prIndex).setzFlag(false);
			}
		}*/
		System.out.println("DIV : " + opr1 + " / " + opr2 + " = " + res);
		
	}

	//Move instructions
	
	public void mov() {
		loadOperands("INT");
		PipelineProcessor.intFU.getStageInstruction(0).setResult(opr1); 
		
	}

	public void movc() {
		loadOperands("INT");
		System.out.println("LOAD Result : " + opr1);
		PipelineProcessor.intFU.getStageInstruction(0).setResult(opr1);

	}
	//Load store instructions
	
	public void load() {
		loadOperands("INT");		res = opr1 + opr2;
		System.out.println("Load adderss " + res);
		PipelineProcessor.intFU.getStageInstruction(0).setResult(res); 
		
	}

	public void store() {
		loadOperands("INT");
		res = opr1 + opr2;
		System.out.println("Store adderss " + res);
		PipelineProcessor.intFU.getStageInstruction(0).setResult(res); 
		
	}

	//logical instructions
	
	public void and() {
		if(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr() != null && PipelineProcessor.intFU.getStageInstruction(0).getsReg2Addr() != null) {
			
			loadOperands("INT");
			res = opr1 & opr2;
			PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
		}
		
		
	}
	
	public void or() {
		if(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr() != null && PipelineProcessor.intFU.getStageInstruction(0).getsReg2Addr() != null) {
			
			loadOperands("INT");
			res = opr1 | opr2;
			PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
		}
		
	}

	public void xor() {
		if(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr() != null && PipelineProcessor.intFU.getStageInstruction(0).getsReg2Addr() != null) {
			
			loadOperands("INT");
			res = opr1 ^ opr2;
			PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
		}
		
		
	}

	public static void not() {
		
	}

	//branch instructions
	public void bz() {
		System.out.println("Branch instr -- : ");
		//if(Register.flagRegisters.getZeroFlag() == true) {
		if(this.getStageInstruction(0).getZeroFlag() == true) {
			PipelineProcessor.pc = PipelineProcessor.intFU.getStageInstruction(0).getInstrAddress() + PipelineProcessor.intFU.getStageInstruction(0).getLiteral();
			if(PipelineProcessor.fetch.getInstr() != null)
			System.out.println("Fetch flush : " + PipelineProcessor.fetch.getInstr().toString());
			if(PipelineProcessor.decodeRF.getInstr() != null)
			System.out.println("DecodeRF flush : " + PipelineProcessor.decodeRF.getInstr().toString());
			PipelineProcessor.fetch.setInstr(null);
			PipelineProcessor.decodeRF.setInstr(null);
			BIS.revertStage(this.getStageInstruction(0).getInstrAddress());
			System.out.println("## BZ taken");
			System.out.println("New PC : " +  PipelineProcessor.pc);
			Stage.isFlushed = true;
			Instruction.branchTaken = true;
			Instruction.branchCount ++;
		}else {
			System.out.println("## BZ not taken");
		}

	}
	
	public void bnz() {
		System.out.println("Zero flag : " + this.getStageInstruction(0).getZeroFlag());
		//if(Register.flagRegisters.getZeroFlag() == false) {
		
		if(this.getStageInstruction(0).getZeroFlag() == false) {
			System.out.println("BNZ taken");
			PipelineProcessor.pc = PipelineProcessor.intFU.getStageInstruction(0).getInstrAddress() + PipelineProcessor.intFU.getStageInstruction(0).getLiteral();
			System.out.println("Fetch flush : " + PipelineProcessor.fetch.getInstr().toString());
			System.out.println("DecodeRF flush : " + PipelineProcessor.decodeRF.getInstr().toString());
			PipelineProcessor.fetch.setInstr(null);
			PipelineProcessor.decodeRF.setInstr(null);
			BIS.revertStage(this.getStageInstruction(0).getInstrAddress());
			System.out.println("New PC : " +  PipelineProcessor.pc);
			Stage.isFlushed = true;
			Instruction.branchTaken = true;
			Instruction.branchCount ++;

		}else System.out.println("BNZ not taken");

	}
	
	//jump instructions
	public void jal() {
		System.out.println("JAL exec");
		//System.out.println("Implementation remaining..");
	
		//1. save pc + 4 into dest register
		//2. new instruction address : sreg + literal
		//3. flush fetch + decode
		//4. update branch related flags
		//5. change pc
		//2
		loadOperands("INT");
		//res = PipelineProcessor.pc + 4;
		res = this.getStageInstruction(0).getInstrAddress() + 4;
		PipelineProcessor.intFU.getStageInstruction(0).setResult(res);
		//3
		PipelineProcessor.fetch.setInstr(null);
		PipelineProcessor.decodeRF.setInstr(null);
		//4
		Stage.isFlushed = true;
		Instruction.branchTaken = true;
		Instruction.branchCount ++;
		//5
		
		if(PipelineProcessor.intFU.getStageInstruction(0).getOpr1() != null) {
			PipelineProcessor.pc = PipelineProcessor.intFU.getStageInstruction(0).getOpr1() + 
					PipelineProcessor.intFU.getStageInstruction(0).getLiteral();			

		}
		else if(renameTabLoc(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()) == 0) {
			PipelineProcessor.pc = PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()).getValue() + 
					PipelineProcessor.intFU.getStageInstruction(0).getLiteral();			
		}else {
			PipelineProcessor.pc = PipelineProcessor.registerFile.get(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()).getValue() + 
					PipelineProcessor.intFU.getStageInstruction(0).getLiteral();						
		}
		System.out.println("jal next addr : " + PipelineProcessor.pc);
	}
	
	public void jump() {
		if(PipelineProcessor.intFU.getStageInstruction(0).getOpr1() != null) {
			PipelineProcessor.pc = PipelineProcessor.intFU.getStageInstruction(0).getOpr1() 
					+ PipelineProcessor.intFU.getStageInstruction(0).getLiteral();			
			
		}
		else if(renameTabLoc(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()) == 1) {
			PipelineProcessor.pc = PipelineProcessor.registerFile.get(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()).getValue() 
					+ PipelineProcessor.intFU.getStageInstruction(0).getLiteral();			
		}else {
			PipelineProcessor.pc = PipelineProcessor.pRegisterFile.get(PipelineProcessor.intFU.getStageInstruction(0).getsReg1Addr()).getValue() 
					+ PipelineProcessor.intFU.getStageInstruction(0).getLiteral();			
			
		}
		PipelineProcessor.fetch.setInstr(null);
		PipelineProcessor.decodeRF.setInstr(null);
		Stage.isFlushed = true;
		Stage.isJumped = true;
		System.out.println("Jump in exec");
	}
	
	public void loadOperands(String fu) {
		if(fu == "INT") {
			//System.out.println(PipelineProcessor.execI.getInstr().toString() + " " + PipelineProcessor.execI.getInstr().getOpr1()+ " " + PipelineProcessor.execI.getInstr().getOpr2());
			switch(PipelineProcessor.execI.getInstr().getInstr()) {
		
			case "ADD": case "ADDC": case "SUB": case "AND": case "OR": case "XOR":
				//load opr1
				if(renameTabLoc(this.getStageInstruction(0).getsReg1Addr()) == 0) {					
					//change
					System.out.println("P reg");
					if(this.getStageInstruction(0).getOpr1() != null)
						this.setOpr1(this.getStageInstruction(0).getOpr1());
					else if(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg1Addr()).getrStatus().equals(main.resources.PhysicalRegister.status.VALID))
						this.setOpr1(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue());
					
				}else {
					System.out.println("Commited value");
					if(this.getStageInstruction(0).getOpr1() != null)
						this.setOpr1(this.getStageInstruction(0).getOpr1());
					else if(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue() != null)
						this.setOpr1(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue());
						
					
				}
				//load opr2
				if(renameTabLoc(this.getStageInstruction(0).getsReg2Addr()) == 0) {
					if(this.getStageInstruction(0).getOpr2() != null)
						this.setOpr2(this.getStageInstruction(0).getOpr2());
					else if(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg2Addr()).getValue() != null)
						this.setOpr2(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg2Addr()).getValue()); 
						
				}else {
					if(this.getStageInstruction(0).getOpr2() != null)
						this.setOpr2(this.getStageInstruction(0).getOpr2());
					else if(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg2Addr()).getValue() != null)
						this.setOpr2(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg2Addr()).getValue());
 
					
				}
				break;	
				
			case "LOAD":
				//load opr1
				if(renameTabLoc(this.getStageInstruction(0).getsReg1Addr()) == 0) {
					if(this.getStageInstruction(0).getOpr1() != null)
						this.setOpr1(this.getStageInstruction(0).getOpr1());			
					else if(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue() != null)
						this.setOpr1(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue());
					
				}else {
					if(this.getStageInstruction(0).getOpr1() != null)
						this.setOpr1(this.getStageInstruction(0).getOpr1());			
					else if(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue() != null)
						this.setOpr1(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue());
					
				}
				//load opr2
				this.setOpr2(this.getStageInstruction(0).getLiteral());
				break;
			
			case "STORE":	
				//load opr1
				if(renameTabLoc(this.getStageInstruction(0).getsReg1Addr()) == 0) {
					
					if(this.getStageInstruction(0).getOpr1() != null)
						this.setOpr1(this.getStageInstruction(0).getOpr1());
					else if(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue() != null)
						this.setOpr1(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue());

				}else {
					
					if(this.getStageInstruction(0).getOpr1() != null)
						this.setOpr1(this.getStageInstruction(0).getOpr1());
					else if(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue() != null)
						this.setOpr1(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue());
					
				}
				//load opr2
				this.setOpr2(this.getStageInstruction(0).getLiteral());			
				break;
				
			case "MOV" : case "MOVC":
				//load opr1
					this.setOpr1(this.getStageInstruction(0).getLiteral());
				break;
				
			case "JUMP":
				break;
			
			case "JAL":	
				//load opr1
				if(renameTabLoc(this.getStageInstruction(0).getsReg1Addr()) == 0) {
					
					if(this.getStageInstruction(0).getOpr1() != null)
						this.setOpr1(this.getStageInstruction(0).getOpr1());
					if(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue() != null)
						this.setOpr1(PipelineProcessor.pRegisterFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue());
				}else {
					if(this.getStageInstruction(0).getOpr1() != null)
						this.setOpr1(this.getStageInstruction(0).getOpr1());
					else if(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue() != null)
						this.setOpr1(PipelineProcessor.registerFile.get(this.getStageInstruction(0).getsReg1Addr()).getValue());

					
				}
				//load opr2
				this.setOpr2(this.getStageInstruction(0).getLiteral());
				break;
				
			case "BZ": case "BNZ":
				break;
			
			case "HALT":
				break;
			
			default:	
				break;
			}
			
		}else if(fu == "MUL") {
			//load opr1
			if(renameTabLoc(this.getStageInstruction(0).getsReg1Addr()) == 0) {

				if(PipelineProcessor.execM1.getInstr().getOpr1() != null)
					this.setOpr1(PipelineProcessor.execM1.getInstr().getOpr1());
				else if(PipelineProcessor.pRegisterFile.get(PipelineProcessor.execM1.getInstr().getsReg1Addr()).getValue() != null)
					this.setOpr1(PipelineProcessor.pRegisterFile.get(PipelineProcessor.execM1.getInstr().getsReg1Addr()).getValue());

			}else {
				if(PipelineProcessor.execM1.getInstr().getOpr1() != null)
					this.setOpr1(PipelineProcessor.execM1.getInstr().getOpr1());
				else if(PipelineProcessor.registerFile.get(PipelineProcessor.execM1.getInstr().getsReg1Addr()).getValue() != null)
					this.setOpr1(PipelineProcessor.registerFile.get(PipelineProcessor.execM1.getInstr().getsReg1Addr()).getValue());

				
			}	
			//load opr2
			if(renameTabLoc(this.getStageInstruction(0).getsReg2Addr()) == 0) {
				if(PipelineProcessor.pRegisterFile.get(PipelineProcessor.execM1.getInstr().getsReg2Addr()).getValue() != null)
					this.setOpr2(PipelineProcessor.pRegisterFile.get(PipelineProcessor.execM1.getInstr().getsReg2Addr()).getValue());
				else 
					this.setOpr2(PipelineProcessor.execM1.getInstr().getOpr2());
			}else {
				if(PipelineProcessor.execM1.getInstr().getOpr2() != null)
					this.setOpr2(PipelineProcessor.execM1.getInstr().getOpr2());
				if(PipelineProcessor.registerFile.get(PipelineProcessor.execM1.getInstr().getsReg2Addr()).getValue() != null)
					this.setOpr2(PipelineProcessor.registerFile.get(PipelineProcessor.execM1.getInstr().getsReg2Addr()).getValue());

			}	
			
		}else if(fu == "DIV"){
			//load opr1
			System.out.println(this.getStageInstruction(0).toString());
			if(renameTabLoc(this.getStageInstruction(0).getsReg1Addr()) == 0) {
				if(PipelineProcessor.execD1.getInstr().getOpr1() != null)
					this.setOpr1(PipelineProcessor.execD1.getInstr().getOpr1());
				else if(PipelineProcessor.pRegisterFile.get(PipelineProcessor.execD1.getInstr().getsReg1Addr()).getValue() != null)
					this.setOpr1(PipelineProcessor.pRegisterFile.get(PipelineProcessor.execD1.getInstr().getsReg1Addr()).getValue());

			}else {

				if(PipelineProcessor.execD1.getInstr().getOpr1() != null)
					this.setOpr1(PipelineProcessor.execD1.getInstr().getOpr1());
				else if(PipelineProcessor.registerFile.get(PipelineProcessor.execD1.getInstr().getsReg1Addr()).getValue() != null)
					this.setOpr1(PipelineProcessor.registerFile.get(PipelineProcessor.execD1.getInstr().getsReg1Addr()).getValue());

				
			}	
			//load opr2
			if(renameTabLoc(this.getStageInstruction(0).getsReg2Addr()) == 0) {
				if(PipelineProcessor.pRegisterFile.get(PipelineProcessor.execD1.getInstr().getsReg2Addr()).getValue() != null)
					this.setOpr2(PipelineProcessor.pRegisterFile.get(PipelineProcessor.execD1.getInstr().getsReg2Addr()).getValue());
				else 
					this.setOpr2(PipelineProcessor.execD1.getInstr().getOpr2());
			}else {
				if(PipelineProcessor.execD1.getInstr().getOpr2() != null)
					this.setOpr2(PipelineProcessor.execD1.getInstr().getOpr2());
				else if(PipelineProcessor.registerFile.get(PipelineProcessor.execD1.getInstr().getsReg2Addr()).getValue() != null)
					this.setOpr2(PipelineProcessor.registerFile.get(PipelineProcessor.execD1.getInstr().getsReg2Addr()).getValue());
				
			}
			
		}
	}
	
	public static int renameTabLoc(int ind) {
		int loc = 0;
		
		//RenameTableEntry.dispalyRenameTable();
		for(int i=0; i< PipelineProcessor.renameTable.size(); i++) {
			if(PipelineProcessor.renameTable.get(i).getIndex() == ind) {
				if(PipelineProcessor.renameTable.get(i).getLoc() == 0) {
					loc = 0;
				}else
					loc = 1;
				System.out.println("I : " + i);
				break;
			}
	}
		System.out.println(ind + " LOC : " + loc + " ");
	return loc;
	}
}
