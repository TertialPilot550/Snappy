package dataStructures.DirectedVectorGraph;

import dataStructures.LocationRepresentations.Vector;

/**
 * Represents a directed edge that has a magnitude and direction. Used to map
 * nodes in a directed graph.
 * 
 * @author sammc
 *
 * @param <E>
 */
public class DirectedEdge<E> {

	private Node<E> source; // source node
	private Node<E> destination; // destination node

	private Vector vector;

	private boolean directionLocked; // whether or not the direction is locked
	private boolean magnitudeLocked; // whether or not the magnitude is locked

	/**
	 * Constructor for a DirectedEdge. Requires both the source and destination be
	 * passed, though this can be avoided by passing null, and updating the values
	 * later.
	 * 
	 * @param source
	 * @param destination
	 * @param direction
	 * @param magnitude
	 */
	public DirectedEdge(Node<E> source, Node<E> destination, Vector vector) {
		this.source = source;
		this.destination = destination;
		this.vector = vector;
	}

	/**
	 * Sets the direction and magnitude of this directed edge to the passed values.
	 * 
	 * @param direction
	 * @param magnitude
	 */
	public void setVector(Vector v) {
		vector = v;
	}

	/**
	 * Returns a length 2 double array represent the direction and magnitude of the
	 * edges vector
	 * 
	 * @return double[]
	 */
	public Vector getVector() {
		return vector;
	}

	/**
	 * Returns the source node of this directed edge.
	 * 
	 * @return Node<E>
	 */
	public Node<E> getSource() {
		return source;
	}

	/**
	 * Returns the destination node of this directed edge.
	 * 
	 * @return Node<E>
	 */
	public Node<E> getDestination() {
		return destination;
	}

	/**
	 * Returns the direction in degrees of the vector.
	 * 
	 * @return
	 */
	public double getDirection() {
		return vector.direction;
	}

	/**
	 * Returns the magnitude of the vector.
	 * 
	 * @return double
	 */
	public double getMagnitude() {
		return vector.magnitude;
	}

	/**
	 * Sets whether the direction is locked or not based on the parameter boolean.
	 * 
	 * @param locked
	 */
	public void setDirectionLocked(boolean locked) {
		directionLocked = locked;
	}

	/**
	 * Sets whether the magnitude is locked or not based on the parameter boolean.
	 * 
	 * @param locked
	 */
	public void setMagnitudeLocked(boolean locked) {
		magnitudeLocked = locked;
	}

	/**
	 * Returns whether the direction of this relationship is currently locked.
	 * 
	 * @return boolean
	 */
	public boolean isDirectionLocked() {
		return directionLocked;
	}

	/**
	 * Returns whether the magnitude of this relationship is currently locked.
	 * 
	 * @return boolean
	 */
	public boolean isMagnitudeLocked() {
		return magnitudeLocked;
	}

}
