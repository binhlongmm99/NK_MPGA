package utils;

import java.util.Comparator;
import models.NetkeysUnifiedInd;

public class IndComparator extends ObjComparator implements Comparator<NetkeysUnifiedInd>{
	public int task = 0;
	public IndComparator(int task){
		this.task = task;
	}
	
	@Override
	public int compare(NetkeysUnifiedInd ind1, NetkeysUnifiedInd ind2) {
		// TODO Auto-generated method stub
		return (ind1.getFactorialCost()[task] < ind2.getFactorialCost()[task] ? -1
				: ind1.getFactorialCost()[task] > ind2.getFactorialCost()[task] ? 1 : 0);
	}
	
	public static Comparator<NetkeysUnifiedInd> compareByScalarFitness = new Comparator<NetkeysUnifiedInd>() {
		public int compare(NetkeysUnifiedInd one, NetkeysUnifiedInd other) {
			return Double.compare(other.getScalarFitness(), one.getScalarFitness());
		}
	};
	
}
