package expose;

public class OpcatExposeChangeResult {

	private String reseon = "";
	private boolean allow = true;
	private boolean fail = false;
	private boolean warning = false;

	public OpcatExposeChangeResult() {
		super();
	}

	public void fail(String failReason) {
		fail = true;
		allow = false;
		warning = false;
		this.reseon = failReason;
	}

	public void warn(String failReason) {
		warning = true;
		allow = false;
		fail = false;
		this.reseon = failReason;
	}

	public void approve() {
		allow = true;
		fail = false;
		warning = false;
		this.reseon = "";
	}

	public boolean isApproved() {
		return allow;
	}

	public boolean isDenied() {
		return fail;
	}

	public boolean isWarning() {
		return warning;
	}

	public String getReaseon() {
		return reseon;
	}

	public void addAtEndOfMessage(String added) {
		this.reseon = this.reseon + "\n" + added;
	}

	public void addAtStartOfMessage(String added) {
		this.reseon = added + "\n" + this.reseon;
	}
}
