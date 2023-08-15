package models;

public class NetkeysUnifiedInd extends UnifiedInd{
	private double[] chromosome;
	
	public NetkeysUnifiedInd(int nTasks) {
		super(nTasks);
	}
	
	public double[] getChromosome() {
		return chromosome;
	}
	public void setChromosome(double[] chromosome) {
		this.chromosome = chromosome;
	}
	
}
