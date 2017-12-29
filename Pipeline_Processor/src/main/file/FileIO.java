package main.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import main.PipelineProcessor;
import main.instruction.Instruction;
import main.instruction.Instruction.Stage;
import main.resources.Code;

public class FileIO {
String filePath,line;

public String getFilePath() {
	return filePath;
}

public void setFilePath(String filePath) {
	this.filePath = filePath;
}

public void readFile() {
	FileReader fr=null;
	try {
		String[] temp;
		fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		int i = 0;
		while((line = br.readLine()) != null) {
			//temp = line.replace(",","").split(" ");
			temp = line.replace(" ","").split(",");
			PipelineProcessor.codeMemory.add(new Code(temp,4000 + 4*i));
			i++;
			PipelineProcessor.ic++;
		}
		fr.close();
	} catch (FileNotFoundException e) {
		System.out.println("File not found...");
		e.printStackTrace();
		
	} catch (IOException e) {
		System.out.println("Error reading file...");
		e.printStackTrace();
	}finally {
		try {
			fr.close();
		} catch (IOException e) {
			System.out.println("Error in closing file");
			e.printStackTrace();
		}
	}
}

public static void decodeInstruction(String instrId, int instrAddress) {
		
	String instr, regA, regB, label, literal;
	String instructionString[];
	
	instructionString = PipelineProcessor.codeMemory.get(Integer.parseInt(instrId.toUpperCase().replace("I", ""))).getInstruction();
	instr = instructionString[0].toUpperCase();
	Instruction instruction;
	try {
		switch(instr) {
			case "ADD": case "ADDC": case "SUB": 
				if(instructionString[3].toUpperCase().contains("R")) {
				instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")),
							  Integer.parseInt(instructionString[3].trim().toUpperCase().replace("R","")),
							  Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),"", null, instrAddress, null, 0,"ATM","INT");
				}else {
					instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")),
							  null,Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),"", 
							  Integer.parseInt(instructionString[3].trim().toUpperCase().replace("#","")), instrAddress, null, 0, "ATM", "INT");				
				}
				break;
				
			case "AND": case "OR": case "XOR":
				if(instructionString[3].toUpperCase().contains("R")) {
				instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")),
							  Integer.parseInt(instructionString[3].trim().toUpperCase().replace("R","")),
							  Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),"", null, instrAddress, null, 0,"LG","INT");
				}else {
					instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")),
							  null,Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),"", 
							  Integer.parseInt(instructionString[3].trim().toUpperCase().replace("#","")), instrAddress, null, 0, "LG", "INT");				
				}
				break;
				
			case "MUL":
				if(instructionString[3].toUpperCase().contains("R")) {
				instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")),
							  Integer.parseInt(instructionString[3].trim().toUpperCase().replace("R","")),
							  Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),"", null, instrAddress, null, 0,"ATM","MUL");
				}else {
					instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")),
							  null,Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),"", 
							  Integer.parseInt(instructionString[3].trim().toUpperCase().replace("#","")), instrAddress, null, 0, "ATM", "MUL");				
				}
				break;
				
			case "DIV":
				if(instructionString[3].toUpperCase().contains("R")) {
				instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")),
							  Integer.parseInt(instructionString[3].trim().toUpperCase().replace("R","")),
							  Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),"", null, instrAddress, null, 0,"ATM","DIV");
				}else {
					instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")),
							  null,Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),"", 
							  Integer.parseInt(instructionString[3].trim().toUpperCase().replace("#","")), instrAddress, null, 0, "ATM", "DIV");				
				}
				break;
			case "LOAD":
				instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].toUpperCase().replace("R","")), 
						      null, Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),"", 
						      Integer.parseInt(instructionString[3].trim().toUpperCase().replace("#","")), instrAddress, null, 0,"LS","INT");
				
				break;
			case "STORE":
				instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")), 
						      Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")), null, "", 
						      Integer.parseInt(instructionString[3].trim().toUpperCase().replace("#","")), instrAddress, null, 0,"LS","INT");			
				break;
			case "MOV" : case "MOVC":
				instruction = new Instruction(instrId, instr, null, null, Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),
							  "", Integer.parseInt(instructionString[2].trim().toUpperCase().replace("#","")), instrAddress, null, 0,"MV","INT");
				break;
			case "JAL":	
				instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[2].trim().toUpperCase().replace("R","")),
							  null, Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")),
						      "", Integer.parseInt(instructionString[3].trim().toUpperCase().replace("#","")), instrAddress, null, 0,"JMP","INT");
				break;
			case "JUMP":
				instruction = new Instruction(instrId, instr, Integer.parseInt(instructionString[1].trim().toUpperCase().replace("R","")), 
							  null, null, "", Integer.parseInt(instructionString[2].trim().toUpperCase().replace("#","")), instrAddress, null, 0,"JMP","INT");
				break;
			case "BZ": case "BNZ":
				instruction = new Instruction(instrId, instr, null, null, null, "", Integer.parseInt(instructionString[1].trim().toUpperCase().replace("#","")), 
							  instrAddress, null, 0,"BR","INT");
				break;
			case "HALT":	
				instruction = new Instruction(instrId, instr, null, null, null, "", null, instrAddress, null, 0,"HLT","INT");
				break;
			default:
				instruction = null;
				System.out.println("Invalid instruction");
				break;
		}
			//PipelineProcessor.listOfInstr.add(instruction);
			PipelineProcessor.decodeRF.setInstr(instruction);
	}catch(Exception e) {
		System.out.println("Error occoured in decoding instruction");
		e.printStackTrace();
	}		
}

public static void genereateSummary() {
	XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Gannt chart");
    
    for(int i=0; i< PipelineProcessor.n; i++) {
    	Row row = sheet.createRow(++i);
    	int c=0;
    	Cell cell1 = row.createCell(++c);   
    	cell1.setCellValue(PipelineProcessor.ganttChart[i].getFetch());
    	Cell cell2 = row.createCell(++c);   
    	cell1.setCellValue(PipelineProcessor.ganttChart[i].getDecodeRF());
    	Cell cell3 = row.createCell(++c);   
    	cell1.setCellValue(PipelineProcessor.ganttChart[i].getExecute());
    	Cell cell4 = row.createCell(++c);   
    	cell1.setCellValue(PipelineProcessor.ganttChart[i].getMemory());
    	Cell cell5 = row.createCell(++c);   
    	cell1.setCellValue(PipelineProcessor.ganttChart[i].getWriteBack());

    	System.out.println("Row : " + i + cell1 + cell2 + cell3 + cell4 + cell5);
    }
}
}
