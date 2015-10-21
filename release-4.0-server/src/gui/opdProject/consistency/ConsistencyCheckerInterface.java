package gui.opdProject.consistency;

public interface ConsistencyCheckerInterface {
	
	public  void check() ;
	public void deploy(ConsistencyResult checkResult) ;
	public boolean isDeploy() ;
	public ConsistencyOptions getMyOptions() ;
	public boolean isStoping() ; 

}
