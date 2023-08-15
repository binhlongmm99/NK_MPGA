package utils;

import java.util.Comparator;

import models.Cluster;
import models.Pair;

public class ObjComparator {

	public static Comparator<Cluster> compareByNumberOfCluster = new Comparator<Cluster>() {
		public int compare(Cluster cluster1, Cluster cluster2) {
			return Integer.compare(cluster1.getCluster().size(), cluster2.getCluster().size());
		}
	};

	public static Comparator<Pair> compareByValueAscending = new Comparator<Pair>() {
		public int compare(Pair pair1, Pair pair2) {
			return Double.compare(pair1.getValue(), pair2.getValue());

		}
	};

}
