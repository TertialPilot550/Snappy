package ui.ControlPanel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import sceneModel.Scene;
import ui.ScreenPanel.ScreenPanel;
import ui.Window.AnimationWindow;

/**
 * A control panel which should be placed on a seperate thread. Contains an
 * array of subcomponent Panels, all of which control a particular aspect of
 * the scene currently being shown
 * @author sammc
 *
 */
public class ControlPanel extends JPanel implements Runnable {

	RotationScalePanel rotationScalePanel;
	SizeDepthPanel sizeDepthPanel;
	ComponentListPanel componentListPanel;
	ComplexComponentPanel complexComponentPanel;
	AnimatedComponentPanel animatedComponentPanel;
	
	// Generated Serial ID
	private static final long serialVersionUID = 1757196206270840296L;

	public ControlPanel(ScreenPanel screenPanel, Scene scene) {
		if (screenPanel == null) {
			throw new IllegalArgumentException();
		}
		
		setFocusable(false);
		setSize(AnimationWindow.SIZE, 200);
		setBackground(Color.lightGray);

		JPanel containerPanel = new JPanel() {
			// Generated Serial ID
			private static final long serialVersionUID = 2397981491748879840L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 150);
			}
		};
		rotationScalePanel = new RotationScalePanel(screenPanel);
		add(rotationScalePanel);
		
		sizeDepthPanel = new SizeDepthPanel(screenPanel);
		add(sizeDepthPanel);
		
		componentListPanel = new ComponentListPanel(screenPanel);
		componentListPanel.updateComponentsJList(scene.getComponents());
		add(componentListPanel);
		
		complexComponentPanel = new ComplexComponentPanel(screenPanel);
		add(complexComponentPanel);
		
		animatedComponentPanel = new AnimatedComponentPanel(screenPanel);
		add(animatedComponentPanel);
		
		
		//run();
	}
	
	
	
	public void update() {
		rotationScalePanel.update();
		sizeDepthPanel.update();
		componentListPanel.update();
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
	}
}
