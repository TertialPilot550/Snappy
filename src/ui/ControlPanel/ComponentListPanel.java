package ui.ControlPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import sceneModel.AbstractComponent;
import ui.ScreenPanel.ScreenPanel;

public class ComponentListPanel extends ControlModulePanel {

	private static final long serialVersionUID = 3726471167007099015L;
	private JList<AbstractComponent> componentsList;

	public ComponentListPanel(ScreenPanel screenPanel) {
		super(screenPanel);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setFocusable(false);
		
		JLabel componentListLabel = new JLabel("Components");
		add(componentListLabel);
		
		componentsList = new JList<AbstractComponent>() {
			
			// Generated Serial ID
			private static final long serialVersionUID = 6605961897251347165L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 100);
			}
		};
		componentsList.setFocusable(false);
		add(componentsList);
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setFocusable(false);
		add(buttonPanel);

		JButton addButton = new JButton("New");
		addButton.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO once I have an add new component popup menu
				
			}
		});
		addButton.setFocusable(false);
		buttonPanel.add(addButton);
		
		JButton duplicateButton = new JButton("Duplicate");
		duplicateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractComponent selectedFromJList = componentsList.getSelectedValue();
				AbstractComponent copyComponent = selectedFromJList.copy();
				getTargetScreen().addComponent(copyComponent);
			}
		
		});
		duplicateButton.setFocusable(false);
		buttonPanel.add(duplicateButton);

		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractComponent selectedFromJList = componentsList.getSelectedValue();
				getTargetScreen().removeComponent(selectedFromJList);
			}
			
		});
		removeButton.setFocusable(false);
		buttonPanel.add(removeButton);	
		
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(300, 150);
	}
	
	
	@Override
	public void update() {
		ArrayList<AbstractComponent> components = getTargetScreen().getComponentList();	
		updateComponentsJList(components);
	}
	
	public void updateComponentsJList(ArrayList<AbstractComponent> components) {
		DefaultListModel<AbstractComponent> compModel = new DefaultListModel<AbstractComponent>();
		for (AbstractComponent ac : components) {
			compModel.addElement(ac);
		}

		componentsList.setModel(compModel);
		repaint();
	}

	@Override
	protected void updateControlsForComponent(AbstractComponent comp) {
		return; // unused, becasue update() is overriden.
	}

	
}
