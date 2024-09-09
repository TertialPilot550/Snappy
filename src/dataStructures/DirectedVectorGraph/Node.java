package dataStructures.DirectedVectorGraph;

import java.util.ArrayList;

import dataStructures.LocationRepresentations.CartesianPoint;
import dataStructures.LocationRepresentations.Vector;
import sceneModel.AbstractComponent;

/**
 * Generic Node class used to make a directed graph
 * 
 * @author sammc
 *
 * @param <E>
 */
public class Node<E> {

	public DirectedEdge<E> parent; // leads to the parent node
	public ArrayList<DirectedEdge<E>> children; // lead to each of the children nodes.
	public E data; // stores the node's data.
	private int id;

	/**
	 * Constructor for a Node with null data and no children
	 */
	public Node(int id) {
		children = new ArrayList<DirectedEdge<E>>();
		data = null;
		this.id = id;
	}

	/**
	 * Constructor for a node with no children, but speciied data
	 * 
	 * @param E data
	 */
	public Node(E data, int ID) {
		children = new ArrayList<DirectedEdge<E>>();
		this.data = data;
		this.id = ID;
	}

	/**
	 * Returns all children of this node.
	 * 
	 * @return ArrayList<DirectedEdge<E>>
	 */
	public ArrayList<DirectedEdge<E>> getOutgoing() {
		return children;
	}

	/**
	 * Removes the paramaterized edge from the list of children edges.
	 * 
	 * @param edge
	 */
	public void removeChild(DirectedEdge<E> edge) {
		children.remove(edge);
	}

	/**
	 * Spawns a new child node by creating a new directed edge with the given
	 * direction and magnitude. Set the data to the data parameter
	 * 
	 * @param direction
	 * @param magnitude
	 * @return Node<E>
	 */
	public Node<E> spawnChild(E data, Vector vector, int newNodeID) {
		Node<E> child = new Node<E>(newNodeID);
		DirectedEdge<E> edgeToChild = new DirectedEdge<E>(this, child, vector); // source is this node,
		// destination is the child node
		child.setIncoming(edgeToChild);
		children.add(edgeToChild);
		child.setData(data);
		return child;
	}

	/**
	 * Set the data stored in the node to the passed parameter.
	 * 
	 * @param E data
	 */
	private void setData(E data) {
		this.data = data;

	}

	
	public CartesianPoint getCartesianRelativeToParent() {
		DirectedEdge<E> parent = getIncoming();
		if (parent == null) {
			return null;
		}
		Vector parentEdgeVector = parent.getVector();
		CartesianPoint relativeLocation = parentEdgeVector.convertToCartesian();
		return relativeLocation;
	}
	
	public CartesianPoint getCartesianRelativeToStructure() {
		Vector relationshipWithStructure = new Vector(0, 0);
		Node<E> cursor = this;
		while (cursor.getIncoming() != null) {
			DirectedEdge<E> edge = cursor.getIncoming();
			relationshipWithStructure = relationshipWithStructure.combine(edge.getVector());
		}

		return relationshipWithStructure.convertToCartesian();
	}

	/**
	 * Returns the DirectedEdge<E> that leads to the parent of this node.
	 * 
	 * @return DirectedEdge<E>
	 */
	public DirectedEdge<E> getIncoming() {
		return parent;
	}

	/**
	 * Sets the path to the parent of this node to the DirectedEdge<E> passed as a
	 * parameter.
	 * 
	 * @param DirectedEdge<E> parent
	 */
	public void setIncoming(DirectedEdge<E> parent) {
		this.parent = parent;
	}
	
	public int getID() {
		return id;
	}
	
	@Override 
	public String toString() {
		return "Parent: " + !(getIncoming() == null) + ", Children: " + getOutgoing().size() + ", Data: " + data.toString();
	}
	
	// componentDataName parentDataComponentName ID direction magnitude
	public static String getNodePrintData(Node<AbstractComponent> node) {
		String result = node.data.getName() + " ";
		
		if (node.parent != null) {
			result = result + node.parent.getSource().getID() + " ";
		}
		
		result = result + node.getID() + " " + node.parent.getDirection() + " " + node.parent.getMagnitude();
		return result;
	}

}
