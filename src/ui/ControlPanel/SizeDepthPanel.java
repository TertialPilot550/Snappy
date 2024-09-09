package ui.ControlPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dataStructures.LocationRepresentations.CartesianPoint;
import sceneModel.AbstractComponent;
import ui.ScreenPanel.ScreenPanel;

public class SizeDepthPanel extends ControlModulePanel {

	// Generated Serial ID
	private static final long serialVersionUID = 806571780508910046L;
		
	private JSpinner widthSpinner;
	private JSpinner heightSpinner;
	private JSpinner depthSpinner;

	
	public SizeDepthPanel(ScreenPanel screenPanel) {
		super(screenPanel);
	
		setBackground(Color.lightGray);
		setFocusable(false);
		setVisible(true);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		
		
		JLabel widthLabel = new JLabel("Width: ");
		add(widthLabel);
		
		widthSpinner = new JSpinner();
		widthSpinner.setFocusable(false);
		widthSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateSelectedSize();
			}
		});
		add(widthSpinner);
		
		
		
		JLabel heightLabel = new JLabel("Height: ");
		add(heightLabel);
		
		heightSpinner = new JSpinner();
		heightSpinner.setFocusable(false);
		heightSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				updateSelectedSize();
			}
			
		});
		add(heightSpinner);
		
		
		
		JLabel depthLabel = new JLabel("Depth: ");
		add(depthLabel);

		depthSpinner = new JSpinner();
		depthSpinner.setFocusable(false);
		depthSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				getTargetScreen().setDepthSelected((int) depthSpinner.getValue());
			}
		
		});
		
		
		add(depthSpinner);
		
		JButton centerSelectedButton = new JButton("Center");
		centerSelectedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ScreenPanel screen = getTargetScreen();
				int x = screen.getWidth() / 2;
				int y = screen.getHeight() / 2;
				
				AbstractComponent selected = screen.getSelected();
				if (selected != null) {
					x -= selected.getWidth() / 2;
					y -= selected.getHeight() / 2;
				}
				
				CartesianPoint newPosition = new CartesianPoint(x, y);
				
				getTargetScreen().setLocationSelected(newPosition);
			}
				
		});
		centerSelectedButton.setFocusable(false);
		add(centerSelectedButton);
		
		JCheckBox visibleBox = new JCheckBox("Visible");
		visibleBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				getTargetScreen().setVisibleSelected(visibleBox.isEnabled());
				
			}
		});
		visibleBox.setFocusable(false);
		add(visibleBox);
		
	}
	
	private void updateSelectedSize() {
		int width = (int) widthSpinner.getValue();
		int height = (int) heightSpinner.getValue();
		AbstractComponent selected = getTargetScreen().getSelected();
		if (width == 0 && selected != null) {
			width = selected.getWidth();
		}
		if (height == 0 && selected != null) {
			height = selected.getHeight();
		}
		getTargetScreen().setSizeSelected(width, height);
		getTargetScreen().repaint();
	}

	@Override
	protected void updateControlsForComponent(AbstractComponent comp) {
		if (comp == null) {
			widthSpinner.setValue(0);
			heightSpinner.setValue(0);
			depthSpinner.setValue(0);
			return;
		}
		
		widthSpinner.setValue(comp.getWidth());
		heightSpinner.setValue(comp.getHeight());
		depthSpinner.setValue(comp.getDepth());
	}


	
}
