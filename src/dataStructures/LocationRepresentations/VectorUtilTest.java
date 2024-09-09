package dataStructures.LocationRepresentations;


import java.util.Random;

import org.junit.jupiter.api.Test;


class VectorUtilTest {

	@Test
	void polarToCartesianTest() {
		double[] v0 = {180, 10};
		double[] v1 = {180, -10};
		double[] p0 = VectorUtil.polarToCartesian(v0);
		double[] p1 = VectorUtil.polarToCartesian(v1);
		
		double[] er0 = {-10, 0};
		double[] er1 = {10, 0};
		
		testEquality(er0, p0);
		testEquality(er1, p1);
		
		double[] v2 = {270, 30};
		double [] p2 = VectorUtil.polarToCartesian(v2);
		double[] er3 = {0, -30};
		testEquality(er3, p2);
		
		double[] v3 = {315, 10};
		double[] p3 = VectorUtil.polarToCartesian(v3);
		double[] er4 = {7, -7};
		testEquality(er4, p3);
		
		double[] v4 = {182, 10};
		double[] p4 = VectorUtil.polarToCartesian(v4);
		double[] er5 = {-9, 0};
		testEquality(er5, p4);		
	}
	
	
	
	@Test
	void cartesianToPolarTest() {
		
		/*
		 * Test using polarToCartesian and checking 
		 * aginst the original, since that's guarenteed 
		 * to work now
		 */
		final int numTests = 10;
		
		Random random = new Random();
		for (int i = 0 ; i < numTests; i++) {

			int direction = random.nextInt(360);
			int magnitude = random.nextInt(25);
			
			double[] v0 = {direction, magnitude};
			System.out.println("ORIGINAL VECTOR: [" + direction + ", " + magnitude + "]");
			double[] p0 = VectorUtil.polarToCartesian(v0);
			double[] v_0 = VectorUtil.cartesianToPolar(p0);
			testEquality(v0, v_0);
		}
		
		
		
		
		
	}
	
	void testEquality(double[] er, double[] ar) {
		final int tolerance = 10;
		
		System.out.println("ER: [" + er[0] + ", " + er[1] + "]");
		System.out.println("AR: [" + ar[0] + ", " + ar[1] + "]");
		
		boolean directionRoughlyEqual = Math.abs(er[0] - ar[0]) < tolerance;
		boolean magntiudeIsZero = er[0] == 0.0;
		assert(directionRoughlyEqual || magntiudeIsZero);
		
		boolean magnitudeRoughlyEqual = Math.abs(er[1] - ar[1]) < tolerance;
		assert(magnitudeRoughlyEqual);
	}
}
