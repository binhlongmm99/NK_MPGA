package models;

public class Parameters {
	public String[] fileNames = {
			"10berlin52", "10eil51", 
			"10eil76", "10kroB100", 
			"10pr76", "10rat99", 
			"10st70", "15berlin52", 
			"15eil51", "15eil76", 
			"15pr76", "15st70", 
			"25eil101", "25kroA100", 
			"25lin105", "25rat99", 
			"50eil101", "50kroA100", 
			"50kroB100", "50lin105", 
			"50rat99", "5berlin52", 
			"5eil51", "5eil76", 
			"5pr76", "5st70",
		
			"10a280", "10gil262", 
			"10lin318", "10pcb442", 
			"10pr439", "25a280", 
			"25gil262", "25lin318", 
			"25pcb442", "25pr439", 
			"50a280", "50gil262", 
			"50lin318", "50pcb442",			  

			};

	public int nRuns = 30;

	public double rmp = 0.3;
	public double muRate = 0.05;
	
	public static int n_c = 5;
	public static int n_m = 5;

	public int generations = 500;
	public int nTasks = 2;
	public int popSize = 100;

}
