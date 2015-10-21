package opmTypes;

import java.util.ArrayList;
import java.util.Iterator;

public class opmStatus {

  ArrayList possibleStates;

  public opmStatus() {
    this.possibleStates = new ArrayList();
  }

  public boolean addState(opmState newState) {
    // adds the state only if there will be a single default state after the addition
    if (newState.getIsCurrent()==false) {
      this.possibleStates.add(newState);
      return true;
    }
    int numberOfDefaults = 0;
    Iterator psi = this.possibleStates.iterator();
    while(psi.hasNext()) {
		if (((opmState)psi.next()).getIsCurrent()) {
			numberOfDefaults++;
		}
	}
    if (numberOfDefaults == 0) {
       this.possibleStates.add(newState);
       return true;
     }
     return false;
  }

  public opmState getStatus() {
    opmState currentState = null;
    Iterator psi = this.possibleStates.iterator();
    while(psi.hasNext()) {
      opmState si = (opmState)psi.next();
      if (si.getIsCurrent()) {
		currentState = si;
	}
    }
    return currentState;
  }

   public void setStatus(String newStateName) {
    this.getStatus().whenExists();
    opmState newState = null;
    Iterator psi = this.possibleStates.iterator();
    while(psi.hasNext()) {
      opmState si = (opmState)psi.next();
      if (si.getStateName() == newStateName) {
		newState = si;
	}
    }
    newState.whenEnters();
  }
}