package ui.ScreenPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import dataStructures.DirectedVectorGraph.Node;
import fileHandling.FileSavingUtil;
import sceneModel.AbstractComponent;
import sceneModel.ComplexComponent;
import sceneModel.Scene;
import sceneModel.SimpleComponent;

/**
 * Renderer class. Once a scene is set for the render to use, using setScene(),
 * can refresh the screen based on any changes in scene data, or save the screen
 * data to an image file. To be used as an object by a surrounding JPanel to
 * support abstraction.
 * 
 * @author sammc
 */
class Renderer {

	private final int imageWidth = 1800; // stores the intended width of the panel being rendered to
	private final int imageHeight = 1200; // stores the intended height of the panel being rendered to
	private final int imageType = BufferedImage.TYPE_INT_RGB; // the image type used for the buffered image

	private int currentNumImages; // stores the total count of images creates with the save
									// function to implement increasing indexed names

	private Scene scene; // stores the scene being used for the renderer
	private BufferedImage screenContent; // stores the data to be either saved or written to screen

	/**
	 * Creates a new renderer with no assigned scene.
	 */
	public Renderer() {
		screenContent = new BufferedImage(imageWidth, imageHeight, imageType);
	}

	/**
	 * Sets the renderer's scene to the scene parameter's value. The render() method
	 * cannot be called before this is called at least once.
	 * 
	 * @param Scene scene
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
		currentNumImages = scene.getNumSaves();
	}

	/**
	 * Returns the scene that is currently being used to generate screen content.
	 * 
	 * @return Scene
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * Render the currently assigned scene. Uses the surrounding panel's graphics
	 * context. Creates the image based on the state of the scene and all it's
	 * components.
	 * 
	 * @param Graphics g - the surrounding panel's graphics context.
	 * @param int      width
	 * @param int      height
	 */
	public void render(Graphics g, int width, int height) {
		// create a new screen buffer to clear the screen
		screenContent = new BufferedImage(imageWidth, imageHeight, imageType);
		Graphics2D screenGraphics = screenContent.createGraphics();
		renderBackgroundsToBuffer(screenGraphics, width, height);
		renderComponentsToBuffer(screenGraphics);
		g.drawImage(screenContent, 0, 0, null);

	}
	
	private void renderBackgroundsToBuffer(Graphics2D screenGraphics, int width, int height) {
		screenGraphics.drawImage(scene.getCurrentBackground(), 0, 0, width, height, null);
	}
	
	private void renderComponentsToBuffer(Graphics2D screenGraphics) {
		scene.sortComponents();
		for (int i = 0; i < scene.getNumComponents(); i++) { // iterate through each component
			renderComponent(screenGraphics, scene.getComponent(i)); // render each component
		}
	}

	/**
	 * Private helper method that renders one component to the screen buffer.
	 * 
	 * @param Graphics2D        g
	 * @param AbstractComponent component
	 */
	private void renderComponent(Graphics2D screenGraphics, AbstractComponent component) {
		if (component.isDrawable()) { // if drawable, add it's image at it's location to the screen
			drawComponent(screenGraphics, (SimpleComponent) component); // draw the component
		} else {
			renderComplexComponent(screenGraphics, (ComplexComponent) component);
		}
	}

	/**
	 * Renders a ComplexComponent to the provided Graphics2D object. 
	 * 
	 * @param Graphics2D       g
	 * @param ComplexComponent component
	 */
	private void renderComplexComponent(Graphics2D screenGraphics, ComplexComponent component) {		
		for (Node<AbstractComponent> node : component.getGraph()) {
			AbstractComponent nodeData = node.data;
			if (nodeData.isDrawable()) {
				drawComponent(screenGraphics, node.data);
			}
		}
	}



	/**
	 * Helper method for render(). Renders an individual component onto the passed
	 * graphics context. Accounts for the components rotation and scale variables.
	 * 
	 * CONTAINS BORROWED CODE. Not perfectly functional, but adequetly functional
	 * for the time being.
	 * 
	 * @param Graphics2D      g
	 * @param SimpleComponent comp
	 */
	private void drawComponent(Graphics2D g, AbstractComponent comp) {
		
		if (!comp.isDrawable() || !((SimpleComponent) comp).isVisible()) {
			return;
		}
		
		SimpleComponent component = (SimpleComponent) comp;
		
		double angle = component.getRotation();
		
		if (scene.isSubcomponent(component)) {
			ComplexComponent parentStructure = scene.getParentComponent(component);
			angle += parentStructure.getStructureRotation();
		}
		
		BufferedImage bimg = component.getImage();

		if (bimg == null) {
			return;
		}
		
		// borrowed code. rotates the image
		double sin = Math.abs(Math.sin(Math.toRadians(angle)));
		double cos = Math.abs(Math.cos(Math.toRadians(angle)));

		int w = bimg.getWidth();
		int h = bimg.getHeight();

		int neww = (int) Math.floor(w * cos + h * sin);
		int newh = (int) Math.floor(h * cos + w * sin);

		// calulate new size adjusted for rotation
		int compW = component.getWidth();
		int compH = component.getHeight();
		int newCompW = (int) Math.floor(compW * cos + compH * sin);
		int newCompH = (int) Math.floor(compH * cos + compW * sin);

		BufferedImage rotated = new BufferedImage(neww, newh, bimg.getType());
		Graphics2D graphic = rotated.createGraphics();
		graphic.translate((neww - w) / 2, (newh - h) / 2); // translates it half the difference of the two widths
		graphic.rotate(Math.toRadians(angle), w / 2, h / 2);
		graphic.drawRenderedImage(bimg, null);
		graphic.dispose();

		// draw the image with the adjusted width and height
		g.drawImage(rotated, component.getX(), component.getY(), newCompW, newCompH, null);
	}
	
	/**
	 * Saves the contents of the current screenContent BufferedImage, to a new jpg
	 * in the /ProjectFiles/Frames/ directory, and increments the currentNumImages
	 * variable to create unique names for the screen captures.
	 */
	public void saveContents() {
		FileSavingUtil.saveImage(screenContent, "./res/frames/" + scene.getName() + currentNumImages++ + ".png");
		
	}
}
