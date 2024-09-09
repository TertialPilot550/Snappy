package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JWindow;

import fileHandling.FileChooser;
import fileHandling.FileLoadingUtil;
import fileHandling.FileUtil;
import sceneModel.Scene;
import ui.Window.AnimationWindow;

public class Snappy {

	private Thread[] windowThreads;
	private boolean doOpenScene;
	private boolean doOpenComplexComponentBuilder;
	private boolean doQuit;
	private boolean running;
	
	public static void main(String[] args) {
		Snappy snappy = new Snappy();
		snappy.run();
		
	}
	
	public Snappy() {
		showSplashScreen();
		windowThreads = new Thread[4];
	}
	
	private void showSplashScreen() {

		// splash screen portion
		JWindow window = new JWindow();
		window.setBounds(550, 250, 300, 200);
		window.setVisible(true);
		Graphics g = window.getGraphics();
		BufferedImage splashScreenImage = FileLoadingUtil.loadImage("./splashScreen.jpg");
		g.drawImage(splashScreenImage, 0, 0, window.getWidth(), window.getHeight(), null);

		// duration
		try {
		    Thread.sleep(4000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		
		window.setVisible(false);
		
	}
	
	public void run() {
		openScene();
		mainLoop();
	}
	
	// allow user to open multiple windows or open a new complex character builder
	private void mainLoop() {
		running = true;
		while (running) {
			if (doOpenScene) {
				openScene();
			} else if (doOpenComplexComponentBuilder) {
				openComplexComponentBuilder();
			} else if (doQuit) {
				break;
			}
		}
	}
	
	
	private void openComplexComponentBuilder() {
		if (getNumThreads() < 5) {
			// TODO open a complex character builder
		}
		
		doOpenComplexComponentBuilder = false;
	}

	
	private void openNewWindow(Scene scene) {
		if (getNumThreads() < 5) {
			AnimationWindow window = new AnimationWindow(scene);
			int firstOpenIndex = getNumThreads();
			windowThreads[firstOpenIndex] = new Thread(window);
			windowThreads[firstOpenIndex].start();
		}
		doOpenScene = false;
	}
	
	private int getNumThreads() {
		int result = 0;
		for (Thread t : windowThreads) {
			if (t != null && t.isAlive()) {
				result++;
			}
		}
		return result;
	}
	

	
	private void openScene() {
		Scene scene = chooseScene();
		openNewWindow(scene);
	}
	
	private Scene chooseScene() {
		File sceneFile = FileChooser.chooseFile(FileUtil.FILETYPE_SCENE);
		if (sceneFile == null) {
			return null;
		}
		Scene scene = FileLoadingUtil.loadScene(sceneFile);
		return scene;
		
	}
	
	

}
