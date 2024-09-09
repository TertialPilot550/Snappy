package fileHandling;

import dataStructures.LocationRepresentations.Vector;

/**
 * Helper class used by the file loading util class when loading
 * a complex component.
 * 
 * @author sammc
 *
 */
class StoredNodeData implements Comparable<StoredNodeData> {
	
	String componentDataName;
	int parentID;
	int ID;
	Vector vector;
	
	StoredNodeData(String componentDataName, int parentID, int id, Vector vector) {
		this.componentDataName = componentDataName;
		this.parentID = parentID;
		this.ID = id;
		this.vector = vector;
	}

	@Override
	public int compareTo(StoredNodeData storedNodeData) {
		return Integer.compare(ID, storedNodeData.ID);
	}
}
