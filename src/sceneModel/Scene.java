package sceneModel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fileHandling.FileSavingUtil;

/**
 * A class that stores a scene, which includes a set of backgrounds that can be
 * toggled through using the scene object, and a list of components with
 * individually defined positions, size, and properties.
 * 
 * @author sammc
 */
public class Scene {

	private String name; // Stores the name of the scene.
	private ArrayList<BufferedImage> backgrounds; // Stores all backgrounds contained in the scene.
	private ArrayList<AbstractComponent> components; // Stores all components contained in the scene.

	private int numSaves;
	private int currentBackground; // Stores the index for the currently selected background.

	/**
	 * Creates a new Scene with the specified name.
	 * 
	 * @param String name
	 */
	public Scene(String name, int numSaves) {
		this.name = name;
		backgrounds = new ArrayList<BufferedImage>();
		components = new ArrayList<AbstractComponent>();
		currentBackground = 0;
		this.numSaves = numSaves;
	}
	
	public Scene(String name) {
		this(name, 0);
	}

	/**
	 * Copy constructor for a scene.
	 * 
	 * @param Scene copyScene
	 */
	public Scene(Scene copyScene) {
		// copy name
		this(copyScene.name);

		// copy components
		for (int i = 0; i < copyScene.components.size(); i++) {
			AbstractComponent componentCopy = copyScene.getComponent(i).copy();
			this.components.add(componentCopy);
		}

		// copy backgrounds
		for (int i = 0; i < copyScene.backgrounds.size(); i++) {
			BufferedImage backgroundCopy = copyScene.copyBackground(i);
			this.backgrounds.add(backgroundCopy);
		}

		// copies the currentBackground index
		this.currentBackground = copyScene.currentBackground;

	}

	/**
	 * Returns a String with the name of the scene.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the component ArrayList. 
	 * 
	 * @return ArrayList<AbstractComponent>
	 */
	public ArrayList<AbstractComponent> getComponents() {
		return components;
	}

	/**
	 * Adds the AbstractComponent c to the components list.
	 * 
	 * @param AbstractComponent c
	 */
	public void addComponent(AbstractComponent c) {
		components.add(c);
	}

	/**
	 * Removes the component c from the components list.
	 * 
	 * @param AbstractComponent c
	 */
	public void removeComponent(AbstractComponent c) {
		components.remove(c);
	}

	/**
	 * Returns the component stored at index index of the ArrayList.
	 * 
	 * @param int index
	 * @return AbstractComponent
	 */
	public AbstractComponent getComponent(int index) {
		return components.get(index);
	}

	/**
	 * Returns how many components are held in the scene, not including backgrounds.
	 * 
	 * @return int
	 */
	public int getNumComponents() {
		return components.size();
	}

	/**
	 * Uses insertion sort to sort all components by depth, which is their natural
	 * ordering.
	 */
	public void sortComponents() {
		for (int i = 1; i < components.size(); i++) {
			AbstractComponent temp = components.get(i);
			int j = i - 1;
			while (j > 0 && temp.compareTo(components.get(j)) > 0) {
				components.set(j + 1, components.get(j));
				j--;
			}
			components.set(j + 1, temp);
		}
	}

	/**
	 * Adds an image to the list of backgrounds for this scene.
	 * 
	 * @param BufferedImage image
	 */
	public void addBackground(BufferedImage image) {
		backgrounds.add(image);
	}

	/**
	 * Gets the background at the specified index. For use in child classes/copying.
	 * 
	 * @param int index
	 * @return BufferedImage
	 */
	protected BufferedImage getBackground(int index) {
		return backgrounds.get(index);
	}

	/**
	 * Returns a BufferedImage object that contains the current selected background
	 * for the scene.
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getCurrentBackground() {
		if (backgrounds.size() == 0) {
			return null;
		}
		// return current background
		return backgrounds.get(currentBackground);
	}

	/**
	 * - BORROWED CODE - Draws the background image at index index onto a new BufferedImage,
	 * and returns the new one. Usable by child classes.
	 * 
	 * @param int index
	 * @return BufferedImage
	 */
	protected BufferedImage copyBackground(int index) {
		BufferedImage source = backgrounds.get(index);
		BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = result.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return result;
	}

	/**
	 * Advances to the next available background for the scene. If there are no more
	 * available items, set the background to the last background in the collection.
	 */
	public void nextBackground() {
		currentBackground++;
		if (currentBackground >= backgrounds.size())
			currentBackground = backgrounds.size() - 1;
	}

	/**
	 * Returns to the last available background for the scene. If there are no more
	 * available items, set the background to the first background in the
	 * collection.
	 */
	public void previousBackground() {
		currentBackground--;
		if (currentBackground < 0)
			currentBackground = 0;
	}
	
	/**
	 * Returns the currentBackgroundIndex
	 * @return int
	 */
	public int getBackgroundIndex() {
		return currentBackground;
	}
	
	/**
	 * Sets the currentBackground index to the parameter ind.
	 * If the index is outside the legal bounds for the backgrounds
	 * ArrayList, this method will have no effect.
	 * @param int ind
	 */
	public void setBackgroundIndex(int ind) {
		if (ind >= 0 && ind < backgrounds.size()) {
			currentBackground = ind;
		}
	}

	/**
	 * Returns an ArrayList of BufferedImages containing the backgrounds 
	 * for the current scene.
	 * @return ArrayList<BufferedImage>
	 */
	public ArrayList<BufferedImage> getBackgrounds() {
		return backgrounds;
	}

	/**
	 * Saves the state of the scene and all images to files
	 */
	public void save() {
		FileSavingUtil.saveScene(this);
		numSaves++;
	}

	/**
	 * Sets the components ArrayList to the passed object. DOES NOT create a copy.
	 * @param ArrayList<AbstractComponents> components
	 */
	public void setComponents(ArrayList<AbstractComponent> components) {
		this.components = components;
	}

	/**
	 * Sets the backgrounds ArrayList to the passed object. DOES NOT create a copy.
	 * @param ArrayList<BufferedImages> backgrounds
	 */
	public void setBackgrounds(ArrayList<BufferedImage> backgrounds) {
		this.backgrounds = backgrounds;
	}
	

	/**
	 * Returns true if the passed AbstractComponent is a free-standing simple component.
	 * @param comp
	 * @return
	 */
	public boolean isSimple(AbstractComponent comp) {
		if (comp.isDrawable() && inSceneList(comp)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the passed AbstractComponent is a free-standing ComplexComponent.
	 * @param comp
	 * @return
	 */
	public boolean isComplex(AbstractComponent comp) {
		if (!comp.isDrawable() && inSceneList(comp)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the passed AbstractComponent is a subcomponent of a ComplexComponent.
	 * @param AbstractComponent comp
	 * @return boolean
	 */
	public boolean isSubcomponent(AbstractComponent comp) {
		if (!inSceneList(comp)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Given a component comp, that is assumerd to be a subcomponent, returns the
	 * parent component that houses the subcomponent.
	 * @param AbstractComponent comp
	 * @return ComplexComponent
	 */
	public ComplexComponent getParentComponent(AbstractComponent comp) {
		// For each component in the scene list...
		for (AbstractComponent component : components) {
			// if it is a ComplexComponent...
			if (!component.isDrawable()) {
				// and it contains comp, return the component.
				if (((ComplexComponent) component).contains(comp)) {
					return (ComplexComponent) component;
				}
				

			}
		}
		// Otherwise return null
		return null;
	}
	
	/**
	 * Private method that checks whether the passed AbstractComponent
	 * is in the Scene's Component's list. 
	 * @param comp
	 * @return boolean
	 */
	public boolean inSceneList(AbstractComponent comp) {
		if (components.contains(comp)) {
			return true;
		}
		return false;
	}

	public int getNumSaves() {
		return numSaves;
	}
	

}
