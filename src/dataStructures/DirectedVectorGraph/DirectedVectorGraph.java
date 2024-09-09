package dataStructures.DirectedVectorGraph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;

import dataStructures.LocationRepresentations.Vector;

/**
 * Represents a graph which spawns from one central root node. Each node may
 * have any number of children, but only one parent. Each edge has a vertex on
 * both sides, and also contains a direction and magnitude, representing the
 * relative 2d location compared to it's parent node, much like the polar
 * coordinate system.
 * 
 * @author sammc
 *
 * @param <E> data held in each node
 */
public class DirectedVectorGraph<E> implements Iterable<Node<E>> {

	private int ROOT_ID = 0;
	
	public Node<E> root; // stores the root node for this structure
	private int currentID;

	/**
	 * Constructor for a new DirectedVectorFanGraph(). Creates an empty node as the
	 * root.
	 */
	public DirectedVectorGraph(E data) {
		root = new Node<E>(data, ROOT_ID);
		currentID = 1;
	}

	/**
	 * Returns the root node of this graph.
	 * 
	 * @return Node<E>
	 */
	public Node<E> getRoot() {
		return root;
	}
	
	/**
	 * Adds a child node with the provided data to the given node, with a
	 * displacement represented by the direction and magnitude parameters.
	 * 
	 * @param Node<E> node
	 * @param E       data
	 * @param double  direction
	 * @param double  magnitude
	 */
	public void addNode(Node<E> node, E data, Vector vector) {
		node.spawnChild(data, vector, currentID++);
	}

	// iterator works using breadth first (level order) traversal
	@Override
	public Iterator<Node<E>> iterator() {
		return new BreadthFirstIterator();
	}

	/**
	 * BreadthFirstIterator to simplify dealing with traversals
	 * 
	 * @author sammc
	 */
	private class BreadthFirstIterator implements Iterator<Node<E>> {

		// Stores the list of nodes to be served, starting from the front.
		private Deque<Node<E>> nodeQ = new ArrayDeque<Node<E>>();

		/**
		 * Constructor for a breadthFirstIterator. Creates a new empty nodeQ, and adds
		 * the root
		 */
		public BreadthFirstIterator() {
			nodeQ = new ArrayDeque<Node<E>>();
			nodeQ.addLast(root); // add the root to start the process
		}

		@Override
		public boolean hasNext() {
			return !nodeQ.isEmpty();
		}

		@Override
		public Node<E> next() {
			// get the current node in the queue
			Node<E> current = nodeQ.pop();

			// add all child nodes to the nodeQ
			ArrayList<DirectedEdge<E>> outgoing = current.getOutgoing();
			for (DirectedEdge<E> outgoingEdge : outgoing) {
				Node<E> child = outgoingEdge.getDestination();
				nodeQ.addLast(child);
			}

			// return the current node
			return current;
		}
	}
}
