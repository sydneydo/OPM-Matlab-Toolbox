package gui.dataProject;

public class MetaStatus {
	
	boolean loadFail = false ; 
	String failReason = "" ; 
	
	public MetaStatus() {
		loadFail = false ; 
		failReason = "" ; 
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public boolean isLoadFail() {
		return loadFail;
	}

	public void setLoadFail(boolean loadFail) {
		this.loadFail = loadFail;
	}

}
