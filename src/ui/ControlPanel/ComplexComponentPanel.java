package ui.ControlPanel;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import sceneModel.AbstractComponent;
import ui.ScreenPanel.ScreenPanel;

public class ComplexComponentPanel extends ControlModulePanel {

	// Generated Serial ID
	private static final long serialVersionUID = 5598188058385360225L;

	public ComplexComponentPanel(ScreenPanel screenPanel) {
		super(screenPanel);
		
		
	}

	@Override
	protected void updateControlsForComponent(AbstractComponent comp) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(300, 150);
	}
	

}
