package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import models.CluInstance;
import models.CluUnifiedInstance;
import models.Cluster;
import models.NetkeysUnifiedInd;
import models.Parameters;
import models.Results;
import operators.Crossover;
import operators.Decoding;
import operators.Encoding;
import operators.Evaluation;
import operators.Mutation;
import operators.PopInitialization;
import utils.IndComparator;
import utils.ObjComparator;
import utils.ReadData;
import utils.Utils;


public class NK_MFEA {
	public Crossover crossover = new Crossover();
	public Mutation mutation = new Mutation();
	public Encoding encoding = new Encoding();
	public Decoding decoding = new Decoding();
	public Evaluation evaluation = new Evaluation();
	public PopInitialization initPop = new PopInitialization();

	public static void main(String[] args) {
		Parameters parameters = new Parameters();
		Results results = new Results();
		Results results1 = new Results();
		NK_MFEA mfea = new NK_MFEA();
		Random rnd = new Random();

		for (int i = 0; i < parameters.fileNames.length; i += 2) {
			String fileName = "dataset/" + parameters.fileNames[i] + ".clt";
			String fileName1 = "dataset/" + parameters.fileNames[i + 1] + ".clt";
			results.name = parameters.fileNames[i];
			results1.name = parameters.fileNames[i + 1];
			results.dirName = "results/MFEA/" + parameters.fileNames[i] + "_and_" + parameters.fileNames[i + 1];
			results1.dirName = "results/MFEA/" + parameters.fileNames[i] + "_and_" + parameters.fileNames[i + 1];
			mfea.runExperiment(fileName, fileName1, parameters, results, results1, rnd);
		}

	}

	private void runExperiment(String fileName, String fileName1, Parameters parameters, Results results,
			Results results1, Random rnd) {
		// TODO Auto-generated method stub
		CluInstance ins = new CluInstance();
		CluInstance ins1 = new CluInstance();
		ReadData readData = new ReadData();
		ins = readData.readData(fileName);
		ins1 = readData.readData(fileName1);
		Collections.sort(ins.getClusters(), ObjComparator.compareByNumberOfCluster);
		Collections.sort(ins1.getClusters(), ObjComparator.compareByNumberOfCluster);
		CluUnifiedInstance unifiedIns = new CluUnifiedInstance(ins.getClusters(), ins1.getClusters());
		for (int i = 0; i < parameters.nRuns; i++) {
			System.out.println(
					"------------------------------------------------------------------------------------------------------");
			rnd.setSeed(i);
			results.seed = i;
			results1.seed = i;
			mfea(parameters.popSize, parameters.generations, parameters.rmp, parameters.muRate, parameters.nTasks,
					ins, ins1, unifiedIns, results, results1, rnd);
		}
	}

	private void mfea(int popSize, int generations, double rmp, double muRate, int nTasks,
			CluInstance ins, CluInstance ins1, CluUnifiedInstance unifiedIns, Results results, Results results1,
			Random rnd) {
		// TODO Auto-generated method stub

		Utils utils = new Utils();
		long start = System.currentTimeMillis();

		ArrayList<NetkeysUnifiedInd> curPop = new ArrayList<>();
		double[][] bestEachGenerations = new double[generations][nTasks];

		int[] maxClustersLength = unifiedIns.getMaxClustersLength();
		int[] sgmOffsets = unifiedIns.getSgmOffsets();
				
		curPop = initPop.initPopulation(maxClustersLength, popSize, nTasks, rnd);
		curPop = updateFactorialCost(curPop, ins, ins1, sgmOffsets, rnd);
		curPop = updateFactorialRank(curPop);
		curPop = updateSkillFactor(curPop);

		Collections.sort(curPop, IndComparator.compareByScalarFitness);

		ArrayList<NetkeysUnifiedInd> bestIndividuals = getBestIndividual(curPop, nTasks);
		double[] bestCosts = getBestCosts(bestIndividuals, nTasks);
		

		for (int i = 0; i < generations; i++) {
			ArrayList<NetkeysUnifiedInd> offspringPop = new ArrayList<>();
			ArrayList<NetkeysUnifiedInd> intermediatePop = new ArrayList<>();

			offspringPop = applyGeneticOperators(curPop, popSize, unifiedIns.getClusters(), sgmOffsets, rmp, muRate,
					rnd);
			offspringPop = updateFactorialCost(offspringPop, ins, ins1, sgmOffsets, rnd);
			intermediatePop.addAll(offspringPop);
			intermediatePop.addAll(curPop);

			intermediatePop = updateFactorialRank(intermediatePop);
			intermediatePop = updateSkillFactor(intermediatePop);

			Collections.sort(intermediatePop, IndComparator.compareByScalarFitness);
			for (int j = 0; j < popSize; j++) {
				curPop.set(j, intermediatePop.get(j));
			}
			bestIndividuals = getBestIndividual(curPop, nTasks);

			double[] newbestCosts = getBestCosts(bestIndividuals, nTasks);
			bestCosts = updateBestCosts(bestCosts, newbestCosts, nTasks);
			bestEachGenerations[i] = bestCosts;
		}

		long end = System.currentTimeMillis();
		results.time = utils.getTimeFromMillis(end - start);
		results1.time = results.time;
		results.bestCost = bestCosts[0];
		results1.bestCost = bestCosts[1];
		results.bestCostInEachGeneration = bestEachGenerations;
		System.out.println("|" + results.seed + "\t|" + results.name + "\t|" + results1.name + "\t|" + results.bestCost
				+ "\t|" + results1.bestCost + "\t|" + results.time + "|");
		results.storeBestSolution();
		results1.storeBestSolution();
		results.bestCostInEachGeneration(results, results1);

	}


	protected double[] updateBestCosts(double[] oldbestCosts, double[] newbestCosts, int nTasks) {
		// TODO Auto-generated method stub
		for (int i = 0; i < nTasks; i++) {
			if (oldbestCosts[i] < newbestCosts[i]) {
				newbestCosts[i] = oldbestCosts[i];
			}
		}
		return newbestCosts;
	}

	protected ArrayList<NetkeysUnifiedInd> applyGeneticOperators(ArrayList<NetkeysUnifiedInd> pop, int popSize,
			ArrayList<Cluster> clusters, int[] sgmOffsets, double rmp, double muRate, Random rnd) {
		// TODO Auto-generated method stub
		ArrayList<NetkeysUnifiedInd> offsprings = new ArrayList<>();

		while (offsprings.size() < popSize) {
			int pos1 = rnd.nextInt(popSize);
			int pos2 = rnd.nextInt(popSize);
			while (pos1 == pos2) {
				pos2 = rnd.nextInt(popSize);
			}
			NetkeysUnifiedInd par1 = pop.get(pos1);
			NetkeysUnifiedInd par2 = pop.get(pos2);
			double r = rnd.nextDouble();
			if (par1.getSkillFactor() == par2.getSkillFactor() || r < rmp) {

				ArrayList<double[]> childrenChromo = crossover.SBXBounded(par1.getChromosome(), par2.getChromosome(), rnd);
				NetkeysUnifiedInd child = new NetkeysUnifiedInd(2);
				NetkeysUnifiedInd child1 = new NetkeysUnifiedInd(2);
				child.setChromosome(childrenChromo.get(0));
				child1.setChromosome(childrenChromo.get(1));
				if (rnd.nextDouble() < 0.5) {
					child.setSkillFactor(par1.getSkillFactor());
				} else {
					child.setSkillFactor(par2.getSkillFactor());
				}
				if (rnd.nextDouble() < 0.5) {
					child1.setSkillFactor(par1.getSkillFactor());
				} else {
					child1.setSkillFactor(par2.getSkillFactor());
				}
				for (int i = 0; i < child.getChromosome().length; i++) {
					if (rnd.nextDouble() <= 0.5) {
						double tmp = child.getChromosome()[i];
						child.getChromosome()[i] = child1.getChromosome()[i];
						child1.getChromosome()[i] = tmp;
					}
				}

				offsprings.add(child);
				offsprings.add(child1);

			} else {
				par1.setChromosome(
						mutation.polyMutationBounded(par1.getChromosome(), rnd, muRate));
				par2.setChromosome(
						mutation.polyMutationBounded(par2.getChromosome(), rnd, muRate));
				offsprings.add(par1);
				offsprings.add(par2);
			}
		}
		return offsprings;
	}

	protected double[] getBestCosts(ArrayList<NetkeysUnifiedInd> bestIndividuals, int nTasks) {
		// TODO Auto-generated method stub
		double[] bestCosts = new double[nTasks];
		for (int i = 0; i < nTasks; i++) {
			int skillFactor = bestIndividuals.get(i).getSkillFactor();
			bestCosts[skillFactor] = bestIndividuals.get(i).getFactorialCost()[skillFactor];
		}
		return bestCosts;
	}

	protected ArrayList<NetkeysUnifiedInd> getBestIndividual(ArrayList<NetkeysUnifiedInd> pop, int nTasks) {
		// TODO Auto-generated method stub
		ArrayList<NetkeysUnifiedInd> bestInd = new ArrayList<>();
		for (int i = 0; i < nTasks; i++) {
			int firstElement = 0;
			while (pop.get(firstElement).getSkillFactor() != i) {
				firstElement += 1;
			}
			bestInd.add(pop.get(firstElement));
		}
		return bestInd;
	}

	protected ArrayList<NetkeysUnifiedInd> updateSkillFactor(ArrayList<NetkeysUnifiedInd> pop) {
		// TODO Auto-generated method stub
		for (NetkeysUnifiedInd ind : pop) {
			if (ind.getFactorialRank()[0] <= ind.getFactorialRank()[1]) {
				ind.setSkillFactor(0);
				ind.setScalarFitness(1.0 / (ind.getFactorialRank()[0]));
			} else {
				ind.setSkillFactor(1);
				ind.setScalarFitness(1.0 / (ind.getFactorialRank()[1]));
			}
		}
		return pop;
	}

	protected ArrayList<NetkeysUnifiedInd> updateFactorialRank(ArrayList<NetkeysUnifiedInd> pop) {
		// TODO Auto-generated method stub
		int popSize = pop.size();
		sortPopulationByTask(pop, 0);
		for (int i = 0; i < popSize; i++) {
			pop.get(i).setFactorialRank(i + 1, 0);
		}
		sortPopulationByTask(pop, 1);
		for (int i = 0; i < popSize; i++) {
			pop.get(i).setFactorialRank(i + 1, 1);
		}
		return pop;
	}

	protected ArrayList<NetkeysUnifiedInd> sortPopulationByTask(ArrayList<NetkeysUnifiedInd> pop, int task) {
		// TODO Auto-generated method stub
		IndComparator com = new IndComparator(task);
		Collections.sort(pop, com);
		return pop;
	}

	protected ArrayList<NetkeysUnifiedInd> updateFactorialCost(ArrayList<NetkeysUnifiedInd> pop, CluInstance ins,
			CluInstance ins1, int[] sgmOffsets, Random rnd) {
		// TODO Auto-generated method stub
		int nTasks = 2;
		for (NetkeysUnifiedInd ind : pop) {
			double[] factorialCost = new double[nTasks];
			ArrayList<int[][]> solutions = decoding.decodeTwoTrees(ind.getChromosome(), sgmOffsets, ins, ins1,
					rnd);
			if (ind.getSkillFactor() == 0) {
				factorialCost[0] = evaluation.calClusterTreeCost(solutions.get(0), ins.getWeightMatrix(),
						ins.getnVertices(), 1);
				factorialCost[1] = Double.MAX_VALUE;
			} else if (ind.getSkillFactor() == 1) {
				factorialCost[1] = evaluation.calClusterTreeCost(solutions.get(1), ins1.getWeightMatrix(),
						ins1.getnVertices(), 1);
				factorialCost[0] = Double.MAX_VALUE;
			} else {
				factorialCost[0] = evaluation.calClusterTreeCost(solutions.get(0), ins.getWeightMatrix(),
						ins.getnVertices(), 1);
				factorialCost[1] = evaluation.calClusterTreeCost(solutions.get(1), ins1.getWeightMatrix(),
						ins1.getnVertices(), 1);

			}
			ind.setFactorialCost(factorialCost);
		}
		return pop;
	}



}
