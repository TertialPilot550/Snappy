package dataStructures.LocationRepresentations;

public class Vector {

	public double direction;
	public double magnitude;
	
	public Vector(double direction, double magnitude) {
		this.direction = direction;
		this.magnitude = magnitude;
	}
	
	public int getXComponent() {
		double[] coords = VectorUtil.polarToCartesian(direction, magnitude);
		return (int) Math.round(coords[0]);
	}
	
	public double getXComponentReal() {
		double[] coords = VectorUtil.polarToCartesian(direction, magnitude);
		return coords[0];
	}
	
	public int getYComponent() {
		double[] coords = VectorUtil.polarToCartesian(direction, magnitude);
		return (int) Math.round(coords[1]);
	}
	
	public double getYComponentReal() {
		double[] coords = VectorUtil.polarToCartesian(direction, magnitude);
		return coords[1];
	}
	
	public Vector combine(Vector v) {
		double newXComponent = this.getXComponentReal() + v.getXComponentReal();
		double newYComponent = this.getYComponentReal() + v.getYComponentReal();
		
		CartesianPoint newPoint = new CartesianPoint(newXComponent, newYComponent);
		
		return newPoint.convertToVector();
	}
	
	public CartesianPoint convertToCartesian() {
		return new CartesianPoint(getXComponent(), getYComponent());
	}
	
	@Override
	public String toString() {
		return "[" + direction + ", " + magnitude + "]";
	}
	
}
