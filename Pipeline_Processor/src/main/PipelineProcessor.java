package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import main.Display.CycleInfo;
import main.file.FileIO;
import main.instruction.Instruction;
import main.pipeline.Stage;
import main.pipeline.Stage.stageStatus;
import main.resources.ALU;
import main.resources.ALU.aluStatus;
import main.resources.Code;
import main.resources.Data;
import main.data_structures.BIS;
import main.data_structures.IQEntry;
import main.data_structures.LSQEntry;
import main.data_structures.RenameTableEntry;
import main.resources.PhysicalRegister;
import main.resources.Register;
import main.resources.Register.status;
import main.data_structures.RobEntry;

public class PipelineProcessor {
	public static List<Register> registerFile = new ArrayList<Register>();
	public static List<PhysicalRegister> pRegisterFile = new ArrayList<PhysicalRegister>();
	public static List<IQEntry> issueQueue = new ArrayList<IQEntry>();
	public static List<LSQEntry> LSQ = new ArrayList<LSQEntry>();
	public static List<RobEntry> ROB = new ArrayList<RobEntry>();
	// static List<Data> dataMemory = new ArrayList<Data>();
	public static Data[] dataMemory = new Data[2000];
	public static List<Code> codeMemory = new ArrayList<Code>();
	public static List<Instruction> instructionList = new ArrayList<Instruction>();
	public static Stage fetch, decodeRF, execI, execM1, execM2, execD1, execD2, execD3, execD4, execute, memory, memS2, memS3,
			writeBack;
	public static ALU intFU, mulFU, divFU;
	public static int n;
	public static int cycle;
	static int numberOfInstructions;
	public static Integer pc;
	public static CycleInfo[] ganttChart;
	public static CycleInfo[] cycleInfo;
	public static List<RenameTableEntry> renameTable = new ArrayList<RenameTableEntry>();
	public static List<Instruction> commitInstr = new ArrayList<>();
	public static HashMap<Integer,BIS> bisList = new HashMap<Integer,BIS>();
	
	public static List<RobEntry> tRob = new ArrayList<RobEntry>();
	public static List<IQEntry> tIq = new ArrayList<IQEntry>();
	public static List<Instruction> tCmi = new ArrayList<Instruction>();
	public static List<LSQEntry> tLsq = new ArrayList<LSQEntry>();
	public static List<RenameTableEntry> tRmt = new ArrayList<RenameTableEntry>();
	public static Integer rAInstrReg = null;
	
	public static Integer val= null;
	public static Integer ic = 0;
	// public static List<String[]> listOfInstructions;
	// public static List<Instruction> listOfInstr = new ArrayList<Instruction>();
	public static String sFetch = "Empty", sDecodeRF = "Empty", 
						 sIntFU = "Empty", 
						 sMul1 = "Empty", sMul2 = "Empty", 
						 sDiv1 = "Empty", sDiv2 = "Empty", sDiv3 = "Empty", sDiv4 = "Empty", 
						 sMemory = "Empty", 						 
						 sWriteBack = "Empty",
						 sMem1 = "Empty",sMem2 = "Empty",sMem3 = "Empty";

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println(
				"\n\n........................ Welcome to Simple APEX simulation .........................\n\n");
		int choice = 0;
		while (choice != 4) {
			System.out.println("\n\n" + "1. Initialise	\n" + "2. Simulate		\n" + "3. Display		\n"
					+ "4. Exit			\n");
			System.out.println("\n Enter your choice \n");
			choice = s.nextInt();
			switch (choice) {
			case 1:
				System.out.println("\n\n........................ Initialization .........................\\n\\n");
				try {
					init(args[0].trim());
				} catch (ArrayIndexOutOfBoundsException aob) {
					System.out.println("File path not provided..");
				} catch (Exception e) {
					System.out.println("Something went wrong..");
				}
				break;
			case 2:
				System.out.println("\n\n........................ Simulation .........................\\n\\n");
				System.out.println("Enter the value of n:");
				s = new Scanner(System.in);
				n = s.nextInt();

				// initialization required for fresh start
				ganttChart = new CycleInfo[500];
				cycleInfo = new CycleInfo[500];
				cycle = 0;
				Stage.isFlushed = false;
				Stage.isHalted = false;
				pc = 4000;
				if (n > 0)
					simulate(n);
				else
					System.out.println("Invalid n..");
				break;
			case 3:
				System.out.println("\n\n........................ Memory map .........................\n\n");

				Data.displayMemory();
				RobEntry.displayROB();
				Register.displayAregisterFile();
				PhysicalRegister.displayPregFile();
				IQEntry.displayIQ();
				CycleInfo.displayGanttChart();
				// CycleInfo.displayCycleInfo();
				break;
			case 4:
				System.out.println("Bye bye...");
				// FileIO.genereateSummary();
				CycleInfo.displayCycleInfo();
				s.close();
				break;
			case 5:
				//CycleInfo.displayGanttChart();				
				 CycleInfo.displayCycleInfo();

				break;
			default:
				System.out.println("Invalid choice...");
				break;
			}

		}
	}

	public static void init(String filePath) {
		// create architectuaral register file
		registerFile.add(new Register("R0", 0, status.VALID));
		registerFile.add(new Register("R1", 1, status.VALID));
		registerFile.add(new Register("R2", 2, status.VALID));
		registerFile.add(new Register("R3", 3, status.VALID));
		registerFile.add(new Register("R4", 4, status.VALID));
		registerFile.add(new Register("R5", 5, status.VALID));
		registerFile.add(new Register("R6", 6, status.VALID));
		registerFile.add(new Register("R7", 7, status.VALID));
		registerFile.add(new Register("R8", 8, status.VALID));
		registerFile.add(new Register("R9", 9, status.VALID));
		registerFile.add(new Register("R10", 10, status.VALID));
		registerFile.add(new Register("R11", 11, status.VALID));
		registerFile.add(new Register("R12", 12, status.VALID));
		registerFile.add(new Register("R13", 13, status.VALID));
		registerFile.add(new Register("R14", 14, status.VALID));
		registerFile.add(new Register("R15", 15, status.VALID));

		// create physical register file
		for (int i = 0; i < 32; i++) {
			pRegisterFile.add(
					new PhysicalRegister("P" + i, i, main.resources.PhysicalRegister.status.VALID, null, false, false));
		}

		// create rename table
		for (int i = 0; i < 16; i++) {
			renameTable.add(new RenameTableEntry(i, null));
		}

		/*
		 * //create LSQ and ROB for(int i=0; i<32; i++) { LSQ.add(null); ROB.add(null);
		 * }
		 */

		System.out.println("Registers are initialized..\nAvailable architectural registers : R0-R15");

		System.out.println("Register file is ready..");

		// Make pipeline stages free
		fetch = new Stage(null, Stage.stageStatus.FREE);
		decodeRF = new Stage(null, Stage.stageStatus.FREE);
		execute = new Stage(null, Stage.stageStatus.FREE);
		execI = new Stage(null, Stage.stageStatus.FREE);
		execM1 = new Stage(null, Stage.stageStatus.FREE);
		execM2 = new Stage(null, Stage.stageStatus.FREE);
		execD1 = new Stage(null, Stage.stageStatus.FREE);
		execD2 = new Stage(null, Stage.stageStatus.FREE);
		execD3 = new Stage(null, Stage.stageStatus.FREE);
		execD4 = new Stage(null, Stage.stageStatus.FREE);
		memory = new Stage(null, Stage.stageStatus.FREE);
		memS2 = new Stage(null, Stage.stageStatus.FREE);
		memS3 = new Stage(null, Stage.stageStatus.FREE);
		writeBack = new Stage(null, Stage.stageStatus.FREE);
		System.out.println("Processor pipeline stages are ready");

		// ready FUs
		intFU = new ALU(0, 1, null);
		mulFU = new ALU(0, 2, null);
		divFU = new ALU(0, 4, null);

		// create data memory
		for (int i = 0, j = 0; i < 999; i++, j += 4) {
			/*
			 * dataMemory.get(i).setAddress(j); dataMemory.get(i).setData(null);
			 */ dataMemory[i] = new Data(null, j);
			/*
			 * dataMemory[i].setAddress(j); dataMemory[i].setData(null);
			 */
		}

		// Read file and convert to list<Instruction>
		// create code memory
		openFile(filePath);
	}

	public static void openFile(String filePath) {
		System.out.println(filePath);
		FileIO file = new FileIO();
		file.setFilePath(filePath);
		System.out.println("Assembly program: ");
		file.readFile();
		for (Code code : codeMemory) {

			System.out.println(code.getAddress() + " " + code.getInstructionString());
		}

	}

	public static void simulate(int simulationCycles) {
		int i = 0;

		numberOfInstructions = PipelineProcessor.codeMemory.size();
		System.out.println("Execution starts...");
		try {

			while (cycle < simulationCycles/* && i < numberOfInstructions */) {
				sFetch = "Empty";
				sDecodeRF = "Empty";
				sIntFU = "Empty";
				sMul1 = "Empty";
				sMul2 = "Empty";
				sDiv1 = "Empty";
				sDiv2 = "Empty";
				sDiv3 = "Empty";
				sDiv4 = "Empty";
				sMemory = "Empty";
				sWriteBack = "Empty";
				sMem1 = "Empty";
				sMem2 = "Empty";
				sMem3 = "Empty";

				if (Instruction.branchTaken) {
					System.out.println("--------------------------------------------------------------------------");
					System.out.println("Branch " + Instruction.branchCount);
					Instruction.branchTaken = false;
				}
				System.out.println(
						"-------------------------------------------------------------------------------------------------------------------------------------");
				System.out.println("Cycle : " + (cycle + 1));

				// if the fetch stage is cycle
				if (fetch.getStatus() == stageStatus.FREE && pc != null && Stage.isHalted == false) {

					// if new instruction waiting for fetch
					int index = (pc - 4000) / 4;
					if (index < numberOfInstructions) {
						fetch.setStatus(stageStatus.BUSY);
						codeMemory.get(index).setStage(main.instruction.Instruction.Stage.FETCH_START);
						fetch(codeMemory.get(index).getInstruction(), codeMemory.get(index).getAddress(), index);
						System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t Fetch : " + fetch.getInstr().getInstrId());
						codeMemory.get(index).setStage(main.instruction.Instruction.Stage.FETCH_COMPLETE);
					} else {
						fetch.setStatus(stageStatus.IDLE);
						System.out.println("Fetch stage is idle..");
					}
				}

				// if the instruction in decode stage is ready to decode
				if (decodeRF.getInstr() != null && decodeRF.getInstr().getStage() == main.instruction.Instruction.Stage.DECODE_START) {

					decode();
					System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t Decode : " + decodeRF.getInstr().toString());
				}

				// Execution stages
				// int FU
				execIntFU();

				// MUL FU
				execMul1FU();

				
				if (execM2.getInstr() != null) {
					System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t M2 : " + execM2.getInstr().toString());
					execMul2FU();
				}

				// DIV FU
				execDiv1FU();
				
				if (execD2.getInstr() != null
						&& execD2.getInstr().getStage() == main.instruction.Instruction.Stage.D2_START) {
					System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t D2 : " + execD2.getInstr().toString());
					execDiv2FU();
				}

				if (execD3.getInstr() != null
						&& execD3.getInstr().getStage() == main.instruction.Instruction.Stage.D3_START) {
					System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t D3 : " + execD3.getInstr().toString());
					execDiv3FU();
				}

				if (execD4.getInstr() != null
						&& execD4.getInstr().getStage() == main.instruction.Instruction.Stage.D4_START) {
					System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t D4 : " + execD4.getInstr().toString());
					execDiv4FU();
				}

					
				mem1();
				
				mem2();
				
				mem3();

				//if(ROB.size() > 0) RobEntry.commitInstr();
				updatePipelineStages();
				// if fetch stage is free to take new instruction, then increment i, else don't
				if (fetch.getStatus() == stageStatus.FREE)
					i++;
				cycle++;
				
				updatePc();
				if(issueQueue.isEmpty() && LSQ.isEmpty()) 
					val = cycle;
						
			}
				
			if(val != null)
				System.out.println("CPI is : " + val / ic);
			else
				System.out.println("CPI is : " + simulationCycles/ic);
			
		} catch (ArrayIndexOutOfBoundsException aob) {
			aob.printStackTrace();
			System.out.println("Computed address was invalid...");
		} catch (Exception e) {
			System.out.println("Error occoured while execution");
			e.printStackTrace();
		}
	}

	public static void fetch(String[] instructionString, int instrAddr, int instrNumber) {

		Instruction instr;
		instr = new Instruction("I" + instrNumber, instrAddr);
		instr.setStage(main.instruction.Instruction.Stage.FETCH_START);
		instr.setStage(main.instruction.Instruction.Stage.FETCH_COMPLETE);
		PipelineProcessor.fetch.setInstr(instr);
	}

	public static void decode() {
		String instrId;
		decodeRF.getInstr().setStage(main.instruction.Instruction.Stage.DECODE_START);
		codeMemory.get((decodeRF.getInstr().getInstrAddress() - 4000) / 4)
				.setStage(main.instruction.Instruction.Stage.DECODE_START);
		instrId = decodeRF.getInstr().getInstrId();
		FileIO.decodeInstruction(instrId, decodeRF.getInstr().getInstrAddress());
		decodeRF.getInstr().setStage(main.instruction.Instruction.Stage.DECODE_COMPLETE);
		codeMemory.get((decodeRF.getInstr().getInstrAddress() - 4000) / 4)
				.setStage(main.instruction.Instruction.Stage.DECODE_COMPLETE);
		// make the source and destination registers invalid
		if (decodeRF.getInstr().getdRegAddr() != null) {
			registerFile.get(decodeRF.getInstr().getdRegAddr()).setStatus(main.resources.Register.status.INVALID);
		}
		
		//change
/*		copyIq();
		copyLsq();
		copyRob();
		copyCmIstr();*/
	}

	public static void execIntFU() {
		
		//execI.setInstr(fetchInstr("INT"));
		if (execI.getInstr() != null) {
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t INTFU : " + execI.getInstr().toString());
			System.out.println("Int : " + execI.getInstr().toString());
			sIntFU =  execI.getInstr().toString();
			codeMemory.get((execI.getInstr().getInstrAddress() - 4000) / 4)
					.setStage(main.instruction.Instruction.Stage.EXECUTE_START);
			execI.getInstr().setAluStage(main.instruction.Instruction.ALUStage.I1_START);
			// function logic
			intFU.setStage(0, execI.getInstr());
			switch (intFU.getStageInstruction(0).getInstr()) {

			case "ADD":
				intFU.add();
				break;

			case "ADDC":
				intFU.addc();
				break;

			case "SUB":
				intFU.sub();
				break;

			case "AND":
				intFU.and();
				break;

			case "OR":
				intFU.or();
				break;

			case "XOR":
				intFU.xor();
				break;

			case "LOAD":
				intFU.load();
				LSQEntry.updateTargetAddr(execI.getInstr().getInstrAddress(),execI.getInstr().getResult());
				break;

			case "STORE":
				intFU.store();
				LSQEntry.updateTargetAddr(execI.getInstr().getInstrAddress(),execI.getInstr().getResult());
				break;

			case "MOV":
				intFU.mov();
				break;

			case "MOVC":
				intFU.movc();
				break;

			case "BZ":
				intFU.bz();
				break;

			case "BNZ":
				intFU.bnz();
				break;

			case "JUMP":
				intFU.jump();
				break;

			case "JAL":
				intFU.jal();
				// System.out.println("opt : " + intFU.getStageInstruction(0).getResult());
				break;

			case "HALT":
				System.out.println("halting");
				break;

			default:
				break;

			}
			execI.setInstr(intFU.getStageInstruction(0));
			execI.getInstr().setZeroFlag(intFU.getStageInstruction(0).getZeroFlag());
			System.out.println("Zero flag here : " + execI.getInstr().getZeroFlag());
			//update pReg
			//change
			//if(updateRes(execI.getInstr().prIndex, execI.getInstr().getResult()))
			
			if(execI.getInstr().prIndex != null){				
				pRegisterFile
					.get(execI.getInstr().prIndex)
					.setValue(execI.getInstr().getResult());
				
			}
			////
			execI.getInstr().setStage(main.instruction.Instruction.Stage.I1_END);
			codeMemory.get((execI.getInstr().getInstrAddress() - 4000) / 4)
					.setStage(main.instruction.Instruction.Stage.EXECUTE_COMPLETE);
			
			execI.getInstr().setStage(main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
			RobEntry.updateRobEntry(execI.getInstr().getInstrAddress(), main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
			PhysicalRegister.makePregValid(execI.getInstr().prIndex);
		} else
			System.out.println("INT FU idle...");
	}

	public static void execMul1FU() {
		//execM1.setInstr(fetchInstr("MUL"));
		if (execM1.getInstr() != null) {
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t MULFU : " + execM1.getInstr().toString());
			System.out.println("M1 : " + execM1.getInstr().toString());
			sMul1 = execM1.getInstr().toString();
			codeMemory.get((execM1.getInstr().getInstrAddress() - 4000) / 4)
					.setStage(main.instruction.Instruction.Stage.EXECUTE_START);
			execM1.getInstr().setAluStage(main.instruction.Instruction.ALUStage.M1_START);
			mulFU.setStage(0, execM1.getInstr());
			mulFU.setStatus(aluStatus.BUSY);
			// function logic
			mulFU.mul();

			////
			System.out.println("REg stat : " + pRegisterFile.get(mulFU.getStageInstruction(0).prIndex).getrStatus());
			execM1.setInstr(mulFU.getStageInstruction(0));
			execM1.getInstr().setStage(main.instruction.Instruction.Stage.M1_END);
		} else
			System.out.println("MUL FU idle...");
	}

	public static void execMul2FU() {
		execM2.getInstr().setStage(main.instruction.Instruction.Stage.M2_START);
		// function logic
		sMul2 = execM2.getInstr().toString();
		////
		//execM2.getInstr().setStage(main.instruction.Instruction.Stage.M2_END);
		codeMemory.get((execM2.getInstr().getInstrAddress() - 4000) / 4)
				.setStage(main.instruction.Instruction.Stage.EXECUTE_COMPLETE);
		
		execM2.getInstr().setStage(main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
		RobEntry.updateRobEntry(execM2.getInstr().getInstrAddress(), main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
		if(execM2.getInstr().prIndex != null) {
			pRegisterFile
			.get(execM2.getInstr().prIndex)
			.setValue(execM2.getInstr().getResult());
		}
		PhysicalRegister.makePregValid(execM2.getInstr().getdRegAddr());
	}

	public static void execDiv1FU() {
		//execD1.setInstr(fetchInstr("DIV"));
		if (execD1.getInstr() != null) {
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t DIVFU : " + execD1.getInstr().toString());
			System.out.println("Instruction " + execD1.getInstr().getInstrId() + " is in Div1");
			sDiv1 = execD1.getInstr().toString();
			codeMemory.get((execD1.getInstr().getInstrAddress() - 4000) / 4)
					.setStage(main.instruction.Instruction.Stage.EXECUTE_START);
			execD1.getInstr().setStage(main.instruction.Instruction.Stage.D1_START);
			divFU.setStage(0, execD1.getInstr());
			divFU.setStatus(aluStatus.BUSY);
			// function logic

			divFU.div();
			////
			execD1.setInstr(divFU.getStageInstruction(0));
			execD1.getInstr().setStage(main.instruction.Instruction.Stage.D1_END);
		} else
			System.out.println("DIV FU idle...");
	}

	public static void execDiv2FU() {
		execD2.getInstr().setStage(main.instruction.Instruction.Stage.D2_START);
		// function logic
		sDiv2 = execD2.getInstr().toString();
		////
		execD2.getInstr().setStage(main.instruction.Instruction.Stage.D2_END);
	}

	public static void execDiv3FU() {
		execD3.getInstr().setStage(main.instruction.Instruction.Stage.D3_START);
		// function logic
		sDiv3 = execD3.getInstr().toString();
		////
		execD3.getInstr().setStage(main.instruction.Instruction.Stage.D3_END);
	}

	public static void execDiv4FU() {
		execD4.getInstr().setStage(main.instruction.Instruction.Stage.D4_START);
		// function logic
		sDiv4 = execD4.getInstr().toString();
		////
		execD4.getInstr().setStage(main.instruction.Instruction.Stage.D4_END);
		codeMemory.get((execD4.getInstr().getInstrAddress() - 4000) / 4)
				.setStage(main.instruction.Instruction.Stage.EXECUTE_COMPLETE);
		
		execD4.getInstr().setStage(main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
		RobEntry.updateRobEntry(execD4.getInstr().getInstrAddress(), main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
		if(execD4.getInstr().prIndex != null) {
			pRegisterFile
			.get(execD4.getInstr().prIndex)
			.setValue(execD4.getInstr().getResult());
		}
		PhysicalRegister.makePregValid(execD4.getInstr().prIndex);

	}

	public static void execute() {
		System.out.println("old exec");
		if (execute.getInstr() != null) {

			execute.getInstr().setStage(main.instruction.Instruction.Stage.EXECUTE_START);
			codeMemory.get((execute.getInstr().getInstrAddress() - 4000) / 4)
					.setStage(main.instruction.Instruction.Stage.EXECUTE_START);

			if (execute.getInstr().getInstr().equals("MUL") || execute.getInstr().getInstr().equals("DIV")) {
				if (execute.getInstr().getInstr().equals("MUL")) {
					mulFU.feedInstruction(execute.getInstr());
					mulFU.setStatus(aluStatus.BUSY);
					mulFU.getStageInstruction(0).setAluStage(main.instruction.Instruction.ALUStage.M1_START);
					mulFU.mul();
				} else {
					divFU.feedInstruction(execute.getInstr());
					divFU.setStatus(aluStatus.BUSY);
					divFU.getStageInstruction(0).setAluStage(main.instruction.Instruction.ALUStage.D1_START);
					divFU.div();

				}
			} else {
				intFU.feedInstruction(execute.getInstr());
				intFU.setStatus(aluStatus.BUSY);

				switch (intFU.getStageInstruction(0).getInstr()) {

				case "ADD":
					intFU.add();
					break;

				case "ADDC":
					intFU.addc();
					break;

				case "SUB":
					intFU.sub();
					break;

				case "AND":
					intFU.and();
					break;

				case "OR":
					intFU.or();
					break;

				case "XOR":
					intFU.xor();
					break;

				case "LOAD":
					intFU.load();
					break;

				case "STORE":
					intFU.store();
					break;

				case "MOV":
					intFU.mov();
					break;

				case "MOVC":
					intFU.movc();
					break;

				case "BZ":
					intFU.bz();
					break;

				case "BNZ":
					intFU.bnz();
					break;

				case "JUMP":
					intFU.jump();
					break;

				case "JAL":
					intFU.jal();
					// System.out.println("opt : " + intFU.getStageInstruction(0).getResult());
					break;

				case "HALT":
					break;

				default:
					break;

				}
			}
		}
		execute.setInstr(null);

		// copy the completed instruction back to execute stage div > mul > int from FU
		if (divFU.getStageInstruction(3) != null) {

			if (divFU.getStageInstruction(3).getAluStage().equals(main.instruction.Instruction.ALUStage.D4_START)) {
				execute.setInstr(divFU.getStageInstruction(3));
				sDiv4 = divFU.getStageInstruction(3).getInstrId() + " - "
						+ codeMemory.get(Integer.parseInt(divFU.getStageInstruction(3).getInstrId().replace("I", "")))
								.getInstructionString();
				divFU.setStage(3, null);

			}
		} else if (mulFU.getStageInstruction(1) != null) {

			if (mulFU.getStageInstruction(1).getAluStage().equals(main.instruction.Instruction.ALUStage.M2_START)) {
				execute.setInstr(mulFU.getStageInstruction(1));
				sMul2 = mulFU.getStageInstruction(1).getInstrId() + " - "
						+ codeMemory.get(Integer.parseInt(mulFU.getStageInstruction(1).getInstrId().replace("I", "")))
								.getInstructionString();
				mulFU.setStage(1, null);

			}
		} else if (intFU.getStageInstruction(0) != null) {
			if (Stage.isHalted == false) {
				execute.setInstr(intFU.getStageInstruction(0));
				sIntFU = intFU.getStageInstruction(0).getInstrId() + " - "
						+ codeMemory.get(Integer.parseInt(intFU.getStageInstruction(0).getInstrId().replace("I", "")))
								.getInstructionString();
				intFU.setStage(0, null);
				intFU.setStatus(main.resources.ALU.aluStatus.FREE);

			} else if ( // if HALT ..let other pass completely through respective FU
			mulFU.getStageInstruction(0) == null && mulFU.getStageInstruction(1) == null
					&& divFU.getStageInstruction(0) == null && divFU.getStageInstruction(1) == null
					&& divFU.getStageInstruction(2) == null && divFU.getStageInstruction(3) == null) {
				execute.setInstr(intFU.getStageInstruction(0));
				sIntFU = intFU.getStageInstruction(0).getInstrId() + " - "
						+ codeMemory.get(Integer.parseInt(intFU.getStageInstruction(0).getInstrId().replace("I", "")))
								.getInstructionString();
				intFU.setStage(0, null);
				intFU.setStatus(main.resources.ALU.aluStatus.FREE);

			}
		}

		if (execute.getInstr() != null) {
			execute.getInstr().setStage(main.instruction.Instruction.Stage.EXECUTE_COMPLETE);
			codeMemory.get((execute.getInstr().getInstrAddress() - 4000) / 4)
					.setStage(main.instruction.Instruction.Stage.EXECUTE_COMPLETE);

		}
	}

	public static void mem1() {
		///
		int sReg1, sReg2;
		int memAddr,value;
		if (memory.getInstr() != null) {

			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t Memory : " + memory.getInstr().toString());
			sMem1 = memory.getInstr().toString();
			memory.getInstr().setStage(main.instruction.Instruction.Stage.MEMORY_START);
			codeMemory.get((memory.getInstr().getInstrAddress() - 4000) / 4).setStage(main.instruction.Instruction.Stage.MEMORY_START);
			System.out.println("LS instr : " + memory.getInstr().toString());
			if (memory.getInstr().getInstr().equals("STORE")) {
				sReg1 = memory.getInstr().getsReg1Addr();
				sReg2 = memory.getInstr().getsReg2Addr();
				memAddr = memory.getInstr().getResult();
				
				if(ALU.renameTabLoc(sReg2) == 0) {					
					val = pRegisterFile.get(memory.getInstr().getsReg2Addr()).getValue();
					dataMemory[memory.getInstr().getResult() / 4]
							.setData(pRegisterFile.get(memory.getInstr().getsReg2Addr()).getValue());
					memory.getInstr().setRes(pRegisterFile.get(memory.getInstr().getsReg2Addr()).getValue());
				}else {
					val = registerFile.get(memory.getInstr().getsReg2Addr()).getValue();
					dataMemory[memory.getInstr().getResult() / 4]
							.setData(registerFile.get(memory.getInstr().getsReg2Addr()).getValue());
					memory.getInstr().setRes(registerFile.get(memory.getInstr().getsReg2Addr()).getValue());
				}
				System.out.println("STORE :: " + memAddr + " : " + val);
			} else {
				memAddr = memory.getInstr().getResult();
				pRegisterFile.get(memory.getInstr().getdRegAddr())
						.setValue(dataMemory[memory.getInstr().getResult() / 4].getData());
				val = pRegisterFile.get(memory.getInstr().getdRegAddr()).getValue();  
				System.out.println("LOAD :: " + memAddr + " : " + val);
			}
			
			
		}else System.out.println("Mem is idle..");
		
		///
	}

	public static void mem2() {
		if(memS2.getInstr() != null) {
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t Mem2 : " + memS2.getInstr().toString());
			sMem2 = memS2.getInstr().toString();			
		}
	}
	
	public static void mem3() {
		if(memS3.getInstr() != null) {
			System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t Mem3 : " + memS3.getInstr().toString());
			sMem3 = memS3.getInstr().toString();
			memS3.getInstr().setStage(main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
			codeMemory.get((memS3.getInstr().getInstrAddress() - 4000) / 4).setStage(main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
			RobEntry.updateRobEntry(memS3.getInstr().getInstrAddress(), main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
			
			if(memS3.getInstr().getInstr().equals("STORE")){				
				RobEntry.updateLSInstr(memS3.getInstr().getInstrAddress(),memS3.getInstr().getRes());
			}
			PhysicalRegister.makePregValid(memS3.getInstr().getdRegAddr());
			System.out.println("Mem complete : " + memS3.getInstr().toString());
		}

	}
	
	public static void writeBack() {
		writeBack.getInstr().setStage(main.instruction.Instruction.Stage.WRITEBACK_START);
		codeMemory.get((writeBack.getInstr().getInstrAddress() - 4000) / 4)
				.setStage(main.instruction.Instruction.Stage.WRITEBACK_START);
		switch (writeBack.getInstr().getInstr()) {

		case "ADD":
		case "ADDC":
		case "SUB":
		case "MUL":
		case "DIV":
			// write registers to destination register
			pRegisterFile.get(writeBack.getInstr().getdRegAddr()).setValue(writeBack.getInstr().getResult());

			if (writeBack.getInstr().getResult() == 0) {
				pRegisterFile.get(writeBack.getInstr().getdRegAddr()).setzFlag(true);
				System.out.println("Zero flag is set by " + writeBack.getInstr().getInstrId() + " result : "
						+ writeBack.getInstr().getResult());
			} else {
				//pRegisterFile.get(writeBack.getInstr().getdRegAddr()).setzFlag(false);
				// System.out.println("Zero flag is reset by " +
				// writeBack.getInstr().getInstrId() + " result : " +
				// writeBack.getInstr().getResult());
			}
			break;

		case "AND":
		case "OR":
		case "XOR":
			// write registers to destination register
			pRegisterFile.get(writeBack.getInstr().getdRegAddr()).setValue(writeBack.getInstr().getResult());

		case "LOAD":
			// nothing to do
			break;
		case "STORE":
			// nothing to do
			break;
		case "MOV":
		case "MOVC":
			pRegisterFile.get(writeBack.getInstr().getdRegAddr()).setValue(writeBack.getInstr().getResult());
			break;
		case "JAL":
			pRegisterFile.get(writeBack.getInstr().getdRegAddr()).setValue(writeBack.getInstr().getResult());
			break;
		case "BZ":
		case "BNZ":
		case "JUMP":
			// nothing to do
			break;
		case "HALT":
			// nothning to do
			break;
		default:
			System.out.println(intFU.getStageInstruction(0).toString());
			System.out.println("Something went wrong...");
			break;
		}

		writeBack.getInstr().setStage(main.instruction.Instruction.Stage.WRITEBACK_COMPLETE);
		codeMemory.get((writeBack.getInstr().getInstrAddress() - 4000) / 4).setStage(main.instruction.Instruction.Stage.WRITEBACK_COMPLETE);
		RobEntry.updateRobEntry(writeBack.getInstr().getInstrAddress(), main.instruction.Instruction.Stage.WRITEBACK_COMPLETE);
	}
	
	
	public static void updatePipelineStages() {
	
		forwardResult();
		//change
		if(ROB.size() > 0) RobEntry.commitInstr();
		RenameTableEntry.updateRenameTable();
		//RenameTableEntry.updateRenameTable();
		IQEntry.updateIQ();
		CycleInfo.updateGanttChart();
		updateWB();
		updateMem3();
		updateMem2();
		updateMem();
		updateD4();
		updateD3();
		updateD2();
		updateD1();
		updateM2();
		updateM1();
		updateIntFU();
		//change
		//if(ROB.size() > 0) RobEntry.commitInstr();

		if (!Stage.isFlushed) {
			// update the fetch stage
			updateDRF();
			updateFetch();
		} else {
			//System.out.println("Pipeline is flushed by " + memory.getInstr().getInstrId() + " instruction");
			fetch.setStatus(stageStatus.FREE);
			decodeRF.setStatus(stageStatus.FREE);
		}

		LSQEntry.earlyLoadFetch();
		//CycleInfo.updateCycleInfo(sFetch, sDecodeRF, sIntFU, sMul1, sMul2, sDiv1, sDiv2, sDiv3, sDiv4,tRob,tIq,tLsq,tCmi,tRmt);
		CycleInfo.updateCycleInfo(sFetch, sDecodeRF, sIntFU, sMul1, sMul2, sDiv1, sDiv2, sDiv3, sDiv4,tRob,tIq,tLsq,tCmi,tRmt,sMem1,sMem2,sMem3);
		copyRmt();
		copyIq();
		copyLsq();
		copyRob();
		copyCmIstr();
		
		
	}

	public static void updateWB() {
		if (writeBack.getInstr() != null
				&& writeBack.getInstr().getStage() == main.instruction.Instruction.Stage.WRITEBACK_COMPLETE) {

			// clear writeback stage
			System.out.println("Instruction " + writeBack.getInstr().getInstrId() + " is completed");
			codeMemory.get((writeBack.getInstr().getInstrAddress() - 4000) / 4)
					.setStage(main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
			sWriteBack = writeBack.getInstr().getInstrId() + " - " + codeMemory
					.get(Integer.parseInt(writeBack.getInstr().getInstrId().replace("I", ""))).getInstructionString();
			RobEntry.updateRobEntry(writeBack.getInstr().getInstrAddress(), main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
			//PhysicalRegister.makePregValid();
			writeBack.setInstr(null);
			writeBack.setStatus(stageStatus.FREE);
			
			

		}
	}

	public static void updateMem3() {
		if(memS3.getInstr() != null)	{
			RobEntry.updateRobEntry(memS3.getInstr().getInstrAddress(), main.instruction.Instruction.Stage.EXECUTION_COMPLETE);
			memS3.setInstr(null);
			//System.out.println("Mem 3");
		}		
	}
	public static void updateMem2() {
	if(memS3.getInstr() == null && memS2.getInstr() != null)	{
		memS3.setInstr(memS2.getInstr());
		memS2.setInstr(null);
		//System.out.println("Mem 2");
	}
	}
	
	public static void updateMem() {
		
		if (memory.getInstr() != null) {
			System.out.println("Mem 1" + memory.getInstr().toString());
			sMemory = memory.getInstr().toString();
			memS2.setInstr(memory.getInstr());
			memory.setInstr(null);
			memory.setStatus(stageStatus.FREE);
		}
			memory.setInstr(LSQEntry.fetchNextLS());
/*		if(PipelineProcessor.LSQ.size() != 0) {
			Integer nextLs = LSQEntry.selectInstr();
			if(nextLs != null) {				
				memory.setInstr(
						PipelineProcessor.LSQ.get(nextLs)
						.getInstr()
						);
			}
		}*/	  
	}

	public static void updateD4() {
		if (execD4.getInstr() != null && execD4.getInstr().getStage() == main.instruction.Instruction.Stage.EXECUTION_COMPLETE) {
			sDiv4 = execD4.getInstr().toString();
			execD4.setInstr(null);
			execD4.setStatus(stageStatus.FREE);
			

		}
	}

	public static void updateD3() {
		if (execD3.getInstr() != null && execD3.getInstr().getStage() == main.instruction.Instruction.Stage.D3_END
				&& execD4.getStatus() == stageStatus.FREE) {
			sDiv3 = execD3.getInstr().toString();
			execD4.setInstr(execD3.getInstr());
			execD3.setInstr(null);
			execD3.setStatus(stageStatus.FREE);
			execD4.getInstr().setStage(main.instruction.Instruction.Stage.D4_START);

		}
	}

	public static void updateD2() {
		if (execD2.getInstr() != null && execD2.getInstr().getStage() == main.instruction.Instruction.Stage.D2_END
				&& execD3.getStatus() == stageStatus.FREE) {
			sDiv2 = execD2.getInstr().toString();
			execD3.setInstr(execD2.getInstr());
			execD2.setInstr(null);
			execD2.setStatus(stageStatus.FREE);
			execD3.getInstr().setStage(main.instruction.Instruction.Stage.D3_START);

		}
	}

	public static void updateD1() {
/*		execD1.setInstr(fetchInstr("DIV"));
		if(execD1.getInstr() != null) {
			pRegisterFile.get(execD1.getInstr().prIndex).setrStatus(main.resources.PhysicalRegister.status.INVALID);
		}*/
		if (execD1.getInstr() != null && execD1.getInstr().getStage() == main.instruction.Instruction.Stage.D1_END
				&& execD2.getStatus() == stageStatus.FREE) {
			//System.out.println("Update D1");
			sDiv1 = execD1.getInstr().toString();
			execD2.setInstr(execD1.getInstr());
			execD1.setInstr(null);
			execD2.setStatus(stageStatus.FREE);
			execD2.getInstr().setStage(main.instruction.Instruction.Stage.D2_START);

		}
		
			execD1.setInstr(fetchInstr("DIV"));
	}

	public static void updateM2() {
		
		if (execM2.getInstr() != null && execM2.getInstr().getStage() == main.instruction.Instruction.Stage.EXECUTION_COMPLETE) {
			//System.out.println("Update M2");
			sMul2 = execM2.getInstr().toString();
			execM2.setInstr(null);
			
		}

	}

	public static void updateM1() {
		
/*		execM1.setInstr(fetchInstr("MUL"));
		if(execM1.getInstr() != null) {
			pRegisterFile.get(execM1.getInstr().prIndex).setrStatus(main.resources.PhysicalRegister.status.INVALID);
		}*/
		if (execM1.getInstr() != null && execM1.getInstr().getStage() == main.instruction.Instruction.Stage.M1_END
				&& execM2.getStatus() == stageStatus.FREE) {
			//System.out.println("update M1");
			sMul1 = execM1.getInstr().toString();
			
			execM2.setInstr(execM1.getInstr());
			execM1.setInstr(null);
			execM1.setStatus(stageStatus.FREE);
			execM2.getInstr().setStage(main.instruction.Instruction.Stage.M2_START);
		
		}
			execM1.setInstr(fetchInstr("MUL"));

	}

	public static void updateIntFU() {

		execI.setInstr(null);
		execI.setInstr(fetchInstr("INT"));
		if(execI.getInstr() !=null && (execI.getInstr().getType().equals("ATM") || execI.getInstr().getType().equals("MV") || execI.getInstr().getInstr().equals("LOAD"))) {
			pRegisterFile.get(execI.getInstr().prIndex).setrStatus(main.resources.PhysicalRegister.status.INVALID);
		}
/*		if (execI.getInstr() != null && execI.getInstr().getStage() == main.instruction.Instruction.Stage.EXECUTION_COMPLETE) {
			sIntFU = execI.getInstr().toString();
			execI.setInstr(null);
			execI.setStatus(stageStatus.FREE);

		}else {
			execI.setInstr(fetchInstr("INT"));
			if(execI.getInstr() != null) System.out.println("Next exec : " + execI.getInstr().toString());
		}*/
	}

	public static void updateDRF() {
		Instruction instr = null;
		String fuType = "";
		Boolean isReady = false;

		// remove
		/*if (decodeRF.getInstr() != null)
			System.out.println(decodeRF.getInstr() + " " + decodeRF.getInstr().getFuType());
*/
		if (decodeRF.getInstr() != null && decodeRF.getInstr().getStage() == main.instruction.Instruction.Stage.DECODE_COMPLETE) {
			sDecodeRF = decodeRF.getInstr().toString();

			// part 3
			if(decodeRF.getInstr().getdRegAddr() != null && decodeRF.getInstr().getType().equals("ATM"))
			rAInstrReg = decodeRF.getInstr().getdRegAddr();
			instr = renameInstruction();
		}

		if (instr != null	&& instr.isRenamed) {
			if (instr.getType().equals("LS")) {
				if (ROB.size() < 32 && issueQueue.size() < 16 && LSQ.size() < 32) {
					//ROB.add(new RobEntry(instr));	
					ROB.add(new RobEntry(instr, cycle));
					//LSQ.add(new LSQEntry(instr));
					LSQ.add(new LSQEntry(instr, cycle));
					//change
					if(instr.getdRegAddr() != null)
						pRegisterFile.get(instr.getdRegAddr()).setrStatus(main.resources.PhysicalRegister.status.INVALID);
					issueQueue.add(new IQEntry(instr, cycle));

					decodeRF.setInstr(null);
					decodeRF.setStatus(stageStatus.FREE);
					System.out.println("Enrty added: " + instr.toString());
				} else
					System.out.println("instruction is stalled : LS");

			} else if (ROB.size() < 32 && issueQueue.size() < 16) {
					  
				if(instr.getType().equals("BR") || instr.getType().equals("JMP")) {
					BIS.saveStage(instr);
					//BIS.displayBis();
					if(instr.tReg == null) 
					instr.tReg = rAInstrReg;
					System.out.println("Entry added in BIS : " + instr.toString() + "  ::: " + instr.tReg);
				}	
				//ROB.add(new RobEntry(instr));
				ROB.add(new RobEntry(instr, cycle));
				//change
				if(instr.getdRegAddr() != null)
					pRegisterFile.get(instr.prIndex).setrStatus(main.resources.PhysicalRegister.status.INVALID);
				issueQueue.add(new IQEntry(instr, cycle));

				decodeRF.setInstr(null);
				decodeRF.setStatus(stageStatus.FREE);
			} else
				System.out.println("instruction is stalled : LS + ROB + IQ");
		} else if (instr != null)
			System.out.println("instruction is stalled : isRenamed : " + instr.isRenamed);

	}

	public static void updateFetch() {
		if (fetch.getInstr() != null && fetch.getInstr().getStage() == main.instruction.Instruction.Stage.FETCH_COMPLETE
				&& Stage.isHalted == false && decodeRF.getStatus() == stageStatus.FREE) {
			sFetch = fetch.getInstr().getInstrId() + " - " + codeMemory
					.get(Integer.parseInt(fetch.getInstr().getInstrId().replace("I", ""))).getInstructionString();
			decodeRF.setInstr(fetch.getInstr());
			fetch.setInstr(null);
			decodeRF.setStatus(stageStatus.BUSY);
			fetch.setStatus(stageStatus.FREE);
			decodeRF.getInstr().setStage(main.instruction.Instruction.Stage.DECODE_START);
		} else if (fetch.getInstr() != null) { // fetch is blocked
			sFetch = fetch.getInstr().getInstrId() + " - " + codeMemory
					.get(Integer.parseInt(fetch.getInstr().getInstrId().replace("I", ""))).getInstructionString();
		} else if (Stage.isHalted == true)
			fetch.setInstr(null);
	}

	public static Instruction renameInstruction() {
		Instruction instr = decodeRF.getInstr();
		System.out.println("Original instruction : " + instr.toString());
		Integer s1Index = null;
		Integer s2Index = null;
		Integer dIndex = null;
		
		
		if (instr.getdRegAddr() != null && !instr.getType().equals("HLT") && !instr.getType().equals("BR")) {

			// check extra reg
			for (int i = 16; i < 32; i++) {
				if (!pRegisterFile.get(i).getAllocated()) {
					dIndex = i;
					pRegisterFile.get(i).setAllocated(true);
					pRegisterFile.get(i).setrStatus(main.resources.PhysicalRegister.status.INVALID);
					if(instr.getInstr().equals("STORE"))	System.out.println("err");
					break;
				}
			}

			if (dIndex == null) {
				for (int i = 0; i < 16; i++) {
					if (!pRegisterFile.get(i).getAllocated()) {
						dIndex = i;
						pRegisterFile.get(i).setAllocated(true);
						break;
					}
				}
			}
		}else if(instr.getType().equals("BR")) {
			instr.isRenamed = true;
			
		}else if(instr.getType().equals("HLT")) {
			instr.isRenamed = true;
		}
		
		if (instr.getsReg1Addr() != null && !instr.getType().equals("HLT") && !instr.getType().equals("BR")) {
			if (renameTable.get(instr.getsReg1Addr()).getIndex() != null) {
				// has exising mapping
					s1Index = renameTable.get(instr.getsReg1Addr()).getIndex();
					//change
					if(renameTable.get(instr.getsReg1Addr()).getLoc() == 1)
						instr.setOpr1(registerFile.get(s1Index).getValue());

			} else {
				System.out.println("freash rename : S1");
				if (pRegisterFile.get(instr.getsReg1Addr()) == null) {
					// register index remains same
					s1Index = instr.getsReg1Addr();
					pRegisterFile.get(s1Index).setAllocated(true);
				} else {
					for (int i = 0; i < 32; i++) {
						if (pRegisterFile.get(i).getAllocated() == false) {
							s1Index = i;
							System.out.println("index : " + i);
							pRegisterFile.get(i).setAllocated(true);
							break;
						}
					}

				}
			}
		}else if(instr.getType().equals("BR")) {
			instr.isRenamed = true;
		}else if(instr.getType().equals("HLT")) {
			instr.isRenamed = true;
		}

		if (instr.getsReg2Addr() != null && !instr.getType().equals("HLT") && !instr.getType().equals("BR")) {
			if (renameTable.get(instr.getsReg2Addr()).getIndex() != null) {
				// has exising mapping
				s2Index = renameTable.get(instr.getsReg2Addr()).getIndex();
				//change
				if(renameTable.get(instr.getsReg2Addr()).getLoc() == 1)
					instr.setOpr2(registerFile.get(s2Index).getValue());

			} else {
				System.out.println("freash rename : S2");
				if (pRegisterFile.get(instr.getsReg2Addr()) == null) {
					// register index remains same
					s2Index = instr.getsReg2Addr();
					pRegisterFile.get(s2Index).setAllocated(true);
				} else {
					for (int i = 0; i < 32; i++) {
						if (pRegisterFile.get(i).getAllocated() == false) {
							s2Index = i;
							System.out.println("index : " + i);
							pRegisterFile.get(i).setAllocated(true);
							break;
						}
					}

				}
			}
		}else if(instr.getType().equals("BR")) {
			
		}else if(instr.getType().equals("HLT")) {
			instr.isRenamed = true;
		}


		
		if (instr.getsReg1Addr() != null) {
			if (s1Index != null) {
				// System.out.println("found");
				renameTable.get(instr.getsReg1Addr()).setIndex(s1Index);
				instr.setsReg1Addr(s1Index);
				instr.isRenamed = true;
			} else {
				// System.out.println("not found : s1");
				instr.isRenamed = false;
			}
		}

		if (instr.getsReg2Addr() != null) {
			if (s2Index != null) {
				// System.out.println("found");
				renameTable.get(instr.getsReg2Addr()).setIndex(s2Index);
				instr.setsReg2Addr(s2Index);
				instr.isRenamed = true;
			} else {
				// System.out.println("not found : s2");
				instr.isRenamed = false;
			}
		}

		if (instr.getdRegAddr() != null) {
			if (dIndex != null) {
				 System.out.println(instr.getdRegAddr() + " to " + dIndex);
				renameTable.get(instr.getdRegAddr()).setIndex(dIndex);
				renameTable.get(instr.getdRegAddr()).setLoc(0);
				
				instr.arIndex = instr.getdRegAddr();
				instr.setdRegAddr(dIndex);
				instr.prIndex = dIndex;
				instr.isRenamed = true;
				
			} else {
				// System.out.println("not found : d");
				instr.isRenamed = false;
			}
		}

		System.out.println("Renamed instr : " + instr.toString() + " " + instr.isRenamed);

		return instr;
	}

	public static Instruction fetchInstr(String fuUnit) {
		Instruction instr = null;
		int a = 0;
		for (IQEntry iqEntry : issueQueue) {
			if (fuUnit.equals("INT")) {
				if (iqEntry.getInstr().getFuType().equals("INT")) {
					switch (iqEntry.getInstr().getType()) {
					case "ATM":
						//if (isReady(a)) {
						if(isInstrReady(a)) {
							instr = iqEntry.getInstr();
							pRegisterFile.get(instr.prIndex)
									.setrStatus(main.resources.PhysicalRegister.status.INVALID);
							//System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t INTFU : " + instr.toString());
						}
						break;
					case "MV":
						instr = iqEntry.getInstr();
						pRegisterFile.get(instr.prIndex)
								.setrStatus(main.resources.PhysicalRegister.status.INVALID);
						//System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t INTFU : " + instr.toString());
						break;
						
					case "LS":
						
						if (iqEntry.getInstr().getInstr().equals("LOAD")) {
							if (pRegisterFile.get(iqEntry.getInstr().getsReg1Addr()).getrStatus()
									.equals(main.resources.PhysicalRegister.status.VALID)) {
								instr = iqEntry.getInstr();
								pRegisterFile.get(instr.prIndex)
										.setrStatus(main.resources.PhysicalRegister.status.INVALID);
								//System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t INTFU : " + instr.toString());
							}

						} else if (iqEntry.getInstr().getInstr().equals("STORE")){
							
							if (pRegisterFile.get(iqEntry.getInstr().getsReg1Addr()).getrStatus().equals(main.resources.PhysicalRegister.status.VALID)
								) {
								instr = iqEntry.getInstr();
								//System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t INTFU : " + instr.toString());
							}
						}
						break;
					case "BR":
						// no registers in the instr format
						if(iqEntry.getInstr().branchReady) {							
							instr = iqEntry.getInstr();
							//System.out.println("branch le");
						}
						break;
					case "JMP":
						// System.out.println("address : " + i.toString());
						if (pRegisterFile.get(iqEntry.getInstr().getsReg1Addr()).getrStatus()
								.equals(main.resources.PhysicalRegister.status.VALID)
								/*&& pRegisterFile.get(iqEntry.getInstr().getsReg2Addr()).getrStatus()
										.equals(main.resources.PhysicalRegister.status.VALID)*/) {
							instr = iqEntry.getInstr();
							//System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t INTFU : " + instr.toString());
						}
						break;
					case "HLT":
						// no registers in the instr format
						if(passHalt(a))
							instr = iqEntry.getInstr();
						else 
							System.out.println("HALT is not selected");
						
						break;
					default:
						System.out.println("Invalid instruction");
						break;
					}
					
					if (instr != null) {
						System.out.println("Removing entry  : " + issueQueue.get(a).getInstr().toString());
						issueQueue.remove(a);
						break;
					}

				}
			} 
			if (fuUnit.equals("MUL")) {
				if (iqEntry.getInstr().getFuType().equals("MUL")) {
					System.out.println("check mul ready");
					//if (isReady(a)) {
					if(isInstrReady(a)) {
						instr = iqEntry.getInstr();
						//change
						pRegisterFile.get(instr.getdRegAddr())
								.setrStatus(main.resources.PhysicalRegister.status.INVALID);
						//System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t MULFU : " + instr.toString());
						issueQueue.remove(a);
						break;
					}
				}
			} 
			if (fuUnit.equals("DIV")) {
				if (iqEntry.getInstr().getInstr().equals("DIV")) {
					//if (isReady(a)) {
					if(isInstrReady(a)) {
						instr = iqEntry.getInstr();
						pRegisterFile.get(instr.prIndex)
								.setrStatus(main.resources.PhysicalRegister.status.INVALID);
						
						issueQueue.remove(a);
						break;
					}else {
						System.out.println("Div not ready : "/* + instr.toString()*/);
					}
				}
			}

			a++;
		}

		return instr;
	}

	public static void updatePc() {

		if (Stage.isJumped) {
			/*
			 * System.out.println("Max value of PC : " + (4000 + (numberOfInstructions * 4)
			 * - 4)); System.out.println("Invalid jump address " + pc); pc = null;
			 */
			System.out.println("Pipeline flushed by jump...");
			Stage.isFlushed = false;
			System.out.println("pc : " + pc);
			Stage.isJumped = false;
		} else if (!Stage.isFlushed) {
			if (fetch.getStatus() == stageStatus.FREE) {
				if (pc != null) {
					pc += 4;
					if (!((pc - 4000) / 4 < numberOfInstructions)) {
						pc = null;
					}
				}
			}
		} else {
			System.out.println("Pipeline flushed...");
			Stage.isFlushed = false;
		}
	}

	public static void freePhysicalRegister() {
		System.out.println("implementation remaining..");
	}

	/*
	 * public static Integer getRecentArithmaticInstr(Instruction instr) { Integer
	 * address = null; String[] instrString; int index = ((instr.getInstrAddress()
	 * -4000)/4) - 1; while(index !=0) { instrString =
	 * codeMemory.get(index).getInstruction(); if(instrString[0].equals("ADD") ||
	 * instrString[0].equals("SUB") || instrString[0].equals("MUL") ||
	 * instrString[0].equals("ADDC") || instrString[0].equals("DIV")) { address =
	 * codeMemory.get(index).getAddress();
	 * System.out.println("Recent arithmatic instruction found..."); break; }
	 * index--; }
	 * 
	 * return address; }
	 */

	public static Integer newGetRecentArithmaticInstr(Instruction instr) {	
		Integer address = null;
		String[] instrString;
		int diff = 999;
		if (intFU.getStageInstruction(0) != null && (intFU.getStageInstruction(0).getInstr().equals("ADD")
				|| intFU.getStageInstruction(0).getInstr().equals("SUB")
				|| intFU.getStageInstruction(0).getInstr().equals("MUL")
				|| intFU.getStageInstruction(0).getInstr().equals("DIV"))) {
			diff = intFU.getStageInstruction(0).getInstrAddress() - instr.getInstrAddress();
			address = intFU.getStageInstruction(0).getInstrAddress();
		}

		if (mulFU.getStageInstruction(0) != null && (mulFU.getStageInstruction(0).getInstr().equals("ADD")
				|| mulFU.getStageInstruction(0).getInstr().equals("SUB")
				|| mulFU.getStageInstruction(0).getInstr().equals("MUL")
				|| mulFU.getStageInstruction(0).getInstr().equals("DIV"))) {
			if (mulFU.getStageInstruction(0).getInstrAddress() - instr.getInstrAddress() < diff) {
				diff = mulFU.getStageInstruction(0).getInstrAddress() - instr.getInstrAddress();
				address = mulFU.getStageInstruction(0).getInstrAddress();
			}

		}

		if (mulFU.getStageInstruction(1) != null && (mulFU.getStageInstruction(1).getInstr().equals("ADD")
				|| mulFU.getStageInstruction(1).getInstr().equals("SUB")
				|| mulFU.getStageInstruction(1).getInstr().equals("MUL")
				|| mulFU.getStageInstruction(1).getInstr().equals("DIV"))) {
			if (mulFU.getStageInstruction(1).getInstrAddress() - instr.getInstrAddress() < diff) {
				diff = mulFU.getStageInstruction(1).getInstrAddress() - instr.getInstrAddress();
				address = mulFU.getStageInstruction(1).getInstrAddress();
			}

		}

		if (divFU.getStageInstruction(0) != null && (divFU.getStageInstruction(0).getInstr().equals("ADD")
				|| divFU.getStageInstruction(0).getInstr().equals("SUB")
				|| divFU.getStageInstruction(0).getInstr().equals("MUL")
				|| divFU.getStageInstruction(0).getInstr().equals("DIV"))) {
			if (divFU.getStageInstruction(0).getInstrAddress() - instr.getInstrAddress() < diff) {
				diff = divFU.getStageInstruction(0).getInstrAddress() - instr.getInstrAddress();
				address = divFU.getStageInstruction(0).getInstrAddress();
			}

		}

		if (divFU.getStageInstruction(1) != null && (divFU.getStageInstruction(1).getInstr().equals("ADD")
				|| divFU.getStageInstruction(1).getInstr().equals("SUB")
				|| divFU.getStageInstruction(1).getInstr().equals("MUL")
				|| divFU.getStageInstruction(1).getInstr().equals("DIV"))) {
			if (divFU.getStageInstruction(1).getInstrAddress() - instr.getInstrAddress() < diff) {
				diff = divFU.getStageInstruction(1).getInstrAddress() - instr.getInstrAddress();
				address = divFU.getStageInstruction(1).getInstrAddress();
			}

		}

		if (divFU.getStageInstruction(2) != null && (divFU.getStageInstruction(2).getInstr().equals("ADD")
				|| divFU.getStageInstruction(2).getInstr().equals("SUB")
				|| divFU.getStageInstruction(2).getInstr().equals("MUL")
				|| divFU.getStageInstruction(2).getInstr().equals("DIV"))) {
			if (divFU.getStageInstruction(2).getInstrAddress() - instr.getInstrAddress() < diff) {
				diff = divFU.getStageInstruction(2).getInstrAddress() - instr.getInstrAddress();
				address = divFU.getStageInstruction(2).getInstrAddress();
			}

		}

		if (divFU.getStageInstruction(3) != null && (divFU.getStageInstruction(3).getInstr().equals("ADD")
				|| divFU.getStageInstruction(3).getInstr().equals("SUB")
				|| divFU.getStageInstruction(3).getInstr().equals("MUL")
				|| divFU.getStageInstruction(3).getInstr().equals("DIV"))) {
			if (divFU.getStageInstruction(3).getInstrAddress() - instr.getInstrAddress() < diff) {
				diff = divFU.getStageInstruction(3).getInstrAddress() - instr.getInstrAddress();
				address = divFU.getStageInstruction(3).getInstrAddress();
			}

		}

		if (address != null)
			return address;

		if (memory.getInstr() != null
				&& (memory.getInstr().getInstr().equals("ADD") || memory.getInstr().getInstr().equals("SUB")
						|| memory.getInstr().getInstr().equals("MUL") || memory.getInstr().getInstr().equals("DIV"))) {
			address = memory.getInstr().getInstrAddress();
			return address;
		}

		if (writeBack.getInstr() != null && (writeBack.getInstr().getInstr().equals("ADD")
				|| writeBack.getInstr().getInstr().equals("SUB") || writeBack.getInstr().getInstr().equals("MUL")
				|| writeBack.getInstr().getInstr().equals("DIV"))) {
			address = writeBack.getInstr().getInstrAddress();
			return address;
		}
		return address;
	}

	public static Integer getRecentArithmaticInstr(String stage, Integer instrAddr) {
		System.out.println("finding");
		Integer addr = null;
		int diff = 99;
		int robTail = ROB.size() - 1;
		// check in the issue queue
		for (int i = robTail; i >= 0; i--) {
			System.out.println("Is : " + ROB.get(i).getInstr().toString() + " " + i);
			System.out.println(ROB.get(i).getInstr().getInstrAddress());
			System.out.println(instrAddr);
			if (ROB.get(i).getInstr().getInstrAddress() < instrAddr && ROB.get(i).getInstr().getType().equals("ATM")) {
				addr = ROB.get(i).getInstr().getInstrAddress();
				System.out.println("Recent Arithmatic instr : " + ROB.get(i).getInstr().toString());
				break;
			}
		}
		return addr;
	}

	public static void forwardResult() {

		int entry = 0;
		for (IQEntry i : issueQueue) {

			switch (i.getInstr().getType()) {
			case "ATM":
				if (execI.getInstr() != null) {
					// System.out.println("from INt");
					if (i.getInstr().getsReg1Addr() == execI.getInstr().prIndex) { // f dep over SR1
						i.getInstr().setOpr1(execI.getInstr().getResult());
						System.out.println("A Forwarding to opr1 of :" + i.getInstr().toString());
					}
					if (i.getInstr().getsReg2Addr() == execI.getInstr().prIndex) { // f dep over SR2)
						i.getInstr().setOpr2(execI.getInstr().getResult());
						System.out.println("A Forwarding to opr2 of :" + i.getInstr().toString());
						//System.out.println("for 2");
					}
				}
				if (execM2.getInstr() != null) {
					//System.out.println("Dest : " + execM2.getInstr().getdRegAddr());
					//System.out.println("SRC : " + i.getInstr().getsReg1Addr());
					if (i.getInstr().getsReg1Addr() == execM2.getInstr().prIndex) {
						i.getInstr().setOpr1(execM2.getInstr().getResult());
						System.out.println("M Forwarding to opr1 of :" + i.getInstr().toString());
					}
					if (i.getInstr().getsReg2Addr() == execM2.getInstr().prIndex) {
						i.getInstr().setOpr2(execM2.getInstr().getResult());
						System.out.println("M Forwarding to opr2 of :" + i.getInstr().toString());
					}
				}

				if (execD4.getInstr() != null) {
					if (i.getInstr().getsReg1Addr() == execD4.getInstr().prIndex) {
						i.getInstr().setOpr1(execD4.getInstr().getResult());
						System.out.println("D Forwarding to opr1 of :" + i.getInstr().toString());
					}
					if (i.getInstr().getsReg2Addr() == execD4.getInstr().prIndex) {
						i.getInstr().setOpr2(execD4.getInstr().getResult());
						System.out.println("D Forwarding to opr2 of :" + i.getInstr().toString());
					}
				}
				break;
			case "MV":
				// handle in LSQ
				break;
			case "BR":
				Integer pReg = renameTable.get(i.getInstr().tReg).getIndex();
				System.out.println("bg adr : " + pReg + " " + i.getInstr().tReg);
				
				if(renameTable.get(i.getInstr().tReg).getLoc() == 1) {
					i.getInstr().setZeroFlag(registerFile.get(pReg).getzFlag());
					i.getInstr().branchReady = true;
					System.out.println("Branch ready.. A reg");
				}else if(renameTable.get(i.getInstr().tReg).getLoc() == 0){
					
					//Integer pReg = renameTable.get(rAInstrReg).getIndex();
					if(pReg != null && pRegisterFile.get(pReg).getValue() != null && pRegisterFile.get(pReg).getrStatus() == main.resources.PhysicalRegister.status.VALID) {
						
						i.getInstr().setZeroFlag(pRegisterFile.get(pReg).getzFlag());
						//System.out.println("Stat  : "+ pRegisterFile.get(pReg).getrStatus());
						System.out.println("Branch ready.. P reg " + " " + pReg + pRegisterFile.get(pReg).getzFlag());
						//System.out.println("va :" + pRegisterFile.get(pReg).getzFlag() + " " + pReg);
						i.getInstr().branchReady = true;
					}else {
						System.out.println("Branch has to wait for " + pReg);
					}
				}else {
					System.out.println("Branch has to wait for " + pReg);
				}

				break;
			case "JMP":
				if (execI.getInstr() != null) {
					if (i.getInstr().getsReg1Addr() == execI.getInstr().getdRegAddr()) { // f dep over SR1
						i.getInstr().setOpr1(execI.getInstr().getResult());
						System.out.println("A Forwarding to opr1 of :" + i.getInstr().toString());
					}
				}

				if (execM2.getInstr() != null) {
					if (i.getInstr().getsReg1Addr() == execM2.getInstr().getdRegAddr()) {
						i.getInstr().setOpr1(execM2.getInstr().getResult());
						System.out.println("M Forwarding to opr1 of :" + i.getInstr().toString());
					}
				}

				if (execD4.getInstr() != null) {
					if (i.getInstr().getsReg1Addr() == execD4.getInstr().getdRegAddr()) {
						i.getInstr().setOpr1(execD4.getInstr().getResult());
						System.out.println("D Forwarding to opr1 of :" + i.getInstr().toString());
					}
				}
				break;
			case "LS":
				if (execI.getInstr() != null) {
					if (i.getInstr().getsReg1Addr() == execI.getInstr().getdRegAddr()) { // f dep over SR1
						i.getInstr().setOpr1(execI.getInstr().getResult());
						System.out.println("A Forwarding to opr1 of :" + i.getInstr().toString());
					}
				}

				if (execM2.getInstr() != null) {
					if (i.getInstr().getsReg1Addr() == execM2.getInstr().getdRegAddr()) {
						i.getInstr().setOpr1(execM2.getInstr().getResult());
						System.out.println("M Forwarding to opr1 of :" + i.getInstr().toString());
					}
				}

				if (execD4.getInstr() != null) {
					if (i.getInstr().getsReg1Addr() == execD4.getInstr().getdRegAddr()) {
						i.getInstr().setOpr1(execD4.getInstr().getResult());
						System.out.println("D Forwarding to opr1 of :" + i.getInstr().toString());
					}
				}
				break;	
			case "HLT":
				break;
			default:
				break;

			}
			entry++;
		}

		// forward to decodeRF stage
		if (decodeRF.getInstr() != null) {
			int s1Addr, s2Addr, dAddr;
			Instruction i = decodeRF.getInstr();
			switch (i.getType()) {
			case "ATM":
				s1Addr = renameTable.get(i.getsReg1Addr()).getIndex();
				s2Addr = renameTable.get(i.getsReg2Addr()).getIndex();
				if (execI.getInstr() != null && execI.getInstr().getdRegAddr()!= null && s1Addr == execI.getInstr().getdRegAddr()) { // f dep over SR1
					i.setOpr1(execI.getInstr().getResult());
					System.out.println("for 1");
				}
				if (execI.getInstr() != null && execI.getInstr().getdRegAddr()!= null && s2Addr == execI.getInstr().getdRegAddr()) { // f dep over SR2)
					i.setOpr2(execI.getInstr().getResult());
					System.out.println("for 2");
				}
	
				if (execM2.getInstr() != null && execM2.getInstr().getdRegAddr() != null) {
					System.out.println("Dest : " + execM2.getInstr().getdRegAddr());
					System.out.println("SRC : " + i.getsReg1Addr());
					
					if (s1Addr == execM2.getInstr().getdRegAddr()) {
						i.setOpr1(execM2.getInstr().getResult());
					}
					if (s2Addr == execM2.getInstr().getdRegAddr()) {
						i.setOpr2(execM2.getInstr().getResult());
					}
				}

				if (execD4.getInstr() != null && execD4.getInstr().getdRegAddr() != null) {
					if (s1Addr == execD4.getInstr().getdRegAddr()) {
						i.setOpr1(execD4.getInstr().getResult());
					}
					if (s2Addr == execD4.getInstr().getdRegAddr()) {
						i.setOpr2(execD4.getInstr().getResult());
					}
				}
	
				break;
			case "MV":
				break;
			case "BR":
				
				System.out.println("DRF BR : " + getRecentArithmaticInstr("IQ", i.getInstrAddress()));
				
				if (execI.getInstr() != null && execI.getInstr().getInstrAddress() == getRecentArithmaticInstr("IQ", i.getInstrAddress())) {
					if (execI.getInstr().getResult() == 0)
						i.setZeroFlag(true);
					else
						i.setZeroFlag(false);
					i.branchReady = true;
					System.out.println("Branch ready..");
				}
				break;
			case "JMP":
				s1Addr = i.getsReg1Addr();
				if (execI.getInstr() != null && execI.getInstr().getdRegAddr() != null) {
					if (s1Addr == execI.getInstr().getdRegAddr()) { // f dep over SR1
						i.setOpr1(execI.getInstr().getResult());
					}
				}

				if (execM2.getInstr() != null && execM2.getInstr().getdRegAddr() != null) {
					if (s1Addr == execM2.getInstr().getdRegAddr()) {
						i.setOpr1(execM2.getInstr().getResult());
					}
				}

				if (execD4.getInstr() != null && execD4.getInstr().getdRegAddr() != null) {
					if (s1Addr == execD4.getInstr().getdRegAddr()) {
						i.setOpr1(execD4.getInstr().getResult());
					}
				}
				break;
			case "LS":
				s1Addr = i.getsReg1Addr();
				
				if (execI.getInstr() != null && execI.getInstr().getdRegAddr() != null) {
					if (s1Addr == execI.getInstr().getdRegAddr()) { // f dep over SR1
						i.setOpr1(execI.getInstr().getResult());
					}
				}

				if (execM2.getInstr() != null && execM2.getInstr().getdRegAddr() != null) {
					if (s1Addr == execM2.getInstr().getdRegAddr()) {
						i.setOpr1(execM2.getInstr().getResult());
					}
				}

				if (execD4.getInstr() != null && execD4.getInstr().getdRegAddr() != null) {
					if (s1Addr == execD4.getInstr().getdRegAddr()) {
						i.setOpr1(execD4.getInstr().getResult());
					}
				}
				break;

			case "HLT":
				break;
			default:
				break;
			}
		}
	}
	
	public static boolean isInstrReady(int ind) {
		Boolean readyO1 = false,readyO2 = false;
		Instruction i = issueQueue.get(ind).getInstr();
		Integer sReg1 = i.getsReg1Addr();
		Integer sReg2 = i.getsReg2Addr();

		
		if(i.getOpr1() != null) {
			readyO1 = true;
		}
		
		if(i.getOpr2() != null) {
			readyO2 = true;
		}

		
		for(int c=0; c<16; c++) {
			if(renameTable.get(c).getIndex() == sReg1) {
				if(renameTable.get(c).getLoc() != null && renameTable.get(c).getLoc() == 0) {
					if(pRegisterFile.get(sReg1).getrStatus() == main.resources.PhysicalRegister.status.VALID) {
						readyO1 = true;
					}
				}else if(renameTable.get(c).getLoc() != null && renameTable.get(c).getLoc() == 1) {
					readyO1 = true;
					System.out.println("extra case");
				}		
			}
			
			if(renameTable.get(c).getIndex() == sReg2) {
				if(renameTable.get(c).getLoc() != null && renameTable.get(c).getLoc() == 0) {
					if(pRegisterFile.get(sReg2).getrStatus() == main.resources.PhysicalRegister.status.VALID) {
						readyO2 = true;
					}
				}else if(renameTable.get(c).getLoc() != null && renameTable.get(c).getLoc() == 1) {
					readyO2 = true;
					System.out.println("extra case");
				}
			
			
			}
			
		}
			return readyO1 & readyO2;
	}

	
	public static boolean isReady(int ind) {
		Integer sReg1 = issueQueue.get(ind).getInstr().getsReg1Addr();
		Integer sReg2 = issueQueue.get(ind).getInstr().getsReg2Addr();

		//change
		Integer dReg = issueQueue.get(ind).getInstr().getdRegAddr();
		Boolean readyO1 = false,readyO2 = false;
		
		for(int i=0;i < 16; i++) {
			if(sReg1 != null) {
				if(renameTable.get(i).getLoc()!=null && renameTable.get(i).getIndex() == sReg1 && renameTable.get(i).getLoc() == 0) {
					if(pRegisterFile.get(sReg1).getrStatus() == main.resources.PhysicalRegister.status.VALID) {
						
						System.out.println(issueQueue.get(ind).getInstr().toString());
						readyO1 = true;
						System.out.println("Valid P" + sReg1);
					}
					if(issueQueue.get(ind).getInstr().getOpr1()!= null) {
						System.out.println("Opr1 aval");
						readyO1 = true;
						System.out.println(issueQueue.get(ind).getInstr().toString());

						//System.out.println("22");
					}/*else System.out.println("RR " + sReg1 + pRegisterFile.get(sReg1).getrStatus());*/
					
				}else if(renameTable.get(i).getLoc()!=null) {
					//System.out.println("T1");
					if(renameTable.get(i).getIndex() == sReg1) {
						//System.out.println("T2");
						if(renameTable.get(i).getLoc() == 1) {
							//System.out.println("T3");
							System.out.println(issueQueue.get(ind).getInstr().toString());

							System.out.println("Commited copy " + sReg1 + " " +  issueQueue.get(ind).getInstr().toString());
							readyO1 =true;
						}
					}
				}
			}else readyO1 = true;	//change
			
			if(sReg2 != null) {
				
				if(renameTable.get(i)!=null && renameTable.get(i).getIndex() == sReg2 && 
						renameTable.get(i).getLoc() == 0) {
					if(pRegisterFile.get(sReg2).getrStatus() == PhysicalRegister.status.VALID) {
						System.out.println(issueQueue.get(ind).getInstr().toString());

						readyO2 = true;
						System.out.println("Valid P" + sReg2);
					}
					if(issueQueue.get(ind).getInstr().getOpr2()!= null) {
						readyO2 = true;
						System.out.println("Opr2 aval");
						System.out.println(issueQueue.get(ind).getInstr().toString());

					}
				}else if(renameTable.get(i).getLoc()!=null && renameTable.get(i).getIndex() == sReg2 && renameTable.get(i).getLoc() == 1) {
					readyO2 =true;
					System.out.println(issueQueue.get(ind).getInstr().toString());

					System.out.println("Committed copy " + issueQueue.get(ind).getInstr().toString());
				}				
			}else readyO2 = true;	//change
			
		}
		if(readyO1 && readyO2) {
			System.out.println("Ready : " + issueQueue.get(ind).getInstr().toString());
			return true;
		}else {
			System.out.println("Not Ready : " + issueQueue.get(ind).getInstr().toString() + readyO1 + " " + readyO2 + " " + pRegisterFile.get(sReg1).getrStatus());
			return false;
		}
	}
	public static boolean updateRes(Integer index, Integer res) {
		boolean upd=false;
		if(res != null) {
			
			for(int i=0; i< renameTable.size(); i++) {
				if(renameTable.get(i).getIndex() == index && renameTable.get(i).getLoc() == 0) {
					//System.out.println("Update it");
					upd = true;
					break;
				}
			}
		}
		return upd;
	}

	public static void displayCommittedInstr(List<Instruction> ie) {
		if(ie != null && ie.size() > 0) {
			
			for(Instruction i : ie) {
				System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t" + "  "+ i.toString());
			}
		}
	}

	public static void copyRob() {
		tRob = new ArrayList<RobEntry>();
		for(RobEntry r : ROB) {
			tRob.add(r);
		}
	}
	
	public static void copyCmIstr() {
		tCmi = new ArrayList<Instruction>();
		for(Instruction i: commitInstr) {
			tCmi.add(i);
		}
	}
	public static void copyLsq() {
		tLsq = new ArrayList<LSQEntry>();
		for(LSQEntry le : LSQ) {
			tLsq.add(le);
			System.out.println("IN lsq : " + le.getInstr().toString());
		}
		
	}
	
	public static void copyIq() {
		tIq = new ArrayList<IQEntry>();
		for(IQEntry e : issueQueue) {
			tIq.add(e);
		}
	}

	public static void copyRmt() {
		tRmt = new ArrayList<RenameTableEntry>();
		for(RenameTableEntry r: renameTable) {
			tRmt.add(r);
		}
	}

	public static boolean passHalt(int ind) {
		System.out.println("Halt cycle" + issueQueue.get(ind).getCycleNo());
		for(IQEntry i : issueQueue) {
			if(issueQueue.get(ind).getCycleNo() <= i.getCycleNo()) {
				System.out.println(i.getInstr().toString() + " cycle : " + i.getCycleNo());
				return true;
			}
		}
		return false;
	}
}
