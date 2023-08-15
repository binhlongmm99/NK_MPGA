package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import models.CluInstance;
import models.Cluster;
import models.Vertex;

public class ReadData {

	public CluInstance readData(String fileName) {
		CluInstance cluIns = new CluInstance();
		BufferedReader br = null;
		try {
			String sCurrentLine = null;
			br = new BufferedReader(new FileReader(fileName));
			for (int j = 0; j < 3; j++) {
				sCurrentLine = br.readLine();
			}

			String[] token = sCurrentLine.split(": ");
			cluIns.setnVertices(Integer.parseInt(token[1]));
			int n_vertices = cluIns.getnVertices();

			sCurrentLine = br.readLine();
			token = sCurrentLine.split(": ");
			cluIns.setnClusters(Integer.parseInt(token[1]));
			int n_clusters = cluIns.getnClusters();

			sCurrentLine = br.readLine();
			sCurrentLine = br.readLine();


			ArrayList<Vertex> listVertices = new ArrayList<>();
			for (int i = 0; i < n_vertices; i++) {
				sCurrentLine = br.readLine();
				token = sCurrentLine.split("\\s+");

				Vertex vertex = new Vertex();
				vertex.setX(Double.parseDouble(token[1]));
				vertex.setY(Double.parseDouble(token[2]));
				listVertices.add(vertex);
			}
			cluIns.setVertices(listVertices);
			
			double weightMatrix[][] = new double[n_vertices][n_vertices];
			for (int i = 0; i < n_vertices; i++) {
				for (int j = 0; j <= i; j++) {
					if (i == j) {
						weightMatrix[i][j] = 0;
					} else {
						weightMatrix[i][j] = weightMatrix[j][i] = Math.sqrt(Math
								.pow(cluIns.getVertices().get(i).getX() - cluIns.getVertices().get(j).getX(), 2)
								+ Math.pow(cluIns.getVertices().get(i).getY() - cluIns.getVertices().get(j).getY(), 2));
					}
				}
			}
			cluIns.setWeightMatrix(weightMatrix);


			
			sCurrentLine = br.readLine();
			sCurrentLine = br.readLine();
			
			ArrayList<Cluster> listCluster = new ArrayList<>();
			for (int i = 0; i < n_clusters; i++) {
				int n_verticesInCluster;
				int label;
				sCurrentLine = br.readLine();
				token = sCurrentLine.split(" ");
				n_verticesInCluster = token.length - 2;
				Cluster cluster = new Cluster();
				for (int j = 0; j < n_verticesInCluster; j++) {
					label = Integer.parseInt(token[j + 1]);
					cluster.addElement(j, label);
				}
				listCluster.add(cluster);
			}
			cluIns.setClusters(listCluster);


			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return cluIns; 

	}
}
