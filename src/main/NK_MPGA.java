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

public class NK_MPGA extends NK_MFEA{
	
	public Crossover crossover = new Crossover();
	public Mutation mutation = new Mutation();
	public Encoding encoding = new Encoding();
	public Decoding decoding = new Decoding();
	public Evaluation evaluation = new Evaluation();
	public PopInitialization initPop = new PopInitialization();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Parameters parameters = new Parameters();
		Results results = new Results();
		Results results1 = new Results();
		NK_MPGA mpea = new NK_MPGA();
		Random rnd = new Random();

		for (int i = 0; i < parameters.fileNames.length; i += 2) {
			String fileName = "dataset/" + parameters.fileNames[i] + ".clt";
			String fileName1 = "dataset/" + parameters.fileNames[i + 1] + ".clt";
			results.name = parameters.fileNames[i];
			results1.name = parameters.fileNames[i + 1];
			results.dirName = "results/MPGA/" + parameters.fileNames[i] + "_and_" + parameters.fileNames[i + 1];
			results1.dirName = "results/MPGA/" + parameters.fileNames[i] + "_and_" + parameters.fileNames[i + 1];
			mpea.runExperiment(fileName, fileName1, parameters, results, results1, rnd);
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
			mpea(parameters.popSize, parameters.generations, parameters.rmp, parameters.muRate, parameters.nTasks,
					ins, ins1, unifiedIns, results, results1, rnd);
		}
	}
	
	private void mpea(int popSize, int generations, double rmp, double muRate, int nTasks,
			CluInstance ins, CluInstance ins1, CluUnifiedInstance unifiedIns, Results results, Results results1,
			Random rnd) {
		// TODO Auto-generated method stub

		Utils utils = new Utils();
		long start = System.currentTimeMillis();

		ArrayList<NetkeysUnifiedInd> curPop = new ArrayList<>();
		double[][] bestEachGenerations = new double[generations][nTasks];
		double[][] imRateEachGeneration = new double[generations][nTasks];
		int[] maxClustersLength = unifiedIns.getMaxClustersLength();
		int[] sgmOffsets = unifiedIns.getSgmOffsets();

		curPop = initPop.initPopulationMPEA(maxClustersLength, popSize, nTasks, rnd);
		curPop = updateFactorialCost(curPop, ins, ins1, sgmOffsets, rnd);
		curPop = updateFactorialRank(curPop);
		curPop = updateSkillFactor(curPop);

		Collections.sort(curPop, IndComparator.compareByScalarFitness);

		ArrayList<NetkeysUnifiedInd> bestIndividuals = getBestIndividual(curPop, nTasks);
		double[] bestCosts = getBestCosts(bestIndividuals, nTasks);

		int subpopSize =  (int) popSize/nTasks;
		double[] im_rate = {0.0, 0.0};
		
		for (int i = 0; i < generations; i++) {

			ArrayList<ArrayList<NetkeysUnifiedInd>> subPops = constructSubPopulation(curPop, nTasks, subpopSize, im_rate, rnd);
			bestIndividuals = new ArrayList<NetkeysUnifiedInd>();
				
			for (int t = 0; t < nTasks; t++) {
				int N_other = (int) (im_rate[t] * subpopSize);

				ArrayList<NetkeysUnifiedInd> offspringPop = new ArrayList<>();
				ArrayList<NetkeysUnifiedInd> intermediatePop = new ArrayList<>();
				
				int[] intertaskIndex = new int[subpopSize];
				int subpopTransferSize = 0;
				for (int j = 0; j < subpopSize; j++) {
					intertaskIndex[j] = 0;
				}

				for (int j = 0; j < subpopSize; j++) {
					if (rnd.nextDouble() >= im_rate[t] || N_other == 0) {
						//intra
						int index = rnd.nextInt(subpopSize);
						while (j == index) {
							index = rnd.nextInt(subpopSize);
						}
						NetkeysUnifiedInd child = applyIntraGeneticOperators(subPops.get(t), j, index,
														unifiedIns.getClusters(), sgmOffsets, muRate, rnd);
						offspringPop.add(child);
						
					} else {
						//inter
						intertaskIndex[j] = 1;
						subpopTransferSize++;
						int index = rnd.nextInt(N_other) + subpopSize;
						NetkeysUnifiedInd child = applyInterGeneticOperators(subPops.get(t), j, index,
														unifiedIns.getClusters(), sgmOffsets, muRate, rnd);
						offspringPop.add(child);

						
					}
				}
				offspringPop = updateSubpopFactorialCost(offspringPop, ins, ins1, sgmOffsets, t, rnd);
				
				// update immigration rate
				im_rate[t] = learnImmigrationRate(offspringPop, subPops, im_rate[t], t, 
													intertaskIndex, subpopSize, subpopTransferSize, rnd);
				imRateEachGeneration[i][t] = im_rate[t];
				
				// update next gen population
				intermediatePop.addAll(offspringPop);
				for (int j = 0; j < subpopSize; j++) {
					intermediatePop.add(subPops.get(t).get(j));
				}
				intermediatePop = updateScalarFitness(intermediatePop, t);			
				
				for (int k = 0; k < subpopSize; k++) {
					subPops.get(t).set(k, intermediatePop.get(k));
				}
				bestIndividuals.add(subPops.get(t).get(0));
				
			}
			
			curPop = new ArrayList<NetkeysUnifiedInd>();
			for (int t = 0; t < nTasks; t++) {
				curPop.addAll(subPops.get(t));
			}
			

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
		results.imRate = imRateEachGeneration;
		System.out.println("|" + results.seed + "\t|" + results.name + "\t|" + results1.name + "\t|" + results.bestCost
				+ "\t|" + results1.bestCost + "\t|" + results.time + "|");
		results.storeBestSolution();
		results1.storeBestSolution();
		results.bestCostInEachGeneration3(results, results1);

	}
	

	private double learnImmigrationRate(ArrayList<NetkeysUnifiedInd> offspringPop, ArrayList<ArrayList<NetkeysUnifiedInd>> parentPops, 
			double im_rate, int task, int[] intertaskIndex, int subpopSize, int subpopTransferSize, Random rnd) {
		// TODO Auto-generated method stub
		int success_evolve = 0;
		int success_transfer = 0;
		double alpha = 0.5;
		
		for (int j = 0; j < offspringPop.size(); j++) {
			if (offspringPop.get(j).getFactorialCost()[task] < parentPops.get(task).get(j).getFactorialCost()[task]) {
				success_evolve++;
				if (intertaskIndex[j] == 1) {
					success_transfer++;
				}
			} 
		}
		
		double success_evolve_rate = ((double) success_evolve)/subpopSize;
		double success_transfer_rate, success_self_rate;

		if (subpopTransferSize != 0) {
			success_transfer_rate = ((double) success_transfer)/subpopTransferSize;
			if (subpopTransferSize != subpopSize) {
				success_self_rate = ((double) success_evolve-success_transfer)/(subpopSize - subpopTransferSize);
				if (success_transfer_rate >= success_self_rate) {
					im_rate = Math.min(1, 
							Math.max(0, im_rate + (success_transfer_rate - success_self_rate)
														+ rnd.nextGaussian() * 0.01));
				} else {
					im_rate = Math.min(1, 
							Math.max(0, im_rate - (success_self_rate - success_transfer_rate)
														+ rnd.nextGaussian() * 0.01));
				}
			} else {
				im_rate = Math.min(1, 
						Math.max(0, im_rate - alpha * (1 - success_transfer_rate)
													+ rnd.nextGaussian() * 0.01));
			}
			

		} else {
			im_rate = Math.min(1, Math.max(0, im_rate + alpha * (1-success_evolve_rate)
													+ rnd.nextGaussian() * 0.01));

			
		}
		return im_rate;
	}

	private ArrayList<NetkeysUnifiedInd> updateScalarFitness(ArrayList<NetkeysUnifiedInd> pop, int task) {
		// TODO Auto-generated method stub
		sortPopulationByTask(pop, task);
		for (int j = 0; j < pop.size(); j++) {
			pop.get(j).setFactorialRank(j + 1, task);
			pop.get(j).setSkillFactor(task);
			pop.get(j).setScalarFitness(1.0 / (pop.get(j).getFactorialRank()[task]));
		}
		return pop;
	}

	public ArrayList<ArrayList<NetkeysUnifiedInd>> constructSubPopulation(ArrayList<NetkeysUnifiedInd> pop,
			int nTasks, int subpopSize, double[] im_rate,  Random rnd) {
		ArrayList<ArrayList<NetkeysUnifiedInd>> subPops = new ArrayList<ArrayList<NetkeysUnifiedInd>>();
		for (int i = 0; i < nTasks; i++) {
			ArrayList<NetkeysUnifiedInd> subpop = new ArrayList<>();
			subPops.add(subpop);
		}
		
		for (int i = 0; i < nTasks; i++) {
			int N_other = (int) (im_rate[i] * subpopSize);
			sortPopulationByTask(pop, i);
			for (int j = 0; j < subpopSize; j++) {
				if (pop.get(j).getSkillFactor() == i) {
					subPops.get(i).add(pop.get(j));
				}
			}
			ArrayList<NetkeysUnifiedInd> tmpPop = new ArrayList<NetkeysUnifiedInd>();
			for (int j = subpopSize; j < pop.size(); j++) {
				tmpPop.add(pop.get(j));
			}
			Collections.shuffle(tmpPop, rnd);
			for (int j = 0; j < N_other; j++) {
				subPops.get(i).add(tmpPop.get(j));
			}
		}
		return subPops;
	} 

	private NetkeysUnifiedInd applyInterGeneticOperators(ArrayList<NetkeysUnifiedInd> curPop, int ind1, int ind2,
			ArrayList<Cluster> clusters, int[] sgmOffsets,  double muRate, Random rnd) {
		// TODO Auto-generated method stub		
		NetkeysUnifiedInd par1 = curPop.get(ind1);
		NetkeysUnifiedInd par2 = curPop.get(ind2);
		
		ArrayList<double[]> childrenChromo = crossover.SBXBounded(par1.getChromosome(), par2.getChromosome(), rnd);
		NetkeysUnifiedInd child = new NetkeysUnifiedInd(2);
		child.setChromosome(
				mutation.polyMutationBounded(childrenChromo.get(0), rnd, muRate));
		child.setSkillFactor(par1.getSkillFactor());
		
		return child;
	}	
	
	private NetkeysUnifiedInd applyIntraGeneticOperators(ArrayList<NetkeysUnifiedInd> curPop, int ind1, int ind2,
			ArrayList<Cluster> clusters, int[] sgmOffsets,  double muRate, Random rnd) {
		// TODO Auto-generated method stub		
		NetkeysUnifiedInd par1 = curPop.get(ind1);
		NetkeysUnifiedInd par2 = curPop.get(ind2);
		
		ArrayList<double[]> childrenChromo = crossover.SBXBounded(par1.getChromosome(), par2.getChromosome(), rnd);
		NetkeysUnifiedInd child = new NetkeysUnifiedInd(2);
		child.setChromosome(
				mutation.polyMutationBounded(childrenChromo.get(0), rnd, muRate));
		child.setSkillFactor(par1.getSkillFactor());
		
		NetkeysUnifiedInd child1 = new NetkeysUnifiedInd(2);
		child1.setChromosome(
				mutation.polyMutationBounded(childrenChromo.get(1), rnd, muRate));
		child1.setSkillFactor(par1.getSkillFactor());
		for (int i = 0; i < child.getChromosome().length; i++) {
			if (rnd.nextDouble() <= 0.5) {
				double tmp = child.getChromosome()[i];
				child.getChromosome()[i] = child1.getChromosome()[i];
				child1.getChromosome()[i] = tmp;
			}
		}
		return child;
	}

	protected ArrayList<NetkeysUnifiedInd> updateSubpopFactorialCost(ArrayList<NetkeysUnifiedInd> pop, CluInstance ins,
			CluInstance ins1, int[] sgmOffsets, int task, Random rnd) {
		// TODO Auto-generated method stub
		int nTasks = 2;
		for (NetkeysUnifiedInd ind : pop) {
			double[] factorialCost = new double[nTasks];
			ArrayList<int[][]> solutions = decoding.decodeTwoTrees(ind.getChromosome(), sgmOffsets, ins, ins1,
					rnd);
			if (task == 0) {
				factorialCost[task] = evaluation.calClusterTreeCost(solutions.get(task), ins.getWeightMatrix(),
						ins.getnVertices(), 1);
			} else {
				factorialCost[task] = evaluation.calClusterTreeCost(solutions.get(task), ins1.getWeightMatrix(),
						ins1.getnVertices(), 1);
			}
			factorialCost[1-task] = Double.MAX_VALUE;	
			ind.setFactorialCost(factorialCost);
		}
		return pop;
	}
		
}
