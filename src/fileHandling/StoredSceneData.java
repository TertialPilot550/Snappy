package fileHandling;

import java.util.ArrayList;

class StoredSceneData {

	String name;
	int numSaves;
	int numBackgrounds;
	int backgroundIndex;
	ArrayList<String> components;
	
	StoredSceneData(String name, int numSaves, int numBackgrounds, int backgroundIndex, ArrayList<String> components) {
		this.name = name;
		this.numSaves = numSaves;
		this.numBackgrounds = numBackgrounds;
		this.backgroundIndex = backgroundIndex;
		this.components = components;
	}
}
