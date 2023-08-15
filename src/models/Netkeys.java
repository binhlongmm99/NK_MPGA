package models;

import java.util.ArrayList;
import java.util.Collections;

import utils.CycleDetection;
import utils.ObjComparator;

public class Netkeys {
	public ArrayList<Edge> decodeEdges(double[] netkeys, int nVertices){
		int offset[] = new int[nVertices];
		offset[0] = 0;
		int tmp = 1;
		for (int i = 1; i < nVertices; i++) {
			offset[i] += offset[i-1] + nVertices - tmp;
			tmp++;
		}
		ArrayList<Pair> sorted = new ArrayList<>();
		for (int j = 0; j < netkeys.length; j++) {
			sorted.add(new Pair(j, netkeys[j]));
		}
		Collections.sort(sorted, ObjComparator.compareByValueAscending.reversed());
		ArrayList<Edge> edgeList = new ArrayList<>();
		CycleDetection cycleDetect = new CycleDetection();

		while (edgeList.size() != (nVertices-1)) {
			int edgeIndex = sorted.get(0).getId();
			sorted.remove(0);
			tmp = 0;
			while (edgeIndex >= offset[tmp]) {
				tmp++;
			}
			Edge e = new Edge(tmp-1, tmp + edgeIndex - offset[tmp-1]);
			edgeList.add(e);
			if (cycleDetect.isCycle(nVertices, edgeList) == 1) {
				int index = edgeList.size() - 1; 
				edgeList.remove(index);
			}
		}
		return edgeList;

	}
	
	public int[][] decodeTree(ArrayList<Edge> edgeList, int nVertices){
		int tree[][] = new int[nVertices][nVertices];
		for (int i = 0; i < edgeList.size(); i++) {
			int u = edgeList.get(i).getSrc();
			int v = edgeList.get(i).getDst();
			tree[u][v] = 1;
		}

		return tree;
	}
	
}
