package utils;

import java.util.ArrayList;

import models.Edge;

public class CycleDetection {
	
	private int find(int parent[], int i){
		if (parent[i] == -1)
			return i;
		return find(parent, parent[i]);
	}

	// A utility function to do union of two subsets
	private void Union(int parent[], int x, int y){
		parent[x] = y;
	}
	
	public int isCycle(int nVertices, ArrayList<Edge> edgeList){

        int parent[] = new int[nVertices];
 
        // Initialize all subsets as single element sets
        for (int i = 0; i < nVertices; i++)
            parent[i] = -1;
 
        // Iterate through all edges of graph, find subset of both
        // vertices of every edge, if both subsets are same, then
        // there is cycle in graph.
        for (int i = 0; i < edgeList.size(); ++i)
        {
            int x = this.find(parent, edgeList.get(i).getSrc());
            int y = this.find(parent, edgeList.get(i).getDst());
            if (x == y)
                return 1;
 
            this.Union(parent, x, y);
        }
        return 0;
    }

}
