package sceneModel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dataStructures.LocationRepresentations.CartesianPoint;

class ComplexComponentTest {

	@Test
	void test() {
		
		ComplexComponent comp = new ComplexComponent("TestComplex", new CartesianPoint(100, 100), 10);
		
		comp.move(new CartesianPoint(5, -5));
		assertEquals(comp.getX(), 105);
		assertEquals(comp.getY(), 95);
		
		
		
	}

}
