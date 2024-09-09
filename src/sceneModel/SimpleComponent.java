package sceneModel;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import dataStructures.LocationRepresentations.CartesianPoint;
import fileHandling.FileUtil;

public class SimpleComponent extends AbstractComponent {

	private static final boolean DRAWABLE = true; // visual components are drawable
	protected static final int DEFAULT_ROTATION = 0; // default rotation value
	protected static final int DEFAULT_SCALE = 1; // default scalar value

	private BufferedImage image; // stores the image content associated with the component
	private double rotation; // stores the rotation value for the visual component
	private double scale; // stores the scalar value for the visual component

	/**
	 * Simple Constructor with no rotation, scaling, or depth parameters.
	 * 
	 * @param String name
	 * @param int    xPos
	 * @param int    yPos
	 * @param int    width
	 * @param int    height
	 */
	public SimpleComponent(String name, CartesianPoint location, int width, int height) {
		this(name, location, width, height, DEFAULT_ROTATION, DEFAULT_SCALE, DEFAULT_DEPTH, false, true);
	}
	
	/**
	 * Overloaded Constructor for ez calling. Non-animated component.
	 * 
	 * @param String name
	 * @param int    xPos
	 * @param int    yPos
	 * @param int    width
	 * @param int    height
	 * @param double rotation
	 * @param double scale
	 * @param int    depth
	 */
	public SimpleComponent(String name, CartesianPoint location, int width, int height, double rotation, double scale,
			int depth) {
		this(name, location, width, height, rotation, scale, depth, false, true);
	}

	/**
	 * Constructor that creates a new visual component as speciefied by the
	 * parameters
	 * 
	 * @param String  name
	 * @param int     xPos
	 * @param int     yPos
	 * @param int     width
	 * @param int     height
	 * @param double  rotation
	 * @param double  scale
	 * @param int     depth
	 * @param boolean animated
	 */
	public SimpleComponent(String name, CartesianPoint location, int width, int height, double rotation, double scale,
			int depth, boolean animated, boolean visible) {
		super(name, location, width, height, depth, DRAWABLE, animated, visible);
		this.rotation = rotation;
		this.scale = scale;
		image = null;
		
	}

	/**
	 * Oversimplified Constructor. Built for ComplexComponent data components where location is defined based on relationships.
	 */
	public SimpleComponent(String name, int width, int height) {
		this(name, new CartesianPoint(0, 0), width, height);
	}

	public AnimatedComponent convertToAnimated() {
		AnimatedComponent animatedCopy =  new AnimatedComponent(getName(), getLocation(), getWidth(), getHeight(), getRotation(), getDepth());
		animatedCopy.addFrame(deepCopyImage());
		
		// create a directory, and move the current image file into the directory
		FileUtil.moveSimpleImageToAnimatedDirectory(getName());
		
		return animatedCopy;
	}

	@Override
	public AbstractComponent copy() {
		SimpleComponent newComp = new SimpleComponent(getName(), getLocation(), getWidth(), getHeight(), getRotation(), getScale(), getDepth());
		
		BufferedImage copyImage = deepCopyImage();
		newComp.setImage(copyImage);
		
		return newComp;
	}

	

	public BufferedImage deepCopyImage() {
		return deepCopy(this.getImage());
	}

	// Borrowed Code
	private BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	@Override
	public int getHeight() {
		return (int) (super.getHeight() * scale);
	}
	
	
	/**
	 * Returns the BufferedImage used by the sprite to be displayed
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getImage() {
		return image;
	}
	
	/**
	 * Returns the current rotation value for the component's image.
	 * 
	 * @return double
	 */
	public double getRotation() {
		return rotation;
	}

	

	/**
	 * Returns the scale value for the components image.
	 * 
	 * @return double
	 */
	public double getScale() {
		return scale;
	}

	@Override
	public int getWidth() {
		return (int) (super.getWidth() * scale);
	}

	/**
	 * Sets the component's image to the parameter BufferedImage.
	 * 
	 * @param BufferedImage image
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * Sets the rotation value for the components image equal to the parameter
	 * rotation.
	 * 
	 * @param double rotation
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * Sets ths scale value for the components image equal to the scale parameter.
	 * 
	 * @param double scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	
}
