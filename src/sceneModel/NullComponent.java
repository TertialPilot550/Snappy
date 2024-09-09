package sceneModel;

import dataStructures.LocationRepresentations.CartesianPoint;

/**
 * NullComponent, used as a way to store an abstract coordinate on the screen
 * using complex components.
 * 
 * @author sammc
 */
public class NullComponent extends AbstractComponent {

	private static final String NAME = "null"; // null components name is null as a String

	// Null components have the following null data values
	private static final int WIDTH = 0; // width
	private static final int HEIGHT = 0; // height
	private static final boolean DRAWABLE = false; // drawable
	private static final boolean ANIMATED = true; // TYPE FLAG; Distinguishs a component from a ComplexComponent
	private static final boolean VISIBLE = false;

	/**
	 * Constructor for a null component with no specified depth or location.
	 */
	public NullComponent() {
		this(new CartesianPoint(0, 0), 0);
	}

	/**
	 * Constructor for a null component.
	 * 
	 * @param int xPos
	 * @param int yPos
	 * @param int depth
	 */
	public NullComponent(CartesianPoint location, int depth) {
		super(NAME, location, WIDTH, HEIGHT, depth, DRAWABLE, ANIMATED, VISIBLE);
	}

	@Override
	public AbstractComponent copy() {
		return new NullComponent(getLocation(), getDepth());
	}

}
