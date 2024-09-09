package fileHandling;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * Wrapper for JFileChooser chooser, becuase I'm not super comfortable with it yet.
 * @author sammc
 */
public class FileChooser {

	/*
	 * 
	 * Types of files I may choose:
	 * 
	 * 	Scenes
	 * 	Components
	 * 	Images for component
	 * 
	 */
	
	private static JFileChooser chooser;
	
	
	public static File chooseFile(int desiredFiletype) {
		initializeChooser(desiredFiletype);
		return getChosenFile();
		
	}
	
	private static void initializeChooser(int desiredFiletype) {
		chooser = new JFileChooser();
		
		String directoryPath = FileUtil.RES_PATH;
		String desiredFiletypeExtension = "";
		
		if (desiredFiletype == FileUtil.FILETYPE_IMAGE) {
			desiredFiletypeExtension = "png";
			directoryPath = directoryPath + "/images";
		} else if (desiredFiletype == FileUtil.FILETYPE_COMPONENT) {
			desiredFiletypeExtension = "cmp";
			directoryPath = directoryPath + "/components";
		} else if (desiredFiletype == FileUtil.FILETYPE_SCENE) {
			desiredFiletypeExtension = "scene";
			directoryPath = directoryPath + "/scenes";
		} 
		
		File desiredSubdirectory = new File (directoryPath);
		chooser.setCurrentDirectory(desiredSubdirectory);

		
		FileNameExtensionFilter fileFilter = new FileNameExtensionFilter(desiredFiletypeExtension.toUpperCase() + " Filter", desiredFiletypeExtension);
		chooser.setFileFilter(fileFilter);
	}
	
	private static File getChosenFile() {
		final JComponent parentComponent = null;
		int operationResult = chooser.showOpenDialog(parentComponent); 
		
		if (operationResult == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}


	
	
	

}
