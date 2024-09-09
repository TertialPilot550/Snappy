package dataStructures.LocationRepresentations;

public class CartesianPoint {

	private double x;
	private double y;
	
	public CartesianPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public CartesianPoint(CartesianPoint point) {
		this.x = point.x;
		this.y = point.y;
	}

	public int getX() {
		return (int) x;
	}
	
	public int getY() {
		return (int) y;
	}
	
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getXReal() {
		return x;
	}
	
	public double getYReal() {
		return y;
	}
	
	public void applyVector(Vector v) {
		x += v.getXComponentReal();
		y += v.getYComponentReal();
	}
	
	public void combine(CartesianPoint p) {
		x += p.x;
		y += p.y;
	}
	
	public Vector convertToVector() {
		double[] v = VectorUtil.cartesianToPolar(x, y);
		return new Vector(v[0], v[1]);
	}
	
	@Override 
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	
}
