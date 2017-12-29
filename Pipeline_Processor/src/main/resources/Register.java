package main.resources;

import main.PipelineProcessor;

public class Register {
	public enum status{VALID,INVALID}
	public static FlagRegister flagRegisters;
	private String reg;
	private int address; 
	private status rStatus;
	Integer value;
	private Boolean zFlag;
	private Boolean cFlag;
	private Boolean sFlag;
	
	public status getrStatus() {
		return rStatus;
	}
	public void setrStatus(status rStatus) {
		this.rStatus = rStatus;
	}
	public Boolean getzFlag() {
		return zFlag;
	}
	public void setzFlag(Boolean zFlag) {
		this.zFlag = zFlag;
	}
	public Boolean getcFlag() {
		return cFlag;
	}
	public void setcFlag(Boolean cFlag) {
		this.cFlag = cFlag;
	}
	public Boolean getsFlag() {
		return sFlag;
	}
	public void setsFlag(Boolean sFlag) {
		this.sFlag = sFlag;
	}
	public Register(String reg, int address, status status) {
		flagRegisters = new FlagRegister();
		this.reg = reg;
		this.address = address;
		this.rStatus = status;
		this.value = 0;
		flagRegisters.carryFlag = false;
		flagRegisters.zeroFlag = false;
		flagRegisters.signFlag = false;
	}
	public String getReg() {
		return reg;
	}
	public void setReg(String reg) {
		this.reg = reg;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public status isStatus() {
		return rStatus;
	}
	public void setStatus(status status) {
		this.rStatus = status;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}

	public class FlagRegister{

		Boolean zeroFlag;
		Boolean carryFlag;
		Boolean signFlag;
		
		public FlagRegister() {
			super();
			this.zeroFlag = false;
			this.carryFlag = false;
			this.signFlag = false;
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
		public Boolean getSignFlag() {
			return signFlag;
		}
		public void setSignFlag(Boolean signFlag) {
			this.signFlag = signFlag;
		}
	}
	
	public static void displayAregisterFile() {				
		System.out.println("Register file :\n");
	for (int y = 0; y < 16; y += 2) {
		System.out.print(PipelineProcessor.registerFile.get(y).getReg() + " : " + PipelineProcessor.registerFile.get(y).getValue());
		System.out.print("\t" + PipelineProcessor.registerFile.get(y + 1).getReg() + " : "
				+ PipelineProcessor.registerFile.get(y + 1).getValue() + "\n");
	}


		
	}
	
	
}



