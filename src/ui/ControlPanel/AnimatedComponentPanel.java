package ui.ControlPanel;

import java.awt.Dimension;

import sceneModel.AbstractComponent;
import ui.ScreenPanel.ScreenPanel;

public class AnimatedComponentPanel extends ControlModulePanel {

	// Generated Serial ID
	private static final long serialVersionUID = -1392681161905075857L;
	
	

	
	
	

	public AnimatedComponentPanel(ScreenPanel screenPanel) {
		super(screenPanel);
	}

	@Override
	protected void updateControlsForComponent(AbstractComponent comp) {

	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(300, 150);
	}
	

}
