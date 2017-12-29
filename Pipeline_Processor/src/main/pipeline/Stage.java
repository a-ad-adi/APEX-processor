package main.pipeline;

import main.PipelineProcessor;
import main.instruction.Instruction;

public class Stage {
	public enum stageStatus{FREE,BUSY,IDLE,STOP};
	Instruction instr;
	stageStatus status;
	public static Boolean isFlushed;
	public static Boolean isHalted;
	public static Boolean isJumped;
	public Stage(Instruction instr, stageStatus free) {
		super();
		this.instr = instr;
		this.status = free;
		isFlushed = false;
		isHalted = false;
		isJumped = false;
	}

	public Instruction getInstr() {
		return instr;
	}

	public void setInstr(Instruction instr) {
		this.instr = instr;
	}

	public stageStatus getStatus() {
		return status;
	}

	public void setStatus(stageStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Stage [instr=" + instr + ", status=" + status + "]";
	}
	
	public void makeRegistersInvalid() {
/*		if(this.getInstr().getsReg1Addr() != null)
		PipelineProcessor.registerFile.get(this.getInstr().getsReg1Addr()).setStatus(main.resources.Register.status.INVALID);
		if(this.getInstr().getsReg2Addr() != null)
			PipelineProcessor.registerFile.get(this.getInstr().getsReg2Addr()).setStatus(main.resources.Register.status.INVALID);
*/		if(this.getInstr().getdRegAddr() != null)
			PipelineProcessor.registerFile.get(this.getInstr().getdRegAddr()).setStatus(main.resources.Register.status.INVALID);

	}

	public void makeRegistersValid() {
/*		if(this.getInstr().getsReg1Addr() != null)
		PipelineProcessor.registerFile.get(this.getInstr().getsReg1Addr()).setStatus(main.resources.Register.status.VALID);
		if(this.getInstr().getsReg2Addr() != null)
			PipelineProcessor.registerFile.get(this.getInstr().getsReg2Addr()).setStatus(main.resources.Register.status.VALID);
		if(this.getInstr().getdRegAddr() != null)
*/			PipelineProcessor.registerFile.get(this.getInstr().getdRegAddr()).setStatus(main.resources.Register.status.VALID);

	}
}
