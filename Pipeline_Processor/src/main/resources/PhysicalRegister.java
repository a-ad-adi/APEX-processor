package main.resources;

import main.PipelineProcessor;

public class PhysicalRegister {
	public enum status {
		VALID, INVALID
	}

	private String reg;
	private int address;
	private status rStatus;
	private Integer value;
	private Boolean allocated;
	private Boolean renamed;
	private int src;
	private Boolean zFlag;
	private Boolean cFlag;
	private Boolean sFlag;

	public PhysicalRegister(String reg, int address, status rStatus, Integer value, Boolean allocated,
			Boolean renamed) {
		super();
		this.reg = reg;
		this.address = address;
		this.rStatus = rStatus;
		this.value = value;
		this.allocated = allocated;
		this.renamed = renamed;
		this.cFlag = false;
		this.sFlag = false;
		this.zFlag = false;

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

	public status getrStatus() {
		return rStatus;
	}

	public void setrStatus(status rStatus) {
		this.rStatus = rStatus;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Boolean getAllocated() {
		return allocated;
	}

	public void setAllocated(Boolean allocated) {
		this.allocated = allocated;
	}

	public Boolean getRenamed() {
		return renamed;
	}

	public void setRenamed(Boolean renamed) {
		this.renamed = renamed;
	}

	public int getSrc() {
		return src;
	}

	public void setSrc(int src) {
		this.src = src;
	}

	
	@Override
	public String toString() {
		return "PhysicalRegister [reg=" + reg + ", address=" + address + ", rStatus=" + rStatus + ", value=" + value
				+ ", allocated=" + allocated + ", renamed=" + renamed + ", src=" + src + ", zFlag=" + zFlag + ", cFlag="
				+ cFlag + ", sFlag=" + sFlag + "]";
	}

	public static void displayPregFile() {
		System.out.println("Physical register file : ");
		System.out.println("Reg   		Val   		Alc(1/T)   	Renm(1/T)   	zFlg(1/T)");
		for(PhysicalRegister pReg : PipelineProcessor.pRegisterFile) {
			
			
			System.out.println("P" + pReg.address 				+ "    		" +
								pReg.getValue() 				+ "    		" +
								(pReg.getAllocated()? 1 : 0)	+ "    		" +
								(pReg.getRenamed()? 1 : 0) 		+ "    		" +
								(pReg.getzFlag()? 1 : 0) 	  	+ "    		");
		}
		
		System.out.println("\n\n");
	}
	public static void makePregValid(Integer dReg) {
		for(int i=0; i<16; i++) {
			if(PipelineProcessor.renameTable.get(i).getIndex() != null && PipelineProcessor.renameTable.get(i).getLoc() != null && 
				PipelineProcessor.renameTable.get(i).getIndex() == dReg && 
				PipelineProcessor.renameTable.get(i).getLoc() == 0
					) {
				//System.out.println("valid now");
				PipelineProcessor.pRegisterFile.get(dReg).setrStatus(main.resources.PhysicalRegister.status.VALID);
				break;
				
			}
		}
	}
}
