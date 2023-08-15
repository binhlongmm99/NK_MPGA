package models;

public class PopModel {
	private int nsamples;
	private double data[][]; 
	private double mean[];
	private double std[];
	
	public int getnSamples() {
		return nsamples;
	}
	public void setnSamples(int nsamples) {
		this.nsamples = nsamples;
	}
	public double[] getMean() {
		return mean;
	}
	public void setMean(double[] mean) {
		this.mean = mean;
	}
	public double[] getStd() {
		return std;
	}
	public void setStd(double[] std) {
		this.std = std;
	}
	
	public double[][] getData() {
		return data;
	}
	public void setData(double[][] data) {
		this.data = data;
	}
	
	
}
