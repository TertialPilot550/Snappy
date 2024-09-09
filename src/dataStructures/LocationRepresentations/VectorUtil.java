package dataStructures.LocationRepresentations;

public class VectorUtil {
	
	
	public static double[] polarToCartesian(double[] vector) {
		return polarToCartesian(vector[0], vector[1]);
	}

	
	
	
	public static double[] polarToCartesian(double direction, double magnitude) {
		
		double[] point = new double[2];

		point[0] = (magnitude * Math.cos(Math.toRadians(direction)));
		point[1] = (magnitude * Math.sin(Math.toRadians(direction)));
		
		return point;
	}

	public static double[] cartesianToPolar(double[] coords) {
		return cartesianToPolar(coords[0], coords[1]);
	}

	public static double[] cartesianToPolar(double x, double y) {
		final int directionValue = 0;
		final int magnitudeValue = 1;
		
		double[] vector = new double[2];

		if (y != 0) {
			
			
			double temp = ((double) y) / x;
			double directionRadians = Math.atan(temp);
			double theta = Math.toDegrees(directionRadians);
			
			if (x < 0) {
				theta += 180;
			} else if (y < 0) {
				theta += 360;
			}
			
			vector[directionValue] = theta;
		} else {
			vector[directionValue] = 0;
		}
		
		double magnitudeSquared = Math.pow(x, 2) + Math.pow(y, 2);
		vector[magnitudeValue] = Math.sqrt(magnitudeSquared); // hypotenus of the triangle
		
		return vector;
		
	}	
	

}
