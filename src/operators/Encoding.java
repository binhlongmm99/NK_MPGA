package operators;

import java.util.ArrayList;

import models.Cluster;

public class Encoding {
	
	public int[] getSegOffsets(int[] clustersLength) {
		int nClusters = clustersLength.length;
		int[] segOffsets = new int[nClusters + 3];
		segOffsets[0] = 0;
		segOffsets[1] = nClusters * (nClusters-1)/2;
		segOffsets[2] = segOffsets[1] + nClusters-1;
		
		for (int i = 3; i < segOffsets.length; i++) {
			if (clustersLength[i-3] == 1) {
				segOffsets[i] = segOffsets[i-1] + 1;
			} else {
				segOffsets[i] = segOffsets[i-1] + clustersLength[i-3] * (clustersLength[i-3]-1)/2;
			}
		}
		
		return segOffsets;
	}
	
	public int[] getClustersLength(ArrayList<Cluster> clusters) {
		int nClusters = clusters.size();
		int[] clustersLength = new int[nClusters];
		for (int i = 0; i < nClusters; i++) {
			clustersLength[i] = clusters.get(i).getCluster().size();
		}
		return clustersLength;
	}
	
	public int[] getTwoArrayMinElements(int[] array1, int[] array2) {
		int lenArray1 = array1.length;
		int lenArray2 = array2.length;
		int maxLen = Math.max(lenArray1, lenArray2);
		int[] minElements = new int[maxLen];

		for (int i = 0; i < maxLen; i++) {
			if (i < lenArray1 && i < lenArray2) {
				minElements[i] = Math.min(array1[i], array2[i]);
			} else if (i >= lenArray1) {
				minElements[i] = array2[i];
			} else {
				minElements[i] = array1[i];
			}
		}

		return minElements;
	}
	
	public int[] getMinClustersLength(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		int[] clustersLength1 = getClustersLength(clusters1);
		int[] clustersLength2 = getClustersLength(clusters2);
		int[] minClustersLength = getTwoArrayMinElements(clustersLength1, clustersLength2);

		return minClustersLength;

	}
	
	public int[] getTwoArrayMaxElements(int[] array1, int[] array2) {
		int lenArray1 = array1.length;
		int lenArray2 = array2.length;
		int maxLen = Math.max(lenArray1, lenArray2);
		int[] maxElements = new int[maxLen];

		for (int i = 0; i < maxLen; i++) {
			if (i < lenArray1 && i < lenArray2) {
				maxElements[i] = Math.max(array1[i], array2[i]);
			} else if (i >= lenArray1) {
				maxElements[i] = array2[i];
			} else {
				maxElements[i] = array1[i];
			}
		}

		return maxElements;
	}
	
	public int[] getMaxClustersLength(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		int[] clustersLength1 = getClustersLength(clusters1);
		int[] clustersLength2 = getClustersLength(clusters2);
		int[] maxClustersLength = getTwoArrayMaxElements(clustersLength1, clustersLength2);

		return maxClustersLength;

	}
	
	public int getMaxNumVertices(int[] maxClustersLength) {
		int maxNumVertices = 0;
		for (int i = 0; i < maxClustersLength.length; i++) {
			maxNumVertices += maxClustersLength[i];
		}

		return maxNumVertices;
	}
	
	public ArrayList<Cluster> buildMaxCluster(ArrayList<Cluster> clusters1, ArrayList<Cluster> clusters2) {
		ArrayList<Cluster> maxClusters = new ArrayList<>();
		int[] maxClustersLength = getMaxClustersLength(clusters1, clusters2);
		int vertex = 0;
		for (int i = 0; i < maxClustersLength.length; i++) {
			Cluster cluster = new Cluster();
			for (int j = 0; j < maxClustersLength[i]; j++) {
				cluster.addElement(j, vertex);
				vertex++;
			}
			maxClusters.add(cluster);
		}
		return maxClusters;

	}

}
