package ui.ScreenPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import dataStructures.DirectedVectorGraph.Node;
import dataStructures.LocationRepresentations.CartesianPoint;
import sceneModel.AbstractComponent;
import sceneModel.AnimatedComponent;
import sceneModel.ComplexComponent;
import sceneModel.Scene;
import sceneModel.SimpleComponent;
import ui.ControlPanel.ControlPanel;

/**
 * A type of JPanel that can visually display a scene object using a renderer
 * object. A scene object contains multiple components, which represent sprites
 * or other visual objects, and a background set. A renderer requires a scene to
 * be assigned to use, it is the developers responsibility to implement this
 * correctly. The renderer's render() method recieves the ScreenPanel's graphics
 * context, and uses that to display the scene's data to the screen. This should
 * be the lowest level UI object directly used in the program.
 * 
 * @author sammc
 */
public class ScreenPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{

	// Serial ID. TBH not sure what this is for but IDE says I need it.
	private static final long serialVersionUID = 1L;

	private Renderer renderer; // handles painting the component's graphics context
	private Scene scene;

	private ControlPanel controlPanel;
	private AbstractComponent selectedComponent;
	private Node<AbstractComponent> selectedNode;
	private boolean doHighlightSelected;
	
	private CartesianPoint mouseOrgin;


	/**
	 * Constructs a new ScreenPanel with no set scene.
	 */
	public ScreenPanel() {
		renderer = new Renderer();
		setFocusable(true);
		// Input Listeners
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		mouseOrgin = new CartesianPoint(-1, -1);
		doHighlightSelected = true;
	}

	/**
	 * Advances all components in the scene to the next available animation state if
	 * and only if the component is an AnimatedComponent, and the autoToggle
	 * variable for the AnimatedComponent is set to true.
	 */
	public void advanceAnimations() {
		for (int i = 0; i < scene.getNumComponents(); i++) {
			AbstractComponent ac = scene.getComponent(i);
			if (ac.isAnimated() && ((AnimatedComponent) ac).autoToggle()) {
				((AnimatedComponent) ac).nextFrame();
			} else if (!ac.isDrawable()) {
				((ComplexComponent) ac).advanceAnimations();
			}
		}
	}
	
	private AbstractComponent getComponentIfContainsPoint(AbstractComponent component, CartesianPoint location) {
		if (component.isDrawable()) {
			if (component.containsPoint(location)) {
				return component;
			}
		} else {
			ComplexComponent cmplxComp = (ComplexComponent) component;

						
			if (cmplxComp.rootContainsPoint(location)) {
				selectedNode = null;
				return cmplxComp;
			}						
						
			for (Node<AbstractComponent> current : cmplxComp.getGraph()) {
				AbstractComponent data = current.data;

				if (data.isDrawable() && data.containsPoint(location)) {
					return data;
				}
			}
		}
		return null;
	}
	
	/**
	 * Gets all components contained in the scene
	 * @return ArrayList<AbstractComponent>
	 */
	public ArrayList<AbstractComponent> getComponentList() {
		return scene.getComponents();
	}
	
	/**
	 * Returns the element with the given location, or null if no element
	 * has such. Returns the element with the lowest depth.
	 * 
	 * @param location
	 * @return AbstractComponent
	 */
	public AbstractComponent getElementAt(CartesianPoint location) {
		scene.sortComponents();
		
		for (AbstractComponent component : scene.getComponents()) {
			
			AbstractComponent c = getComponentIfContainsPoint(component, location);
			if (c != null) {
				return c;
			}
			
		}
		return null;
	}

	
	public void addComponent(AbstractComponent component) {
		scene.addComponent(component);
		repaint();
	}
	
	/**
	 * Returns the selected component
	 * 
	 * @return AbstractComponent
	 */
	public AbstractComponent getSelected() {
		return selectedComponent;
	}

	/**
	 * Returns the selected subcomponent, or null if one does not exist.
	 * 
	 * @return Node<AbstractComponent>
	 */
	public Node<AbstractComponent> getSelectedNode() {
		return selectedNode;
	}

	/**
	 * Returns true if the panel has a selected element.
	 * 
	 * @return boolean
	 */
	public boolean hasSelectedComponent() {
		return selectedComponent != null;
	}

	/**
	 * Returns whether or not a specific node of a ComplexComponent has been selected.
	 * @return boolean
	 */
	public boolean hasSelectedNode() {
		if (selectedNode == null) {
			return false;
		}
		return true;
	}

	private void highlightComplexComponent(Graphics g, ComplexComponent component) {
		// Cursor
		int circleSize = ComplexComponent.ROOT_SIZE;
		Rectangle rootBoundingBox = component.getRootBoundingBox();
		g.drawRect(rootBoundingBox.x, rootBoundingBox.y - rootBoundingBox.height, rootBoundingBox.width, rootBoundingBox.height);
		g.drawLine(component.getX() - circleSize / 2, component.getY(), component.getX() + circleSize / 2, component.getY());
		g.drawLine(component.getX(), component.getY() - circleSize / 2, component.getX(), component.getY() + circleSize / 2);
		
		for (Node<AbstractComponent> node : component.getGraph()) {
			AbstractComponent nodeData = node.data;
			g.drawLine(nodeData.getX(), nodeData.getY(), component.getX(), component.getY());
		}
		

		/*
		 * Box around selected component if there is one
		 */
		if (selectedNode != null) {
			AbstractComponent selectedComponent = selectedNode.data;
			// if there is a selected node, draw a rectangle around it.
			if (selectedComponent != null) {
				g.drawRect(selectedComponent.getX(), selectedComponent.getY(), selectedComponent.getWidth(), selectedComponent.getHeight());
			}
		}
	}

	private void highlightSelectedComponent(Graphics g) {
		if (doHighlightSelected && selectedComponent != null) {
			g.setColor(Color.cyan);

			// A simple component is selected
			if (selectedComponent.isDrawable()) {
				highlightSimpleComponent(g, (SimpleComponent) selectedComponent);
				
			// A complex component is selected
			} else {
				highlightComplexComponent(g, (ComplexComponent) selectedComponent);
				
			}
		}
	}
	
	private void highlightSimpleComponent(Graphics g, SimpleComponent component) {
		g.drawRect(selectedComponent.getX(), selectedComponent.getY(), selectedComponent.getWidth(), selectedComponent.getHeight());
	}

	/**
	 * Sets the selcted item's rotation equal to the old value plus the value of the
	 * deltaRotation parameter TODO COMPLEX
	 * 
	 * @param double deltaRotation
	 */
	public void modifyRotationSelected(double deltaRotation) {
		if (selectedComponent != null && selectedComponent.isDrawable()) {
			setRotationSelected(((SimpleComponent) selectedComponent).getRotation() + deltaRotation);
		}
	}

	/**
	 * Moves the selected element according to the deltaX and deltaY parameters.
	 * 
	 * 
	 * @param int deltaX
	 * @param int deltaY
	 */
	public void moveSelected(CartesianPoint relativeLocation) {
		if (selectedComponent == null) {
			return;
		}
		if (scene.isSimple(selectedComponent)) { 
			selectedComponent.move(relativeLocation);
		} else if (scene.isComplex(selectedComponent) && !hasSelectedNode()) {
			((ComplexComponent) selectedComponent).move(relativeLocation);
		} else if (scene.isComplex(selectedComponent) && hasSelectedNode()){
			((ComplexComponent) selectedComponent).moveNode(selectedNode, relativeLocation);	
		}
	}

	public void moveSelected(int dx, int dy) {
		CartesianPoint location = new CartesianPoint(dx, dy);
		moveSelected(location);
	}

	/**
	 * Advances the scene to the next background.
	 */
	public void nextBackground() {
		scene.nextBackground();
	}
	
	/*
	 * Overriden paintComponent() method which passes the graphics context to the
	 * renderer, which then handles the painting operations.
	 */
	@Override
	public void paintComponent(Graphics g) {
		// use the renderer to render the Scene's BufferedImage to the contentPanel's
		// graphics, i.e. onto the screen
		renderer.render(g, getWidth(), getHeight());
		highlightSelectedComponent(g);
		
	}

	/**
	 * Returns the scene to the last background.
	 */
	public void previousBackground() {
		scene.previousBackground();
	}
	
	/**
	 * Uses the renderer to save the current screen contents to a file.
	 */
	public void saveContents() {
		renderer.saveContents();
		scene.save();
	}

	/**
	 * Sets the screen's selected component to the parameter comp.
	 *   this is the screen one
	 * @param AbstractComponent comp
	 */
	public void select(AbstractComponent toBeSelected) {
		// Update the selected component
		this.selectedComponent = toBeSelected;

		if (selectedComponent == null) {
			repaint();
			return;
		}
		
		if (scene.isComplex(selectedComponent)) {
			selectedNode = null;
		} else if (scene.isSubcomponent(toBeSelected)) {
			selectSubcomponent(toBeSelected);
		}
		repaint();
		controlPanel.update();
	}

	public boolean selectedComponentIsSimple() {
		if (selectedComponent == null) {
			return false;
		}
		return selectedComponent.isDrawable();
	}

	private void selectSubcomponent(AbstractComponent toBeSelected) {
		selectedComponent = scene.getParentComponent(toBeSelected);
		selectedNode = ((ComplexComponent) selectedComponent).getNodeContaining(toBeSelected);
	}

	/**
	 * Sets whether or not to highlight the selected component with the parameter
	 * 
	 * @param boolean highlightSelected
	 */
	public void setHighlightSelected(boolean doHighlight) {
		this.doHighlightSelected = doHighlight;
	}
	
	/**
	 * Sets the selected item's rotation equal to the rotation parameter. 
	 * 
	 * @param double rotation
	 */
	public void setRotationSelected(double rotation) {
		if (hasSelectedComponent()) {
			if (selectedComponentIsSimple()) {
				((SimpleComponent) selectedComponent).setRotation(rotation);
			} else if (!hasSelectedNode()) {
				((ComplexComponent) selectedComponent).setStructureRotation(rotation);
			} else if (hasSelectedNode()) {
				((ComplexComponent) selectedComponent).setNodeRotation(selectedNode, rotation);
			}
		}
	}

	/**
	 * Sets the selected item's scale equal to the scale parameter. 
	 * 
	 * @param double scale
	 */
	public void setScaleSelected(double scale) {
		if (hasSelectedComponent()) {
			if (selectedComponentIsSimple()) {
				((SimpleComponent) selectedComponent).setScale(scale);
			} else if (!hasSelectedNode()) {
				((ComplexComponent) selectedComponent).setStructureScale(scale);
			} else if (hasSelectedNode()) {
				((ComplexComponent) selectedComponent).setNodeScale(selectedNode, scale);
			}
		}
	}

	/**
	 * Sets the scene. This method must be called before render is called for proper
	 * implementation. This can be used to change the scene during execution.
	 * 
	 * @param Scene scene
	 */
	public void setScene(Scene scene) {
		renderer.setScene(scene);
		this.scene = scene;
	}

	public void setSizeSelected(int width, int height) {
		if (selectedComponent != null) selectedComponent.setSize(width, height);
	}

	public void setDepthSelected(int depth) {
		if (selectedComponent != null) selectedComponent.setDepth(depth);
	}

	public void removeComponent(AbstractComponent component) {
		scene.removeComponent(component);
		repaint();
	}

	public void setLocationSelected(CartesianPoint location) {
		selectedComponent.setLocation(location);
		repaint();
	}

	
	
	/////////////////// MOUSE DRAGGED ////////////////////
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!wasPreviousMouseDraggedCall()) {
			handleFirstMouseDraggedCall(e);
		} else {
			handleLaterMouseDraggedCalls(e);
		}
	}
	
	private boolean wasPreviousMouseDraggedCall() {
		if (mouseOrgin.getX() == -1 && mouseOrgin.getY() == -1) {
			return false;
		} else {
			return true;
		}
	}
	
	private void handleFirstMouseDraggedCall(MouseEvent e) {
		CartesianPoint mouseLocation = new CartesianPoint(e.getX(), e.getY());
		AbstractComponent ac = getElementAt(mouseLocation);
		if (ac == null) {
			select(null);
		} else {
			select(ac);
			updateMouseOrgin(e);	
		}
	}
	
	
	private void updateMouseOrgin(MouseEvent e) {
		mouseOrgin = new CartesianPoint(e.getX(), e.getY());
	}
	
	private void handleLaterMouseDraggedCalls(MouseEvent e) {
		CartesianPoint movementSinceLastMouseEvent = getMovementSinceLastMouseEvent(e);
		updateMouseOrgin(e);

		moveSelectedElement(movementSinceLastMouseEvent);
	}

	private CartesianPoint getMovementSinceLastMouseEvent(MouseEvent e) {
		int deltaX = e.getX() - mouseOrgin.getX();
		int deltaY = e.getY() - mouseOrgin.getY();
		return new CartesianPoint(deltaX, deltaY);
	}
	
	private void moveSelectedElement(CartesianPoint relativeLocation) {
		int dx = relativeLocation.getX();
		int dy = relativeLocation.getY();
		if (hasSelectedComponent()) {
			moveSelected(dx, dy);
		}
		repaint();
	}
	
//////////////////////////////////////////////////

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {

		case 'e': // Rotate Clockwise
			modifyRotationSelected(5);
			repaint();
			controlPanel.update();
			break;
		case 'q': // Rotate Counter-Clockwise
			modifyRotationSelected(-5);
			repaint();
			controlPanel.update();
			break;

		case 'a': // Move left
			moveSelected(-10, 0);
			repaint();
			break;
		case 's': // Move down
			moveSelected(0, 10);
			repaint();
			break;
		case 'd': // Move right
			moveSelected(10, 0);
			repaint();
			break;
		case 'w': // Move up
			moveSelected(0, -10);
			repaint();
			break;

		case '~': // Save screen content
			saveContents();
			break;
			
		case '\\': // Advance Animation
			advanceAnimations();
			break;

		case ',': // Previous Background
			previousBackground();
			repaint();
			break;

		case '.': // Next Background
			nextBackground();
			repaint();
			break;
		}

	}

	

	@Override
	public void mouseClicked(MouseEvent e) {
		// Select the component located at the mouse press location. 
		// Does not count as mouse activity for the dragged method operation.
		
		AbstractComponent ac = getElementAt(getMouseLocation(e));
		select(ac);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Select the component located at the mouse press location. 
		// Does not count as mouse activity for the dragged method operation.
		AbstractComponent ac = getElementAt(getMouseLocation(e));
		select(ac);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// end mouse activity
		mouseOrgin.setLocation(-1, -1);
		 // flag for no current mouse activity
	}

	private CartesianPoint getMouseLocation(MouseEvent e) {
		return new CartesianPoint(e.getX(), e.getY());
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// NOT USED
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// NOT USED
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// NOT USED
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		// NOT USED
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// NOT USED
	}

	public void setControlPanel(ControlPanel controlPanel) {
		this.controlPanel = controlPanel;
	}

	public void setVisibleSelected(boolean visible) {
		selectedComponent.setVisible(visible);
		repaint();
	}
	
}
