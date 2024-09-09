package sceneModel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ListIterator;

import dataStructures.LocationRepresentations.CartesianPoint;
import fileHandling.FileLoadingUtil;

public class AnimatedComponent extends SimpleComponent {

	private static final boolean ANIMATED = true; // Animation is defined for this component

	private ArrayList<BufferedImage> frames; // Stores all the animation frames
	private ListIterator<BufferedImage> iter; // Stores the iterator to track the current frame

	private boolean autoToggle; // on by default

	/**
	 * Constructor for an animated component that can cycle through multiple states
	 * 
	 * @param String name
	 * @param int    xPos
	 * @param int    yPos
	 * @param int    width
	 * @param int    height
	 * @param int    rotation
	 * @param int    depth
	 */
	public AnimatedComponent(String name, CartesianPoint location, int width, int height, double rotation, int depth) {
		super(name, location, width, height, rotation, depth, 1, ANIMATED, true); // REPLACE 1 with scale TODO

		frames = new ArrayList<BufferedImage>();
		iter = frames.listIterator();
		autoToggle = true;
	}

	/**
	 * Overloaded constructor that allows you to set the autoToggle value for the
	 * component.
	 * 
	 * @param String  name
	 * @param int     xPos
	 * @param int     yPos
	 * @param int     width
	 * @param int     height
	 * @param int     rotation
	 * @param int     depth
	 * @param boolean autoToggle
	 */
	public AnimatedComponent(String name, CartesianPoint location, int width, int height, double rotation, int depth,
			boolean autoToggle) {
		this(name, location, width, height, rotation, depth);
		this.autoToggle = autoToggle;
	}

	/**
	 * Adds the BufferedImage newFrame parameter as a new frame to the end of the
	 * animation cycle.
	 * 
	 * @param BufferedImage newFrame
	 */
	public void addFrame(BufferedImage newFrame) {
		frames.add(newFrame);
	}

	/**
	 * Adds multiple images, one for each child file of the directory stored at the
	 * the parameter dirPath.
	 * 
	 * @param String dirPath
	 */
	public void addFrames(String dirPath) {
		frames = FileLoadingUtil.loadImages(dirPath);
	}

	/**
	 * Returns whether autoToggle is enabled for the component's animation cycle.
	 * 
	 * @return boolean
	 */
	public boolean autoToggle() {
		return autoToggle;
	}

	@Override
	public AnimatedComponent copy() {
		AnimatedComponent copyComponent = new AnimatedComponent(getName(), getLocation(), getWidth(), getHeight(), getRotation(), getDepth());
		
		for (BufferedImage frame : frames) {
			copyComponent.addFrame(frame);
		}
		
		copyComponent.autoToggle = this.autoToggle;
		copyComponent.iter = frames.listIterator();
		
		return copyComponent;
		
	}

	/**
	 * Returns the BufferedImage containing the frame data for the currently
	 * selected frame.
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getCurrentFrame() {
		int ind = iter.nextIndex();
		return frames.get(ind);
	}

	/**
	 * Navigates the iterator the previous frame. If there is no previous frame,
	 * loop around to the end of the list.
	 */
	public void lastFrame() {
		if (iter.hasPrevious())
			iter.previous();
		else
			iter = frames.listIterator(frames.size() - 1);
	}

	/**
	 * Navigates the iterator to the next frame. If there is no previous frame, loop
	 * around to the end of the list.
	 */
	public void nextFrame() {
		if (iter.hasNext())
			iter.next();
		else
			iter = frames.listIterator();

	}
	
	/**
	 * Sets the autoToggle variable to the setting parameter
	 * 
	 * @param boolean setting
	 */
	public void setAutoToggle(boolean setting) {
		autoToggle = setting;
	}

}
