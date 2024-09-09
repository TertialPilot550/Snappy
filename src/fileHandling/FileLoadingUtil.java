package fileHandling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import javax.imageio.ImageIO;

import dataStructures.DirectedVectorGraph.Node;
import dataStructures.LocationRepresentations.CartesianPoint;
import dataStructures.LocationRepresentations.Vector;

import sceneModel.AbstractComponent;
import sceneModel.AnimatedComponent;
import sceneModel.ComplexComponent;
import sceneModel.Scene;
import sceneModel.SimpleComponent;

public class FileLoadingUtil {

	/**
	 * Wraps the loadComponent() method to allow passing a file object
	 * @param componentFile
	 * @return
	 */
	public static AbstractComponent loadComponent(File componentFile) {
		return loadComponent(componentFile.getAbsolutePath());
	}

	/**
	 * Loads a component from a file into a corresponding AbstractComponent
	 * descendant, based on the component's properties.
	 * 
	 * @param String filename
	 * @return AbstractComponent
	 */
	public static AbstractComponent loadComponent(String filename) {
		// Get the file data
		String fileData = FileUtil.retrieveFileData(filename);
		// Check for null file data
		if (fileData.equals("")) {
			return null;
		}

		// Parse the file data
		Scanner scan = new Scanner(fileData);

		// Advances the scanner to the open brace
		String current = scan.next();
		while (!current.contains("{")) {
			current = scan.next();
		}

		// scan all the basic attributes into corresponding variables
		String name = scan.next();
		int xPos = scan.nextInt(); // stores the x position in 2D swing space
		int yPos = scan.nextInt(); // stores the y position in 2D swing space
		CartesianPoint location = new CartesianPoint(xPos, yPos);

		int width = scan.nextInt(); // stores the width of the component
		int height = scan.nextInt(); // stores the height of the component
		int depth = scan.nextInt(); // stores the depth of the component
	

		boolean drawable = scan.nextBoolean(); // stores whether or not the item has an associated image
		boolean animated = scan.nextBoolean(); // stores whether or not the item has a set of animated frames
		boolean visible = scan.nextBoolean(); // stores whether the object should be rendered or not
	
		// Determine what type of component, and construct it before returning it
		if (drawable) {

			// scan the remaining attributes into corresponding variables
			double rotation = scan.nextDouble(); // stores the rotation value for the visual component
			double scale = scan.nextDouble(); // stores the scalar value for the visual component

			if (animated) {
				AnimatedComponent comp = new AnimatedComponent(name, location, width, height, rotation, depth);

				// add all the frames to the animated component
				comp.setVisible(visible);
				String imageDir = scan.nextLine();
				comp.addFrames(imageDir);
				scan.close();
				return comp;
			} else {
				scan.close();
				
				SimpleComponent comp = new SimpleComponent(name, location, width, height, rotation, scale, depth);
				comp.setVisible(visible);
				// add the image to the component
				String imagePath = FileUtil.getImageDirectoryPath() + "/" + name + ".png";
				
				
				BufferedImage image = loadImage(imagePath);
				comp.setImage(image);

				return comp;
			}
		} else {
			// Complex Component
			ComplexComponent comp = new ComplexComponent(name, location, depth);
			comp.setVisible(visible);
			ArrayList<StoredNodeData> componentData = new ArrayList<StoredNodeData>();

			double structureRotation = scan.nextDouble();
			double structureScale = scan.nextDouble();
			comp.setStructureRotation(structureRotation);
			comp.setStructureScale(structureScale);
			
			// load each node's portion into a helper data object 
			while (scan.hasNext()) {			
				// componentDataName parentDataComponentName ID direction magnitude
				String dataComponentName = scan.next();
				
				if (dataComponentName.equals("}")) {
					break;
				}
				
				int parentID = scan.nextInt();
				int ID = scan.nextInt();
				double direction = scan.nextDouble();
				double magnitude = scan.nextDouble();
				Vector vector = new Vector(direction, magnitude);
				StoredNodeData nodeData = new StoredNodeData(dataComponentName, parentID, ID, vector);
				componentData.add(nodeData);
				
			}
			scan.close();
			
			// sort the componentDataList by ID, low to high
			sortComparableArrayList(componentData);
			
			// add them in ID order from low to high
			Node<AbstractComponent> attatchementLocation = new Node<AbstractComponent>(-1);
			for (StoredNodeData nodeData : componentData) {
				
				for (Node<AbstractComponent> node : comp.getGraph()) {
					if (node.getID() == nodeData.parentID) {
						attatchementLocation = node;
						break;
					}
				}

				if (attatchementLocation.data == null) {
					System.out.println("FAILURE LOADING A COMPLEX COMPONENT");
					return null;
				}
				
				AbstractComponent data = FileLoadingUtil.loadComponent(FileUtil.getComponentDirectoryPath() + "/" + nodeData.componentDataName + ".ac");
				attatchementLocation.spawnChild(data, nodeData.vector, nodeData.ID);
				
				
			}
			
			
			return comp;
		}
	}
	
	
	
	public static Scene loadScene(File sceneFile) {
		return loadScene(sceneFile.getAbsolutePath());
	}
	
	/**
	 * Loads a scene from a file into a Scene object.
	 * 
	 * @param String filename
	 * @return Scene
	 */
	public static Scene loadScene(String filepath) {
		StoredSceneData sceneData = loadSceneData(filepath);
		return buildSceneFromSceneData(sceneData);
		
	}
	
	private static StoredSceneData loadSceneData(String filepath) {
		// get the scene filedata
		String sceneData = FileUtil.retrieveFileData(filepath);

		// scan through the scene filedata
		Scanner scan = new Scanner(sceneData);
		String name = scan.next();
		int numSaves = scan.nextInt();
		int numBackgrounds = scan.nextInt();
		int currentBackground = scan.nextInt();

		// scan all components
		ArrayList<String> componentNames = new ArrayList<String>();
		while (scan.hasNext()) {
			String currentToken = scan.next();
			if (!currentToken.equals("")) {
				componentNames.add(currentToken);
			}
		}
		scan.close();
		
		StoredSceneData storedSceneData = new StoredSceneData(name, numSaves, numBackgrounds, currentBackground, componentNames);
		return storedSceneData;
		
	}
	
	private static Scene buildSceneFromSceneData(StoredSceneData sceneData) {
		// create new scene with the given name
		Scene scene = new Scene(sceneData.name, sceneData.numSaves);

		// add backgrounds
		ArrayList<BufferedImage> backgrounds = loadImages(sceneData.name, sceneData.numBackgrounds);
		scene.setBackgrounds(backgrounds);
		scene.setBackgroundIndex(sceneData.backgroundIndex);

		// add each component in the componentsDirPath
		ArrayList<AbstractComponent> components = loadComponents(sceneData.components);
		scene.setComponents(components);

		// return the scene
		return scene;
	}
	
	/**
	 * Loads an image from a file into a BufferedImage.
	 * 
	 * @param String filename
	 * @return BufferedImage
	 */
	public static BufferedImage loadImage(String filepath) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(filepath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * Loads all the images in the specified directory path, if it exists into an
	 * ArrayList of BufferedImages.
	 * 
	 * @param String dirPath
	 * @return ArrayList<BufferedImage>
	 */
	public static ArrayList<BufferedImage> loadImages(String dirPath) {
		ArrayList<BufferedImage> result = new ArrayList<BufferedImage>();
		File imgDir = new File(dirPath);
		if (imgDir.exists() && imgDir.isDirectory()) {
			File[] children = imgDir.listFiles();
			for (File child : children) {
				result.add(loadImage(child));
			}
		}
		return result;
	}
	


	/**
	 * Loads all the images with a specified scene name
	 * 
	 * @param String dirPath
	 * @return ArrayList<BufferedImage>
	 */
	public static ArrayList<BufferedImage> loadImages(String sceneName, int numBackgrounds) {
		ArrayList<BufferedImage> result = new ArrayList<BufferedImage>();
		for (int i = 0; i < numBackgrounds; i++) {
			String imagePath = FileUtil.getImageDirectoryPath() + "/" + sceneName + "_" + i + ".png";
			result.add(loadImage(imagePath));
		}
		return result;
	}

	/**
	 * Loads the corresponding component for each name in the names parameter list,
	 * and returns all of them in an ArrayList.
	 * 
	 * @param ArrayList<String> componentNames
	 * @return ArrayList<AbstractComponent>
	 */
	private static ArrayList<AbstractComponent> loadComponents(ArrayList<String> componentNames) {
		ArrayList<AbstractComponent> result = new ArrayList<AbstractComponent>();
		for (String name : componentNames) {
			AbstractComponent component = loadComponent(FileUtil.getComponentDirectoryPath() + "/" + name + ".ac");
			
			result.add(component);
		}
		return result;
	}

	/**
	 * Loads an image from a file into a BufferedImage.
	 * 
	 * @param String filename
	 * @return BufferedImage
	 */
	public static BufferedImage loadImage(File file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	// helper method for sorting the nodes in a loaded complex component.
	@SuppressWarnings("unchecked")
	private static void sortComparableArrayList(ArrayList<? extends Comparable<StoredNodeData>> list) {
		@SuppressWarnings("rawtypes")
		Comparator compare = new Comparator<StoredNodeData>() {
			@Override
			public int compare(StoredNodeData o1, StoredNodeData o2) {
				return o1.compareTo(o2);
			}
		};
		list.sort(compare);
		
	}
	
	

}
