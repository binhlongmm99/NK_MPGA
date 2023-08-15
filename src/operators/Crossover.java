package operators;

import java.util.ArrayList;
import java.util.Random;

import models.Parameters;

public class Crossover {
	
    public ArrayList<double[]> SBX(double[] father, double[] mother, Random rnd ){
    	int genLength = father.length;
    	double[] offspring1 = new double[genLength];
    	double[] offspring2 = new double[genLength];
		ArrayList<double[]> offsprings = new ArrayList<double[]>();
		for (int i = 0; i < genLength; i++) {
	    	double beta;
			double u = rnd.nextDouble();
			if (u <= 0.5) {
				beta = Math.pow((2*u), 1.0/(Parameters.n_c +1));
			} else {
				beta = Math.pow(2*(1-u), -1.0/(Parameters.n_c +1));
			}
			offspring1[i] = 0.5 * ((1+beta)*father[i] + (1-beta)*mother[i]);
			offspring2[i] = 0.5 * ((1-beta)*father[i] + (1+beta)*mother[i]);
		}
		offsprings.add(offspring1);
		offsprings.add(offspring2);
		return offsprings;       
    }
    
    public ArrayList<double[]> SBXBounded(double[] father, double[] mother, Random rnd ){
    	double x_l = 0.0, x_u = 1.0; 
    	int genLength = father.length;
    	double[] offspring1 = new double[genLength];
    	double[] offspring2 = new double[genLength];
		ArrayList<double[]> offsprings = new ArrayList<double[]>();
		for (int i = 0; i < genLength; i++) {
			if (rnd.nextDouble() <= 0.5) {
				if (Math.abs(father[i] - mother[i]) > 1e-14) {
					double x1 = Math.min(father[i], mother[i]);
					double x2 = Math.max(father[i], mother[i]);
					double u = rnd.nextDouble();
			    	double beta, alpha, betaq;
			    	beta = 1 + (2 * (x1-x_l)/(x2-x1));
			    	alpha = 2 - Math.pow(beta, -(Parameters.n_c + 1));
			    	if (u <= 1.0/alpha) {
			    		betaq = Math.pow((u * alpha), (1/(Parameters.n_c + 1)));
			    	} else {
			    		betaq = Math.pow((1/(2 - u*alpha)), (1/(Parameters.n_c + 1)));
			    	}
					double c1 = 0.5 * ((1+betaq)*x1 + (1-betaq)*x2);

			    	beta = 1 + (2 * (x_u-x2)/(x2-x1));
			    	alpha = 2 - Math.pow(beta, -(Parameters.n_c + 1));
			    	if (u <= 1.0/alpha) {
			    		betaq = Math.pow((u * alpha), (1/(Parameters.n_c + 1)));
			    	} else {
			    		betaq = Math.pow((1/(2 - u*alpha)), (1/(Parameters.n_c + 1)));
			    	}
					double c2 = 0.5 * ((1-betaq)*x1 + (1+betaq)*x2);
					
					c1 = Math.min(Math.max(c1, x_l), x_u);
					c2 = Math.min(Math.max(c2, x_l), x_u);
					
					if (rnd.nextDouble() <= 0.5) {
						offspring1[i] = c2;
						offspring2[i] = c1;
					} else {
						offspring1[i] = c1;
						offspring2[i] = c2;
					}
				} else {
					offspring1[i] = father[i];
					offspring2[i] = mother[i];
				}
			} else {
				offspring1[i] = father[i];
				offspring2[i] = mother[i];
			}
		}


    	offsprings.add(offspring1);
		offsprings.add(offspring2);
		return offsprings;     
    }
    
}
