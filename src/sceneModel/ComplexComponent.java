package sceneModel;

import java.awt.Rectangle;

import dataStructures.DirectedVectorGraph.DirectedEdge;
import dataStructures.DirectedVectorGraph.DirectedVectorGraph;
import dataStructures.DirectedVectorGraph.Node;
import dataStructures.LocationRepresentations.CartesianPoint;
import dataStructures.LocationRepresentations.Vector;

/**
 * Complex Component is an AbstractComponent that represents a graph of
 * connections between subcomponents, which are AbstractComponents. Maintains
 * the relationships, allowing for complex 'characters' with multiple image
 * components
 * 
 * Functions:
 * 
 * - add node 
 * - remove node 
 * - move node (absolute location) 
 * - direction lock/unlock node 
 * - magnitude lock/unlock node
 * 
 * TODO:
 * - copy (yikes)
 * 
 * @author sammc
 */

public class ComplexComponent extends AbstractComponent {
	
	public static final int ROOT_SIZE = 25;
	
	private static final boolean DRAWABLE = false; // type flag
	private static final boolean ANIMATED = false; 

	private static final int SIZE = 0; // no absolute size

	private double structureRotation;
	private double structureScale;

	DirectedVectorGraph<AbstractComponent> graph; // stores component location relationship information
	
	/**
	 * Constructor for a ComplexComponent.
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param depth
	 */
	public ComplexComponent(String name, CartesianPoint location, int depth) {
		super(name, location, SIZE, SIZE, depth, DRAWABLE, ANIMATED, true);
		// new graph, with a root with a NullComponent as data
		graph = new DirectedVectorGraph<AbstractComponent>(new NullComponent(location, depth));
	}

	
	/**
	 * Adds the given data to a new node linked to the root.
	 * 
	 * @param AbstractComponent data
	 * @param int               xPos
	 * @param int               yPos
	 */
	public void addNode(AbstractComponent passedData, Vector vector) {
		addNode(graph.getRoot(), passedData, vector);
	}

	public void addNode(Node<AbstractComponent> parent, AbstractComponent passedData, Vector vector) {
		graph.addNode(parent, passedData, vector);
		updateStructure();
	}
	
	public void advanceAnimations() {
		for (Node<AbstractComponent> node : graph) {
			AbstractComponent nodeData = node.data;
			if (nodeData.isAnimated() && ((AnimatedComponent) nodeData).autoToggle()) {
				((AnimatedComponent) nodeData).nextFrame();
			}
		}	
	}
	
	/**
	 * Returns true if the structure contains a node with data that matches the data parameter
	 * @param data
	 * @return boolean
	 */
	public boolean contains(AbstractComponent data) {
		for (Node<AbstractComponent> node : graph) {
			if (node.data == data) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsPoint(CartesianPoint point) {
		return rootContainsPoint(point) || subcomponentContainsPoint(point);
	}

	@Override
	public AbstractComponent copy() {
		// TODO Auto-generated method stub
		return null;
	}
	

	/**
	 * Returns the graph used by this component to store subcomponent relationships
	 * 
	 * @return DirectedVectorGraph<AbstractComponent>
	 */
	public DirectedVectorGraph<AbstractComponent> getGraph() {
		return graph;
	}

	/**
	 * Returns the node within the graph of this complex component with
	 * data that is identical to the passed AbstractComponent.
	 * @param AbstractComponent selected
	 * @return Node<AbstractComponent>
	 */
	public Node<AbstractComponent> getNodeContaining(AbstractComponent data) {
		for (Node<AbstractComponent> node : graph) {
			if (node.data == data) {
				return node;
			}
		}
		return null;
	}

	private int getParentDepth(Node<AbstractComponent> node) {
		Node<AbstractComponent> parent = node.getIncoming().getSource();
		return parent.data.getDepth();
	}

	
	private CartesianPoint getParentLocation(Node<AbstractComponent> node) {
		Node<AbstractComponent> parent = node.getIncoming().getSource();
		return parent.data.getLocation();
	
	}

	public Rectangle getRootBoundingBox() {
		return new Rectangle(this.getX() - ROOT_SIZE / 2, this.getY() + ROOT_SIZE / 2, ROOT_SIZE, ROOT_SIZE);
	}

	/**
	 * Returns the rotation of the structure
	 * 
	 * @return double
	 */
	public double getStructureRotation() {
		return structureRotation;
	}

	/**
	 * Returns the scale of the structure
	 * 
	 * @return double
	 */
	public double getStructureScale() {
		return structureScale;
	}

	

	@Override
	public void move(CartesianPoint location) {
		super.move(location);
		updateStructure();
	}
	
	
	
	
	
	/**
	 * TODO TODO TODO TODO TODO main thing that don't work rn.
	 * 
	 * 
	 * Moves the absolute location of the parameterized node by (xPos, yPos), and
	 * updates the edges to reflect the new relationship created by the movement.
	 * 
	 * @param Node<AbstractComponent> node
	 * @param int                     xPos
	 * @param int                     yPos
	 */
	public void moveNode(Node<AbstractComponent> node, CartesianPoint relativeLocation) {
		DirectedEdge<AbstractComponent> parentEdge = node.getIncoming();
		// update vector
		Vector edgeVector = parentEdge.getVector();
		
		CartesianPoint nodeRelativeLocation = edgeVector.convertToCartesian();
		
		
		
		Vector displacementVector = relativeLocation.convertToVector();
		nodeRelativeLocation.applyVector(displacementVector);
		
		
		Vector newVector = nodeRelativeLocation.convertToVector();
		
		
		
		// if the edge is direction locked, do not change direction
		if (parentEdge.isDirectionLocked()) {
			newVector.direction = parentEdge.getDirection();
		}
		
		// if the edge is magnitude locked, do not change magnitude
		if (parentEdge.isMagnitudeLocked()) {
			newVector.magnitude = parentEdge.getMagnitude();
		}

		
		// update the edge's vector
		parentEdge.setVector(newVector);

		updateStructure(); 

		


	}

	/**
	 * Removes the given node from the structure.
	 * 
	 * @param Node<AbstractComponent> node
	 */
	public void removeNode(Node<AbstractComponent> node) {
		DirectedEdge<AbstractComponent> parentEdge = node.getIncoming();
		Node<AbstractComponent> parent = parentEdge.getSource();

		parent.removeChild(parentEdge);
		
		updateStructure();
	}
	
	// Interfaces with awt shapes, can't replace the paramters on contains()
	public boolean rootContainsPoint(CartesianPoint point) {
		Rectangle complexComponentRootBox = getRootBoundingBox();
		return complexComponentRootBox.contains(point.getX(), point.getY());
	}
	
	@Override 
	public void setDepth(int depth) {
		super.setDepth(depth);
		updateStructure();
	}
	
	
	@Override
	public void setLocation(CartesianPoint location) {
		super.setLocation(location);
		updateStructure();
	}
	
	/**
	 * Sets the lock direction of the relationship with the parameter node's parent
	 * to the locked boolean parameter.
	 * 
	 * @param Node<AbstractComponent> child
	 * @param boolean                 locked
	 */
	public void setLockDirection(Node<AbstractComponent> child, boolean locked) {
		DirectedEdge<AbstractComponent> edge = child.getIncoming();
		edge.setDirectionLocked(locked);
	}
	

	
	/**
	 * Sets the lock magnitude of the relationship with the parameter node's parent
	 * to the locked boolean parameter.
	 * 
	 * @param Node<AbstractComponent> child
	 * @param boolean                 locked
	 */
	public void setLockMagnitude(Node<AbstractComponent> child, boolean locked) {
		DirectedEdge<AbstractComponent> edge = child.getIncoming();
		edge.setMagnitudeLocked(locked);
	}

	public void setNodeRotation(Node<AbstractComponent> selectedNode, double rotation) {
		AbstractComponent nodeData = selectedNode.data;
		if (nodeData.isDrawable()) {
			((SimpleComponent) nodeData).setRotation(rotation);
		}
	}


	public void setNodeScale(Node<AbstractComponent> selectedNode, double scale) {
		AbstractComponent nodeData = selectedNode.data;
		if (nodeData.isDrawable()) {
			((SimpleComponent) nodeData).setScale(scale);
		}
	}
	
	
	/**
	 * Sets the rotation of the structure to the double parameter.
	 * 
	 * @param structureRotation
	 */
	public void setStructureRotation(double structureRotation) {
		this.structureRotation = structureRotation;
	}
	
	/**
	 * Sets the scale of the structure to the double parameter
	 * 
	 * @param structureScale
	 */
	public void setStructureScale(double structureScale) {
		this.structureScale = structureScale;
	}
	
	private boolean subcomponentContainsPoint(CartesianPoint point) {
		for (Node<AbstractComponent> node : graph) {
			AbstractComponent nodeData = node.data;
			if (nodeData.containsPoint(point)) {
				return true;
			}
		}
		return false;
	}
	
	private void updateNodeLocation(Node<AbstractComponent> node) {		
		if (node.getIncoming() == null) {
			updateRootLocation();
		} else {
			updateNonRootNodeLocation(node);
		}
	}
	
	private void updateNonRootNodeLocation(Node<AbstractComponent> node) {
		DirectedEdge<AbstractComponent> parentEdge = node.getIncoming();
		
		CartesianPoint location = getParentLocation(node);
		Vector displacementVector = parentEdge.getVector();
		location.applyVector(displacementVector);

		int depth = getParentDepth(node) + 1;
				
		node.data.setLocation(location);
		node.data.setDepth(depth);

	}

	private void updateRootLocation() {
		AbstractComponent rootData = graph.root.data;
		rootData.setDepth(this.getDepth());
		rootData.setLocation(this.getLocation()); 
	}
	

	private void updateStructure() {
		for (Node<AbstractComponent> node : graph) {
			updateNodeLocation(node);
		}

	}


	
	

}
