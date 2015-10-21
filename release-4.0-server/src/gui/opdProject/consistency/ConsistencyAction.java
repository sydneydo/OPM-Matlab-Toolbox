package gui.opdProject.consistency;

import gui.opdProject.Opd; 
import gui.projectStructure.Instance;

public class ConsistencyAction {
	
	public static final int DELETION = 1 ;
	public static final int THING_INZOOMED_ADDITION = 2 ; 	
	public static final int CHANGE = 3 ;
	public static final int LINK_ADDITION = 4 ;
	public static final int THING_SD_ADDITION = 5 ;
	public static final int ADDITION = 6 ;	
	public static final int THING_INZOMMED_INSIDE_ADDITION = 7 ;
	public static final int INSTRUMENT_LINKS_ADDITION = 8 ;	
	public static final int GLOBAL = 9 ;
	public static final int GLOBAL_THING_REPORT = 10 ;
	
	
	
	public static final int NO_ACTION = 0 ;
	
	private Opd myOpd = null ; 
	private int myType = ConsistencyAction.NO_ACTION;
	private Instance mySourceInstance = null ; 
	private Instance[] myEffectedInstances = null; 
	private String ID ; 
	
	public ConsistencyAction (int type, Opd opd, Instance sourceInstance, Instance[] effectedInstances ) {
		this.myType = type ; 
		this.myOpd = opd ;  
		this.mySourceInstance = sourceInstance ; 
		this.myEffectedInstances = effectedInstances ; 
		
		this.ID = sourceInstance.getKey().toString();  
		for(int i = 0 ; i < effectedInstances.length ; i++) {
			this.ID = this.ID + effectedInstances[i].getKey().toString(); 
		}
	}

	
	public Opd getMyOpd() {
		return this.myOpd;
	}

	public int getMyType() {
		return this.myType;
	}

	public Instance getMySourceInstance() {
		return this.mySourceInstance;
	}


	public String getID() {
		return this.ID;
	}


	public Instance[] getMyEffectedInstances() {
		return this.myEffectedInstances;
	}

	public String toString() {
		return this.getID() ; 
	}
	
}
