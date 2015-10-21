package gui.opdProject.consistency.deleting.rules;

import java.util.Enumeration;
import java.util.Iterator;

import gui.opdProject.OpdProject;
import gui.opdProject.consistency.ConsistencyAction;
import gui.opdProject.consistency.ConsistencyAbstractChecker;
import gui.opdProject.consistency.ConsistencyCheckerInterface;
import gui.opdProject.consistency.ConsistencyOptions;
import gui.opdProject.consistency.ConsistencyResult;
import gui.projectStructure.Entry;
import gui.projectStructure.Instance;


public class InzoomedDeletion extends ConsistencyAbstractChecker implements ConsistencyCheckerInterface {
	
	private boolean deploy = false ; 

	
	public InzoomedDeletion (ConsistencyOptions options, ConsistencyResult results) {
		super(options, results) ; 
	}

	public boolean isStoping() {
		return false;
	}
	public void check() {	 
		
		OpdProject project =  this.getMyOptions().getProject() ; 
		
		Iterator deletedEnum = this.getMyOptions().getInstences() ; 
		
		while (deletedEnum.hasNext()) {
			Instance deletedins = (Instance) deletedEnum.next() ;
			//now search project 
			Entry entry = project.getCurrentOpd().getMainEntry() ;
			if (entry != null ) {
				//get all the instances of the main entry i\and look for 
				//the deleted instance in their OPD
				Enumeration mainEnum = entry.getInstances() ; 
				while (mainEnum.hasMoreElements()) {
					Instance main = (Instance) mainEnum.nextElement() ;
					Entry deletedEntry = deletedins.getEntry() ; 
					Enumeration deletedInsEnum = deletedEntry.getInstances() ; 
					while (deletedInsEnum.hasMoreElements()) {
						Instance ins = (Instance) deletedInsEnum.nextElement();
						if(main.getOpd().isContaining(ins)) {
							Instance[] insArray = new Instance[1] ; 
							insArray[0] = main ; 
							ConsistencyAction action = new ConsistencyAction(ConsistencyAction.DELETION, 
															main.getOpd(), 
															ins, 
															insArray) ;   
							this.getResults().setAction(action) ; 
						}
					}
				}
			}
			
		}
		
	}
	
	public void deploy(ConsistencyResult checkResult) {
		//
	}

	public boolean isDeploy() {
		//deploy is done in the generic deletion deploy 
		//as this rule is needing no speciel treatment
		return this.deploy;
	}

	public void setDeploy(boolean deploy) {
		this.deploy = deploy;
	}
	

}
