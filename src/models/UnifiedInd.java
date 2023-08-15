package models;

public class UnifiedInd {
	private int nTasks;
	private double[] factorialCost;
	private int[] factorialRank;
	private double scalarFitness;
	private int skillFactor;
	
	public UnifiedInd(int nTasks) {
		super();
		this.nTasks = nTasks;
		this.factorialCost = new double[nTasks];
		this.factorialRank = new int[nTasks];
	}

	public UnifiedInd(int nTasks, double[] factorialCost, int[] factorialRank, double scalarFitness, int skillFactor) {
		super();
		this.nTasks = nTasks;
		this.factorialCost = factorialCost;
		this.factorialRank = factorialRank;
		this.scalarFitness = scalarFitness;
		this.skillFactor = skillFactor;
	}
	
	public int getnTasks() {
		return nTasks;
	}
	public void setnTasks(int nTasks) {
		this.nTasks = nTasks;
	}

	public double[] getFactorialCost() {
		return factorialCost;
	}
	public void setFactorialCost(double[] factorialCost) {
		this.factorialCost = factorialCost;
	}

	public int[] getFactorialRank() {
		return factorialRank;
	}
	public void setFactorialRank(int factorialRank, int task) {
		this.factorialRank[task] = factorialRank;
	}

	public double getScalarFitness() {
		return scalarFitness;
	}
	public void setScalarFitness(double scalarFitness) {
		this.scalarFitness = scalarFitness;
	}

	public int getSkillFactor() {
		return skillFactor;
	}
	public void setSkillFactor(int skillFactor) {
		this.skillFactor = skillFactor;
	}
	
}
