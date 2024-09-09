package fileHandling;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Utility class that contains some methods for loading and storing both images
 * and AbstractComponents from files.
 * 
 * @author sammc
 * 
 */
public class FileUtil {

	public final static String RES_PATH = "./res";
	
	public final static int FILETYPE_IMAGE = 0;
	public final static int FILETYPE_COMPONENT = 1;
	public final static int FILETYPE_SCENE = 2;

	/**
	 * Helper method for the component methods. Returns the contents of the file
	 * with the specified filename as one string.
	 * 
	 * @param String filename
	 * @return String
	 */
	static String retrieveFileData(String filename) {
		String fileData = "";
		try {
			FileInputStream inStream = new FileInputStream(filename);
			Scanner scan = new Scanner(inStream);
			while (scan.hasNext()) {
				fileData = fileData + " " + scan.next();
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileData;
	}

	
	
	public static String getSceneDirectoryPath() {
		return RES_PATH + "/scenes";
	}
	

	public static String getComponentDirectoryPath() {
		return RES_PATH + "/components";
	}


	public static String getImageDirectoryPath() {
		return RES_PATH + "/images";
	}


	
	// FOR ANIMATED COMPONENT ONLY, A LITTLE MESSY
	/**
	 * Takes an image of a given name, not including the extension, and moves into 
	 * into a directory in the images folder with the name imageName.
	 * @param componentName
	 */
	public static void moveSimpleImageToAnimatedDirectory(String componentName) {
		String imagePath = getImageDirectoryPath() + "/" + componentName + ".png";
		String dirPath = getImageDirectoryPath() + "/" + componentName;
		String newImagePath = getImageDirectoryPath() + "/" + componentName + "/0.png";
		
		File targetDirectory = new File(dirPath);
		targetDirectory.mkdir();
		
		
		File targetImageFile = new File(imagePath);
		BufferedImage  targetFile = FileLoadingUtil.loadImage(newImagePath);
		FileSavingUtil.saveImage(targetFile, newImagePath);
		targetImageFile.delete();
		
	}


}
