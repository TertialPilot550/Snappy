package fileHandling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import dataStructures.DirectedVectorGraph.Node;
import sceneModel.AbstractComponent;
import sceneModel.ComplexComponent;
import sceneModel.Scene;
import sceneModel.SimpleComponent;

public class FileSavingUtil {

	/**
	 * TODO Saves a given AbstractComponent component's attributes to a file with
	 * the specified filepath, to allow it to be saved for later use.
	 * 
	 * @param String            filepath
	 * @param AbstractComponent component
	 */
	public static void saveComponent(String filepath, AbstractComponent component) {
		try {
			FileOutputStream outStream = new FileOutputStream(filepath);
			PrintWriter writer = new PrintWriter(outStream);

			writer.println("abstract component {");
			writer.println(component.getName());
			writer.println(component.getX());
			writer.println(component.getY());
			writer.println(component.getWidth());
			writer.println(component.getHeight());
			writer.println(component.getDepth());
			writer.println(component.isDrawable());
			writer.println(component.isAnimated());
			writer.println(component.isVisible());

			if (component.isDrawable()) {
				SimpleComponent comp = (SimpleComponent) component;
				writer.println(comp.getRotation());
				writer.println(comp.getScale());
				// Animated visual component, images dir path
				// Regular visual component, single image path
			} else {
				// COMPLEX COMPONENT

				/*
				 * Save structural rotation and scale
				 */
				ComplexComponent comp = (ComplexComponent) component;
				writer.println(comp.getStructureRotation());
				writer.println(comp.getStructureScale());

				for (Node<AbstractComponent> node : comp.getGraph()) {
					if (node.parent != null) {
						writer.println(Node.getNodePrintData(node)); // Save the node's relative location, id, and
																		// component data name
						FileSavingUtil.saveComponent(node.data.getName() + ".ac", node.data); // Save the nodeData
					}

				}

				// save the structure in some way
				// save each node using level order traversal, including it's parent, and list
				// the component's name so it can be loaded from the components file
				// - since the nodes are stored with level order traversal, it's parent will
				// alway be loaded and then it can be created with add node
				// - store the vector edge from it's parent to this as well

				/*
				 * Include in node's printed data: - component data's name - parent edge's
				 * vector - parent's id
				 * 
				 * componentDataName parentID ID direction magnitude
				 * 
				 * 
				 * 
				 * 
				 * 
				 * 
				 */

			}

			writer.println("}");

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves a scene object's attributes into a file with the specified filepath.
	 * 
	 * 
	 * @param filepath
	 */
	public static void saveScene(Scene scene) {
		String sceneDirectoryPath = FileUtil.RES_PATH + "/scenes";

		try {

			FileOutputStream outStream = new FileOutputStream(sceneDirectoryPath + "/" + scene.getName() + ".scene");
			PrintWriter writer = new PrintWriter(outStream);

			ArrayList<BufferedImage> backgrounds = scene.getBackgrounds();
			ArrayList<AbstractComponent> components = scene.getComponents();

			writer.println(scene.getName()); // - Name
			writer.println(scene.getNumSaves()); // - numSaves
			writer.println(backgrounds.size()); // - # of backgrounds
			writer.println(scene.getBackgroundIndex()); // - Background Index
			// write the name of each component so they can be initialized from files later,
			// and save it to a file
			for (AbstractComponent ac : components) {
				writer.println(ac.getName());
				saveComponent(FileUtil.getComponentDirectoryPath() + "/" + ac.getName() + ".ac", ac);
			}
			writer.flush();
			writer.close();
			outStream.flush();
			outStream.close();

			// Save Backgrounds to the images subfolder of res with a unique id and
			// formatted name
			for (int i = 0; i < backgrounds.size(); i++) {
				ImageIO.write(backgrounds.get(i), "png",
						new File(FileUtil.getImageDirectoryPath() + "/" + scene.getName() + "_" + i + ".png"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveImage(BufferedImage image, String filepath) {
		try {
			ImageIO.write(image, "png", new File(filepath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
