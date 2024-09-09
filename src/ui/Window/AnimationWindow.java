package ui.Window;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import dataStructures.LocationRepresentations.CartesianPoint;
import sceneModel.AbstractComponent;
import sceneModel.Scene;
import ui.ControlPanel.ControlPanel;
import ui.ScreenPanel.ScreenPanel;

/**
 * Main Application Window. Handles all mouse and keyboard inputs/button inputs.
 * 
 * @author sammc
 *
 */
@SuppressWarnings("serial")
public class AnimationWindow extends JFrame implements Runnable {

	/* -----------------------------------------------------------------------------------------------------------------	 * 
	 * TODO:
	 * 
	 * 
	 * DEMO:
	 * 	 - draw the character in parts, and assemble as a complex component by hand
	 * 
	 * FUNCTION:
	 * 	 - ensure proper implementation of the screenshot feature / number of photos taken being saved properly
	 * 	 - ComplexComponent copy() method
	 * 
	 * UI:
	 *	 - menu options
	 * 	 - controls
	 *	 - flesh out ComponentsListPanel
	 *
	 * 
	 *
	 * -----------------------------------------------------------------------------------------------------------------
	 * Menu Options:
	 * -----------------------------------------------------------------------------------------------------------------
	 * 
	 * 		- add existing component file (to allow independent construction of complex components and importing later)
	 * 
	 * 		- add new component 
	 * 		- select an image using FileChooser. copy it into images directory and rename it appropriately
	 *  	- add a background 
	 * 		- select an image using FileChooser. copy it into images directory and rename it appropriately
	 *  
	 * 		- new scene
	 *  
	 *  	- open existing scene
	 *  
	 *  	- duplicate current scene (checkpoint save)
	 * 
	 * 	
	 * -----------------------------------------------------------------------------------------------------------------
	 * Controls:
	 * -----------------------------------------------------------------------------------------------------------------
	 *
	 * Flesh out SelectedControlPanel:
	 * 		- enable visible 
	 * 
	 * Create ComplexControlPanel in SceneControlPanel:
	 * 		- activates when a complexComponent is selected
	 * 
	 * Create AnimatedControlPanel in SceneControlPanel:
	 * 		- show frames in a list
	 *	 	- add frame (will turn a simple component into an animated one)
	 * 		- advance animation
	 *  	- previous animation
	 * -----------------------------------------------------------------------------------------------------------------
	 */

	public static final int SIZE = 700;

	private Scene scene;
	
	private ScreenPanel screenPanel;
	private ControlPanel controlPanel;
	

	public AnimationWindow(Scene scene) {
		this.scene = scene;
	}

	@Override
	public void run() {
		if (scene != null) {
			init();
		}
	}
	
	private void init() {
		// Set starting state for the JFrame
		setFocusable(true);
		setVisible(true);
		setSize(2 * SIZE, SIZE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Snappy");
		
		AnimationMenuBar menuBar = new AnimationMenuBar();
		setJMenuBar(menuBar);

		// Screen Panel
		screenPanel = new ScreenPanel();
		screenPanel.setScene(scene);
		add(screenPanel);

		// Footer panel that will contain controls
		controlPanel = new ControlPanel(screenPanel, scene);
		add(controlPanel, BorderLayout.SOUTH);

		screenPanel.setControlPanel(controlPanel);
		
		
	}

	
}
