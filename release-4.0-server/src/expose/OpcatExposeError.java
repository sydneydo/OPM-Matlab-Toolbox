package expose;

public class OpcatExposeError {
	
	private String errorMessage ; 
	private String errorCouserName ;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorCouserName() {
		return errorCouserName;
	}

	public OpcatExposeError(String errorMessage, String errorCouserName) {
		super();
		this.errorMessage = errorMessage;
		this.errorCouserName = errorCouserName;
	}

	

}
