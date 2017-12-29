package main.resources;

import main.PipelineProcessor;

public class Data {
Integer data;
int address;

public Integer getData() {
	return data;
}
public void setData(Integer data) {
	this.data = data;
}
public Data(Integer data, int address) {
	this.data = data;
	this.address = address;
}
public int getAddress() {
	return address;
}
public void setAddress(int address) {
	this.address = address;
}


public static void displayMemory() {
	System.out.println("\n\n--------------------------Data memory------------------------------\n");
	for (int x = 0; x < 100; x += 4) {
		System.out.print(PipelineProcessor.dataMemory[x].getAddress() + " : " + PipelineProcessor.dataMemory[x].getData());
		System.out.print("\t" + PipelineProcessor.dataMemory[x + 1].getAddress() + " : " + PipelineProcessor.dataMemory[x + 1].getData());
		System.out.print("\t" + PipelineProcessor.dataMemory[x + 2].getAddress() + " : " + PipelineProcessor.dataMemory[x + 2].getData());
		System.out
				.print("\t" + PipelineProcessor.dataMemory[x + 3].getAddress() + " : " + PipelineProcessor.dataMemory[x + 3].getData() + "\n");
	}
	
	System.out.println("\n\n");
}
}

