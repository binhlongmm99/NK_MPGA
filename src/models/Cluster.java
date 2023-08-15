package models;

import java.util.ArrayList;

public class Cluster {
	private ArrayList<Integer> cluster = new ArrayList<Integer>();

	public Cluster() {
		
	}
	
	public Cluster(ArrayList<Integer> cluster) {
		super();
		this.cluster = cluster;
	}

	public ArrayList<Integer> getCluster() {
		return cluster;
	}

	public void setCluster(ArrayList<Integer> cluster) {
		this.cluster = cluster;
	}
	
	public void addElement(int index, int value) {
		cluster.add(index, value);
		
	}
}
