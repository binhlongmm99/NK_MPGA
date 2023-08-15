package operators;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Evaluation {
	
	public double calClusterTreeCost(int[][] tree, double[][] weightMatrix, int nVertices, int root) {
		double clusterTreeCost = 0;
		boolean[] isVisited = new boolean[nVertices];
		int[] preVertex = new int[nVertices];
		int[] weightVertex = new int[nVertices];
		preVertex[root] = -1;
		Queue<Integer> queue = new LinkedList<>();
		Stack<Integer> stack = new Stack<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			int parentVertex = queue.poll();
			isVisited[parentVertex] = true;
			for (int i = 0; i < nVertices; i++) {
				if (tree[parentVertex][i] > 0 && !isVisited[i]) {
					queue.add(i);
					stack.push(i);
					isVisited[i] = true;
					preVertex[i] = parentVertex;
				}
			}
		}
		while (!stack.isEmpty()) {
			int acentdentVertex = stack.pop();
			weightVertex[acentdentVertex] += 1;
			int parentVertex = preVertex[acentdentVertex];
			weightVertex[parentVertex] += weightVertex[acentdentVertex];
			clusterTreeCost += weightVertex[acentdentVertex] * (nVertices - weightVertex[acentdentVertex])
					* weightMatrix[acentdentVertex][parentVertex];
		}
		return clusterTreeCost;
	}

}
