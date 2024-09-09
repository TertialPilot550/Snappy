package ui.ControlPanel;

import javax.swing.JPanel;

import sceneModel.AbstractComponent;
import ui.ScreenPanel.ScreenPanel;

public abstract class ControlModulePanel extends JPanel {
	
	// Generated Serial ID
	private static final long serialVersionUID = -9088920706773517169L;
	
	private ScreenPanel screenPanel;

	public ControlModulePanel(ScreenPanel screenPanel) {
		this.screenPanel = screenPanel;
	}
	
	public void update() {
		updateControlsForComponent(screenPanel.getSelected());
	}
	
	public ScreenPanel getTargetScreen() {
		return screenPanel;
	}

	protected abstract void updateControlsForComponent(AbstractComponent comp);

}
