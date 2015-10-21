package gui.dataProject;

import gui.metaLibraries.logic.MetaLibrary;

public class MetaDataItem extends  DataAbstractItem implements ItemInterface{


	private int coloringLlevel;


	public MetaDataItem(String extId, String projectGlobalID) {
		super("", extId) ; 
	}



	public int getColoringLevel() {
		int level = -1;

		if (coloringLlevel != -1) {
			level = coloringLlevel;
		}
		return level;
	}
	
	public int getColoringLevel(MetaLibrary meta) {
		return getColoringLevel();
	}	

	public void setColoringLevel(int level) {
		this.coloringLlevel = level;
	}

	
}
