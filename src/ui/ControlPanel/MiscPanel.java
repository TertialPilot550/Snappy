package ui.ControlPanel;

import javax.swing.JCheckBox;

import sceneModel.AbstractComponent;
import ui.ScreenPanel.ScreenPanel;

public class MiscPanel extends ControlModulePanel {

//	 * 		TODO
//	 * 		- enable visible
//	 * 		- add visible to AbstractComponent so that complexComponents have it, and account for this in the renderer
//	 * 		- change loading and saving to accompany this change
//	 *
//	 *		- duplicate
	
	// Generated Serial ID
	private static final long serialVersionUID = 4990713076802739161L;

	private JCheckBox isVisibleCheckBox;
	
	public MiscPanel(ScreenPanel screenPanel) {
		super(screenPanel);
		
		isVisibleCheckBox = new JCheckBox("Visible"); 
	}

	@Override
	protected void updateControlsForComponent(AbstractComponent comp) {
		if (comp.isVisible()) {
			isVisibleCheckBox.setSelected(true);
		}
	}

}
