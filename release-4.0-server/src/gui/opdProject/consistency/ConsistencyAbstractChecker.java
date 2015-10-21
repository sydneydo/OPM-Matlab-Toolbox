package gui.opdProject.consistency;


public abstract class ConsistencyAbstractChecker implements ConsistencyCheckerInterface {
	
	private ConsistencyResult results = new ConsistencyResult() ; 
	private ConsistencyOptions myOptions = null ; 
	private boolean deploy = false; 
	
	public ConsistencyAbstractChecker (ConsistencyOptions options, ConsistencyResult results) {
		this.myOptions = options ; 
		this.results = results ; 
	}	
	
	public  void check() {
		//empty stub
	} 
	
	public void deploy(ConsistencyResult checkResult) {
		//empty stub
	}
	
	public boolean isDeploy() {
		return this.deploy ;
	}
	

	public ConsistencyResult getResults() {
		return this.results;
	}

	public ConsistencyOptions getMyOptions() {
		return this.myOptions;
	}
	
	protected void finalize() throws Throwable {
		super.finalize();		
	}

	
}
