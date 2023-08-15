package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import models.NetkeysUnifiedInd;
import utils.Utils;

public class PopInitialization {

	public double[] initChromosome(int[] clustersLength, Random rnd) {
		ArrayList<Double> chromosome = new ArrayList<>();
		int nClusters = clustersLength.length;
		
		ArrayList<Double> randomNetkeysGlobal = initRandomNetkeysString(nClusters, rnd);
		chromosome.addAll(randomNetkeysGlobal);
		ArrayList<Double> edgeIndex = new ArrayList<>();
		for (int i = 0; i < nClusters - 1; i++) {
			edgeIndex.add(rnd.nextDouble());
		}
		chromosome.addAll(edgeIndex);
		
		for (int i = 0; i < nClusters; i++) {
			ArrayList<Double> randomNetkeysLocal = initRandomNetkeysString(clustersLength[i], rnd);
			chromosome.addAll(randomNetkeysLocal);
		}
		Utils utils = new Utils();
		double[] newChromosome = utils.convertDoubles(chromosome);
		return newChromosome;
	}

	private ArrayList<Double> initRandomNetkeysString(int nVertices, Random rnd) {
		ArrayList<Double> randomNetkeys = new ArrayList<Double>();
		if (nVertices == 1) {
			randomNetkeys.add(Double.NEGATIVE_INFINITY);// -inf: the number of vertices in a cluster = 1;
		} else {
			int size = nVertices * (nVertices-1)/2;
			for (int j = 0; j < size; j++) {
				randomNetkeys.add(rnd.nextDouble());
			}
			Collections.shuffle(randomNetkeys, rnd);
		}
		return randomNetkeys;
	}


	public ArrayList<NetkeysUnifiedInd> initPopulation(int[] clustersLength, int popLength, int nTasks, Random rnd) {
		ArrayList<NetkeysUnifiedInd> population = new ArrayList<>();
		for (int m = 0; m < popLength; m++) {
			NetkeysUnifiedInd individual = new NetkeysUnifiedInd(nTasks);
			double[] chromosome = initChromosome(clustersLength, rnd);
			individual.setChromosome(chromosome);
			individual.setSkillFactor(rnd.nextInt(2));
			population.add(individual);
		}
		return population;
	}
	
	public ArrayList<NetkeysUnifiedInd> initPopulationMPEA(int[] clustersLength, int popLength, int nTasks, Random rnd) {
		ArrayList<NetkeysUnifiedInd> population = new ArrayList<>();
		for (int m = 0; m < popLength; m++) {
			NetkeysUnifiedInd individual = new NetkeysUnifiedInd(nTasks);
			double[] chromosome = initChromosome(clustersLength, rnd);
			individual.setChromosome(chromosome);
			int skillfactor = m % 2;
			individual.setSkillFactor(skillfactor);
			population.add(individual);
		}
		return population;
	}

	public ArrayList<NetkeysUnifiedInd> initPopulationGA(int[] clustersLength, int popLength, int nTasks, Random rnd) {
		ArrayList<NetkeysUnifiedInd> population = new ArrayList<>();
		for (int m = 0; m < popLength; m++) {
			NetkeysUnifiedInd individual = new NetkeysUnifiedInd(nTasks);
			double[] chromosome = initChromosome(clustersLength, rnd);
			individual.setChromosome(chromosome);
			individual.setSkillFactor(-1);
			population.add(individual);
		}
		return population;
	}

}
