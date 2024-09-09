package sceneModel;

import dataStructures.LocationRepresentations.CartesianPoint;

/**
 * Serves as the parent for all components that can be passed to the renderer.
 * Naturally ordered by depth.
 * 
 * @author sammc
 */
public abstract class AbstractComponent implements Comparable<AbstractComponent> {

	protected static final int DEFAULT_DEPTH = 5; // stores the default depth value

	private String name; // stores the name of the component
	
	private CartesianPoint location;

	private int width; // stores the width of the component
	private int height; // stores the height of the component
	private int depth; // stores the depth of the component

	private boolean drawable; // stores whether or not the item has an associated image
	private boolean animated; // stores whether or not the item has a set of animated frames
	private boolean visible; // stores whether or not the item should be rendered

	/**
	 * Constructor
	 * 
	 * @param int xPos
	 * @param int yPos
	 * @param int width
	 * @param int height
	 * @param int depth
	 * @param int drawable
	 */
	public AbstractComponent(String name, CartesianPoint location, int width, int height, int depth, boolean drawable,
			boolean animated, boolean visible) {
		this.name = name;
		this.location = location;
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.drawable = drawable;
		this.animated = animated;
		this.visible = visible;
	}

	/**
	 * Set the location of the component to the passed cartesian coordinate
	 * 
	 * @param int[] coords
	 */
	public void setLocation(CartesianPoint location) {
		this.location = location;
	}

	/**
	 * Sets the size of the component
	 * 
	 * @param int width
	 * @param int height
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the components X position
	 * 
	 * @return int
	 */
	public int getX() {
		return location.getX();
	}

	/**
	 * Returns the components Y position
	 * 
	 * @return int
	 */
	public int getY() {
		return location.getY();
	}

	/**
	 * Returns an int array of length to containing the x coordinate at index 0 and
	 * y coordinate at index 1.
	 * 
	 * @return int[]
	 */
	public CartesianPoint getLocation() {
		return new CartesianPoint(location);
	}

	/**
	 * Returns the component's height
	 * 
	 * @return int
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the component's height
	 * 
	 * @return int
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the component's depth
	 * 
	 * @return int
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Sets the depth value for the component equal to the depth parameter
	 * 
	 * @param int depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * Moves the component from it's current position, rather than setting location.
	 * 
	 * @param int deltaX
	 * @param int deltaY
	 */
	public void move(CartesianPoint relativeLocation) {
		location.combine(relativeLocation);
	}
	
	/**
	 * Tests whether a point (x, y) is contained within the swing 2D space occupied
	 * by this component.
	 * 
	 * @param int x
	 * @param int y
	 * @return boolean
	 */
	public boolean containsPoint(CartesianPoint point) {
		if (point.getX() >= getX() && point.getX() <= getX() + getWidth()) {
			if (point.getX() >= getY() && point.getY() <= getY() + getHeight()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns whether or not the renderer should render the object
	 * 
	 * @return boolean
	 */
	public boolean isDrawable() {
		return drawable;
	}

	/**
	 * Abstract method to be implemented by child classes that returns a deep copy.
	 * 
	 * @return AbstractComponent
	 */
	public abstract AbstractComponent copy();

	@Override
	public int compareTo(AbstractComponent ac) {
		return Integer.compare(this.depth, ac.depth);
	}

	/**
	 * Returns true if the element has a defined animation.
	 * 
	 * @return boolean
	 */
	public boolean isAnimated() {
		return animated;
	}

	/**
	 * Returns true if the element is visible
	 * @return boolean
	 */
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * Returns the name of the AbstractComponent
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name + " " + location.getX() + " " + location.getY() + " " + width + " " + height + " " + depth + " " + drawable + " " + animated;
	}


}
