package models;

import java.util.ArrayList;

public class CluInstance {
	private ArrayList<Cluster> clusters = new ArrayList<Cluster>();
	private ArrayList<Vertex> vertices = new ArrayList<>();
	private int nVertices;
	private int nClusters;
	private double weightMatrix[][];
	
	public ArrayList<Cluster> getClusters() {
		return clusters;
	}
	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}
	
	public ArrayList<Vertex> getVertices() {
		return vertices;
	}
	public void setVertices(ArrayList<Vertex> vertices) {
		this.vertices = vertices;
	}
	
	public int getnVertices() {
		return nVertices;
	}
	public void setnVertices(int nVertices) {
		this.nVertices = nVertices;
	}
	
	public int getnClusters() {
		return nClusters;
	}
	public void setnClusters(int nClusters) {
		this.nClusters = nClusters;
	}
	
	public double[][] getWeightMatrix() {
		return weightMatrix;
	}
	public void setWeightMatrix(double[][] weightMatrix) {
		this.weightMatrix = weightMatrix;
	}

}
