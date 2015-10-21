package gui.opdProject.consistency;

import exportedAPI.OpdKey;
import gui.projectStructure.Instance;

import java.util.Hashtable;
import java.util.Vector; 

public class ConsistencyResult {
	
	private String message = "" ;
	private Vector actions = new Vector() ; 
	private Hashtable deployed = new Hashtable(); 
	
	public ConsistencyResult () {
		this.message = ""; 
	}
	
	public boolean isConsistent() {
		return (this.actions.size() == 0 );
	}
	
	public String getMessage() {
		return this.message;
	}

	public Vector getActions() {
		return this.actions;
	}

	public void setAction(ConsistencyAction action) {
		//check if action is alrady in collection.		
		boolean found = false ; 
		for(int i = 0 ; i < this.actions.size() ; i++) {
			if (action.getID().equalsIgnoreCase( ((ConsistencyAction) this.actions.get(i)).getID())) {
				found = true; 
				break ; 
			}
		}
		
		if (!found) {
			this.actions.add(action);
		}
	}

	public Hashtable getDeployed() {
		return this.deployed;
	}

	public void setDeployed(Instance ins) {
		this.deployed.put(ins.getKey().toString(), ins); 
	}
	
	
	public boolean isDeployed(Instance ins) {
		return this.deployed.containsKey(ins.getKey().toString()); 
	}	
	
	public boolean isDeployed(OpdKey key) {
		return this.deployed.containsKey(key.toString()); 
	}	

}
