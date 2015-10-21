package gui.scenarios;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Scenarios{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String,Scenario> scenarios ; 
	
	public Scenarios() {
		scenarios = new HashMap<String,Scenario> () ; 
	}
	
	public void add(Scenario scenrio) {
		scenarios.put(scenrio.getId(), scenrio) ; 
	}
	
	public HashMap<String,Scenario> getScenriosHashMap() {
		return scenarios  ; 
	}
	
	public Collection<Scenario>  values() {
		return scenarios.values() ; 
	}	
	
	public Set<String> keySet() {
		return scenarios.keySet() ; 
	}
	
	public Scenario get(String id) {
		return (Scenario) scenarios.get(id) ;
	}
	
	public Scenario remove(String id) {
		return (Scenario) scenarios.remove(id) ;
	}
	
	public String toString() {
		return "Scenarios" ; 
	}


}
