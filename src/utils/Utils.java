package utils;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Utils {
	public String getTimeFromMillis(long millis) {
		String string = String.format("%02d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
				millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
		return string;
	}
	
	public double[] convertDoubles(ArrayList<Double> doubles) {
		double[] res = new double[doubles.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = doubles.get(i).doubleValue();
		}
		return res;
	}
	
	public double[][] ones(int a, int b) {
		double[][] matrix = new double[a][b];
		for (int i = 0; i < a; i++) {
			for (int j = 0; j < b; j++) {
				matrix[i][j] = 1;
			}
		}
		return matrix;
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
