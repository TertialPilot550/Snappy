package ui.ControlPanel;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import sceneModel.AbstractComponent;
import sceneModel.ComplexComponent;
import sceneModel.SimpleComponent;
import ui.ScreenPanel.ScreenPanel;

public class RotationScalePanel extends ControlModulePanel {
	
	private static final long serialVersionUID = -4447345668315007535L;
	
	private JSlider rotationSlider;
	private JSlider scaleSlider;
	
	
	public RotationScalePanel(ScreenPanel screenPanel) {
		super(screenPanel);
		
		setBackground(Color.lightGray);
		setFocusable(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel rotationLabel = new JLabel("Rotation:");
		add(rotationLabel);
		
		rotationSlider = new JSlider(-180, 180, 0);
		rotationSlider.setMajorTickSpacing(90);
		rotationSlider.setMinorTickSpacing(15);
		rotationSlider.setPaintTicks(true);
		rotationSlider.setPaintLabels(true);
		rotationSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				screenPanel.setRotationSelected(rotationSlider.getValue());
				screenPanel.repaint();
			}
		});
		add(rotationSlider);

		JLabel scaleLabel = new JLabel("Scale:");
		add(scaleLabel);

		scaleSlider = new JSlider(1, 5, 1);
		scaleSlider.setMajorTickSpacing(1);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		scaleSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				screenPanel.setScaleSelected(scaleSlider.getValue());
				screenPanel.repaint();
			}
		});
		add(scaleSlider);

	}

	
	@Override
	public void updateControlsForComponent(AbstractComponent comp) {
		// If comp is null... 
		if (comp == null) {
			// disable sliders.
			rotationSlider.setValue(0);
			scaleSlider.setValue(0);
			return;
		// If a component is a ComplexComponent...
		} else if (!comp.isDrawable()) {
			// set the sliders to the structures's values
			rotationSlider.setValue((int) ((ComplexComponent) comp).getStructureRotation());
			scaleSlider.setValue((int) ((ComplexComponent) comp).getStructureScale());
			return;
		}
		// Otherwise, set it to the value of the selected simple component
		rotationSlider.setValue((int) ((SimpleComponent) comp).getRotation());
		scaleSlider.setValue((int) ((SimpleComponent) comp).getScale());
	}

	
	
}
