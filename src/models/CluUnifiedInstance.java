package models;


import java.util.ArrayList;

import operators.Encoding;

public class CluUnifiedInstance extends CluInstance{	
	private int[] minClustersLength;
	private int[] maxClustersLength;
	private int minnClusters;
	private int maxnClusters;
	private int[] sgmOffsets;
	private Encoding encoding = new Encoding();	
	
	public CluUnifiedInstance(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		setminClustersLength(clusters1, clusters2);
		setmaxClustersLength(clusters1, clusters2);
		setMinnClusters(clusters1, clusters2);
		setMaxnClusters(clusters1, clusters2);
		setCluster(clusters1, clusters2);
		setnVertices(maxClustersLength);
		setsgmOffsets(maxClustersLength);
	}


	private void setnVertices(int[] maxClustersLength) {
		// TODO Auto-generated method stub
		super.setnVertices(encoding.getMaxNumVertices(maxClustersLength));
	}
	

	private void setsgmOffsets(int[] maxClustersLength) {
		// TODO Auto-generated method stub
		this.sgmOffsets = encoding.getSegOffsets(maxClustersLength);
	}

	private void setmaxClustersLength(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		// TODO Auto-generated method stub
		this.maxClustersLength = encoding.getMaxClustersLength(clusters1, clusters2);
	}

	private void setminClustersLength(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		// TODO Auto-generated method stub
		this.minClustersLength = encoding.getMinClustersLength(clusters1, clusters2);
	}

	private void setMaxnClusters(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		// TODO Auto-generated method stub
		this.setMaxnClusters(Math.max(clusters1.size(), clusters2.size()));
	}

	private void setMinnClusters(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		// TODO Auto-generated method stub
		this.setMinnClusters(Math.min(clusters1.size(), clusters2.size()));
	}

	private void setCluster(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		// TODO Auto-generated method stub
		super.setClusters(encoding.buildMaxCluster(clusters1, clusters2));
	}
	
	
	// getter and setter
	public int[] getSgmOffsets() {
		return sgmOffsets;
	}

	public void setSgmOffsets(int[] sgmOffsets) {
		this.sgmOffsets = sgmOffsets;
	}

	public int[] getMinClustersLength() {
		return minClustersLength;
	}

	public void setMinClustersLength(int[] minClustersLength) {
		this.minClustersLength = minClustersLength;
	}

	public int[] getMaxClustersLength() {
		return maxClustersLength;
	}

	public void setMaxClustersLength(int[] maxClustersLength) {
		this.maxClustersLength = maxClustersLength;
	}

	public int getMinnClusters() {
		return minnClusters;
	}

	public void setMinnClusters(int minnClusters) {
		this.minnClusters = minnClusters;
	}

	public int getMaxnClusters() {
		return maxnClusters;
	}

	public void setMaxnClusters(int maxnClusters) {
		this.maxnClusters = maxnClusters;
	}

	public Encoding getEncoding() {
		return encoding;
	}

	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}


}
