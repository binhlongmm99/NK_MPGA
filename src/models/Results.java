package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Results {
	public String name;
	public String dirName;
	public int seed;
	public String time;
	public double bestCost;
	public double[][] bestCostInEachGeneration;
	public double[] rmp;
	public double[][] imRate;

	public void storeBestSolution() {
		PrintWriter pw = null;
		File d = new File(dirName);
		d.mkdirs();
		try {
			pw = new PrintWriter(new FileWriter(dirName + "/" + name + "-seed(" + seed + ").opt", false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.println("Name: " + name);
		pw.println("Seed: " + seed);
		pw.println("Fitness: " + bestCost);
		pw.println("Time: " + time);
		pw.close();
	}
	
	//for GA
	public void bestCostInEachGeneration(Results results) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(dirName + "/" + results.name + "_seed(" + seed + ").gen", false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.println("Generations \t" + results.name);
		for (int i = 0; i < bestCostInEachGeneration.length; i++) {
			pw.println(i + "\t" + results.bestCostInEachGeneration[i][0]);
		}
		pw.close();
	}
	
	//for MFEA
	public void bestCostInEachGeneration(Results results, Results results1) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(
					dirName + "/" + results.name + "_and_" + results1.name + "_seed(" + seed + ").gen", false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.println("Generations \t" + results.name + "\t" + results1.name );
		for (int i = 0; i < bestCostInEachGeneration.length; i++) {
			pw.println(
					i + "\t" + results.bestCostInEachGeneration[i][0] + "\t" + results.bestCostInEachGeneration[i][1]);
		}
		pw.close();
	}

	//for MFEA_II
	public void bestCostInEachGeneration2(Results results, Results results1) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(
					dirName + "/" + results.name + "_and_" + results1.name + "_seed(" + seed + ").gen", false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.println("Generations \t" + results.name + "\t" + results1.name + "\t" + "RMP");
		for (int i = 0; i < bestCostInEachGeneration.length; i++) {
			pw.println(
					i + "\t" + results.bestCostInEachGeneration[i][0] + "\t" + results.bestCostInEachGeneration[i][1] + "\t" + results.rmp[i]);
		}
		pw.close();
	}
	
	//for MPEA
	public void bestCostInEachGeneration3(Results results, Results results1) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter(
					dirName + "/" + results.name + "_and_" + results1.name + "_seed(" + seed + ").gen", false));
		} catch (IOException e) {
			e.printStackTrace();
		}
		pw.println("Generations \t" + results.name + "\t" + results1.name + "\t" + "RMP0" + "\t" + "RMP1");
		for (int i = 0; i < bestCostInEachGeneration.length; i++) {
			pw.println(
					i + "\t" + results.bestCostInEachGeneration[i][0] + "\t" + results.bestCostInEachGeneration[i][1] + "\t" 
					+ results.imRate[i][0] + "\t" + results.imRate[i][1]);
		}
		pw.close();
	}

}
