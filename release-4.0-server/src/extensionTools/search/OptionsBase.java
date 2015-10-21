package extensionTools.search;

/**
 * the base class of all options for any search algorithems. Any search
 * implemantation must implement it's own options which extends the OptionBase
 * class.
 * 
 * @see OptionsInString
 * @author raanan
 * 
 */
public abstract class OptionsBase {
	boolean inDescription = false;
	boolean inName = false;
	boolean forStates = false;
	boolean forObjects = false;
	boolean forProcess = false;
	boolean forLinks = false;
	boolean restrictToParent = false;

	String searchText = "";

	public OptionsBase() {
		this.inDescription = false;
		this.inName = false;
		this.forStates = false;
		this.forLinks = false;
		this.forObjects = false;
		this.forProcess = false;
		this.restrictToParent = false;
		this.searchText = "";
	}

	public boolean isForProcess() {
		return this.forProcess;
	}

	public boolean isForStates() {
		return this.forStates;
	}

	public boolean isInDescription() {
		return this.inDescription;
	}

	public boolean isInName() {
		return this.inName;
	}

	public String getSearchText() {
		return this.searchText;
	}

	public void setForObjects(boolean forObjects) {
		this.forObjects = forObjects;
	}

	public boolean isForObjects() {
		return this.forObjects;
	}

	public void setForProcess(boolean forProcess) {
		this.forProcess = forProcess;
	}

	public void setForStates(boolean forStates) {
		this.forStates = forStates;
	}

	public void setInDescription(boolean inDescription) {
		this.inDescription = inDescription;
	}

	public void setInName(boolean inName) {
		this.inName = inName;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public boolean isRestrictToParent() {
		return restrictToParent;
	}

	public void setRestrictToParent(boolean restrictToParent) {
		this.restrictToParent = restrictToParent;
	}

	public boolean isForLinks() {
		return forLinks;
	}

	public void setForLinks(boolean forLinks) {
		this.forLinks = forLinks;
	}

}
