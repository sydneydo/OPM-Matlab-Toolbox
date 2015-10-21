package extensionTools.search;

import gui.projectStructure.Entry;

public class OptionsByEntryID extends OptionsBase{
	Entry entry ; 
	
	public OptionsByEntryID (Entry entry) {
		super(); 
		this.entry = entry ; 
	}

	public long getEntryID() {
		return entry.getId() ; 
	}
}
