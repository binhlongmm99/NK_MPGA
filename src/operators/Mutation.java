package operators;

import java.util.Random;
import models.Parameters;

public class Mutation {

	public double[] polyMutation(double[] father, Random rnd, double muRate){
    	double x_l = 0.0, x_u = 1.0; 
		int genLength = father.length;
		double offspring[] = new double[genLength];
		for (int i = 0; i < genLength; i++) {
			offspring[i] = father[i];
		}
		for (int i = 0; i < genLength; i++) {
			if (rnd.nextDouble() <= muRate) {	
				double delta;
				double u = rnd.nextDouble();
				if (u <= 0.5) {
					delta = Math.pow((2*u), 1.0/(1+Parameters.n_m)) - 1;
					offspring[i] = father[i] + delta * (father[i] - x_l);
				} else {
					delta = 1 - Math.pow(2*(1-u), 1.0/(1+Parameters.n_m));
					offspring[i] = father[i] + delta * (x_u - father[i]);
				}
			}
		}
		return offspring;       
    }
	
	public double[] polyMutationBounded(double[] father, Random rnd, double muRate){
		double x_l = 0.0, x_u = 1.0; 
		int genLength = father.length;
		double offspring[] = new double[genLength];
		for (int i = 0; i < genLength; i++) {
			offspring[i] = father[i];
		}
		for (int i = 0; i < genLength; i++) {
			if (rnd.nextDouble() <= muRate) {	
				double x = father[i];
				double delta1 = (x - x_l)/ (x_u - x_l);
				double delta2 = (x_u - x)/ (x_u - x_l);
				double u = rnd.nextDouble();
				double deltaq, tmp;
				if (u <= 0.5) {
					tmp = 2*u + (1-2*u)* Math.pow((1-delta1), (1+Parameters.n_m));
					deltaq = Math.pow(tmp, 1.0/(1+Parameters.n_m)) - 1;
				} else {
					tmp = 2*(1-u) + 2*(u-0.5)* Math.pow((1-delta2), (1+Parameters.n_m));
					deltaq = 1 - Math.pow(tmp, 1.0/(1+Parameters.n_m));
				}
				x = x + deltaq * (x_u - x_l);
				x = Math.min(Math.max(x, x_l), x_u);
				offspring[i] = x;
			}
		}		
		
		return offspring;       
	
	}
	
}
