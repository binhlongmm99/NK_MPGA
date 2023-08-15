package operators;

import java.util.ArrayList;
import java.util.Random;

import models.CluInstance;
import models.Cluster;
import models.Edge;
import models.Netkeys;


public class Decoding {
	public ArrayList<int[][]> decodeTwoTrees(double[] chromosome, int sgmOffsets[], CluInstance cluIns,
			CluInstance cluIns1, Random rnd) {

		ArrayList<int[][]> trees = new ArrayList<>();
		trees.add(decodeTree(chromosome, sgmOffsets, cluIns.getnVertices(), cluIns.getClusters(), cluIns.getWeightMatrix(), rnd));
		trees.add(decodeTree(chromosome, sgmOffsets, cluIns1.getnVertices(), cluIns1.getClusters(), cluIns1.getWeightMatrix(), rnd));
		
		return trees;

	}
	
	public int[][] decodeTree(double[] chromosome, int sgmOffsets[], int n_vertices, ArrayList<Cluster> clusters, 
			double[][] weightmatrix, Random rnd){
		int[][] tree = new int[n_vertices][n_vertices];
		ArrayList<double[]> decodedChromosome = decodeChromosome(chromosome, sgmOffsets, clusters);
		for (int i = 2; i < decodedChromosome.size(); i++) { 
			Cluster curCluster = clusters.get(i - 2);
			int nClusterVertices = curCluster.getCluster().size();
			double localNetkeys[] = decodedChromosome.get(i);
			Netkeys key = new Netkeys();
			ArrayList<Edge> edgeList = key.decodeEdges(localNetkeys, nClusterVertices);
			int netkeysTree[][] = key.decodeTree(edgeList, nClusterVertices);
			tree = mapToClusterTree(curCluster, tree, netkeysTree, nClusterVertices);
		}
		
		//_______________global tree______________//
		int nClusters = clusters.size();
		double globalNetkeys[] = decodedChromosome.get(0);
		double edgeIndexList[] = decodedChromosome.get(1);
		Netkeys key = new Netkeys();
		
		ArrayList<Edge> edgeList = key.decodeEdges(globalNetkeys, nClusters);
		for (int i = 0; i < edgeList.size(); i++) {
			Cluster cluster1 = clusters.get(edgeList.get(i).getSrc());
			Cluster cluster2 = clusters.get(edgeList.get(i).getDst());
			int clusterSize1 = cluster1.getCluster().size();
			int clusterSize2 = cluster2.getCluster().size();
			int nEdges = clusterSize1 * clusterSize2;
			int index = (int) edgeIndexList[i] * nEdges;
			int root1 = (int) index/clusterSize2;
			int root2 = (int) index/clusterSize1;
			tree[cluster1.getCluster().get(root1)][cluster2.getCluster().get(root2)] = 1;
		}

		for (int i = 0; i < n_vertices; i++) {
			for (int j = 0; j < n_vertices; j++) {
				if (tree[i][j] == 1) {
					tree[j][i] = 1;
				}
			}
		}
		return tree;

	}
	public int[][] mapToClusterTree(Cluster cluster, int[][] tree, int[][] netkeysTree, int nVertices) {
		for (int j = 0; j < nVertices; j++) {
			for (int k = 0; k < nVertices; k++) {
				tree[cluster.getCluster().get(j)][cluster.getCluster().get(k)] = netkeysTree[j][k];
			}
		}
		return tree;
	}
	
	
	public ArrayList<double[]> decodeChromosome(double[] chromosome, int sgmOffsets[], ArrayList<Cluster> clusters) {
		ArrayList<double[]> decodedChro = new ArrayList<>();
		int nClusters = clusters.size();
		double globalSeg[] = getSegmentGenes(chromosome, 0, nClusters*(nClusters-1)/2);
		decodedChro.add(globalSeg);
		double edgeSeg[] = getSegmentGenes(chromosome, sgmOffsets[1], nClusters-1);
		decodedChro.add(edgeSeg);
		
		for (int i = 0; i < nClusters; i++) {
			Cluster cur = clusters.get(i);
			int nClusterVertices = cur.getCluster().size();
			if (nClusterVertices == 1) {
				double[] tmp = {Double.NEGATIVE_INFINITY};
				decodedChro.add(tmp);
			} else {
				double cluSegs[] = getSegmentGenes(chromosome, sgmOffsets[i + 2], nClusterVertices*(nClusterVertices-1)/2);
				decodedChro.add(cluSegs);
	
			}
		}
		return decodedChro;
	}
	
	
	private double[] getSegmentGenes(double[] chromosome, int start, int offset) {
		// TODO Auto-generated method stub
		int end = start + offset;
		double[] segmentGenes = new double[offset];
		for (int i = start; i < end; i++) {
			segmentGenes[i - start] = chromosome[i];
		}
		return segmentGenes;
	}
	
	public double[] convertDoubles(ArrayList<Double> doubles) {
		double[] res = new double[doubles.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = doubles.get(i).doubleValue();
		}
		return res;
	}
	
	public double[] convertDoubleArray(ArrayList<double[]> list) {
		ArrayList<Double> doubles = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).length; j++) {
				doubles.add(list.get(i)[j]);
			}
		}
		double[] res = new double[doubles.size()];
		for (int i = 0; i < doubles.size(); i++) {
			res[i] = doubles.get(i).doubleValue();
		}
		return res;
	}

	
}
