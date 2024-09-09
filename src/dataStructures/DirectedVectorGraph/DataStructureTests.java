package dataStructures.DirectedVectorGraph;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dataStructures.LocationRepresentations.CartesianPoint;

class DataStructureTests {


	@Test
	void cartesianPointTest() {
		
		// apply vector
		CartesianPoint expectedResult;
		
		CartesianPoint p1 = new CartesianPoint(5, 5);
		CartesianPoint p2 = new CartesianPoint(3, -5);
		p1.combine(p2);
		
		expectedResult = new CartesianPoint(8, 0);
		
		p2 = new CartesianPoint(1000000, -1000000000);
		p1.combine(p2);
		
		expectedResult = new CartesianPoint(1000008, -1000000000);
		testEquality(expectedResult, p1);
		
		// convert to vector
		
		
		// combine
		
		
		
		
	}
	
	void testEquality(CartesianPoint expectedResult, CartesianPoint actualResult) {
		assertEquals(expectedResult.getX(), actualResult.getX());
		assertEquals(expectedResult.getY(), actualResult.getY());
	}

	@Disabled
	@Test
	void vectorTest() {
		
	}
	
	@Disabled
	@Test
	void nodeTest() {
		
	}
	
	
	
}
