package gui.opdProject.consistency;

import java.util.HashMap;
import java.util.Iterator;

import gui.opdProject.OpdProject;

public class ConsistencyOptions { 

	private HashMap instences = null ;
	private OpdProject project = null ; 
	private int myOP = 0 ; 
	
	public ConsistencyOptions(OpdProject inProject, HashMap inInstances, int op) {
		this.instences = inInstances ;
		this.project = inProject ;  
		this.myOP = op ; 
	}

	public Iterator getInstences() {
		return this.instences.values().iterator();
	}

	public OpdProject getProject() {
		return this.project;
	}

	public int getMyOP() {
		return this.myOP;
	}
}
