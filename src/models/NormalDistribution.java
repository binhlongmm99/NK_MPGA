package models;

public class NormalDistribution {
	public NormalDistribution() {
		
	}


	public double pdf(double x, double mean, double std) {
		return 1 / (std * Math.sqrt(2 * Math.PI)) * 
				Math.exp(-(x - mean) * (x - mean) / (2 * std * std));
	}

}
